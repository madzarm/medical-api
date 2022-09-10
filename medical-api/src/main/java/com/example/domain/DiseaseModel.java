package com.example.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class DiseaseModel {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long id;
    private String name;
    private Boolean curable;
}