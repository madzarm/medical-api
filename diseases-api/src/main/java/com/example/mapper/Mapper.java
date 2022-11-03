package com.example.mapper;


import com.example.domain.Disease;
import com.example.domain.dto.DiseaseDto;
import com.example.exception.exceptions.DiseaseNotFoundException;
import com.example.service.request.CreateDiseaseRequest;
import com.example.service.result.SearchDiseasesResult;

import javax.ws.rs.core.Response;
import java.util.List;

@org.mapstruct.Mapper(componentModel = "cdi")
public abstract class Mapper {

    public abstract Disease requestToDisease(CreateDiseaseRequest createDiseaseRequest);

    public abstract List<DiseaseDto> diseaseToDiseaseDto(List<Disease> diseases);

    public abstract DiseaseDto diseaseToDiseaseDto(Disease disease);

    public Response constructResponse(List<Disease> diseases) {
        List<DiseaseDto> diseaseDtoList = diseaseToDiseaseDto(diseases);
        SearchDiseasesResult result = new SearchDiseasesResult(diseaseDtoList);
        return Response.ok(result).build();
    }

    public Response constructResponse(Disease disease) {
        DiseaseDto diseaseDto = diseaseToDiseaseDto(disease);
        SearchDiseasesResult result = new SearchDiseasesResult(List.of(diseaseDto));
        return Response.ok(result).build();
    }

    public Response constructResponse(int success) {
        if (success == 0)
            throw new DiseaseNotFoundException();

        return Response.ok().entity("Operation successful!").build();
    }

}
