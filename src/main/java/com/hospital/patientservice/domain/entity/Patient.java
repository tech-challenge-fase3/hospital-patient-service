package com.hospital.patientservice.domain.entity;

import java.time.LocalDate;
import java.util.UUID;

public class Patient {

    private final Long id;
    private final UUID patientId;
    private final String name;
    private final String cpf;
    private final String email;
    private final LocalDate birthDate;

    public Patient(Long id, UUID patientId, String name, String cpf, String email, LocalDate birthDate) {
        this.id = id;
        this.patientId = patientId;
        this.name = name;
        this.cpf = cpf;
        this.email = email;
        this.birthDate = birthDate;
    }

    public static Patient create(
            String name,
            String cpf,
            String email,
            LocalDate birthDate) {

        return new Patient(
                null,
                UUID.randomUUID(),
                name,
                cpf,
                email,
                birthDate
        );
    }

    public Long getId() {
        return id;
    }

    public UUID getPatientId() {
        return patientId;
    }

    public String getName() {
        return name;
    }

    public String getCpf() {
        return cpf;
    }

    public String getEmail() {
        return email;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }
}
