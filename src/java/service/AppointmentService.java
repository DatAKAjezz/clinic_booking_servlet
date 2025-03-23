package service;

import dao.AppointmentDAO;
import model.Appointment;
import java.util.List;

public class AppointmentService {
    private AppointmentDAO appointmentDAO = new AppointmentDAO();

    public List<Appointment> getBookingsByPatientId(int patientId) {
        return appointmentDAO.getBookingsByPatientId(patientId);
    }

    public boolean bookAppointment(Appointment appointment) {
        return appointmentDAO.bookAppointment(appointment);
    }

    public List<Appointment> getAppointmentsByDoctorId(int doctorId, String statusFilter, String searchPatient, int page, int pageSize) {
        return appointmentDAO.getAppointmentsByDoctorId(doctorId, statusFilter, searchPatient, page, pageSize);
    }

    public int getAppointmentsCountByDoctorId(int doctorId, String statusFilter, String searchPatient) {
        return appointmentDAO.getAppointmentsCountByDoctorId(doctorId, statusFilter, searchPatient);
    }

    public boolean cancelAppointment(int appointmentId) {
        return appointmentDAO.cancelAppointment(appointmentId);
    }

    public int getScheduleIdByAppointmentId(int appointmentId) {
        return appointmentDAO.getScheduleIdByAppointmentId(appointmentId);
    }

    public List<Appointment> getMedicalHistoryByPatientId(int patientId) {
        return appointmentDAO.getMedicalHistoryByPatientId(patientId);
    }

    public boolean confirmAppointment(int appointmentId) {
        return appointmentDAO.confirmAppointment(appointmentId);
    }

    public List<Appointment> getAllAppointments(String statusFilter, String searchPatient, String searchDoctor, int page, int pageSize) {
        return appointmentDAO.getAllAppointments(statusFilter, searchPatient, searchDoctor, page, pageSize);
    }

    public int getAllAppointmentsCount(String statusFilter, String searchPatient, String searchDoctor) {
        return appointmentDAO.getAllAppointmentsCount(statusFilter, searchPatient, searchDoctor);
    }

    public boolean completeAppointment(int appointmentId, String note) {
        return appointmentDAO.completeAppointment(appointmentId, note);
    }
}