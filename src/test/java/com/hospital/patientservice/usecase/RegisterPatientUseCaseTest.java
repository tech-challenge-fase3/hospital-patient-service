package com.hospital.patientservice.usecase;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.hospital.patientservice.application.port.out.KeycloakIdentityProvisioner;
import com.hospital.patientservice.domain.entity.Patient;
import com.hospital.patientservice.domain.repository.PatientRepository;

class RegisterPatientUseCaseTest {

    @Test
    void shouldProvisionIdentityAfterSavingPatient() {
        PatientRepository patientRepository = mock(PatientRepository.class);
        KeycloakIdentityProvisioner provisioner = mock(KeycloakIdentityProvisioner.class);

        Patient patient = Patient.create("Maria Santos", "12345678901", "maria@email.com", LocalDate.of(1990, 1, 15));

        when(patientRepository.findByCpf(patient.getCpf())).thenReturn(Optional.empty());
        when(patientRepository.findByEmail(patient.getEmail())).thenReturn(Optional.empty());
        when(patientRepository.save(any(Patient.class))).thenAnswer(invocation -> invocation.getArgument(0));

        RegisterPatientUseCase useCase = new RegisterPatientUseCase(patientRepository, provisioner);

        Patient savedPatient = useCase.execute(patient, "Senha@123");

        verify(patientRepository).save(any(Patient.class));
        verify(provisioner).provision(savedPatient, "Senha@123");
    }

    @Test
    void shouldRejectDuplicateEmailWithSpecificMessage() {
        PatientRepository patientRepository = mock(PatientRepository.class);
        KeycloakIdentityProvisioner provisioner = mock(KeycloakIdentityProvisioner.class);

        Patient patient = Patient.create("Maria Santos", "12345678901", "maria@email.com", LocalDate.of(1990, 1, 15));

        when(patientRepository.findByCpf(patient.getCpf())).thenReturn(Optional.empty());
        when(patientRepository.findByEmail(patient.getEmail())).thenReturn(Optional.of(patient));

        RegisterPatientUseCase useCase = new RegisterPatientUseCase(patientRepository, provisioner);

        try {
            useCase.execute(patient, "Senha@123");
        } catch (RuntimeException ex) {
            assert ex.getMessage().contains("e-mail");
            return;
        }

        throw new AssertionError("Esperava exceção por e-mail duplicado");
    }
}
