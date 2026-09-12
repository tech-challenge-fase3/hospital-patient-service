package com.hospital.patientservice.usecase;

import com.hospital.patientservice.application.port.out.KeycloakIdentityProvisioner;
import com.hospital.patientservice.domain.entity.Patient;
import com.hospital.patientservice.domain.exception.PatientAlreadyExistsException;
import com.hospital.patientservice.domain.repository.PatientRepository;

public class RegisterPatientUseCase {

    private final PatientRepository patientRepository;
    private final KeycloakIdentityProvisioner keycloakIdentityProvisioner;

    public RegisterPatientUseCase(PatientRepository patientRepository,
            KeycloakIdentityProvisioner keycloakIdentityProvisioner) {
        this.patientRepository = patientRepository;
        this.keycloakIdentityProvisioner = keycloakIdentityProvisioner;
    }

    public Patient execute(Patient patient, String password) {
        if (patientRepository.findByCpf(patient.getCpf()).isPresent()) {
            throw new PatientAlreadyExistsException("Já existe um paciente cadastrado com o mesmo CPF");
        }

        if (patientRepository.findByEmail(patient.getEmail()).isPresent()) {
            throw new PatientAlreadyExistsException("Já existe um paciente cadastrado com o e-mail informado");
        }

        Patient patientWithId = patient.getPatientId() != null
                ? patient
                : Patient.create(
                        patient.getName(),
                        patient.getCpf(),
                        patient.getEmail(),
                        patient.getBirthDate()
                );

        Patient savedPatient = patientRepository.save(patientWithId);
        keycloakIdentityProvisioner.provision(savedPatient, password);
        return savedPatient;
    }
}
