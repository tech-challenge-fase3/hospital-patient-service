package com.hospital.patientservice.presentation.controller.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.hospital.patientservice.domain.entity.Patient;
import com.hospital.patientservice.presentation.controller.dto.PatientRequestDTO;
import com.hospital.patientservice.presentation.controller.dto.PatientResponseDTO;

@Mapper(componentModel = "spring")
public interface PatientWebMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "patientId", ignore = true)
    Patient toDomain(PatientRequestDTO requestDTO);

    PatientResponseDTO toResponseDTO(Patient patient);
}
