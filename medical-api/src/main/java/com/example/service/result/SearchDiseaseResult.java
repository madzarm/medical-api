package com.example.service.result;

import com.example.domain.dto.DiseaseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchDiseaseResult {
    List<DiseaseDto> diseases;
}
