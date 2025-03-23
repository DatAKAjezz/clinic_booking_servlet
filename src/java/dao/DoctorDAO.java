package dao;

import model.Doctor;
import utils.DBUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DoctorDAO {
    public List<Doctor> getAllDoctors() {
        List<Doctor> doctors = new ArrayList<>();
        String sql = "SELECT doctor_id, user_id, full_name, specialty, experience FROM Doctors";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Doctor doctor = new Doctor(
                    rs.getInt("doctor_id"),
                    rs.getInt("user_id"),
                    rs.getString("full_name"),
                    rs.getString("specialty"),
                    rs.getInt("experience")
                );
                doctors.add(doctor);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return doctors;
    }
}