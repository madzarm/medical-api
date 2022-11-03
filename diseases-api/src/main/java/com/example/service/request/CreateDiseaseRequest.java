package com.example.service.request;

import com.example.exception.validation.ValidateRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ValidateRequest
public class CreateDiseaseRequest {
    private String name;
    private Boolean curable;
}
