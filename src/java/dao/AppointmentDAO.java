package dao;

import model.Appointment;
import utils.DBUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AppointmentDAO {

    public List<Appointment> getBookingsByPatientId(int patientId) {
        List<Appointment> bookings = new ArrayList<>();
        String sql = "SELECT a.appointment_id, a.patient_id, a.doctor_id, a.schedule_id, a.service_id, "
                + "a.appointment_date, a.appointment_time, a.reason, a.status, a.note, "
                + "d.full_name AS doctor_name, s.name AS service_name "
                + "FROM Appointments a "
                + "JOIN Doctors d ON a.doctor_id = d.doctor_id "
                + "JOIN Services s ON a.service_id = s.service_id "
                + "WHERE a.patient_id = ?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, patientId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Appointment appointment = new Appointment(
                        rs.getInt("appointment_id"),
                        rs.getInt("patient_id"),
                        rs.getInt("doctor_id"),
                        rs.getInt("schedule_id"),
                        rs.getInt("service_id"),
                        rs.getDate("appointment_date"),
                        rs.getTime("appointment_time"),
                        rs.getString("reason"),
                        rs.getString("status"),
                        null, // patientName
                        rs.getString("doctor_name"),
                        rs.getString("service_name"),
                        rs.getString("note")
                );
                bookings.add(appointment);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return bookings;
    }

    public boolean bookAppointment(Appointment appointment) {
        String sql = "INSERT INTO Appointments (patient_id, doctor_id, schedule_id, service_id, "
                + "appointment_date, appointment_time, reason, status) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, appointment.getPatientId());
            ps.setInt(2, appointment.getDoctorId());
            ps.setInt(3, appointment.getScheduleId());
            ps.setInt(4, appointment.getServiceId());
            ps.setDate(5, appointment.getAppointmentDate());
            ps.setTime(6, appointment.getAppointmentTime());
            ps.setString(7, appointment.getReason());
            ps.setString(8, appointment.getStatus());
            return ps.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Appointment> getAppointmentsByDoctorId(int doctorId, String statusFilter, String searchPatient, int page, int pageSize) {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT a.appointment_id, a.patient_id, a.doctor_id, a.schedule_id, a.service_id, "
                + "a.appointment_date, a.appointment_time, a.reason, a.status, a.note, "
                + "p.full_name AS patient_name, s.name AS service_name "
                + "FROM Appointments a "
                + "JOIN Patients p ON a.patient_id = p.patient_id "
                + "JOIN Services s ON a.service_id = s.service_id "
                + "WHERE a.doctor_id = ?"
                + (statusFilter != null && !statusFilter.isEmpty() ? " AND a.status = ?" : "")
                + (searchPatient != null && !searchPatient.isEmpty() ? " AND p.full_name LIKE ?" : "")
                + " ORDER BY a.appointment_date, a.appointment_time "
                + "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            int paramIndex = 1;
            ps.setInt(paramIndex++, doctorId);
            if (statusFilter != null && !statusFilter.isEmpty()) {
                ps.setString(paramIndex++, statusFilter);
            }
            if (searchPatient != null && !searchPatient.isEmpty()) {
                ps.setString(paramIndex++, "%" + searchPatient + "%");
            }
            ps.setInt(paramIndex++, (page - 1) * pageSize);
            ps.setInt(paramIndex, pageSize);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Appointment appointment = new Appointment(
                        rs.getInt("appointment_id"),
                        rs.getInt("patient_id"),
                        rs.getInt("doctor_id"),
                        rs.getInt("schedule_id"),
                        rs.getInt("service_id"),
                        rs.getDate("appointment_date"),
                        rs.getTime("appointment_time"),
                        rs.getString("reason"),
                        rs.getString("status"),
                        rs.getString("patient_name"),
                        null,
                        rs.getString("service_name"),
                        rs.getString("note")
                );
                appointments.add(appointment);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return appointments;
    }

    public int getAppointmentsCountByDoctorId(int doctorId, String statusFilter, String searchPatient) {
        String sql = "SELECT COUNT(*) "
                + "FROM Appointments a "
                + "JOIN Patients p ON a.patient_id = p.patient_id "
                + "WHERE a.doctor_id = ?"
                + (statusFilter != null && !statusFilter.isEmpty() ? " AND a.status = ?" : "")
                + (searchPatient != null && !searchPatient.isEmpty() ? " AND p.full_name LIKE ?" : "");
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            int paramIndex = 1;
            ps.setInt(paramIndex++, doctorId);
            if (statusFilter != null && !statusFilter.isEmpty()) {
                ps.setString(paramIndex++, statusFilter);
            }
            if (searchPatient != null && !searchPatient.isEmpty()) {
                ps.setString(paramIndex, "%" + searchPatient + "%");
            }
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public boolean cancelAppointment(int appointmentId) {
        String sql = "UPDATE Appointments SET status = 'canceled' WHERE appointment_id = ? AND status IN ('pending', 'confirmed')";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, appointmentId);
            return ps.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int getScheduleIdByAppointmentId(int appointmentId) {
        String sql = "SELECT schedule_id FROM Appointments WHERE appointment_id = ?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, appointmentId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("schedule_id");
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public List<Appointment> getMedicalHistoryByPatientId(int patientId) {
        List<Appointment> history = new ArrayList<>();
        String sql = "SELECT a.appointment_id, a.patient_id, a.doctor_id, a.schedule_id, a.service_id, "
                + "a.appointment_date, a.appointment_time, a.reason, a.status, a.note, "
                + "d.full_name AS doctor_name, s.name AS service_name "
                + "FROM Appointments a "
                + "JOIN Doctors d ON a.doctor_id = d.doctor_id "
                + "JOIN Services s ON a.service_id = s.service_id "
                + "WHERE a.patient_id = ? AND a.status = 'completed'";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, patientId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Appointment appointment = new Appointment(
                        rs.getInt("appointment_id"),
                        rs.getInt("patient_id"),
                        rs.getInt("doctor_id"),
                        rs.getInt("schedule_id"),
                        rs.getInt("service_id"),
                        rs.getDate("appointment_date"),
                        rs.getTime("appointment_time"),
                        rs.getString("reason"),
                        rs.getString("status"),
                        null, // patientName
                        rs.getString("doctor_name"),
                        rs.getString("service_name"),
                        rs.getString("note")
                );
                history.add(appointment);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return history;
    }

    public boolean confirmAppointment(int appointmentId) {
        String sql = "UPDATE Appointments SET status = 'confirmed' WHERE appointment_id = ? AND status = 'pending'";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, appointmentId);
            return ps.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean completeAppointment(int appointmentId, String note) {
        String sql = "UPDATE Appointments SET status = 'completed', note = ? WHERE appointment_id = ? AND status = 'confirmed'";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, note);
            ps.setInt(2, appointmentId);
            return ps.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Appointment> getAllAppointments(String statusFilter, String searchPatient, String searchDoctor, int page, int pageSize) {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT a.appointment_id, a.patient_id, a.doctor_id, a.schedule_id, a.service_id, "
                + "a.appointment_date, a.appointment_time, a.reason, a.status, a.note, "
                + "p.full_name AS patient_name, d.full_name AS doctor_name, s.name AS service_name "
                + "FROM Appointments a "
                + "JOIN Patients p ON a.patient_id = p.patient_id "
                + "JOIN Doctors d ON a.doctor_id = d.doctor_id "
                + "JOIN Services s ON a.service_id = s.service_id "
                + "WHERE 1=1"
                + (statusFilter != null && !statusFilter.isEmpty() ? " AND a.status = ?" : "")
                + (searchPatient != null && !searchPatient.isEmpty() ? " AND p.full_name LIKE ?" : "")
                + (searchDoctor != null && !searchDoctor.isEmpty() ? " AND d.full_name LIKE ?" : "")
                + " ORDER BY a.appointment_date, a.appointment_time "
                + "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            int paramIndex = 1;
            if (statusFilter != null && !statusFilter.isEmpty()) {
                ps.setString(paramIndex++, statusFilter);
            }
            if (searchPatient != null && !searchPatient.isEmpty()) {
                ps.setString(paramIndex++, "%" + searchPatient + "%");
            }
            if (searchDoctor != null && !searchDoctor.isEmpty()) {
                ps.setString(paramIndex++, "%" + searchDoctor + "%");
            }
            ps.setInt(paramIndex++, (page - 1) * pageSize);
            ps.setInt(paramIndex, pageSize);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Appointment appointment = new Appointment(
                        rs.getInt("appointment_id"),
                        rs.getInt("patient_id"),
                        rs.getInt("doctor_id"),
                        rs.getInt("schedule_id"),
                        rs.getInt("service_id"),
                        rs.getDate("appointment_date"),
                        rs.getTime("appointment_time"),
                        rs.getString("reason"),
                        rs.getString("status"),
                        rs.getString("patient_name"),
                        rs.getString("doctor_name"),
                        rs.getString("service_name"),
                        rs.getString("note")
                );
                appointments.add(appointment);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return appointments;
    }

    public int getAllAppointmentsCount(String statusFilter, String searchPatient, String searchDoctor) {
        String sql = "SELECT COUNT(*) "
                + "FROM Appointments a "
                + "JOIN Patients p ON a.patient_id = p.patient_id "
                + "JOIN Doctors d ON a.doctor_id = d.doctor_id "
                + "WHERE 1=1"
                + (statusFilter != null && !statusFilter.isEmpty() ? " AND a.status = ?" : "")
                + (searchPatient != null && !searchPatient.isEmpty() ? " AND p.full_name LIKE ?" : "")
                + (searchDoctor != null && !searchDoctor.isEmpty() ? " AND d.full_name LIKE ?" : "");
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            int paramIndex = 1;
            if (statusFilter != null && !statusFilter.isEmpty()) {
                ps.setString(paramIndex++, statusFilter);
            }
            if (searchPatient != null && !searchPatient.isEmpty()) {
                ps.setString(paramIndex++, "%" + searchPatient + "%");
            }
            if (searchDoctor != null && !searchDoctor.isEmpty()) {
                ps.setString(paramIndex++, "%" + searchDoctor + "%");
            }
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }
}