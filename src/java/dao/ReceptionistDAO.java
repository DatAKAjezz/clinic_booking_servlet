package dao;

import model.Receptionist;
import utils.DBUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReceptionistDAO {

    public List<Receptionist> getAllReceptionists() {
        List<Receptionist> receptionists = new ArrayList<>();
        String sql = "SELECT receptionist_id, user_id, full_name, experience FROM Receptionists";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Receptionist receptionist = new Receptionist(
                    rs.getInt("receptionist_id"),
                    rs.getInt("user_id"),
                    rs.getString("full_name"),
                    rs.getInt("experience")
                );
                receptionists.add(receptionist);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return receptionists;
    }

    public Receptionist getReceptionistByUserId(int userId) {
        String sql = "SELECT receptionist_id, user_id, full_name, experience FROM Receptionists WHERE user_id = ?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Receptionist(
                        rs.getInt("receptionist_id"),
                        rs.getInt("user_id"),
                        rs.getString("full_name"),
                        rs.getInt("experience")
                    );
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}