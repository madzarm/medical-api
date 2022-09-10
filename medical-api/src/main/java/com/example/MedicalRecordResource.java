package com.example;

import com.example.exception.exceptions.BadCreateMedicalRecordRequestException;
import com.example.exception.exceptions.BadSearchMedicalRecordRequestException;
import com.example.service.MedicalRecordService;
import com.example.service.request.CreateMedicalRecordRequest;
import com.example.service.request.CreatePersonRequest;
import com.example.service.request.GetMedicalRecordsRequest;
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

@Path("/medicalRecord")
public class MedicalRecordResource {

    @Inject
    MedicalRecordService service;
    @Inject
    Validator validator;

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Gets medical records.")
    public Response getMedicalRecords(
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
                    pattern = "dd/MM/yyyy") Date to,
            @QueryParam("diseaseName") String diseaseName
    ) {
        boolean hasPersonId = Objects.nonNull(personIds) && !personIds.isEmpty();
        boolean hasDiseaseId = Objects.nonNull(diseaseIds) && !diseaseIds.isEmpty();
        boolean hasName = Objects.nonNull(firstName) || Objects.nonNull(lastName);
        boolean hasDiseaseName = Objects.nonNull(diseaseName);

        if(hasDiseaseName && (hasPersonId || hasDiseaseId || hasName))
                throw new BadSearchMedicalRecordRequestException();

        GetMedicalRecordsRequest request = new GetMedicalRecordsRequest(personIds, diseaseIds, firstName, lastName,
                weightLowerLimit, weightUpperLimit, ageLowerLimit, ageUpperLimit, from, to, diseaseName);

        return service.getMedicalRecords(request);
    }

    @POST
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    @Operation(summary = "Creates a medical record.")
    public Response createMedicalRecord(@RequestBody CreateMedicalRecordRequest request) {
        if(!request.getDiseaseIds().isEmpty() && request.getDiseaseName()!=null)
            throw new BadCreateMedicalRecordRequestException();

        ValidationResult validationResult = validate(request);
        if(!validationResult.isSuccess()) {
            return Response.ok(validationResult).build();
        }
        return service.createMedicalRecord(request);
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