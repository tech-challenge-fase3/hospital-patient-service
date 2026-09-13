package com.hospital.patientservice.domain.repository;

import java.util.Optional;

import com.hospital.patientservice.domain.entity.Patient;

public interface PatientRepository {

    Patient save(Patient patient);

    Optional<Patient> findByCpf(String cpf);

    Optional<Patient> findByEmail(String email);
}
