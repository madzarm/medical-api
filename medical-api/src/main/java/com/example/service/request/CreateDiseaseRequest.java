package com.example.service.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateDiseaseRequest {

    @Pattern(regexp = "^[A-Za-z]+$", message = "You may only use alphabetic characters [a-zA-z]")
    @NotBlank(message = "Name may not be blank!")
    private String name;

    @NotNull(message = "Curable may not be null!")
    private Boolean curable;
}
