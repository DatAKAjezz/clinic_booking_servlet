package dao;

import model.Patient;
import utils.DBUtils;
import java.sql.*;

public class PatientDAO {
    public boolean addPatient(Patient patient) {
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
}