package com.example.service;

import com.example.domain.DiseaseHistory;
import com.example.domain.Person;
import com.example.domain.dto.DiseaseHistoryDto;
import com.example.domain.dto.PersonDto;
import com.example.exception.exceptions.DiseaseAlreadyExistsException;
import com.example.exception.exceptions.DiseaseHistoryDoesNotExistException;
import com.example.exception.exceptions.EntityNotFoundException;
import com.example.service.request.AddDiseaseHistoriesRequest;
import com.example.service.request.CreatePersonRequest;
import com.example.service.request.UpdateDiseaseHistoryRequest;
import com.example.service.request.UpdatePersonRequest;
import com.example.service.result.SearchPeopleResult;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.Response;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@ApplicationScoped
public class PersonService {

    public Response createPerson(CreatePersonRequest request) {
        Person person = convertRequestToPerson(request);
        person.persist();

        List<PersonDto> personDtos = convertPeopleToDtos(List.of(person),true,true);
        SearchPeopleResult result = new SearchPeopleResult(personDtos);

        return Response.status(201).entity(result).build();
    }

    public Response addDiseaseHistory(AddDiseaseHistoriesRequest request) {
        Optional<Person> personOptional = Person.findByIdOptional(request.getPersonId());
        if(personOptional.isEmpty())
            throw new EntityNotFoundException();

        Person person = personOptional.get();
        List<DiseaseHistory> diseaseHistories = convertRequestToDiseaseHistory(request);
        if(checkIfPersonAlreadyContainsThatDiseaseHistories(person,diseaseHistories))
            throw new DiseaseAlreadyExistsException();

        person.addDiseaseHistories(diseaseHistories);
        person.persist();

        return checkIfEmptyAndConvertToResult(List.of(person),true,true);
    }

    public Response getAll() {
        List<Person> people = Person.listAll();

        return checkIfEmptyAndConvertToResult(people,true,true);
    }
    public Response getByIds(List<Long> personId) {
        List<Person> people = Person.listByIdsIn(personId);

        return checkIfEmptyAndConvertToResult(people,true,true);
    }

    public Response getByDiseaseIds(List<Long> diseaseId) {
        List<Person> people = Person.listByDiseaseIdsIn(diseaseId);

        return checkIfEmptyAndConvertToResult(people,true,true);
    }

    public Response getByName(String firstName, String lastName, List<Long> diseaseIds) {
        boolean hasFirstName = Objects.nonNull(firstName);
        boolean hasLastName = Objects.nonNull(lastName);
        boolean hasBoth = hasFirstName && hasLastName;

        List<Person> people;

        if(!diseaseIds.isEmpty()){
            if(hasBoth) people = Person.listByFirstAndLastNameAndIdsIn(firstName,lastName,diseaseIds);
            else if (hasFirstName) people = Person.listByFirstNameAndIdsIn(firstName,diseaseIds);
            else people = Person.listByLastNameAndIdsIn(lastName,diseaseIds);
        } else {
            if(hasBoth) people = Person.listByFirstAndLastName(firstName,lastName);
            else if (hasFirstName) people = Person.listByFirstName(firstName);
            else people = Person.listByLastName(lastName);
        }

        return checkIfEmptyAndConvertToResult(people,true,true);
    }

    public Response getByAge(Integer ageLowerLimit, Integer ageUpperLimit, List<Long> diseaseIds) {
        boolean hasLower = Objects.nonNull(ageLowerLimit);
        boolean hasUpper = Objects.nonNull(ageUpperLimit);
        boolean hasBoth = hasLower && hasUpper;

        List<Person> people;

        if(!diseaseIds.isEmpty()) {
            if(hasBoth) people = Person.listByAgeLowerAndUpperAndIdsIn(ageLowerLimit,ageUpperLimit,diseaseIds);
            else if (hasLower) people = Person.listByAgeLowerAndIdsIn(ageLowerLimit,diseaseIds);
            else people = Person.listByAgeUpperAndIdsIn(ageUpperLimit,diseaseIds);
        } else {
            if(hasBoth) people = Person.listByAgeLowerAndUpper(ageLowerLimit,ageUpperLimit);
            else if (hasLower) people = Person.listByAgeLower(ageLowerLimit);
            else people = Person.listByAgeUpper(ageUpperLimit);
        }

        return checkIfEmptyAndConvertToResult(people,true,true);
    }

