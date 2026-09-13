ALTER TABLE patients
    ADD COLUMN patient_id UUID;

UPDATE patients
SET patient_id = gen_random_uuid()
WHERE patient_id IS NULL;

ALTER TABLE patients
    ALTER COLUMN patient_id SET NOT NULL;

ALTER TABLE patients
    ADD CONSTRAINT uk_patients_patient_id UNIQUE (patient_id);
