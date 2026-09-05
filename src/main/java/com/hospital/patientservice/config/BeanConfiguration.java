package com.hospital.patientservice.config;

import com.hospital.patientservice.domain.repository.PatientRepository;
import com.hospital.patientservice.usecase.RegisterPatientUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public RegisterPatientUseCase registerPatientUseCase(PatientRepository patientRepository) {
        return new RegisterPatientUseCase(patientRepository);
    }
}