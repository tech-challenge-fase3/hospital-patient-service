package com.hospital.patientservice.application.port.out;

import com.hospital.patientservice.domain.entity.Patient;

public interface KeycloakIdentityProvisioner {

    void provision(Patient patient, String password);
}
