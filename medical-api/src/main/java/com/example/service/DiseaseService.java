package com.example.service;

import com.example.client.DiseaseClient;
import com.example.service.request.CreateDiseaseRequest;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.Response;
import java.util.List;

@ApplicationScoped
public class DiseaseService {

    @RestClient
    DiseaseClient diseaseClient;

    public Response createDisease(CreateDiseaseRequest request) {

        return diseaseClient.createDisease(request);
    }

    public Response getDiseases(String name, Boolean curable, List<Long> ids) {

        return diseaseClient.getDiseases(name, curable, ids);
    }

    public Response deleteDiseases(String name, Boolean curable, List<Long> ids) {

        return diseaseClient.deleteDiseases(name, curable, ids);
    }

    public Response updateDiseases(String name, Boolean curable, Long id) {

        return diseaseClient.updateDiseases(name, curable, id);
    }


}
