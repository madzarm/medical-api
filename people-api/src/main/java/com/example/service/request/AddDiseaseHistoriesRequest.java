package com.example.service.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddDiseaseHistoriesRequest {

    @NotNull(message = "Person id may not be null!")
    private Long personId;

    @NotEmpty(message = "Diseases ids may not be empty!")
    private List<Long> diseaseIds;
}
