package com.example.mapper;

import com.example.domain.DiseaseHistory;
import com.example.domain.dto.DiseaseHistoryDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


@Mapper(componentModel = "cdi")
public interface DiseaseHistoryMapper {

    DiseaseHistoryMapper MAPPER = Mappers.getMapper(DiseaseHistoryMapper.class);

    DiseaseHistoryDto diseaseHistoryToDto(DiseaseHistory diseaseHistory);
}
