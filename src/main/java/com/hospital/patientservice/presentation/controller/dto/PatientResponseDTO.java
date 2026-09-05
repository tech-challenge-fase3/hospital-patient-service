package com.hospital.patientservice.presentation.controller.dto;

import java.time.LocalDate;

public record PatientResponseDTO(
        Long id,
        String name,
        String cpf,
        String email,
        LocalDate birthDate
) {
}
