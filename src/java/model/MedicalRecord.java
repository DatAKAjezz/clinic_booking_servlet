package model;

import java.sql.Date;

public class MedicalRecord {

    private int recordId;      
    private int patientId;     
    private int doctorId;     
    private int appointmentId;
    private Date visitDate;    
    private String diagnosis;  
    private String prescription;
    private String notes;    
    private String attachment; 

    public MedicalRecord() {
    }

    public MedicalRecord(int recordId, int patientId, int doctorId, int appointmentId, Date visitDate,
            String diagnosis, String prescription, String notes, String attachment) {
        this.recordId = recordId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.appointmentId = appointmentId;
        this.visitDate = visitDate;
        this.diagnosis = diagnosis;
        this.prescription = prescription;
        this.notes = notes;
        this.attachment = attachment;
    }

    // Getters và Setters
    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public Date getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(Date visitDate) {
        this.visitDate = visitDate;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getPrescription() {
        return prescription;
    }

    public void setPrescription(String prescription) {
        this.prescription = prescription;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    @Override
    public String toString() {
        return "MedicalRecord{"
                + "recordId=" + recordId
                + ", patientId=" + patientId
                + ", doctorId=" + doctorId
                + ", appointmentId=" + appointmentId
                + ", visitDate=" + visitDate
                + ", diagnosis='" + diagnosis + '\''
                + ", prescription='" + prescription + '\''
                + ", notes='" + notes + '\''
                + ", attachment='" + attachment + '\''
                + '}';
    }
}
