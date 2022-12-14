package com.example.service;

import com.example.domain.DiseaseHistory;
import com.example.domain.Person;
import com.example.domain.converter.Converter;
import com.example.exception.exceptions.EntityNotFoundException;
import com.example.mapper.PersonMapper;
import com.example.service.request.CreatePersonRequest;
import com.example.service.request.UpdatePersonRequest;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Response;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class PersonService {

    @Inject
    PersonMapper personMapper;
    @Inject
    Converter converter;

    public Response createPerson(CreatePersonRequest request) {
        Person person = personMapper.requestToPerson(request);
        person.persist();
        return personMapper.createResponse(person);
    }

    public Response getAll() {
        List<Person> people = Person.listAll();
        checkIfEmpty(people);
        return personMapper.createResponse(people);
    }

    public Response getByIds(List<Long> personId) {
        List<Person> people = Person.listByIdsIn(personId);
        checkIfEmpty(people);
        return personMapper.createResponse(people);
    }

    public Response getByDiseaseIds(List<Long> diseaseId) {
        List<Person> people = Person.listByDiseaseIdsIn(diseaseId);
        checkIfEmpty(people);
        return personMapper.createResponse(people);
    }

    public Response getByName(String firstName, String lastName, List<Long> diseaseIds) {
        boolean hasFirstName = Objects.nonNull(firstName);
        boolean hasLastName = Objects.nonNull(lastName);
        boolean hasBoth = hasFirstName && hasLastName;

        List<Person> people;

        if (!diseaseIds.isEmpty()) {
            if (hasBoth) people = Person.listByFirstAndLastNameAndIdsIn(firstName, lastName, diseaseIds);
            else if (hasFirstName) people = Person.listByFirstNameAndIdsIn(firstName, diseaseIds);
            else people = Person.listByLastNameAndIdsIn(lastName, diseaseIds);
        } else {
            if (hasBoth) people = Person.listByFirstAndLastName(firstName, lastName);
            else if (hasFirstName) people = Person.listByFirstName(firstName);
            else people = Person.listByLastName(lastName);
        }

        checkIfEmpty(people);
        return personMapper.createResponse(people);
    }

    public Response getByAge(Integer ageLowerLimit, Integer ageUpperLimit, List<Long> diseaseIds) {
        boolean hasLower = Objects.nonNull(ageLowerLimit);
        boolean hasUpper = Objects.nonNull(ageUpperLimit);
        boolean hasBoth = hasLower && hasUpper;

        List<Person> people;

        if (!diseaseIds.isEmpty()) {
            if (hasBoth) people = Person.listByAgeLowerAndUpperAndIdsIn(ageLowerLimit, ageUpperLimit, diseaseIds);
            else if (hasLower) people = Person.listByAgeLowerAndIdsIn(ageLowerLimit, diseaseIds);
            else people = Person.listByAgeUpperAndIdsIn(ageUpperLimit, diseaseIds);
        } else {
            if (hasBoth) people = Person.listByAgeLowerAndUpper(ageLowerLimit, ageUpperLimit);
            else if (hasLower) people = Person.listByAgeLower(ageLowerLimit);
            else people = Person.listByAgeUpper(ageUpperLimit);
        }

        checkIfEmpty(people);
        return personMapper.createResponse(people);
    }

    public Response getByWeight(Integer weightLowerLimit, Integer weightUpperLimit, List<Long> diseaseIds) {
        boolean hasLower = Objects.nonNull(weightLowerLimit);
        boolean hasUpper = Objects.nonNull(weightUpperLimit);
        boolean hasBoth = hasLower && hasUpper;

        List<Person> people;

        if (!diseaseIds.isEmpty()) {
            if (hasBoth)
                people = Person.listByWeightLowerAndUpperAndIdsIn(weightLowerLimit, weightUpperLimit, diseaseIds);
            else if (hasLower) people = Person.listByWeightLowerAndIdsIn(weightLowerLimit, diseaseIds);
            else people = Person.listByWeightUpperAndIdsIn(weightUpperLimit, diseaseIds);
        } else {
            if (hasBoth) people = Person.listByWeightLowerAndUpper(weightLowerLimit, weightUpperLimit);
            else if (hasLower) people = Person.listByWeightLower(weightLowerLimit);
            else people = Person.listByWeightUpper(weightUpperLimit);
        }

        checkIfEmpty(people);
        return personMapper.createResponse(people);
    }

    public Response getByDate(String from, String to, List<Long> diseaseIds) {
        boolean hasFrom = Objects.nonNull(from);
        boolean hasTo = Objects.nonNull(to);
        boolean hasBoth = hasFrom && hasTo;

        LocalDate dateFrom = converter.convertStringToLocalDate(from);
        LocalDate dateTo = converter.convertStringToLocalDate(to);

        List<Person> people;
        if (!diseaseIds.isEmpty()) {
            if (hasBoth) people = Person.listByDateDiscoveredBetweenAndIdsIn(dateFrom, dateTo, diseaseIds);
            else if (hasFrom) people = Person.listByDateDiscoveredAfterAndIdsIn((dateFrom), diseaseIds);
            else people = Person.listByDateDiscoveredBeforeAndIdsIn((dateTo), diseaseIds);
        } else {
            if (hasBoth) people = Person.listByDateDiscoveredBetween((dateFrom), (dateTo));
            else if (hasFrom) people = Person.listByDateDiscoveredAfter((dateFrom));
            else people = Person.listByDateDiscoveredBefore((dateTo));
        }

        checkIfEmpty(people);
        return personMapper.createResponse(people);
    }

    public Response updatePerson(UpdatePersonRequest request) {
        Optional<Person> personOptional = Person.findByIdOptional(request.getPersonId());
        LocalDate date;
        if (personOptional.isEmpty())
            throw new EntityNotFoundException("Person with id: " + request.getPersonId() + " was not found!");

        Person person = personOptional.get();
        if (Objects.nonNull(request.getAge()))
            person.setAge(request.getAge());
        if (Objects.nonNull(request.getWeight()))
            person.setWeight(request.getWeight());
        if (Objects.nonNull(request.getFirstName()))
            person.setFirstName(request.getFirstName());
        if (Objects.nonNull(request.getLastName()))
            person.setLastName(request.getLastName());
        if (Objects.nonNull(request.getDiseaseHistoryId())) {
            Optional<DiseaseHistory> diseaseHistoryOptional = person.getDiseaseHistories().stream()
                    .filter(dh -> dh.getId().equals(request.getDiseaseHistoryId())).findFirst();
            if (diseaseHistoryOptional.isEmpty())
                throw new BadRequestException("Perosn with id: " + request.getPersonId() + " does not a have a " +
                        "disease history with id: " + request.getDiseaseHistoryId());
            else {
                DiseaseHistory diseaseHistory = diseaseHistoryOptional.get();
                person.removeDiseaseHistory(diseaseHistory);
                if (Objects.nonNull(request.getDateDiscovered())) {
                    date = converter.convertStringToLocalDate(request.getDateDiscovered());
                    diseaseHistory.setDateDiscovered(date);
                }
                if (Objects.nonNull(request.getDiseaseId())) {
                    List<Long> diseaseIds = getDiseaseIdsFromPersonList(List.of(person));
                    if (diseaseIds.contains(request.getDiseaseId()))
                        throw new BadRequestException("Person already has a diseaseHistory with diseaseId: " + request.getDiseaseId());
                    diseaseHistory.setDiseaseId(request.getDiseaseId());
                }
                person.addDiseaseHistories(List.of(diseaseHistory));
                diseaseHistory.persist();
            }
        } else if (request.getDiseaseId() != null) {
            List<Long> diseaseIds = getDiseaseIdsFromPersonList(List.of(person));
            if (diseaseIds.contains(request.getDiseaseId()))
                throw new BadRequestException("Person already has a diseaseHistory with diseaseId: " + request.getDiseaseId());
            DiseaseHistory diseaseHistory = new DiseaseHistory();
            diseaseHistory.setDiseaseId(request.getDiseaseId());
            if (request.getDateDiscovered() == null)
                diseaseHistory.setDateDiscovered(LocalDate.now());
            else
                diseaseHistory.setDateDiscovered(converter.convertStringToLocalDate(request.getDateDiscovered()));
            person.addDiseaseHistories(List.of(diseaseHistory));
            diseaseHistory.persist();
        }

        return personMapper.createResponse(person);
    }

    public Response deletePerson(Long personId) {
        Optional<Person> person = Person.findByIdOptional(personId);
        return person.map(p -> {
            p.delete();
            return personMapper.createResponse(p);
        }).orElseThrow(() -> new EntityNotFoundException("Person with id: " + personId + " not found!"));
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
            }).orElseThrow(() -> new EntityNotFoundException("Disease history with id: " + diseaseHistoryId + " not found!"));
        }).orElseThrow(() -> new EntityNotFoundException("Person with id: " + personId + " not found!"));
    }

    private void checkIfEmpty(List<Person> people) {
        if (people.isEmpty())
            throw new EntityNotFoundException();
    }

    private List<Long> getDiseaseIdsFromPersonList(List<Person> people) {
        List<Long> distinctLongs = new ArrayList<>();
        List<List<Long>> longs = people.stream()
                .map(p -> p.getDiseaseHistories().stream()
                        .map(DiseaseHistory::getDiseaseId)
                        .collect(Collectors.toList()))
                .collect(Collectors.toList());

        longs.forEach(l -> l.forEach(lng -> {
            if (!distinctLongs.contains(lng))
                distinctLongs.add(lng);
        }));

        return distinctLongs;
    }
}