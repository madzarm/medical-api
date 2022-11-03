package com.example.mapper;

import com.example.domain.DiseaseHistory;
import com.example.domain.Person;
import com.example.domain.converter.Converter;
import com.example.domain.dto.PersonDto;
import com.example.service.request.CreatePersonRequest;
import com.example.service.result.SearchPeopleResult;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


@org.mapstruct.Mapper(componentModel = "cdi", uses = {DiseaseHistoryMapper.class})
public abstract class PersonMapper {

    @Inject
    Converter converter;

    public Person requestToPerson(CreatePersonRequest request) {
        Person person = Person.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .age(request.getAge())
                .weight(request.getWeight())
                .build();

        List<DiseaseHistory> diseaseHistories = request.getDiseaseIds().stream()
                .map(id -> {
                    DiseaseHistory diseaseHistory = new DiseaseHistory();
                    diseaseHistory.setDiseaseId(id);
                    if (request.getDateDiscovered() != null)
                        diseaseHistory.setDateDiscovered(converter.convertStringToLocalDate(request.getDateDiscovered()));
                    else
                        diseaseHistory.setDateDiscovered(LocalDate.now());
                    return diseaseHistory;
                }).collect(Collectors.toList());

        person.setDiseaseHistories(diseaseHistories);
        return person;
    }

    public Response createResponse(List<Person> people) {
        List<PersonDto> personDtoList = personToDto(people);
        SearchPeopleResult result = new SearchPeopleResult(personDtoList);
        return Response.ok(result).build();
    }

    public Response createResponse(Person person) {
        PersonDto personDto = personToDto(person);
        SearchPeopleResult result = new SearchPeopleResult(List.of(personDto));
        return Response.ok(result).build();
    }

    public abstract PersonDto personToDto(Person person);

    public abstract List<PersonDto> personToDto(List<Person> people);

}
