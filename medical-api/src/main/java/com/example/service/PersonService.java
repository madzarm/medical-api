package com.example.service;

import com.example.client.DiseaseClient;
import com.example.client.PersonClient;
import com.example.domain.DiseaseHistoryModel;
import com.example.domain.DiseaseModel;
import com.example.domain.PersonModel;
import com.example.domain.dto.DiseaseHistoryDto;
import com.example.domain.dto.MedicalRecordDto;
import com.example.exception.ExceptionResponse;
import com.example.exception.exceptions.DiseaseServiceException;
import com.example.exception.exceptions.DownstreamServiceException;
import com.example.exception.exceptions.PersonServiceException;
import com.example.service.request.CreatePersonRequest;
import com.example.service.result.SearchDiseaseResult;
import com.example.service.result.SearchMedicalRecordsResult;
import com.example.service.result.model.SearchDiseasesModel;
import com.example.service.result.model.SearchPeopleModel;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class PersonService {

    @RestClient
    DiseaseClient diseaseClient;
    @RestClient
    PersonClient personClient;






}
