package com.example.service.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePersonRequest {

    private Long personId;
    private String firstName;
    private String lastName;
    private Integer weight;
    private Integer age;
    private Long diseaseHistoryId;
    private String dateDiscovered;
    private Long diseaseId;

}
