package com.hospital.patientservice.infrastructure.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataPatientRepository extends JpaRepository<PatientEntity, Long> {

    Optional<PatientEntity> findByCpf(String cpf);

    Optional<PatientEntity> findByEmail(String email);
}