    public Response getByWeight(Integer weightLowerLimit, Integer weightUpperLimit, List<Long> diseaseIds) {
        boolean hasLower = Objects.nonNull(weightLowerLimit);
        boolean hasUpper = Objects.nonNull(weightUpperLimit);
        boolean hasBoth = hasLower && hasUpper;

        List<Person> people;

        if(!diseaseIds.isEmpty()) {
            if(hasBoth) people = Person.listByWeightLowerAndUpperAndIdsIn(weightLowerLimit,weightUpperLimit,diseaseIds);
            else if (hasLower) people = Person.listByWeightLowerAndIdsIn(weightLowerLimit,diseaseIds);
            else people = Person.listByWeightUpperAndIdsIn(weightUpperLimit,diseaseIds);
        } else {
            if(hasBoth) people = Person.listByWeightLowerAndUpper(weightLowerLimit,weightUpperLimit);
            else if (hasLower) people = Person.listByWeightLower(weightLowerLimit);
            else people = Person.listByWeightUpper(weightUpperLimit);
        }

        return checkIfEmptyAndConvertToResult(people,true,true);
    }

    public Response getByDate(Date from, Date to, List<Long> diseaseIds) {
        boolean hasFrom = Objects.nonNull(from);
        boolean hasTo = Objects.nonNull(to);
        boolean hasBoth = hasFrom && hasTo;

        List<Person> people;
        if(!diseaseIds.isEmpty()) {
            if (hasBoth) people = Person.listByDateDiscoveredBetweenAndIdsIn(convertDateToLocalDate(from), convertDateToLocalDate(to),diseaseIds);
            else if (hasFrom) people = Person.listByDateDiscoveredAfterAndIdsIn(convertDateToLocalDate(from),diseaseIds);
            else people = Person.listByDateDiscoveredBeforeAndIdsIn(convertDateToLocalDate(to),diseaseIds);
        } else {
            if (hasBoth) people = Person.listByDateDiscoveredBetween(convertDateToLocalDate(from), convertDateToLocalDate(to));
            else if (hasFrom) people = Person.listByDateDiscoveredAfter(convertDateToLocalDate(from));
            else people = Person.listByDateDiscoveredBefore(convertDateToLocalDate(to));
        }

        return checkIfEmptyAndConvertToResult(people,true,true);
    }

    public Response updatePerson(UpdatePersonRequest request) {
        Optional<Person> personOptional = Person.findByIdOptional(request.getPersonId());
        if (personOptional.isEmpty())
            throw new EntityNotFoundException();

        Person person = personOptional.get();
        if (Objects.nonNull(request.getAge()))
            person.setAge(request.getAge());
        if(Objects.nonNull(request.getWeight()))
            person.setWeight(request.getWeight());
        if(Objects.nonNull(request.getFirstName()))
            person.setFirstName(request.getFirstName());
        if(Objects.nonNull(request.getLastName()))
            person.setLastName(request.getLastName());

        person.persist();
        return checkIfEmptyAndConvertToResult(List.of(person),true,true);
    }
    public Response updateDiseaseHistory(UpdateDiseaseHistoryRequest request) {

        Optional<Person> personOptional = Person.findByIdOptional(request.getPersonId());
        if (personOptional.isEmpty())
            throw new EntityNotFoundException("Person with id:  \""+ request.getPersonId() + "\" not found!");

        Person person = personOptional.get();
        Optional<DiseaseHistory> diseaseHistoryOptional = person.getDiseaseHistories().stream()
                .filter(dh -> dh.getId().equals(request.getDiseaseHistoryId()))
                .findFirst();

        if(diseaseHistoryOptional.isEmpty())
            throw new DiseaseHistoryDoesNotExistException(request.getDiseaseHistoryId());

        DiseaseHistory diseaseHistory = diseaseHistoryOptional.get();
        diseaseHistory.setDiseaseId(request.getNewDiseaseId());

        person.persist();

        return checkIfEmptyAndConvertToResult(List.of(person),true,true);
    }

