package dao;

import model.Service;
import utils.DBUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ServiceDAO {
    public List<Service> getAllServices() {
        List<Service> services = new ArrayList<>();
        String sql = "SELECT service_id, name, price, duration FROM Services";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Service service = new Service(
                    rs.getInt("service_id"),
                    rs.getString("name"),
                    rs.getDouble("price"),
                    rs.getInt("duration")
                );
                services.add(service);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return services;
    }
}