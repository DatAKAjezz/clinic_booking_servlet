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

    // Kiểm tra lịch trùng
    public boolean isScheduleBooked(int scheduleId, java.sql.Date appointmentDate) {
        String sql = "SELECT COUNT(*) FROM Appointments WHERE schedule_id = ? AND appointment_date = ? AND status NOT IN ('canceled') AND isActive = 1";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, scheduleId);
            ps.setDate(2, appointmentDate);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Error checking schedule: " + e.getMessage(), e);
        }
        return false;
    }

    // Lấy danh sách đặt lịch theo patientId
    public List<Appointment> getBookingsByPatientId(int patientId) {
        List<Appointment> bookings = new ArrayList<>();
        String sql = "SELECT a.appointment_id, a.patient_id, a.doctor_id, a.schedule_id, a.service_id, "
                + "a.appointment_date, a.reason, a.status, a.note, a.guest_name, a.guest_phone, "
                + "d.full_name AS doctor_name, s.name AS service_name "
                + "FROM Appointments a "
                + "JOIN Doctors d ON a.doctor_id = d.doctor_id "
                + "JOIN Services s ON a.service_id = s.service_id "
                + "WHERE a.patient_id = ? AND a.isActive = 1 "
                + "ORDER BY a.appointment_date DESC";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, patientId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Appointment appointment = new Appointment(
                        rs.getInt("appointment_id"),
                        rs.getObject("patient_id") != null ? rs.getInt("patient_id") : null,
                        rs.getInt("doctor_id"),
                        rs.getInt("schedule_id"),
                        rs.getInt("service_id"),
                        rs.getDate("appointment_date"),
                        rs.getString("reason"),
                        rs.getString("status"),
                        null, // patientName
                        rs.getString("doctor_name"),
                        rs.getString("service_name"),
                        rs.getString("note"),
                        rs.getString("guest_name"),
                        rs.getString("guest_phone")
                );
                bookings.add(appointment);
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Error retrieving bookings: " + e.getMessage(), e);
        }
        return bookings;
    }

    // Đặt lịch mới
    public boolean bookAppointment(Appointment appointment) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO Appointments (patient_id, doctor_id, schedule_id, service_id, appointment_date, reason, status, guest_name, guest_phone, isActive) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, 1)";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            if (appointment.getPatientId() != null) {
                ps.setInt(1, appointment.getPatientId());
            } else {
                ps.setNull(1, java.sql.Types.INTEGER);
            }
            ps.setInt(2, appointment.getDoctorId());
            ps.setInt(3, appointment.getScheduleId());
            ps.setInt(4, appointment.getServiceId());
            ps.setDate(5, appointment.getAppointmentDate());
            ps.setString(6, appointment.getReason());
            ps.setString(7, appointment.getStatus());
            ps.setString(8, appointment.getGuestName());
            ps.setString(9, appointment.getGuestPhone());
            return ps.executeUpdate() > 0;
        }
    }

    // Cập nhật lịch
    public boolean updateAppointment(Appointment appointment) {
        String sql = "UPDATE Appointments SET patient_id = ?, doctor_id = ?, schedule_id = ?, service_id = ?, "
                + "appointment_date = ?, reason = ?, status = ?, guest_name = ?, guest_phone = ? WHERE appointment_id = ? AND isActive = 1";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            if (appointment.getPatientId() != null) {
                ps.setInt(1, appointment.getPatientId());
            } else {
                ps.setNull(1, java.sql.Types.INTEGER);
            }
            ps.setInt(2, appointment.getDoctorId());
            ps.setInt(3, appointment.getScheduleId());
            ps.setInt(4, appointment.getServiceId());
            ps.setDate(5, appointment.getAppointmentDate());
            ps.setString(6, appointment.getReason());
            ps.setString(7, appointment.getStatus());
            ps.setString(8, appointment.getGuestName());
            ps.setString(9, appointment.getGuestPhone());
            ps.setInt(10, appointment.getAppointmentId());
            return ps.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Error updating appointment: " + e.getMessage(), e);
        }
    }

    // Lấy danh sách lịch theo doctorId với phân trang
    public List<Appointment> getAppointmentsByDoctorId(int doctorId, String statusFilter, String searchPatient, int page, int pageSize) {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT a.appointment_id, a.patient_id, a.doctor_id, a.schedule_id, a.service_id, "
                + "a.appointment_date, a.reason, a.status, a.note, a.guest_name, a.guest_phone, "
                + "p.full_name AS patient_name, s.name AS service_name "
                + "FROM Appointments a "
                + "LEFT JOIN Patients p ON a.patient_id = p.patient_id "
                + "JOIN Services s ON a.service_id = s.service_id "
                + "WHERE a.doctor_id = ? AND a.isActive = 1"
                + (statusFilter != null && !statusFilter.isEmpty() ? " AND a.status = ?" : "")
                + (searchPatient != null && !searchPatient.isEmpty() ? " AND (p.full_name LIKE ? OR a.guest_name LIKE ?)" : "")
                + " ORDER BY a.appointment_date DESC "
                + "OFFSET " + ((page - 1) * pageSize) + " ROWS FETCH NEXT " + pageSize + " ROWS ONLY";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            int paramIndex = 1;
            ps.setInt(paramIndex++, doctorId);
            if (statusFilter != null && !statusFilter.isEmpty()) {
                ps.setString(paramIndex++, statusFilter);
            }
            if (searchPatient != null && !searchPatient.isEmpty()) {
                ps.setString(paramIndex++, "%" + searchPatient + "%");
                ps.setString(paramIndex++, "%" + searchPatient + "%");
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Appointment appointment = new Appointment(
                        rs.getInt("appointment_id"),
                        rs.getObject("patient_id") != null ? rs.getInt("patient_id") : null,
                        rs.getInt("doctor_id"),
                        rs.getInt("schedule_id"),
                        rs.getInt("service_id"),
                        rs.getDate("appointment_date"),
                        rs.getString("reason"),
                        rs.getString("status"),
                        rs.getString("patient_name"),
                        null, // doctorName
                        rs.getString("service_name"),
                        rs.getString("note"),
                        rs.getString("guest_name"),
                        rs.getString("guest_phone")
                );
                appointments.add(appointment);
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Error retrieving appointments by doctor: " + e.getMessage(), e);
        }
        return appointments;
    }

    // Lấy lịch theo appointmentId
    public Appointment getAppointmentById(int appointmentId) {
        String sql = "SELECT a.appointment_id, a.patient_id, a.doctor_id, a.schedule_id, a.service_id, "
                + "a.appointment_date, a.reason, a.status, a.note, a.guest_name, a.guest_phone, "
                + "p.full_name AS patient_name, d.full_name AS doctor_name, s.name AS service_name "
                + "FROM Appointments a "
                + "LEFT JOIN Patients p ON a.patient_id = p.patient_id "
                + "JOIN Doctors d ON a.doctor_id = d.doctor_id "
                + "JOIN Services s ON a.service_id = s.service_id "
                + "WHERE a.appointment_id = ? AND a.isActive = 1";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, appointmentId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Appointment(
                            rs.getInt("appointment_id"),
                            rs.getObject("patient_id") != null ? rs.getInt("patient_id") : null,
                            rs.getInt("doctor_id"),
                            rs.getInt("schedule_id"),
                            rs.getInt("service_id"),
                            rs.getDate("appointment_date"),
                            rs.getString("reason"),
                            rs.getString("status"),
                            rs.getString("patient_name"),
                            rs.getString("doctor_name"),
                            rs.getString("service_name"),
                            rs.getString("note"),
                            rs.getString("guest_name"),
                            rs.getString("guest_phone")
                    );
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Error retrieving appointment by ID: " + e.getMessage(), e);
        }
        return null;
    }

    // Đếm số lượng lịch theo doctorId
    public int getAppointmentsCountByDoctorId(int doctorId, String statusFilter, String searchPatient) {
        String sql = "SELECT COUNT(*) "
                + "FROM Appointments a "
                + "LEFT JOIN Patients p ON a.patient_id = p.patient_id "
                + "WHERE a.doctor_id = ? AND a.isActive = 1"
                + (statusFilter != null && !statusFilter.isEmpty() ? " AND a.status = ?" : "")
                + (searchPatient != null && !searchPatient.isEmpty() ? " AND (p.full_name LIKE ? OR a.guest_name LIKE ?)" : "");
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            int paramIndex = 1;
            ps.setInt(paramIndex++, doctorId);
            if (statusFilter != null && !statusFilter.isEmpty()) {
                ps.setString(paramIndex++, statusFilter);
            }
            if (searchPatient != null && !searchPatient.isEmpty()) {
                ps.setString(paramIndex++, "%" + searchPatient + "%");
                ps.setString(paramIndex++, "%" + searchPatient + "%");
            }
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Error counting appointments: " + e.getMessage(), e);
        }
        return 0;
    }

    // Hủy lịch
    public boolean cancelAppointment(int appointmentId) {
        String sql = "UPDATE Appointments SET status = 'canceled' WHERE appointment_id = ? AND status IN ('pending', 'confirmed') AND isActive = 1";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, appointmentId);
            return ps.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Error canceling appointment: " + e.getMessage(), e);
        }
    }

    // Lấy scheduleId theo appointmentId
    public int getScheduleIdByAppointmentId(int appointmentId) {
        String sql = "SELECT schedule_id FROM Appointments WHERE appointment_id = ? AND isActive = 1";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, appointmentId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("schedule_id");
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Error retrieving schedule ID: " + e.getMessage(), e);
        }
        return -1;
    }

    // Lấy lịch sử khám bệnh theo patientId
    public List<Appointment> getMedicalHistoryByPatientId(int patientId) {
        List<Appointment> history = new ArrayList<>();
        String sql = "SELECT a.appointment_id, a.patient_id, a.doctor_id, a.schedule_id, a.service_id, "
                + "a.appointment_date, a.reason, a.status, a.note, a.guest_name, a.guest_phone, "
                + "d.full_name AS doctor_name, s.name AS service_name "
                + "FROM Appointments a "
                + "JOIN Doctors d ON a.doctor_id = d.doctor_id "
                + "JOIN Services s ON a.service_id = s.service_id "
                + "WHERE a.patient_id = ? AND a.status = 'completed' AND a.isActive = 1 "
                + "ORDER BY a.appointment_date DESC";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, patientId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Appointment appointment = new Appointment(
                        rs.getInt("appointment_id"),
                        rs.getObject("patient_id") != null ? rs.getInt("patient_id") : null,
                        rs.getInt("doctor_id"),
                        rs.getInt("schedule_id"),
                        rs.getInt("service_id"),
                        rs.getDate("appointment_date"),
                        rs.getString("reason"),
                        rs.getString("status"),
                        null, // patientName
                        rs.getString("doctor_name"),
                        rs.getString("service_name"),
                        rs.getString("note"),
                        rs.getString("guest_name"),
                        rs.getString("guest_phone")
                );
                history.add(appointment);
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Error retrieving medical history: " + e.getMessage(), e);
        }
        return history;
    }

    // Xác nhận lịch
    public boolean confirmAppointment(int appointmentId) {
        String sql = "UPDATE Appointments SET status = 'confirmed' WHERE appointment_id = ? AND status = 'pending' AND isActive = 1";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, appointmentId);
            return ps.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Error confirming appointment: " + e.getMessage(), e);
        }
    }

    // Hoàn thành lịch
    public boolean completeAppointment(int appointmentId, String note) {
        String sql = "UPDATE Appointments SET status = 'completed', note = ? WHERE appointment_id = ? AND status = 'confirmed' AND isActive = 1";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, note);
            ps.setInt(2, appointmentId);
            return ps.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Error completing appointment: " + e.getMessage(), e);
        }
    }

    // Lấy tất cả lịch với phân trang
    public List<Appointment> getAllAppointments(String statusFilter, String searchPatient, String searchDoctor, int page, int pageSize) {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT a.appointment_id, a.patient_id, a.doctor_id, a.schedule_id, a.service_id, "
                + "a.appointment_date, a.reason, a.status, a.note, a.guest_name, a.guest_phone, "
                + "p.full_name AS patient_name, d.full_name AS doctor_name, s.name AS service_name "
                + "FROM Appointments a "
                + "LEFT JOIN Patients p ON a.patient_id = p.patient_id "
                + "JOIN Doctors d ON a.doctor_id = d.doctor_id "
                + "JOIN Services s ON a.service_id = s.service_id "
                + "WHERE a.isActive = 1"
                + (statusFilter != null && !statusFilter.isEmpty() ? " AND a.status = ?" : "")
                + (searchPatient != null && !searchPatient.isEmpty() ? " AND (p.full_name LIKE ? OR a.guest_name LIKE ?)" : "")
                + (searchDoctor != null && !searchDoctor.isEmpty() ? " AND d.full_name LIKE ?" : "")
                + " ORDER BY a.appointment_date DESC "
                + "OFFSET " + ((page - 1) * pageSize) + " ROWS FETCH NEXT " + pageSize + " ROWS ONLY";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            int paramIndex = 1;
            if (statusFilter != null && !statusFilter.isEmpty()) {
                ps.setString(paramIndex++, statusFilter);
            }
            if (searchPatient != null && !searchPatient.isEmpty()) {
                ps.setString(paramIndex++, "%" + searchPatient + "%");
                ps.setString(paramIndex++, "%" + searchPatient + "%");
            }
            if (searchDoctor != null && !searchDoctor.isEmpty()) {
                ps.setString(paramIndex++, "%" + searchDoctor + "%");
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Appointment appointment = new Appointment(
                        rs.getInt("appointment_id"),
                        rs.getObject("patient_id") != null ? rs.getInt("patient_id") : null,
                        rs.getInt("doctor_id"),
                        rs.getInt("schedule_id"),
                        rs.getInt("service_id"),
                        rs.getDate("appointment_date"),
                        rs.getString("reason"),
                        rs.getString("status"),
                        rs.getString("patient_name"),
                        rs.getString("doctor_name"),
                        rs.getString("service_name"),
                        rs.getString("note"),
                        rs.getString("guest_name"),
                        rs.getString("guest_phone")
                );
                appointments.add(appointment);
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Error retrieving all appointments: " + e.getMessage(), e);
        }
        return appointments;
    }

    // Đếm tất cả lịch
    public int getAllAppointmentsCount(String statusFilter, String searchPatient, String searchDoctor) {
        String sql = "SELECT COUNT(*) "
                + "FROM Appointments a "
                + "LEFT JOIN Patients p ON a.patient_id = p.patient_id "
                + "JOIN Doctors d ON a.doctor_id = d.doctor_id "
                + "WHERE a.isActive = 1"
                + (statusFilter != null && !statusFilter.isEmpty() ? " AND a.status = ?" : "")
                + (searchPatient != null && !searchPatient.isEmpty() ? " AND (p.full_name LIKE ? OR a.guest_name LIKE ?)" : "")
                + (searchDoctor != null && !searchDoctor.isEmpty() ? " AND d.full_name LIKE ?" : "");
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            int paramIndex = 1;
            if (statusFilter != null && !statusFilter.isEmpty()) {
                ps.setString(paramIndex++, statusFilter);
            }
            if (searchPatient != null && !searchPatient.isEmpty()) {
                ps.setString(paramIndex++, "%" + searchPatient + "%");
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
            throw new RuntimeException("Error counting all appointments: " + e.getMessage(), e);
        }
        return 0;
    }

    // Lấy danh sách lịch theo doctorId và ngày
    public List<Appointment> getAppointmentsByDoctorIdAndDate(int doctorId, String date, String statusFilter, String searchPatient, int page, int pageSize) {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT a.appointment_id, a.patient_id, a.doctor_id, a.schedule_id, a.service_id, "
                + "a.appointment_date, a.reason, a.status, a.note, a.guest_name, a.guest_phone, "
                + "p.full_name AS patient_name, s.name AS service_name "
                + "FROM Appointments a "
                + "LEFT JOIN Patients p ON a.patient_id = p.patient_id "
                + "JOIN Services s ON a.service_id = s.service_id "
                + "WHERE a.doctor_id = ? AND a.appointment_date = ? AND a.isActive = 1"
                + (statusFilter != null && !statusFilter.isEmpty() ? " AND a.status = ?" : "")
                + (searchPatient != null && !searchPatient.isEmpty() ? " AND (p.full_name LIKE ? OR a.guest_name LIKE ?)" : "")
                + " ORDER BY a.appointment_date DESC "
                + "OFFSET " + ((page - 1) * pageSize) + " ROWS FETCH NEXT " + pageSize + " ROWS ONLY";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            int paramIndex = 1;
            ps.setInt(paramIndex++, doctorId);
            ps.setDate(paramIndex++, java.sql.Date.valueOf(date));
            if (statusFilter != null && !statusFilter.isEmpty()) {
                ps.setString(paramIndex++, statusFilter);
            }
            if (searchPatient != null && !searchPatient.isEmpty()) {
                ps.setString(paramIndex++, "%" + searchPatient + "%");
                ps.setString(paramIndex++, "%" + searchPatient + "%");
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Appointment appointment = new Appointment(
                        rs.getInt("appointment_id"),
                        rs.getObject("patient_id") != null ? rs.getInt("patient_id") : null,
                        rs.getInt("doctor_id"),
                        rs.getInt("schedule_id"),
                        rs.getInt("service_id"),
                        rs.getDate("appointment_date"),
                        rs.getString("reason"),
                        rs.getString("status"),
                        rs.getString("patient_name"),
                        null, // doctorName
                        rs.getString("service_name"),
                        rs.getString("note"),
                        rs.getString("guest_name"),
                        rs.getString("guest_phone")
                );
                appointments.add(appointment);
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Error retrieving appointments by doctor and date: " + e.getMessage(), e);
        }
        return appointments;
    }

    // Đếm số lượng lịch theo doctorId và ngày
    public int getAppointmentsCountByDoctorIdAndDate(int doctorId, String date, String statusFilter, String searchPatient) {
        String sql = "SELECT COUNT(*) "
                + "FROM Appointments a "
                + "LEFT JOIN Patients p ON a.patient_id = p.patient_id "
                + "WHERE a.doctor_id = ? AND a.appointment_date = ? AND a.isActive = 1"
                + (statusFilter != null && !statusFilter.isEmpty() ? " AND a.status = ?" : "")
                + (searchPatient != null && !searchPatient.isEmpty() ? " AND (p.full_name LIKE ? OR a.guest_name LIKE ?)" : "");
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            int paramIndex = 1;
            ps.setInt(paramIndex++, doctorId);
            ps.setDate(paramIndex++, java.sql.Date.valueOf(date));
            if (statusFilter != null && !statusFilter.isEmpty()) {
                ps.setString(paramIndex++, statusFilter);
            }
            if (searchPatient != null && !searchPatient.isEmpty()) {
                ps.setString(paramIndex++, "%" + searchPatient + "%");
                ps.setString(paramIndex++, "%" + searchPatient + "%");
            }
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Error counting appointments by doctor and date: " + e.getMessage(), e);
        }
        return 0;
    }

    // Lấy danh sách lịch theo ngày
    public List<Appointment> getAllAppointmentsByDate(String date, String statusFilter, String searchPatient, String searchDoctor, int page, int pageSize) {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT a.appointment_id, a.patient_id, a.doctor_id, a.schedule_id, a.service_id, "
                + "a.appointment_date, a.reason, a.status, a.note, a.guest_name, a.guest_phone, "
                + "p.full_name AS patient_name, d.full_name AS doctor_name, s.name AS service_name "
                + "FROM Appointments a "
                + "LEFT JOIN Patients p ON a.patient_id = p.patient_id "
                + "JOIN Doctors d ON a.doctor_id = d.doctor_id "
                + "JOIN Services s ON a.service_id = s.service_id "
                + "WHERE a.appointment_date = ? AND a.isActive = 1"
                + (statusFilter != null && !statusFilter.isEmpty() ? " AND a.status = ?" : "")
                + (searchPatient != null && !searchPatient.isEmpty() ? " AND (p.full_name LIKE ? OR a.guest_name LIKE ?)" : "")
                + (searchDoctor != null && !searchDoctor.isEmpty() ? " AND d.full_name LIKE ?" : "")
                + " ORDER BY a.appointment_date DESC "
                + "OFFSET " + ((page - 1) * pageSize) + " ROWS FETCH NEXT " + pageSize + " ROWS ONLY";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            int paramIndex = 1;
            ps.setDate(paramIndex++, java.sql.Date.valueOf(date));
            if (statusFilter != null && !statusFilter.isEmpty()) {
                ps.setString(paramIndex++, statusFilter);
            }
            if (searchPatient != null && !searchPatient.isEmpty()) {
                ps.setString(paramIndex++, "%" + searchPatient + "%");
                ps.setString(paramIndex++, "%" + searchPatient + "%");
            }
            if (searchDoctor != null && !searchDoctor.isEmpty()) {
                ps.setString(paramIndex++, "%" + searchDoctor + "%");
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Appointment appointment = new Appointment(
                        rs.getInt("appointment_id"),
                        rs.getObject("patient_id") != null ? rs.getInt("patient_id") : null,
                        rs.getInt("doctor_id"),
                        rs.getInt("schedule_id"),
                        rs.getInt("service_id"),
                        rs.getDate("appointment_date"),
                        rs.getString("reason"),
                        rs.getString("status"),
                        rs.getString("patient_name"),
                        rs.getString("doctor_name"),
                        rs.getString("service_name"),
                        rs.getString("note"),
                        rs.getString("guest_name"),
                        rs.getString("guest_phone")
                );
                appointments.add(appointment);
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Error retrieving appointments by date: " + e.getMessage(), e);
        }
        return appointments;
    }

    // Đếm số lượng lịch theo ngày
    public int getAllAppointmentsCountByDate(String date, String statusFilter, String searchPatient, String searchDoctor) {
        String sql = "SELECT COUNT(*) "
                + "FROM Appointments a "
                + "LEFT JOIN Patients p ON a.patient_id = p.patient_id "
                + "JOIN Doctors d ON a.doctor_id = d.doctor_id "
                + "WHERE a.appointment_date = ? AND a.isActive = 1"
                + (statusFilter != null && !statusFilter.isEmpty() ? " AND a.status = ?" : "")
                + (searchPatient != null && !searchPatient.isEmpty() ? " AND (p.full_name LIKE ? OR a.guest_name LIKE ?)" : "")
                + (searchDoctor != null && !searchDoctor.isEmpty() ? " AND d.full_name LIKE ?" : "");
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            int paramIndex = 1;
            ps.setDate(paramIndex++, java.sql.Date.valueOf(date));
            if (statusFilter != null && !statusFilter.isEmpty()) {
                ps.setString(paramIndex++, statusFilter);
            }
            if (searchPatient != null && !searchPatient.isEmpty()) {
                ps.setString(paramIndex++, "%" + searchPatient + "%");
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
            throw new RuntimeException("Error counting appointments by date: " + e.getMessage(), e);
        }
        return 0;
    }

    // Xóa lịch hẹn (chuyển isActive = 0)
    public boolean deleteAppointment(int appointmentId) {
        String sql = "UPDATE Appointments SET isActive = 0 WHERE appointment_id = ? AND isActive = 1";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, appointmentId);
            return ps.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Error deleting appointment: " + e.getMessage(), e);
        }
    }
}