    private Person convertRequestToPerson(CreatePersonRequest request) {
        List<DiseaseHistory> diseaseHistories = new ArrayList<>();
        if(!request.getDiseaseIds().isEmpty()) {
            diseaseHistories = request.getDiseaseIds().stream()
                    .map(id -> DiseaseHistory.builder()
                            .diseaseId(id)
                            .dateDiscovered(LocalDate.now())
                            .build())
                    .collect(Collectors.toList());
        }

        Person person = Person.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .age(request.getAge())
                .weight(request.getWeight())
                .build();

        person.setDiseaseHistories(diseaseHistories);
        return person;
    }

    private List<DiseaseHistory> convertRequestToDiseaseHistory(AddDiseaseHistoriesRequest request) {
        return request.getDiseaseIds().stream()
                .map(id -> DiseaseHistory.builder()
                        .diseaseId(id)
                        .dateDiscovered(LocalDate.now())
                        .build())
                .collect(Collectors.toList());
    }

    private List<PersonDto> convertPeopleToDtos(List<Person> people, boolean includePersonIds, boolean includeDiseaseHistoryIds) {
        return people.stream().map(person -> {
            PersonDto personDto = PersonDto.builder()
                    .firstName(person.getFirstName())
                    .lastName(person.getLastName())
                    .weight(person.getWeight())
                    .age(person.getAge())
                    .diseaseHistories(convertDiseaseHistoriesToDtos(person.getDiseaseHistories(),includeDiseaseHistoryIds))
                    .build();
            if(includePersonIds)
                personDto.setId(person.getId());
            return personDto;
        }).collect(Collectors.toList());
    }

    private List<DiseaseHistoryDto> convertDiseaseHistoriesToDtos(List<DiseaseHistory> diseaseHistories, boolean includeIds) {
        return diseaseHistories.stream().map(dh -> {
            DiseaseHistoryDto dto = DiseaseHistoryDto.builder()
                    .diseaseId(dh.getDiseaseId())
                    .dateDiscovered(LocalDate.now())
                    .build();
            if(includeIds)
                dto.setId(dh.getId());
            return dto;
        }).collect(Collectors.toList());
    }
    public LocalDate convertDateToLocalDate(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    private Response checkIfEmptyAndConvertToResult(List<Person> people, boolean includePersonIds, boolean includeDiseaseHistoryIds ) {
        if(people.isEmpty())
            throw new EntityNotFoundException();

        List<PersonDto> personDtos = convertPeopleToDtos(people,includePersonIds,includeDiseaseHistoryIds);
        SearchPeopleResult result = new SearchPeopleResult(personDtos);

        return Response.ok().entity(result).build();
    }

    private boolean checkIfPersonAlreadyContainsThatDiseaseHistories(Person person, List<DiseaseHistory> diseaseHistories){
        AtomicBoolean contains = new AtomicBoolean(false);
        if(diseaseHistories.get(0).getId()!=null){
            diseaseHistories.forEach(dh -> {
                if(person.getDiseaseHistories().stream()
                        .mapToLong(DiseaseHistory::getId)
                        .anyMatch(l -> l==dh.getId())) {
                    contains.set(true);
                }
            });
        } else {
            diseaseHistories.forEach(dh -> {
                if (person.getDiseaseHistories().stream()
                        .mapToLong(DiseaseHistory::getDiseaseId)
                        .anyMatch(l -> l == dh.getDiseaseId()))
                    contains.set(true);
            });
        }
        return contains.get();
    }
}