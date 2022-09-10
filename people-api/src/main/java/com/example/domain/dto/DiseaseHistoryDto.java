package com.example.domain.dto;

import com.example.domain.Person;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiseaseHistoryDto {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long id;
    private LocalDate dateDiscovered;
    private Long diseaseId;
}
