package com.hospital.patientservice.infrastructure.mappers;

import org.mapstruct.Mapper;

import com.hospital.patientservice.domain.entity.Patient;
import com.hospital.patientservice.infrastructure.persistence.PatientEntity;

@Mapper(componentModel = "spring")
public interface PatientMapper {
    PatientEntity toEntity(Patient patient);

    Patient toDomain(PatientEntity entity);
}
