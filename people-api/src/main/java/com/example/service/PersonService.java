package com.example.service;

import com.example.domain.DiseaseHistory;
import com.example.domain.Person;
import com.example.domain.dto.DiseaseHistoryDto;
import com.example.domain.dto.PersonDto;
import com.example.exception.exceptions.DateParseException;
import com.example.exception.exceptions.EntityNotFoundException;
import com.example.service.request.CreatePersonRequest;
import com.example.service.request.UpdatePersonRequest;
import com.example.service.result.SearchPeopleResult;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Response;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
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

    public Response getByDate(String from, String to, List<Long> diseaseIds) {
        boolean hasFrom = Objects.nonNull(from);
        boolean hasTo = Objects.nonNull(to);
        boolean hasBoth = hasFrom && hasTo;

        LocalDate dateFrom = convertStringToLocalDate(from);
        LocalDate dateTo = convertStringToLocalDate(to);

        List<Person> people;
        if(!diseaseIds.isEmpty()) {
            if (hasBoth) people = Person.listByDateDiscoveredBetweenAndIdsIn(dateFrom, dateTo,diseaseIds);
            else if (hasFrom) people = Person.listByDateDiscoveredAfterAndIdsIn((dateFrom),diseaseIds);
            else people = Person.listByDateDiscoveredBeforeAndIdsIn((dateTo),diseaseIds);
        } else {
            if (hasBoth) people = Person.listByDateDiscoveredBetween((dateFrom), (dateTo));
            else if (hasFrom) people = Person.listByDateDiscoveredAfter((dateFrom));
            else people = Person.listByDateDiscoveredBefore((dateTo));
        }

        return checkIfEmptyAndConvertToResult(people,true,true);
    }

    public Response updatePerson(UpdatePersonRequest request) {
        Optional<Person> personOptional = Person.findByIdOptional(request.getPersonId());
        LocalDate date;
        if (personOptional.isEmpty())
            throw new EntityNotFoundException("Person with id: " + request.getPersonId() + " was not found!");

        Person person = personOptional.get();
        if (Objects.nonNull(request.getAge()))
            person.setAge(request.getAge());
        if(Objects.nonNull(request.getWeight()))
            person.setWeight(request.getWeight());
        if(Objects.nonNull(request.getFirstName()))
            person.setFirstName(request.getFirstName());
        if(Objects.nonNull(request.getLastName()))
            person.setLastName(request.getLastName());
        if(Objects.nonNull(request.getDiseaseHistoryId())) {
            Optional<DiseaseHistory> diseaseHistoryOptional = person.getDiseaseHistories().stream()
                    .filter(dh -> dh.getId().equals(request.getDiseaseHistoryId())).findFirst();
            if(diseaseHistoryOptional.isEmpty())
                throw new BadRequestException("Perosn with id: " + request.getPersonId() + " does not a have a " +
                        "disease history with id: " + request.getDiseaseHistoryId());
            else {
                DiseaseHistory diseaseHistory = diseaseHistoryOptional.get();
                person.removeDiseaseHistory(diseaseHistory);
                if(Objects.nonNull(request.getDateDiscovered())) {
                    date = convertStringToLocalDate(request.getDateDiscovered());
                    diseaseHistory.setDateDiscovered(date);
                }
                if(Objects.nonNull(request.getDiseaseId())) {
                    List<Long> diseaseIds = getDiseaseIdsFromPersonList(List.of(person));
                    if(diseaseIds.contains(request.getDiseaseId()))
                        throw new BadRequestException("Person already has a diseaseHistory with diseaseId: " + request.getDiseaseId());
                    diseaseHistory.setDiseaseId(request.getDiseaseId());
                }
                person.addDiseaseHistories(List.of(diseaseHistory));
                diseaseHistory.persist();
            }
        } else if (request.getDiseaseId() != null) {
            List<Long> diseaseIds = getDiseaseIdsFromPersonList(List.of(person));
            if(diseaseIds.contains(request.getDiseaseId()))
                throw new BadRequestException("Person already has a diseaseHistory with diseaseId: " + request.getDiseaseId());
            DiseaseHistory diseaseHistory = new DiseaseHistory();
            diseaseHistory.setDiseaseId(request.getDiseaseId());
            if(request.getDateDiscovered() == null)
                diseaseHistory.setDateDiscovered(LocalDate.now());
            else
                diseaseHistory.setDateDiscovered(convertStringToLocalDate(request.getDateDiscovered()));
            person.addDiseaseHistories(List.of(diseaseHistory));
            diseaseHistory.persist();
        }
        return checkIfEmptyAndConvertToResult(List.of(person),true,true);
    }

    public Response deletePerson(Long personId) {
        Optional<Person> person = Person.findByIdOptional(personId);
        return person.map(p -> {
            p.delete();
            return Response.ok(p).build();
        }).orElseThrow(() -> new EntityNotFoundException(personId));
    }

    public Response deleteDiseaseHistory(Long personId, Long diseaseHistoryId) {
        Optional<Person> person = Person.findByIdOptional(personId);
        return person.map(p -> {
            Optional<DiseaseHistory> dhOptional = p.getDiseaseHistories().stream()
                    .filter(dh -> dh.getId().equals(diseaseHistoryId))
                    .findFirst();
            return dhOptional.map(dh -> {
                p.removeDiseaseHistory(dh);
                return Response.ok(dh).build();
            }).orElseThrow(() -> new EntityNotFoundException(diseaseHistoryId));
        }).orElseThrow(() -> new EntityNotFoundException(personId));
    }

    private Person convertRequestToPerson(CreatePersonRequest request) {
        List<DiseaseHistory> diseaseHistories = new ArrayList<>();
        if(!request.getDiseaseIds().isEmpty()) {
            diseaseHistories = request.getDiseaseIds().stream()
                    .map(id -> {
                        DiseaseHistory diseaseHistory = DiseaseHistory.builder()
                                .diseaseId(id)
                                .build();
                        if(request.getDateDiscovered() == null)
                           diseaseHistory.setDateDiscovered(LocalDate.now());
                        else
                            diseaseHistory.setDateDiscovered(convertStringToLocalDate(request.getDateDiscovered()));
                        return diseaseHistory;
                    })
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
                    .dateDiscovered(dh.getDateDiscovered())
                    .build();
            if(includeIds)
                dto.setId(dh.getId());
            return dto;
        }).collect(Collectors.toList());
    }
    public LocalDate convertDateToLocalDate(Date dateToConvert) {
        if(dateToConvert == null)
            return null;
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

    private Date convertStringToDate(String date) {
        if(date == null)
            return null;
        try {
            return new SimpleDateFormat("dd-MM-yyyy").parse(date);
        } catch (Exception e) {
            try {
                return new SimpleDateFormat("dd/MM/yyyy").parse(date);
            } catch (Exception e1) {
                try {
                    return new SimpleDateFormat("dd.MM.yyyy").parse(date);
                } catch (Exception e2) {
                    try {
                        return new SimpleDateFormat("ddMMyyyy").parse(date);
                    } catch (Exception e3) {
                        try {
                            return new SimpleDateFormat("yyyy-MM-dd").parse(date);
                        } catch (Exception e4) {
                            try {
                                return new SimpleDateFormat("yyyy/MM/dd").parse(date);
                            } catch (Exception e5) {
                                try {
                                    return new SimpleDateFormat("yyyy.MM.dd").parse(date);
                                } catch (Exception e6) {
                                    try {
                                        return new SimpleDateFormat("yyyyMMdd").parse(date);
                                    } catch (Exception e7) {
                                        throw new DateParseException(date);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    private LocalDate convertStringToLocalDate(String date) {
        return convertDateToLocalDate(convertStringToDate(date));
    }

    private List<Long> getDiseaseIdsFromPersonList(List<Person> people) {
        List<Long> distinctLongs = new ArrayList<>();
        List<List<Long>> longs = people.stream()
                .map(p -> p.getDiseaseHistories().stream()
                        .map(DiseaseHistory::getDiseaseId)
                        .collect(Collectors.toList()))
                .collect(Collectors.toList());

        longs.forEach(l -> l.forEach(lng -> {
            if(!distinctLongs.contains(lng))
                distinctLongs.add(lng);
        }));

        return distinctLongs;
    }


}