package service;

import dao.ScheduleDAO;
import model.Schedule;
import java.util.List;

public class ScheduleService {
    private ScheduleDAO scheduleDAO = new ScheduleDAO();

    public boolean updateScheduleStatus(int scheduleId, String status) {
        return scheduleDAO.updateScheduleStatus(scheduleId, status);
    }

    public List<Schedule> getAvailableSchedules() {
        return scheduleDAO.getAvailableSchedules();
    }

    public List<Schedule> getSchedulesByDoctorId(int doctorId) {
        return scheduleDAO.getAvailableSchedulesByDoctor(doctorId);
    }
    
    public List<Schedule> getAvailableSchedulesByDoctor(int doctorId) {
        return scheduleDAO.getAvailableSchedulesByDoctor(doctorId);
    }
    
    public List<Schedule> getAllSchedules() {
        return scheduleDAO.getAllSchedules();
    }
}