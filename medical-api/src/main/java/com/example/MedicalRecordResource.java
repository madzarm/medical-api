package com.example;

import com.example.exception.exceptions.BadCreateMedicalRecordRequestException;
import com.example.exception.exceptions.BadRequestException;
import com.example.exception.exceptions.BadSearchMedicalRecordRequestException;
import com.example.exception.validation.Date;
import com.example.service.MedicalRecordService;
import com.example.service.request.CreateMedicalRecordRequest;
import com.example.service.request.GetMedicalRecordsRequest;
import com.example.service.request.UpdateMedicalRecordRequest;
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

@Path("/medicalRecord")
public class MedicalRecordResource {

    @Inject
    MedicalRecordService service;

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
            @QueryParam("from") @Date String from,
            @QueryParam("to") @Date String to,
            @QueryParam("diseaseName") String diseaseName
    ) {

        boolean hasPersonId = Objects.nonNull(personIds) && !personIds.isEmpty();
        boolean hasDiseaseId = Objects.nonNull(diseaseIds) && !diseaseIds.isEmpty();
        boolean hasName = Objects.nonNull(firstName) || Objects.nonNull(lastName);
        boolean hasDiseaseName = Objects.nonNull(diseaseName);

        if (hasDiseaseName && (hasPersonId || hasDiseaseId || hasName))
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
    public Response createMedicalRecord(@RequestBody @Valid CreateMedicalRecordRequest request) {
        if (!request.getDiseaseIds().isEmpty() && request.getDiseaseName() != null)
            throw new BadCreateMedicalRecordRequestException();

        return service.createMedicalRecord(request);
    }

    @DELETE
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    @Operation(
            summary = "Deletes a medical record or a disease history",
            description = "Send only a personId if you want to delete a whole medical record, " +
                    "or send a personId with a diseaseHistoryId to only delete that person's diseasehistory"
    )
    public Response deleteMedicalRecord(
            @QueryParam("personId") Long personId,
            @QueryParam("diseaseHistoryId") Long diseaseHistoryId
    ) {
        if(Objects.isNull(personId))
            throw new BadRequestException("PersonId is required!");
        return service.deleteMedicalRecord(personId, diseaseHistoryId);
    }


    @PUT
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    @Operation(summary = "updates a person by id")
    public Response updateMedicalRecord(@Valid @RequestBody UpdateMedicalRecordRequest request) {
        boolean hasDiseaseId = Objects.nonNull(request.getDiseaseId());
        boolean hasDiseaseName = Objects.nonNull(request.getDiseaseName());

        if(hasDiseaseName && hasDiseaseId) {
            throw new BadRequestException("You can not have both diseaseName and diseaseId");
        }

        return service.updateMedicalRecord(request);
    }

}