package com.hospital.patientservice.domain.entity;

import java.time.LocalDate;

public class Patient {
    private final Long id;
    private final String name;
    private final String cpf;
    private final String email;
    private final LocalDate birthDate;

    public Patient(Long id, String name, String cpf, String email, LocalDate birthDate) {
        this.id = id;
        this.name = name;
        this.cpf = cpf;
        this.email = email;
        this.birthDate = birthDate;
    }

    public Long getId() {
        return id;
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
