package com.example.service.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePersonRequest {

    @NotNull(message = "PersonId may not be null!")
    private Long personId;

    @Pattern(regexp = "^[A-Za-z]+$", message = "You may only use alphabetic characters [a-zA-z]")
    private String firstName;

    @Pattern(regexp = "^[A-Za-z]+$", message = "You may only use alphabetic characters [a-zA-z]")
    private String lastName;

    @Max(value = 500, message = "Weight must be less than 500kg!")
    @Min(value = 0, message = "Weight must be more than 0kg!")
    private Integer weight;

    @Max(value = 150, message = "Age must be less than 0!")
    @Min(value = 0, message = "Age must be more than 0!")
    private Integer age;
}
