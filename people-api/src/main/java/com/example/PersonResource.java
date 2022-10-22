package com.example;

import com.example.exception.exceptions.BadRequestException;
import com.example.service.PersonService;
import com.example.service.request.AddDiseaseHistoriesRequest;
import com.example.service.request.CreatePersonRequest;
import com.example.service.request.UpdateDiseaseHistoryRequest;
import com.example.service.request.UpdatePersonRequest;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Objects;

@Path("/person")
public class PersonResource {

    @Inject
    PersonService personService;

    @POST
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    @Operation(summary = "Creates a person")
    public Response createPerson(@RequestBody CreatePersonRequest request) {
        return personService.createPerson(request);
    }

    @PUT
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    @Operation(summary = "updates a person by id")
    public Response updatePerson(@RequestBody UpdatePersonRequest request) {
        return personService.updatePerson(request);
    }

    @POST
    @Path("/diseaseHistory")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    @Operation(summary = "Adds disease histories to a person")
    public Response addDiseaseHistories(@RequestBody AddDiseaseHistoriesRequest request) {
        return personService.addDiseaseHistory(request);
    }

    @PUT
    @Path("/diseaseHistory")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    @Operation(summary = "Updates diseaseHistories' DiseaseId")
    public Response updateDiseaseHistory(@RequestBody UpdateDiseaseHistoryRequest request) {
        return personService.updateDiseaseHistory(request);
    }

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Finds people")
    public Response getPeople(
            @QueryParam("personIds") List<Long> personIds,
            @QueryParam("diseaseIds") List<Long> diseaseIds,
            @QueryParam("firstName") String firstName,
            @QueryParam("lastName") String lastName,
            @QueryParam("weightLowerLimit") Integer weightLowerLimit,
            @QueryParam("weightUpperLimit") Integer weightUpperLimit,
            @QueryParam("ageLowerLimit") Integer ageLowerLimit,
            @QueryParam("ageUpperLimit") Integer ageUpperLimit,
            @QueryParam("from") String from,
            @QueryParam("to") String to
            ) {

        boolean hasPersonId = Objects.nonNull(personIds) && !personIds.isEmpty();
        boolean hasDiseaseId = Objects.nonNull(diseaseIds) && !diseaseIds.isEmpty();
        boolean hasName = Objects.nonNull(firstName) || Objects.nonNull(lastName);
        boolean hasWeight = Objects.nonNull(weightLowerLimit) || Objects.nonNull(weightUpperLimit);
        boolean hasAge = Objects.nonNull(ageLowerLimit) || Objects.nonNull(ageUpperLimit);
        boolean hasDate = Objects.nonNull(from) || Objects.nonNull(to);

        boolean hasPersonIdCombination = hasPersonId && (hasDiseaseId || hasName || hasWeight || hasAge || hasDate);
        boolean hasDiseaseIdCombination = hasDiseaseId && hasPersonId;
        boolean hasNameCombination = hasName && (hasPersonId || hasWeight || hasAge || hasDate);
        boolean hasWeightCombination = hasWeight && (hasPersonId || hasName || hasAge || hasDate);
        boolean hasAgeCombination = hasAge && (hasPersonId || hasName || hasWeight || hasDate);
        boolean hasDateCombination = hasDate && (hasPersonId || hasName || hasWeight || hasAge);
        boolean hasAnyCombination = hasPersonIdCombination || hasDiseaseIdCombination || hasNameCombination ||
                hasWeightCombination || hasAgeCombination || hasDateCombination;

        if(hasAnyCombination) throw new BadRequestException();
        else if (hasPersonId) return personService.getByIds(personIds);
        else if (hasName) return personService.getByName(firstName,lastName,diseaseIds);
        else if (hasAge) return personService.getByAge(ageLowerLimit,ageUpperLimit,diseaseIds);
        else if (hasWeight) return personService.getByWeight(weightLowerLimit,weightUpperLimit,diseaseIds);
        else if (hasDate) return personService.getByDate(from,to,diseaseIds);
        else if (hasDiseaseId) return personService.getByDiseaseIds(diseaseIds);
        return personService.getAll();
    }

}