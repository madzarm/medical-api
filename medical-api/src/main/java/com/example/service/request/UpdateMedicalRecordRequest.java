package com.example.service.request;

import com.example.exception.validation.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMedicalRecordRequest {

    @Positive
    private Long personId;

    @Pattern(regexp = "^[A-Za-z]+$", message = "You may only use alphabetic characters [a-zA-z]")
    private String firstName;

    @Pattern(regexp = "^[A-Za-z]+$", message = "You may only use alphabetic characters [a-zA-z]")
    private String lastName;

    @Min(0) @Max(500)
    private Integer weight;

    @Min(0) @Max(150)
    private Integer age;

    @Positive
    private Long diseaseHistoryId;

    @Date
    private String dateDiscovered;

    @Positive
    private Long diseaseId;

    @Pattern(regexp = "^[A-Za-z]+$", message = "You may only use alphabetic characters [a-zA-z]")
    private String diseaseName;
}
