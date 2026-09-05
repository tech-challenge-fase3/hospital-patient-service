package com.hospital.patientservice.presentation.controller.mapper;

import com.hospital.patientservice.domain.entity.Patient;
import com.hospital.patientservice.presentation.controller.dto.PatientRequestDTO;
import com.hospital.patientservice.presentation.controller.dto.PatientResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PatientWebMapper {

    @Mapping(target = "id", ignore = true)
    Patient toDomain(PatientRequestDTO requestDTO);

    PatientResponseDTO toResponseDTO(Patient patient);
}