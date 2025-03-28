package service;

import dao.AppointmentDAO;
import model.Appointment;
import java.sql.SQLException;
import java.util.List;
import java.sql.Date;

public class AppointmentService {

    private AppointmentDAO appointmentDAO = new AppointmentDAO();

    public boolean bookAppointment(Appointment appointment) throws SQLException, ClassNotFoundException {
        return appointmentDAO.bookAppointment(appointment);
    }

    public boolean updateAppointment(Appointment appointment) {
        return appointmentDAO.updateAppointment(appointment);
    }

    public Appointment getAppointmentById(int appointmentId) {
        return appointmentDAO.getAppointmentById(appointmentId);
    }

    public boolean isScheduleBooked(int scheduleId, Date appointmentDate) {
        return appointmentDAO.isScheduleBooked(scheduleId, appointmentDate);
    }

    public List<Appointment> getBookingsByPatientId(int patientId) {
        return appointmentDAO.getBookingsByPatientId(patientId);
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

    public List<Appointment> getAppointmentsByDoctorIdAndDate(int doctorId, String date, String statusFilter, String searchPatient, int page, int pageSize) {
        return appointmentDAO.getAppointmentsByDoctorIdAndDate(doctorId, date, statusFilter, searchPatient, page, pageSize);
    }

    public int getAppointmentsCountByDoctorIdAndDate(int doctorId, String date, String statusFilter, String searchPatient) {
        return appointmentDAO.getAppointmentsCountByDoctorIdAndDate(doctorId, date, statusFilter, searchPatient);
    }

    public List<Appointment> getAllAppointmentsByDate(String date, String statusFilter, String searchPatient, String searchDoctor, int page, int pageSize) {
        return appointmentDAO.getAllAppointmentsByDate(date, statusFilter, searchPatient, searchDoctor, page, pageSize);
    }

    public int getAllAppointmentsCountByDate(String date, String statusFilter, String searchPatient, String searchDoctor) {
        return appointmentDAO.getAllAppointmentsCountByDate(date, statusFilter, searchPatient, searchDoctor);
    }

    public boolean deleteAppointment(int appointmentId) {
        return appointmentDAO.deleteAppointment(appointmentId);
    }
}