package com.example.service.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.ws.rs.QueryParam;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetMedicalRecordsRequest {
    private List<Long> personIds;
    private List<Long> diseaseIds;
    private String firstName;
    private String lastName;
    private Integer weightLowerLimit;
    private Integer weightUpperLimit;
    private Integer ageLowerLimit;
    private Integer ageUpperLimit;
    private Date from;
    private Date to;
    private String diseaseName;
}
