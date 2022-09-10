package com.example.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicalRecordDto {
    private Long personId;
    private String firstName;
    private String lastName;
    private Integer weight;
    private Integer age;
    private List<DiseaseHistoryDto> diseaseHistories;
}
