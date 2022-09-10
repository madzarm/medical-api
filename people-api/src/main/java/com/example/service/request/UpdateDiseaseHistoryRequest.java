package com.example.service.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateDiseaseHistoryRequest {

    @NotNull(message = "PersonId may not be null!")
    private Long personId;

    @NotNull(message = "DiseaseHistoryId may not be null!")
    private Long diseaseHistoryId;

    @NotNull(message = "NewDiseaseId may not be null!")
    private Long newDiseaseId;
}
