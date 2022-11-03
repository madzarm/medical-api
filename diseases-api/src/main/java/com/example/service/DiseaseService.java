package com.example.service;

import com.example.domain.Disease;
import com.example.exception.exceptions.BadRequestException;
import com.example.exception.exceptions.DatabaseEmptyException;
import com.example.exception.exceptions.DiseaseNotFoundException;
import com.example.mapper.Mapper;
import com.example.service.request.CreateDiseaseRequest;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Objects;

@ApplicationScoped
public class DiseaseService {

    @Inject
    Mapper mapper;

    public Response createDisease(CreateDiseaseRequest request) {
        Disease disease = mapper.requestToDisease(request);
        return (Response) Disease.findByName(disease.getName())
                .map(d -> {
                    throw new BadRequestException("Disease with that name already exists!");
                }).orElseGet(() -> mapper.constructResponse(disease));
    }

    public Response getAllDiseases() {
        List<Disease> diseases = Disease.listAll();
        if (diseases.isEmpty())
            throw new DatabaseEmptyException();
        return mapper.constructResponse(diseases);
    }

    public Response getDiseasesByIds(List<Long> diseaseIds) {
        List<Disease> diseases = Disease.listByIdsIn(diseaseIds);
        if (diseases.isEmpty())
            throw new DiseaseNotFoundException();

        diseaseIds.forEach(id -> {
            if (diseases.stream().mapToLong(Disease::getId).noneMatch(l -> l == id))
                throw new DiseaseNotFoundException(id);
        });
        return mapper.constructResponse(diseases);
    }

    public Response getDiseaseByName(String name) {
        List<Disease> diseases = Disease.listByNameContaining(name);
        if (diseases.isEmpty())
            throw new DiseaseNotFoundException();

        return mapper.constructResponse(diseases);
    }

    public Response getDiseasesByCurable(boolean curable) {
        List<Disease> diseases = Disease.listByCurable(curable);
        if (diseases.isEmpty())
            throw new DiseaseNotFoundException();

        return mapper.constructResponse(diseases);
    }

    public Response updateDiseaseById(Long id, String name, Boolean curable) {
        boolean hasName = Objects.nonNull(name);
        boolean hasCurable = Objects.nonNull(curable);
        int success;

        if (hasName && hasCurable)
            success = Disease.updateByIdWithNameAndCurable(id, name, curable);
        else if (hasName)
            success = Disease.updateByIdWithName(id, name);
        else success = Disease.updateByIdWithCurable(id, curable);

        return mapper.constructResponse(success);

    }

    public Response updateDiseaseByName(String name, Boolean curable) {

        int success = Disease.updateByNameWithCurable(name, curable);

        return mapper.constructResponse(success);
    }

    public Response deleteDiseasesByIds(List<Long> diseaseIds) {

        int success = Disease.deleteByIds(diseaseIds);

        return mapper.constructResponse(success);
    }

    public Response deleteDiseasesByName(String name) {

        int success = Disease.deleteByName(name);

        return mapper.constructResponse(success);
    }

    public Response deleteDiseasesByCurable(boolean curable) {

        int success = Disease.deleteByCurable(curable);

        return mapper.constructResponse(success);
    }

    public Response deleteAllDiseases() {

        int success = (int) Disease.deleteAll();

        return mapper.constructResponse(success);
    }

}
