package com.example.service.result.model;

import com.example.domain.PersonModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchPeopleModel {
    private List<PersonModel> people;
}
