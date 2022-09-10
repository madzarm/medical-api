package com.example.service;

import com.example.client.DiseaseClient;
import com.example.client.PersonClient;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PersonService {

    @RestClient
    DiseaseClient diseaseClient;
    @RestClient
    PersonClient personClient;


}
