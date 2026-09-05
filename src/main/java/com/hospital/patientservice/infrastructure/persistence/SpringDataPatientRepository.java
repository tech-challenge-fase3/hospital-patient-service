package com.hospital.patientservice.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringDataPatientRepository extends JpaRepository<PatientEntity, Long> {
    Optional<PatientEntity> findByCpf(String cpf);
}
