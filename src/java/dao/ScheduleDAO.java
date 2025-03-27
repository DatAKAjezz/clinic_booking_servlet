package dao;

import model.Schedule;
import utils.DBUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ScheduleDAO {
    private static final Logger LOGGER = Logger.getLogger(ScheduleDAO.class.getName());

    public boolean updateScheduleStatus(int scheduleId, String status) {
        String sql = "UPDATE Schedules SET status = ? WHERE schedule_id = ?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, scheduleId);
            return ps.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Error updating schedule status for scheduleId: " + scheduleId, e);
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
                schedules.add(mapResultSetToSchedule(rs));
            }
        } catch (SQLException | ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving available schedules", e);
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
                    schedules.add(mapResultSetToSchedule(rs));
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving available schedules for doctorId: " + doctorId, e);
        }
        return schedules;
    }

    public List<Schedule> getAllSchedules() {
        List<Schedule> schedules = new ArrayList<>();
        String sql = "SELECT schedule_id, doctor_id, start_time, end_time, status " +
                     "FROM Schedules";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                schedules.add(mapResultSetToSchedule(rs));
            }
        } catch (SQLException | ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving all schedules", e);
        }
        return schedules;
    }

    private Schedule mapResultSetToSchedule(ResultSet rs) throws SQLException {
        return new Schedule(
            rs.getInt("schedule_id"),
            rs.getInt("doctor_id"),
            rs.getTime("start_time"),
            rs.getTime("end_time"),
            rs.getString("status")
        );
    }
}