package com.example.service;

import com.example.client.DiseaseClient;
import com.example.client.PersonClient;
import com.example.domain.DiseaseHistoryModel;
import com.example.domain.DiseaseModel;
import com.example.domain.PersonModel;
import com.example.domain.dto.DiseaseHistoryDto;
import com.example.domain.dto.MedicalRecordDto;
import com.example.service.request.*;
import com.example.service.result.SearchMedicalRecordsResult;
import com.example.service.result.model.SearchDiseasesModel;
import com.example.service.result.model.SearchPeopleModel;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityNotFoundException;
import javax.ws.rs.core.Response;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@ApplicationScoped
public class MedicalRecordService {

    @RestClient
    DiseaseClient diseaseClient;
    @RestClient
    PersonClient personClient;

    public Response getMedicalRecords(GetMedicalRecordsRequest request) {

        Response diseaseClientResponse;
        Response personClientResponse;
        List<DiseaseModel> diseaseModels;


        if(request.getDiseaseName()!=null){
            diseaseClientResponse = diseaseClient.getDiseasesByName(request.getDiseaseName());
            diseaseModels = diseaseClientResponse.readEntity(SearchDiseasesModel.class).getDiseases();
            List<Long> diseaseIds = diseaseModels.stream().map(DiseaseModel::getId).collect(Collectors.toList());

            personClientResponse = personClient.getPeople(request.getPersonIds(), diseaseIds, request.getFirstName(),
                    request.getLastName(), request.getWeightLowerLimit(), request.getWeightUpperLimit(),
                    request.getAgeLowerLimit(), request.getAgeUpperLimit(), request.getFrom(), request.getTo());
        } else {
            personClientResponse = personClient.getPeople(request.getPersonIds(), request.getDiseaseIds(),
                    request.getFirstName(), request.getLastName(), request.getWeightLowerLimit(), request.getWeightUpperLimit(),
                    request.getAgeLowerLimit(), request.getAgeUpperLimit(), request.getFrom(), request.getTo());
        }
        List<Long> diseaseIds = getDiseaseIdsFromPersonClientResponse(personClientResponse);

        diseaseClientResponse = diseaseClient.getDiseasesByIds(diseaseIds);

        return mapToMedicalRecordResult(diseaseClientResponse,personClientResponse);
    }

    public Response createMedicalRecord(CreateMedicalRecordRequest request) {
        Response diseaseClientResponse = null;
        Response personClientResponse;
        List<Long> diseaseIds;
        List<DiseaseModel> diseaseModels = new ArrayList<>();
        CreatePersonRequest personRequest;
        SearchMedicalRecordsResult result;

        if(!request.getDiseaseIds().isEmpty()) {
            diseaseClientResponse = diseaseClient.getDiseasesByIds(request.getDiseaseIds());
        } else if (request.getDiseaseName()!=null) {
            diseaseClientResponse = diseaseClient.getDiseasesByName(request.getDiseaseName());
        }

        if(diseaseClientResponse!=null)
            diseaseModels = diseaseClientResponse.readEntity(SearchDiseasesModel.class).getDiseases();

        personRequest = mapMedicalToPersonRequest(request);
        if(request.getDiseaseIds().isEmpty()) {
            diseaseIds = diseaseModels.stream().map(DiseaseModel::getId).collect(Collectors.toList());
            personRequest.setDiseaseIds(diseaseIds);
        }

        personClientResponse = personClient.createPerson(personRequest);

        return mapToMedicalRecordResult(diseaseClientResponse,personClientResponse);
    }

    public Response deleteMedicalRecord(Long personId, Long diseaseHistoryId) {
        return personClient.deletePerson(personId,diseaseHistoryId);
    }

