package model;

import java.sql.Date;

public class Appointment {

    private int appointmentId;
    private Integer patientId; 
    private int doctorId;
    private int scheduleId;
    private int serviceId;
    private Date appointmentDate;
    private String reason;
    private String status;
    private String patientName;
    private String doctorName;
    private String serviceName;
    private String note;
    private String guestName;
    private String guestPhone; 

    public Appointment(int appointmentId, Integer patientId, int doctorId, int scheduleId, int serviceId,
            Date appointmentDate, String reason, String status, String guestName, String guestPhone) {
        this.appointmentId = appointmentId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.scheduleId = scheduleId;
        this.serviceId = serviceId;
        this.appointmentDate = appointmentDate;
        this.reason = reason;
        this.status = status;
        this.guestName = guestName;
        this.guestPhone = guestPhone;
    }

    public Appointment(int appointmentId, Integer patientId, int doctorId, int scheduleId, int serviceId,
            Date appointmentDate, String reason, String status,
            String patientName, String doctorName, String serviceName, String note,
            String guestName, String guestPhone) {
        this(appointmentId, patientId, doctorId, scheduleId, serviceId, appointmentDate, reason, status, guestName, guestPhone);
        this.patientName = patientName;
        this.doctorName = doctorName;
        this.serviceName = serviceName;
        this.note = note;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public int getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(int scheduleId) {
        this.scheduleId = scheduleId;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public Date getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(Date appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getGuestName() {
        return guestName;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    public String getGuestPhone() {
        return guestPhone;
    }

    public void setGuestPhone(String guestPhone) {
        this.guestPhone = guestPhone;
    }
}
