package com.hospital.patientservice.presentation.controller.docs;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import com.hospital.patientservice.presentation.controller.dto.ErrorResponse;
import com.hospital.patientservice.presentation.controller.dto.PatientRequestDTO;
import com.hospital.patientservice.presentation.controller.dto.PatientResponseDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Patients", description = "Gerenciamento de pacientes do hospital")
public interface PatientControllerDocs {

    @Operation(
            summary = "Cadastrar um novo paciente",
            description = "Salva os dados de um novo paciente no sistema validando as regras de negócio e duplicidade de CPF."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Paciente cadastrado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PatientResponseDTO.class),
                            examples = @ExampleObject(
                                    name = "SucessoResponse",
                                    summary = "Exemplo de paciente criado com sucesso",
                                    value = """
                                            {
                                              "id": 1,
                                              "name": "Carlos Alberto",
                                              "cpf": "12345678901",
                                              "email": "carlos.alberto@email.com",
                                              "birthDate": "1985-10-20"
                                            }"""
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados da requisição inválidos",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "BadRequestResponse",
                                    summary = "Exemplo de erro de validação",
                                    value = """
                                            {
                                              "timestamp": "2026-06-06T14:30:00",
                                              "status": 400,
                                              "error": "Bad Request",
                                              "message": "O campo nome n\u00e3o pode estar em branco"
                                            }"""
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Paciente já cadastrado (CPF duplicado) ou erro de integridade de dados na persistência",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "ConflictResponse",
                                    summary = "Exemplo de CPF já existente ou erro de persistência",
                                    value = """
                                            {
                                              "timestamp": "2026-06-06T14:30:00",
                                              "status": 409,
                                              "error": "Conflict",
                                              "message": "N\u00e3o foi poss\u00edvel persistir os dados do paciente. Verifique os dados informados e tente novamente."
                                            }"""
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erro interno do servidor durante a persistência",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "InternalServerErrorResponse",
                                    summary = "Exemplo de erro interno de persistência",
                                                                                                                                                value = """
                                                                                                                                                                                {
                                                                                                                                                                                        "timestamp": "2026-06-06T14:30:00",
                                                                                                                                                                                        "status": 500,
                                                                                                                                                                                        "error": "Internal Server Error",
                                                                                                                                                                                        "message": "Ocorreu um erro interno no servidor. Tente novamente mais tarde."
                                                                                                                                                                                }"""
                            )
                    )
            )
    })
    ResponseEntity<PatientResponseDTO> register(@RequestBody PatientRequestDTO request);
}