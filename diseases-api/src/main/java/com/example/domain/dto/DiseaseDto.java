package com.example.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class DiseaseDto {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long id;
    private String name;
    private Boolean curable;
}
