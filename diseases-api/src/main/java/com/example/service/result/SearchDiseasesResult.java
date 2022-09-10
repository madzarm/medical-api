package com.example.service.result;

import com.example.domain.dto.DiseaseDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class SearchDiseasesResult {
    private List<DiseaseDto> diseases;
}
