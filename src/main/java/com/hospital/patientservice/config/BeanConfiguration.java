package com.hospital.patientservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hospital.patientservice.application.port.out.KeycloakIdentityProvisioner;
import com.hospital.patientservice.domain.repository.PatientRepository;
import com.hospital.patientservice.usecase.RegisterPatientUseCase;

@Configuration
public class BeanConfiguration {

    @Bean
    public RegisterPatientUseCase registerPatientUseCase(
            PatientRepository patientRepository,
            KeycloakIdentityProvisioner keycloakIdentityProvisioner) {
        return new RegisterPatientUseCase(patientRepository, keycloakIdentityProvisioner);
    }
}
