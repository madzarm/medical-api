package com.example.client;

import com.example.service.request.CreatePersonRequest;
import com.example.service.request.UpdatePersonRequest;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@RegisterRestClient(configKey = "person-client")
@Path("/person")
public interface PersonClient {

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    Response getAllPeople();

    @POST
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    Response createPerson(@RequestBody CreatePersonRequest request);

    @PUT
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    @Operation(summary = "updates a person by id")
    Response updatePerson(@Valid @RequestBody UpdatePersonRequest request);

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    Response getPeople(
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
    );

    @DELETE
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    Response deletePerson(
            @QueryParam("personId") Long personId,
            @QueryParam("diseaseHistoryId") Long diseaseHistoryId
    );
}
