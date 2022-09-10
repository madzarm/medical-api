package com.example.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Person extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    @JsonIgnore
    private Long id;
    private String firstName;
    private String lastName;
    private int weight;
    private int age;
    @OneToMany(mappedBy = "person", cascade = {CascadeType.REMOVE, CascadeType.PERSIST}, orphanRemoval = true)
    private List<DiseaseHistory> diseaseHistories = new ArrayList<>();

    public static List<Person> listByIdsIn(List<Long> ids) {
        return Person.list("id in ?1", ids);
    }

    public static List<Person> listByDiseaseIdsIn(List<Long> ids) {
        return Person.list("select p from Person p left join fetch p.diseaseHistories d where d.diseaseId in ?1", ids);
    }

    public static List<Person> listByFirstAndLastName(String firstName, String lastName) {
        return Person.list("firstName = ?1 and lastName = ?2", firstName, lastName);
    }


    ///////////////////////////PANACHE QUERIES///////////////////////////////

    public static List<Person> listByFirstName(String firstName) {
        return Person.list("firstName", firstName);
    }

    public static List<Person> listByLastName(String lastName) {
        return Person.list("lastName", lastName);
    }

    public static List<Person> listByAgeLowerAndUpper(Integer lowerLimit, Integer upperLimit) {
        return Person.list("age > ?1 and age < ?2", lowerLimit, upperLimit);
    }

    public static List<Person> listByAgeLower(Integer lowerLimit) {
        return Person.list("age > ?1", lowerLimit);
    }

    public static List<Person> listByAgeUpper(Integer upperLimit) {
        return Person.list("age < ?1", upperLimit);
    }

    public static List<Person> listByWeightLowerAndUpper(Integer lowerLimit, Integer upperLimit) {
        return Person.list("weight > ?1 and weight < ?2", lowerLimit, upperLimit);
    }

    public static List<Person> listByWeightLower(Integer lowerLimit) {
        return Person.list("weight > ?1", lowerLimit);
    }

    public static List<Person> listByWeightUpper(Integer upperLimit) {
        return Person.list("weight < ?1", upperLimit);
    }

    public static List<Person> listByDateDiscoveredBetween(LocalDate from, LocalDate to) {
        return Person.list("select p from Person p left join fetch p.diseaseHistories d where d.dateDiscovered between ?1 and ?2", from, to);
    }

    public static List<Person> listByDateDiscoveredAfter(LocalDate from) {
        return Person.list("select p from Person p left join fetch p.diseaseHistories d where d.dateDiscovered >= ?1", from);
    }

    public static List<Person> listByDateDiscoveredBefore(LocalDate to) {
        return Person.list("select p from Person p left join fetch p.diseaseHistories d where d.dateDiscovered <= ?1", to);
    }

    public void setDiseaseHistories(List<DiseaseHistory> diseaseHistories) {
        this.diseaseHistories = diseaseHistories;
        diseaseHistories.forEach(dh -> dh.setPerson(this));
    }

    public void addDiseaseHistories(List<DiseaseHistory> diseaseHistories) {
        this.diseaseHistories.addAll(diseaseHistories);
        diseaseHistories.forEach(dh -> dh.setPerson(this));
    }

    public void removeDiseaseHistory(DiseaseHistory diseaseHistory) {
        this.diseaseHistories.remove(diseaseHistory);
        diseaseHistory.setPerson(null);
    }
}
