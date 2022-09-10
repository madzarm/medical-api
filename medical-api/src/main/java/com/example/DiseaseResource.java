package com.example;

import com.example.service.DiseaseService;
import com.example.service.request.CreateDiseaseRequest;
import com.example.service.result.ValidationResult;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Set;

@Path("/disease")
public class DiseaseResource {

    @Inject
    Validator validator;
    @Inject
    DiseaseService diseaseService;

    @POST
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    @Operation(summary = "Creates a disease")
    public Response createDisease(@RequestBody CreateDiseaseRequest request) {
        ValidationResult validationResult = validate(request);
        if (!validationResult.isSuccess()) {
            return Response.ok(validationResult).build();
        }
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
        return diseaseService.getDiseases(name, curable, diseaseIds);
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
        return diseaseService.updateDiseases(name, curable, diseaseId);
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
        return diseaseService.deleteDiseases(name, curable, diseaseIds);
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
