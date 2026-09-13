package com.hospital.patientservice.presentation.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hospital.patientservice.domain.entity.Patient;
import com.hospital.patientservice.presentation.controller.docs.PatientControllerDocs;
import com.hospital.patientservice.presentation.controller.dto.PatientRequestDTO;
import com.hospital.patientservice.presentation.controller.dto.PatientResponseDTO;
import com.hospital.patientservice.presentation.controller.mapper.PatientWebMapper;
import com.hospital.patientservice.usecase.RegisterPatientUseCase;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/patients")
public class PatientController implements PatientControllerDocs {

    private final RegisterPatientUseCase registerPatientUseCase;
    private final PatientWebMapper patientWebMapper;

    public PatientController(RegisterPatientUseCase registerPatientUseCase, PatientWebMapper patientWebMapper) {
        this.registerPatientUseCase = registerPatientUseCase;
        this.patientWebMapper = patientWebMapper;
    }

    @Override
    @PostMapping
    public ResponseEntity<PatientResponseDTO> register(@RequestBody @Valid PatientRequestDTO request) {
        // Converte o DTO de entrada para a Entidade de Domínio usando MapStruct
        Patient patient = patientWebMapper.toDomain(request);

        // Executa o Caso de Uso contendo as regras de negócio
        Patient savedPatient = registerPatientUseCase.execute(patient, request.password());

        // Converte a Entidade salva de volta para o DTO de Resposta usando MapStruct
        PatientResponseDTO response = patientWebMapper.toResponseDTO(savedPatient);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
