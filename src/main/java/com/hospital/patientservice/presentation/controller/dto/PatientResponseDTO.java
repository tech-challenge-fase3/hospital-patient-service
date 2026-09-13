package com.hospital.patientservice.presentation.controller.dto;

import java.time.LocalDate;
import java.util.UUID;

public record PatientResponseDTO(
        Long id,
        UUID patientId,
        String name,
        String cpf,
        String email,
        LocalDate birthDate
        ) {

}
