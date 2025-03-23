package dao;

import model.Patient;
import utils.DBUtils;
import java.sql.*;

public class PatientDAO {

    public boolean createPatient(Patient patient) { // Đổi từ addPatient
        String sql = "INSERT INTO Patients (user_id, full_name, age, address, gender) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, patient.getUserId());
            ps.setString(2, patient.getFullName());
            ps.setInt(3, patient.getAge());
            ps.setString(4, patient.getAddress());
            ps.setString(5, patient.getGender());
            return ps.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Các phương thức khác giữ nguyên
    public boolean updatePatientProfile(Patient patient) {
        String sql = "UPDATE Patients SET full_name = ?, address = ?, gender = ? WHERE user_id = ?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, patient.getFullName());
            ps.setString(2, patient.getAddress());
            ps.setString(3, patient.getGender());
            ps.setInt(4, patient.getUserId());
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Patient getPatientByUserId(int userId) {
        String sql = "SELECT patient_id, user_id, full_name, age, address, gender FROM Patients WHERE user_id = ?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Patient(
                        rs.getInt("patient_id"),
                        rs.getInt("user_id"),
                        rs.getString("full_name"),
                        rs.getInt("age"),
                        rs.getString("address"),
                        rs.getString("gender")
                );
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}