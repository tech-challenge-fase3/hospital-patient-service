package com.hospital.patientservice.domain.repository;

import com.hospital.patientservice.domain.entity.Patient;

import java.util.Optional;

public interface PatientRepository {
    Patient save(Patient patient);
    Optional<Patient> findByCpf(String cpf);
}
