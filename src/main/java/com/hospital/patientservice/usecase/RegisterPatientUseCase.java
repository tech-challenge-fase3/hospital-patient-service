package com.hospital.patientservice.usecase;

import com.hospital.patientservice.domain.entity.Patient;
import com.hospital.patientservice.domain.exception.PatientAlreadyExistsException;
import com.hospital.patientservice.domain.repository.PatientRepository;

public class RegisterPatientUseCase {

    private final PatientRepository patientRepository;

    public RegisterPatientUseCase(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public Patient execute(Patient patient) {
        if (patientRepository.findByCpf(patient.getCpf()).isPresent()) {
            throw new PatientAlreadyExistsException("Já existe um paciente cadastrado com o CPF: " + patient.getCpf());
        }

        return patientRepository.save(patient);
    }
}