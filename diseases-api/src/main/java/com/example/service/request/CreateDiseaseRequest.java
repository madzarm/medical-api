package com.example.service.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateDiseaseRequest {
    @NotBlank(message = "Name may not be blank!")
    private String name;
    @NotNull(message = "Curable may not be null!")
    private Boolean curable;
}
