package com.hospital.patientservice.infrastructure.persistence;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.hospital.patientservice.domain.entity.Patient;
import com.hospital.patientservice.domain.repository.PatientRepository;
import com.hospital.patientservice.infrastructure.mappers.PatientMapper;

@Repository
public class PatientRepositoryAdapter implements PatientRepository {

    private final SpringDataPatientRepository springDataRepository;
    private final PatientMapper patientEntityMapper;

    public PatientRepositoryAdapter(SpringDataPatientRepository springDataRepository, PatientMapper patientEntityMapper) {
        this.springDataRepository = springDataRepository;
        this.patientEntityMapper = patientEntityMapper;
    }

    @Override
    public Patient save(Patient patient) {
        PatientEntity entity = this.patientEntityMapper.toEntity(patient);
        PatientEntity savedEntity = springDataRepository.save(entity);
        return this.patientEntityMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Patient> findByCpf(String cpf) {
        return springDataRepository.findByCpf(cpf).map(patientEntityMapper::toDomain);
    }

    @Override
    public Optional<Patient> findByEmail(String email) {
        return springDataRepository.findByEmail(email).map(patientEntityMapper::toDomain);
    }
}
