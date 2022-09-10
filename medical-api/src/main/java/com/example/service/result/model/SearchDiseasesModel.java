package com.example.service.result.model;

import com.example.domain.DiseaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchDiseasesModel {
    private List<DiseaseModel> diseases;
}
