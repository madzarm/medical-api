package com.example;

import com.example.exception.exceptions.BadRequestException;
import com.example.exception.exceptions.BadSearchDiseaseException;
import com.example.service.DiseaseService;
import com.example.service.request.CreateDiseaseRequest;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Objects;

@Path("/disease")
public class DiseaseResource {


    @Inject
    DiseaseService diseaseService;

    @POST
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    @Operation(summary = "Creates a disease")
    public Response createDisease(@RequestBody @Valid CreateDiseaseRequest request) {
        return diseaseService.createDisease(request);
    }

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Gets disease by name, curable or id",
            description = "You can search diseases by id, name, or curable. You can not combine any of the former."
    )
    public Response getDiseases(
            @QueryParam("name") String name,
            @QueryParam("curable") Boolean curable,
            @QueryParam("diseaseIds") List<Long> diseaseIds
    ) {
        boolean hasNameSearch = Objects.nonNull(name);
        boolean hasCurableSearch = Objects.nonNull(curable);
        boolean hasIdSearch = Objects.nonNull(diseaseIds) && !diseaseIds.isEmpty();
        boolean hasAnyCombinationOfSearches = hasIdSearch ? (hasNameSearch || hasCurableSearch) : (hasNameSearch && hasCurableSearch);

        if (hasAnyCombinationOfSearches)
            throw new BadSearchDiseaseException();
        else if (hasIdSearch)
            return diseaseService.getDiseasesByIds(diseaseIds);
        else if (hasNameSearch)
            return diseaseService.getDiseaseByName(name);
        else if (hasCurableSearch)
            return diseaseService.getDiseasesByCurable(curable);
        return diseaseService.getAllDiseases();
    }

    @PUT
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Updates disease by id or name",
            description = "You must have either name or id to target a disease. If you target by name, " +
                    "you must have curable. If you target by id, you must have either name or curable"
    )
    @Transactional
    public Response updateDiseases(
            @QueryParam("name") String name,
            @QueryParam("curable") Boolean curable,
            @QueryParam("diseaseId") Long diseaseId
    ) {
        boolean hasName = Objects.nonNull(name);
        boolean hasCurable = Objects.nonNull(curable);
        boolean hasId = Objects.nonNull(diseaseId);
        boolean hasOnlyId = hasId && !hasName && !hasCurable;
        boolean hasOnlyNameButNoCurable = !hasId && hasName && !hasCurable;
        boolean hasOnlyCurable = hasCurable && !(hasId || hasName);

        if (hasOnlyId || hasOnlyNameButNoCurable || hasOnlyCurable)
            throw new BadRequestException();
        else if (hasId)
            return diseaseService.updateDiseaseById(diseaseId, name, curable);
        else
            return diseaseService.updateDiseaseByName(name, curable);
    }

    @DELETE
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Deletes disease by id, name, or curable",
            description = "You can delete diseases by id, name, or curable. You can not combine any of the former."
    )
    @Transactional
    public Response deleteDiseases(
            @QueryParam("name") String name,
            @QueryParam("curable") Boolean curable,
            @QueryParam("diseaseIds") List<Long> diseaseIds
    ) {
        boolean hasName = Objects.nonNull(name);
        boolean hasCurable = Objects.nonNull(curable);
        boolean hasId = Objects.nonNull(diseaseIds) && !diseaseIds.isEmpty();
        boolean hasAnyCombination = hasId ? (hasName || hasCurable) : (hasName && hasCurable);

        if (hasAnyCombination)
            throw new BadRequestException();
        else if (hasId)
            return diseaseService.deleteDiseasesByIds(diseaseIds);
        else if (hasName)
            return diseaseService.deleteDiseasesByName(name);
        else if (hasCurable)
            return diseaseService.deleteDiseasesByCurable(curable);
        return diseaseService.deleteAllDiseases();
    }

}