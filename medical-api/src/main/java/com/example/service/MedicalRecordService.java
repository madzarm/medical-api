package com.example.service;

import com.example.client.DiseaseClient;
import com.example.client.PersonClient;
import com.example.domain.DiseaseHistoryModel;
import com.example.domain.DiseaseModel;
import com.example.domain.PersonModel;
import com.example.domain.dto.DiseaseHistoryDto;
import com.example.domain.dto.MedicalRecordDto;
import com.example.exception.ExceptionResponse;
import com.example.service.request.CreateMedicalRecordRequest;
import com.example.service.request.CreatePersonRequest;
import com.example.service.request.GetMedicalRecordsRequest;
import com.example.service.result.SearchMedicalRecordsResult;
import com.example.service.result.model.SearchDiseasesModel;
import com.example.service.result.model.SearchPeopleModel;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;
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
        List<PersonModel> personModels;

        String from = convertDateToString(request.getFrom());
        String to = convertDateToString(request.getTo());

        if(request.getDiseaseName()!=null){
            diseaseClientResponse = diseaseClient.getDiseasesByName(request.getDiseaseName());
            diseaseModels = diseaseClientResponse.readEntity(SearchDiseasesModel.class).getDiseases();
            List<Long> diseaseIds = diseaseModels.stream().map(DiseaseModel::getId).collect(Collectors.toList());

            personClientResponse = personClient.getPeople(request.getPersonIds(), diseaseIds, request.getFirstName(),
                    request.getLastName(), request.getWeightLowerLimit(), request.getWeightUpperLimit(),
                    request.getAgeLowerLimit(), request.getAgeUpperLimit(), from, to);
        } else {
            personClientResponse = personClient.getPeople(request.getPersonIds(), request.getDiseaseIds(),
                    request.getFirstName(), request.getLastName(), request.getWeightLowerLimit(), request.getWeightUpperLimit(),
                    request.getAgeLowerLimit(), request.getAgeUpperLimit(), from, to);
        }

        personModels = personClientResponse.readEntity(SearchPeopleModel.class).getPeople();
        List<Long> diseaseIds = getDiseaseIdsFromPersonModels(personModels);

        diseaseClientResponse = diseaseClient.getDiseasesByIds(diseaseIds);
        diseaseModels = diseaseClientResponse.readEntity(SearchDiseasesModel.class).getDiseases();

        SearchMedicalRecordsResult result = mapToMedicalRecordResult(diseaseModels,personModels);

        return Response.ok().entity(result).build();
    }

    public Response createMedicalRecord(CreateMedicalRecordRequest request) {
        Response responseFromDiseaseClient = null;
        Response responseFromPersonClient;
        List<Long> diseaseIds;
        List<DiseaseModel> diseaseModels = new ArrayList<>();
        CreatePersonRequest personRequest;
        List<PersonModel> personModels;
        SearchMedicalRecordsResult result;

        if(!request.getDiseaseIds().isEmpty()) {

            responseFromDiseaseClient = diseaseClient.getDiseasesByIds(request.getDiseaseIds());
        } else if (request.getDiseaseName()!=null) {
            responseFromDiseaseClient = diseaseClient.getDiseasesByName(request.getDiseaseName());
        }
        if(checkResponseForException(responseFromDiseaseClient)!=null)
            return checkResponseForException(responseFromDiseaseClient);

        if(responseFromDiseaseClient!=null)
            diseaseModels = responseFromDiseaseClient.readEntity(SearchDiseasesModel.class).getDiseases();

        personRequest = mapMedicalToPersonRequest(request);
        if(personRequest.getDiseaseIds().isEmpty()) {
            diseaseIds = diseaseModels.stream().map(DiseaseModel::getId).collect(Collectors.toList());
            personRequest.setDiseaseIds(diseaseIds);
        }

        responseFromPersonClient = personClient.createPerson(personRequest);
        if(checkResponseForException(responseFromPersonClient)!=null)
            return checkResponseForException(responseFromPersonClient);

        personModels = responseFromPersonClient.readEntity(SearchPeopleModel.class).getPeople();

        result = mapToMedicalRecordResult(diseaseModels,personModels);

        return Response.ok().entity(result).build();
    }

    private Response checkResponseForException(Response response) {
        if(response != null) {
            if (response.getStatus() == 202)
                return Response
                        .status(response.getStatus())
                        .entity(response.readEntity(ExceptionResponse.class)).build();
        }
        return null;
    }


    private SearchMedicalRecordsResult mapToMedicalRecordResult(List<DiseaseModel> diseaseModels, List<PersonModel> personModels) {
        List<MedicalRecordDto> medicalRecordDtos = mapToMedicalRecordDtos(diseaseModels,personModels);

        SearchMedicalRecordsResult result = new SearchMedicalRecordsResult();
        result.setMedicalRecords(medicalRecordDtos);

        return result;
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
                .build();
    }

    private List<Long> getDiseaseIdsFromPersonModels(List<PersonModel> personModels) {
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

    private String convertDateToString(Date date) {
        if(date == null)
            return null;
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(date);
    }
}
