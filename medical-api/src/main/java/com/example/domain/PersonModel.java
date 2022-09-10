package com.example.domain;

import com.example.domain.dto.DiseaseHistoryDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonModel {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long id;
    private String firstName;
    private String lastName;
    private Integer weight;
    private Integer age;
    private List<DiseaseHistoryModel> diseaseHistories = new ArrayList<>();
}
