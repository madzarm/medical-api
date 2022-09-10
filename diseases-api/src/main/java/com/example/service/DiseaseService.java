package com.example.service;

import com.example.domain.Disease;
import com.example.domain.dto.DiseaseDto;
import com.example.exception.exceptions.DatabaseEmptyException;
import com.example.exception.exceptions.DiseaseNotFoundException;
import com.example.exception.exceptions.EntityNotFoundException;
import com.example.service.request.CreateDiseaseRequest;
import com.example.service.result.SearchDiseasesResult;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@ApplicationScoped
public class DiseaseService {
    public Response createDisease(CreateDiseaseRequest request) {
        Disease disease = convertRequestToDisease(request);
        disease.persist();

        List<DiseaseDto> diseaseDtoList = convertDiseaseListToDtoList(List.of(disease),true);
        SearchDiseasesResult result = new SearchDiseasesResult(diseaseDtoList);

        return Response.status(201).entity(result).build();
    }

    public Response getAllDiseases(){
        List<Disease> diseases = Disease.listAll();
        if(diseases.isEmpty())
            throw new DatabaseEmptyException();

        List<DiseaseDto> diseaseDtoList = convertDiseaseListToDtoList(diseases,true);
        SearchDiseasesResult searchDiseasesResult = new SearchDiseasesResult(diseaseDtoList);

        return Response.ok(searchDiseasesResult).build();
    }

    public Response getDiseasesByIds(List<Long> diseaseIds){
        List<Disease> diseases = Disease.listByIdsIn(diseaseIds);

        if(diseases.isEmpty())
            throw new DiseaseNotFoundException();

        diseaseIds.forEach(id -> {
            if (diseases.stream().mapToLong(Disease::getId).noneMatch(l -> l == id))
                throw new DiseaseNotFoundException(id);
        });


        List<DiseaseDto> diseaseDtoList = convertDiseaseListToDtoList(diseases,true);
        SearchDiseasesResult searchDiseasesResult = new SearchDiseasesResult(diseaseDtoList);

        return Response.ok(searchDiseasesResult).build();
    }

    public Response getDiseaseByName(String name) {
        List<Disease> diseases = Disease.listByNameContaining(name);
        if(diseases.isEmpty())
            throw new DiseaseNotFoundException();

        List<DiseaseDto> diseaseDtoList = convertDiseaseListToDtoList(diseases,true);
        SearchDiseasesResult searchDiseasesResult = new SearchDiseasesResult(diseaseDtoList);

        return Response.ok(searchDiseasesResult).build();
    }

    public Response getDiseasesByCurable(boolean curable) {
        List<Disease> diseases = Disease.listByCurable(curable);
        if(diseases.isEmpty())
            throw new DiseaseNotFoundException();

        List<DiseaseDto> diseaseDtoList = convertDiseaseListToDtoList(diseases,true);
        SearchDiseasesResult searchDiseasesResult = new SearchDiseasesResult(diseaseDtoList);

        return Response.ok(searchDiseasesResult).build();
    }

    public Response updateDiseaseById(Long id, String name, Boolean curable) {
        boolean hasName = Objects.nonNull(name);
        boolean hasCurable = Objects.nonNull(curable);
        int success;

        if(hasName && hasCurable)
            success = Disease.updateByIdWithNameAndCurable(id,name,curable);
        else if (hasName)
            success = Disease.updateByIdWithName(id,name);
        else success = Disease.updateByIdWithCurable(id,curable);

        return checkExceptionAndReturnResult(success);

    }

    public Response updateDiseaseByName(String name, Boolean curable) {

        int success = Disease.updateByNameWithCurable(name,curable);

        return checkExceptionAndReturnResult(success);
    }

    public Response deleteDiseasesByIds(List<Long> diseaseIds) {

        int success = Disease.deleteByIds(diseaseIds);

        return checkExceptionAndReturnResult(success);
    }

    public Response deleteDiseasesByName(String name) {

        int success = Disease.deleteByName(name);

        return checkExceptionAndReturnResult(success);
    }
    public Response deleteDiseasesByCurable(boolean curable) {

        int success = Disease.deleteByCurable(curable);

        return checkExceptionAndReturnResult(success);
    }

    public Response deleteAllDiseases() {

        int success = (int)Disease.deleteAll();

        return checkExceptionAndReturnResult(success);
    }
    private List<DiseaseDto> convertDiseaseListToDtoList(List<Disease> diseaseList, boolean includeIds) {
        return diseaseList.stream().map(d -> {
                    DiseaseDto diseaseDto = DiseaseDto.builder()
                            .curable(d.getCurable())
                            .name(d.getName()).build();
                    if(includeIds)
                        diseaseDto.setId(d.getId());
                    return diseaseDto;
                }
        ).collect(Collectors.toList());

    }

    private Disease convertRequestToDisease(CreateDiseaseRequest request) {
        return Disease.builder()
                .name(request.getName())
                .curable(request.getCurable()).build();
    }

    private Response checkExceptionAndReturnResult(int success){
        if(success == 0)
            throw new DiseaseNotFoundException();

        return Response.ok().entity(success).build();
    }


}
