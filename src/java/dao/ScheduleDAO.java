package dao;

import model.Schedule;
import utils.DBUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ScheduleDAO {
    public boolean updateScheduleStatus(int scheduleId, String status) {
        String sql = "UPDATE Schedules SET status = ? WHERE schedule_id = ?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, scheduleId);
            return ps.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Schedule> getAvailableSchedules() {
        List<Schedule> schedules = new ArrayList<>();
        String sql = "SELECT schedule_id, doctor_id, start_time, end_time, status " +
                     "FROM Schedules WHERE status = 'available'";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Schedule schedule = new Schedule(
                    rs.getInt("schedule_id"),
                    rs.getInt("doctor_id"),
                    rs.getTime("start_time"),
                    rs.getTime("end_time"),
                    rs.getString("status")
                );
                schedules.add(schedule);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return schedules;
    }

    public List<Schedule> getAvailableSchedulesByDoctor(int doctorId) {
        List<Schedule> schedules = new ArrayList<>();
        String sql = "SELECT schedule_id, doctor_id, start_time, end_time, status " +
                     "FROM Schedules WHERE status = 'available' AND doctor_id = ?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, doctorId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Schedule schedule = new Schedule(
                        rs.getInt("schedule_id"),
                        rs.getInt("doctor_id"),
                        rs.getTime("start_time"),
                        rs.getTime("end_time"),
                        rs.getString("status")
                    );
                    schedules.add(schedule);
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return schedules;
    }
}