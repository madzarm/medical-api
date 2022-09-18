package com.example;

import com.example.exception.exceptions.BadRequestException;
import com.example.service.PersonService;
import com.example.service.request.AddDiseaseHistoriesRequest;
import com.example.service.request.CreatePersonRequest;
import com.example.service.request.UpdateDiseaseHistoryRequest;
import com.example.service.request.UpdatePersonRequest;
import com.example.service.result.ValidationResult;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Path("/person")
public class PersonResource {

    @Inject
    Validator validator;
    @Inject
    PersonService personService;

    @POST
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    @Operation(summary = "Creates a person")
    public Response createPerson(@RequestBody CreatePersonRequest request) {
        ValidationResult validationResult = validate(request);
        if(!validationResult.isSuccess()) {
            return Response.ok(validationResult).build();
        }
        return personService.createPerson(request);
    }

    @PUT
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    @Operation(summary = "updates a person by id")
    public Response updatePerson(@RequestBody UpdatePersonRequest request) {
        ValidationResult validationResult = validate(request);
        if(!validationResult.isSuccess()) {
            return Response.ok(validationResult).build();
        }
        return personService.updatePerson(request);
    }

    @POST
    @Path("/diseaseHistory")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    @Operation(summary = "Adds disease histories to a person")
    public Response addDiseaseHistories(@RequestBody AddDiseaseHistoriesRequest request) {
        ValidationResult validationResult = validate(request);
        if(!validationResult.isSuccess()) {
            return Response.ok(validationResult).build();
        }
        return personService.addDiseaseHistory(request);
    }

    @PUT
    @Path("/diseaseHistory")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    @Operation(summary = "Updates diseaseHistories' DiseaseId")
    public Response updateDiseaseHistory(@RequestBody UpdateDiseaseHistoryRequest request) {
        ValidationResult validationResult = validate(request);
        if(!validationResult.isSuccess()) {
            return Response.ok(validationResult).build();
        }
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
            @QueryParam("from")@JsonFormat(shape = JsonFormat.Shape.STRING,
                    pattern = "dd/MM/yyyy") Date from,
            @QueryParam("to")@JsonFormat(shape = JsonFormat.Shape.STRING,
                    pattern = "dd/MM/yyyy") Date  to
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



    private <T> ValidationResult validate(T request) {
        Set<ConstraintViolation<T>> violations = validator.validate(request);

        if (violations.isEmpty()) {
            return new ValidationResult("Request is valid! It was validated by manual validation.");
        } else {
            return new ValidationResult(violations);
        }
    }
}