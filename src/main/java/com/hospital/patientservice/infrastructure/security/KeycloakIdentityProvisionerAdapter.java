package com.hospital.patientservice.infrastructure.security;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.hospital.patientservice.application.port.out.KeycloakIdentityProvisioner;
import com.hospital.patientservice.domain.entity.Patient;

@Component
public class KeycloakIdentityProvisionerAdapter implements KeycloakIdentityProvisioner {

    private static final Logger log = LoggerFactory.getLogger(KeycloakIdentityProvisionerAdapter.class);

    private final String serverUrl;
    private final String realm;
    private final String adminUsername;
    private final String adminPassword;
    private final String clientId;

    public KeycloakIdentityProvisionerAdapter(
            @Value("${hospital.keycloak.server-url}") String serverUrl,
            @Value("${hospital.keycloak.realm}") String realm,
            @Value("${hospital.keycloak.admin-username}") String adminUsername,
            @Value("${hospital.keycloak.admin-password}") String adminPassword,
            @Value("${hospital.keycloak.client-id}") String clientId) {
        this.serverUrl = serverUrl;
        this.realm = realm;
        this.adminUsername = adminUsername;
        this.adminPassword = adminPassword;
        this.clientId = clientId;
    }

    @Override
    public void provision(Patient patient, String password) {
        Keycloak keycloak = KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm("master")
                .clientId(clientId)
                .username(adminUsername)
                .password(adminPassword)
                .grantType(OAuth2Constants.PASSWORD)
                .build();

        try {
            String username = normalizeUsername(patient.getEmail());
            Map<String, List<String>> patientAttributes = buildPatientAttributes(patient);

            UserRepresentation user = new UserRepresentation();
            user.setUsername(username);
            user.setEmail(patient.getEmail());
            user.setEnabled(true);
            user.setEmailVerified(true);
            user.setFirstName(patient.getName().split(" ")[0]);
            user.setLastName(patient.getName().length() > patient.getName().indexOf(' ') + 1
                    ? patient.getName().substring(patient.getName().indexOf(' ') + 1)
                    : "");
            user.setAttributes(patientAttributes);

            List<UserRepresentation> existingUsers = keycloak.realm(realm)
                    .users()
                    .search(username, true);

            if (existingUsers.isEmpty()) {
                CredentialRepresentation credential = new CredentialRepresentation();
                credential.setTemporary(false);
                credential.setType(CredentialRepresentation.PASSWORD);
                credential.setValue(password);

                user.setCredentials(List.of(credential));

                var response = keycloak.realm(realm)
                        .users()
                        .create(user);

                String userId = CreatedResponseUtil.getCreatedId(response);
                assignPatientRole(keycloak, userId);
                log.info("Paciente provisionado no Keycloak: patientId={}, username={}", patient.getPatientId(), username);
                return;
            }

            UserRepresentation existingUser = existingUsers.get(0);
            existingUser.setEmail(patient.getEmail());
            existingUser.setEnabled(true);
            existingUser.setFirstName(patient.getName().split(" ")[0]);
            existingUser.setLastName(patient.getName().length() > patient.getName().indexOf(' ') + 1
                    ? patient.getName().substring(patient.getName().indexOf(' ') + 1)
                    : "");

            Map<String, List<String>> mergedAttributes = new HashMap<>();
            if (existingUser.getAttributes() != null) {
                mergedAttributes.putAll(existingUser.getAttributes());
            }
            mergedAttributes.putAll(patientAttributes);
            existingUser.setAttributes(mergedAttributes);

            keycloak.realm(realm).users().get(existingUser.getId()).update(existingUser);
            CredentialRepresentation credential = new CredentialRepresentation();
            credential.setTemporary(false);
            credential.setType(CredentialRepresentation.PASSWORD);
            credential.setValue(password);
            keycloak.realm(realm).users().get(existingUser.getId()).resetPassword(credential);
            assignPatientRole(keycloak, existingUser.getId());
            log.info("Paciente já existente no Keycloak atualizado: patientId={}, username={}", patient.getPatientId(), username);
        } catch (Exception ex) {
            log.error("Erro ao provisionar usuário no Keycloak para paciente {}", patient.getPatientId(), ex);
            throw new IllegalStateException("Falha ao provisionar identidade no Keycloak", ex);
        } finally {
            keycloak.close();
        }
    }

    private Map<String, List<String>> buildPatientAttributes(Patient patient) {
        Map<String, List<String>> attributes = new HashMap<>();
        attributes.put("patientId", List.of(patient.getPatientId().toString()));
        return attributes;
    }

    private void assignPatientRole(Keycloak keycloak, String userId) {
        RoleRepresentation patientRole = keycloak.realm(realm)
                .roles()
                .get("PATIENT")
                .toRepresentation();

        keycloak.realm(realm)
                .users()
                .get(userId)
                .roles()
                .realmLevel()
                .add(List.of(patientRole));
    }

    private String normalizeUsername(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email do paciente é obrigatório para provisionar no Keycloak");
        }
        return email.split("@")[0].replaceAll("[^a-zA-Z0-9._-]", "-");
    }
}
