package com.example.client;

import com.example.service.request.CreateDiseaseRequest;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@RegisterRestClient(configKey = "disease-client")
@Path("/disease")
public interface DiseaseClient {

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    Response getAllDiseases();

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    Response getDiseasesByName(@QueryParam("name") String name);

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    Response getDiseasesByCurable(@QueryParam("curable") Boolean curable);

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    Response getDiseasesByIds(@QueryParam("diseaseIds") List<Long> diseaseIds);

    @POST
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    Response createDisease(@RequestBody CreateDiseaseRequest request);

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    Response getDiseases(
            @QueryParam("name") String name,
            @QueryParam("curable") Boolean curable,
            @QueryParam("diseaseIds") List<Long> diseaseIds
    );

    @DELETE
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    Response deleteDiseases(
            @QueryParam("name") String name,
            @QueryParam("curable") Boolean curable,
            @QueryParam("diseaseIds") List<Long> diseaseIds
    );

    @PUT
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    Response updateDiseases(
            @QueryParam("name") String name,
            @QueryParam("curable") Boolean curable,
            @QueryParam("diseaseId") Long diseaseId
    );


}
