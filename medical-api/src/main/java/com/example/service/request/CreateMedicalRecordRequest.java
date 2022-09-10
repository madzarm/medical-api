package com.example.service.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateMedicalRecordRequest {

    @NotBlank(message = "Name may not be blank")
    @Pattern(regexp = "^[A-Za-z]+$", message = "You may only use alphabetic characters [a-zA-z]")
    private String firstName;

    @NotBlank(message = "Last name may not be blank")
    @Pattern(regexp = "^[A-Za-z]+$", message = "You may only use alphabetic characters [a-zA-z]")
    private String lastName;

    @Max(value = 500, message = "Weight must be less than 500kg!")
    @Min(value = 0, message = "Weight must be more than 0kg!")
    @NotNull(message = "Weight may not be null!")
    private Integer weight;

    @Max(value = 150, message = "Age must be less than 0!")
    @Min(value = 0, message = "Age must be more than 0!")
    @NotNull(message = "Age may not be null!")
    private Integer age;

    @Pattern(regexp = "^[A-Za-z]+$", message = "You may only use alphabetic characters [a-zA-z]")
    private String diseaseName;

    private List<Long> diseaseIds = new ArrayList<>();
}