    public Response updateMedicalRecord(UpdateMedicalRecordRequest request) {
        Long diseaseId;
        if(request.getDiseaseName() != null){
            Response response = diseaseClient.getDiseasesByName(request.getDiseaseName());
            List<Long> diseaseIds = getDiseaseIdsFromDiseaseClientResponse(response);
            if(diseaseIds.isEmpty())
                throw new EntityNotFoundException("Disease with name: " + request.getDiseaseName() + " not found!");
            diseaseId = diseaseIds.get(0);
        } else {
            diseaseId = request.getDiseaseId();
            Response response = diseaseClient.getDiseasesByIds(List.of(diseaseId));
            List<Long> diseaseIds = getDiseaseIdsFromDiseaseClientResponse(response);
            if(diseaseIds.isEmpty())
                throw new EntityNotFoundException("Disease with id: " + request.getDiseaseId() + " not found!");
        }

        UpdatePersonRequest updateRequest = UpdatePersonRequest.builder()
                .diseaseId(diseaseId)
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .age(request.getAge())
                .weight(request.getWeight())
                .diseaseHistoryId(request.getDiseaseHistoryId())
                .dateDiscovered(request.getDateDiscovered())
                .personId(request.getPersonId())
                .build();

        Response personClientResponse = personClient.updatePerson(updateRequest);
        List<Long> diseaseIds = getDiseaseIdsFromPersonClientResponse(personClientResponse);
        Response diseaseClientResponse = diseaseClient.getDiseasesByIds(diseaseIds);

        return mapToMedicalRecordResult(diseaseClientResponse, personClientResponse);
    }

    private List<Long> getDiseaseIdsFromDiseaseClientResponse(Response response) {
        return response.readEntity(SearchDiseasesModel.class).getDiseases().stream()
                .map(DiseaseModel::getId).collect(Collectors.toList());
    }


    private Response mapToMedicalRecordResult(Response diseaseClientResponse, Response personClientResponse) {
        List<DiseaseModel> diseaseModels = new ArrayList<>();
        List<PersonModel> personModels = new ArrayList<>();

        try {
            diseaseModels = diseaseClientResponse.readEntity(SearchDiseasesModel.class).getDiseases();
            personModels = personClientResponse.readEntity(SearchPeopleModel.class).getPeople();
        } catch (Exception e){}

        List<MedicalRecordDto> medicalRecordDtos = mapToMedicalRecordDtos(diseaseModels,personModels);

        SearchMedicalRecordsResult result = new SearchMedicalRecordsResult();
        result.setMedicalRecords(medicalRecordDtos);

        return Response.ok(result).build();
    }

    private List<MedicalRecordDto> mapToMedicalRecordDtos(List<DiseaseModel> diseaseModels, List<PersonModel> personModels) {
        return personModels.stream().map(p -> {

            List<DiseaseHistoryDto> diseaseHistories = mapModelToDto(p.getDiseaseHistories(),diseaseModels);
            diseaseHistories.removeAll(Collections.singleton(null));

            return MedicalRecordDto.builder()
                    .diseaseHistories(diseaseHistories)
                    .personId(p.getId())
                    .age(p.getAge())
                    .weight(p.getWeight())
                    .firstName(p.getFirstName())
                    .lastName(p.getLastName()).build();
        }).collect(Collectors.toList());
    }

    private List<DiseaseHistoryDto> mapModelToDto (List<DiseaseHistoryModel> diseaseHistoryModels, List<DiseaseModel> diseaseModels){
        return diseaseHistoryModels.stream().map(dh -> {

            Optional<DiseaseModel> diseaseOptional = diseaseModels.stream().filter(dr -> dr.getId().equals(dh.getDiseaseId())).findFirst();
            return diseaseOptional.map(response -> DiseaseHistoryDto.builder()
                    .diseaseName(response.getName())
                    .id(dh.getId())
                    .diseaseId(dh.getDiseaseId())
                    .curable(response.getCurable())
                    .dateDiscovered(dh.getDateDiscovered()).build()).orElse(null);
        }).collect(Collectors.toList());
    }

    private CreatePersonRequest mapMedicalToPersonRequest(CreateMedicalRecordRequest request) {
        return CreatePersonRequest.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .weight(request.getWeight())
                .age(request.getAge())
                .diseaseIds(request.getDiseaseIds())
                .dateDiscovered(request.getDateDiscovered())
                .build();
    }

    private List<Long> getDiseaseIdsFromPersonClientResponse(Response personClientResponse) {
        List<PersonModel> personModels = personClientResponse.readEntity(SearchPeopleModel.class).getPeople();
        List<Long> distinctLongs = new ArrayList<>();
        List<List<Long>> longs = personModels.stream()
                .map(p -> p.getDiseaseHistories().stream()
                        .map(DiseaseHistoryModel::getDiseaseId)
                        .collect(Collectors.toList()))
                .collect(Collectors.toList());

        longs.forEach(l -> l.forEach(lng -> {
            if(!distinctLongs.contains(lng))
                distinctLongs.add(lng);
        }));

        return distinctLongs;
    }


}
