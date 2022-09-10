package com.example.service.result;

import com.example.domain.dto.MedicalRecordDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchMedicalRecordsResult {
    private List<MedicalRecordDto> medicalRecords;
}
