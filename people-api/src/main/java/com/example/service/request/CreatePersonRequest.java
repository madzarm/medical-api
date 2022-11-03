package com.example.service.request;

import com.example.exception.validation.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreatePersonRequest {

    @NotBlank(message = "Name may not be blank")
    @Pattern(regexp = "^[A-Za-z]+$", message = "You may only use alphabetic characters [a-zA-z]")
    private String firstName;

    @NotBlank(message = "Last name may not be blank")
    @Pattern(regexp = "^[A-Za-z]+$", message = "You may only use alphabetic characters [a-zA-z]")
    private String lastName;

    @Max(value = 500, message = "Weight must be less than 500kg!")
    @Min(value = 0, message = "Weight must be more than 0kg!")
    private Integer weight;

    @Max(value = 150, message = "Age must be less than 0!")
    @Min(value = 0, message = "Age must be more than 0!")
    private Integer age;

    private List<Long> diseaseIds = new ArrayList<>();

    @Date
    private String dateDiscovered;
}
