package com.example.domain.dto;

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
public class PersonDto {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long id;
    private String firstName;
    private String lastName;
    private Integer weight;
    private Integer age;
    private List<DiseaseHistoryDto> diseaseHistories = new ArrayList<>();
}
