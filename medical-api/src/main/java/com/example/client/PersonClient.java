package com.example.client;

import com.example.service.request.CreatePersonRequest;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Date;
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
            @QueryParam("from") @JsonFormat(shape = JsonFormat.Shape.STRING,
                    pattern = "dd/MM/yyyy") Date from,
            @QueryParam("to") @JsonFormat(shape = JsonFormat.Shape.STRING,
                    pattern = "dd/MM/yyyy") Date to
    );
}
