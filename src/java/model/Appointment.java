package model;

import java.sql.Date;
import java.sql.Time;

public class Appointment {
    private int appointmentId;
    private int patientId;
    private int doctorId;
    private int scheduleId;
    private int serviceId;
    private Date appointmentDate;
    private Time appointmentTime;
    private String reason;
    private String status;
    private String patientName;
    private String doctorName;
    private String serviceName;
    private String note; // Thêm thuộc tính note

    // Constructor cho BookAppointmentServlet (9 tham số)
    public Appointment(int appointmentId, int patientId, int doctorId, int scheduleId, int serviceId,
                       Date appointmentDate, Time appointmentTime, String reason, String status) {
        this.appointmentId = appointmentId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.scheduleId = scheduleId;
        this.serviceId = serviceId;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.reason = reason;
        this.status = status;
        this.patientName = null;
        this.doctorName = null;
        this.serviceName = null;
        this.note = null;
    }

    // Constructor đầy đủ (13 tham số)
    public Appointment(int appointmentId, int patientId, int doctorId, int scheduleId, int serviceId,
                       Date appointmentDate, Time appointmentTime, String reason, String status,
                       String patientName, String doctorName, String serviceName, String note) {
        this(appointmentId, patientId, doctorId, scheduleId, serviceId, appointmentDate, 
             appointmentTime, reason, status);
        this.patientName = patientName;
        this.doctorName = doctorName;
        this.serviceName = serviceName;
        this.note = note;
    }

    // Constructor cũ (10 tham số)
    public Appointment(int appointmentId, int patientId, int doctorId, int scheduleId, int serviceId,
                       Date appointmentDate, Time appointmentTime, String reason, String status,
                       String name, String serviceName) {
        this(appointmentId, patientId, doctorId, scheduleId, serviceId, appointmentDate, 
             appointmentTime, reason, status);
        this.doctorName = name;
        this.serviceName = serviceName;
    }

    // Getters and Setters
    public int getAppointmentId() { return appointmentId; }
    public void setAppointmentId(int appointmentId) { this.appointmentId = appointmentId; }
    public int getPatientId() { return patientId; }
    public void setPatientId(int patientId) { this.patientId = patientId; }
    public int getDoctorId() { return doctorId; }
    public void setDoctorId(int doctorId) { this.doctorId = doctorId; }
    public int getScheduleId() { return scheduleId; }
    public void setScheduleId(int scheduleId) { this.scheduleId = scheduleId; }
    public int getServiceId() { return serviceId; }
    public void setServiceId(int serviceId) { this.serviceId = serviceId; }
    public Date getAppointmentDate() { return appointmentDate; }
    public void setAppointmentDate(Date appointmentDate) { this.appointmentDate = appointmentDate; }
    public Time getAppointmentTime() { return appointmentTime; }
    public void setAppointmentTime(Time appointmentTime) { this.appointmentTime = appointmentTime; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }
    public String getDoctorName() { return doctorName; }
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }
    public String getServiceName() { return serviceName; }
    public void setServiceName(String serviceName) { this.serviceName = serviceName; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}