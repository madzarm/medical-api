package com.example;

import com.example.service.PersonService;
import com.example.service.request.CreatePersonRequest;
import com.example.service.request.UpdatePersonRequest;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
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
    public Response updatePerson(@Valid @RequestBody UpdatePersonRequest request) {
        return personService.updatePerson(request);
    }

    @DELETE
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    @Operation(summary = "Deletes a person by id")
    public Response deletePerson(
            @QueryParam("personId") Long personId,
            @QueryParam("diseaseHistoryId") Long diseaseHistoryId
    ) {
        boolean hasDiseaseHistoryId = Objects.nonNull(diseaseHistoryId);
        boolean hasPersonId = Objects.nonNull(personId);

        if(!hasPersonId)
            throw new BadRequestException("PersonId is required!");
        if(hasDiseaseHistoryId)
            return personService.deleteDiseaseHistory(personId, diseaseHistoryId);
        return personService.deletePerson(personId);
    }

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Finds people")
    public Response getPeople(
            @QueryParam("personIds") List<Long> personIds,
            @QueryParam("diseaseIds") List<Long> diseaseIds,
            @QueryParam("firstName") @Pattern(regexp = "^[A-Za-z]+$", message = "You may only use alphabetic characters [a-zA-z]") String firstName,
            @QueryParam("lastName") @Pattern(regexp = "^[A-Za-z]+$", message = "You may only use alphabetic characters [a-zA-z]") String lastName,
            @QueryParam("weightLowerLimit") @Min(0) @Max(500) Integer weightLowerLimit,
            @QueryParam("weightUpperLimit") @Min(0) @Max(500) Integer weightUpperLimit,
            @QueryParam("ageLowerLimit") @Min(0) @Max(150) Integer ageLowerLimit,
            @QueryParam("ageUpperLimit") @Min(0) @Max(150) Integer ageUpperLimit,
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

        if(hasAnyCombination) throw new BadRequestException("You may combine either personId or diseaseIds with other " +
                "types of searches (weight, date, age or name search) ");
        else if (hasPersonId) return personService.getByIds(personIds);
        else if (hasName) return personService.getByName(firstName,lastName,diseaseIds);
        else if (hasAge) return personService.getByAge(ageLowerLimit,ageUpperLimit,diseaseIds);
        else if (hasWeight) return personService.getByWeight(weightLowerLimit,weightUpperLimit,diseaseIds);
        else if (hasDate) return personService.getByDate(from,to,diseaseIds);
        else if (hasDiseaseId) return personService.getByDiseaseIds(diseaseIds);
        return personService.getAll();
    }

}