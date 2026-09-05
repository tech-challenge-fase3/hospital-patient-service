package com.hospital.patientservice.presentation.controller.dto;

import java.time.LocalDate;

public record PatientRequestDTO(
        String name,
        String cpf,
        String email,
        LocalDate birthDate
        ) {
}
