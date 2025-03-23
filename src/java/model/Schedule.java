package model;

import java.sql.Date;
import java.sql.Time;

public class Schedule {
    private int scheduleId;   // schedule_id
    private int doctorId;     // doctor_id
    private Date date;        // date
    private Time startTime;   // start_time
    private Time endTime;     // end_time
    private String status;    // status (available, booked, unavailable)

    public Schedule() {}

    public Schedule(int scheduleId, int doctorId, Date date, Time startTime, Time endTime, String status) {
        this.scheduleId = scheduleId;
        this.doctorId = doctorId;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
    }

    // Getters và Setters
    public int getScheduleId() { return scheduleId; }
    public void setScheduleId(int scheduleId) { this.scheduleId = scheduleId; }
    public int getDoctorId() { return doctorId; }
    public void setDoctorId(int doctorId) { this.doctorId = doctorId; }
    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }
    public Time getStartTime() { return startTime; }
    public void setStartTime(Time startTime) { this.startTime = startTime; }
    public Time getEndTime() { return endTime; }
    public void setEndTime(Time endTime) { this.endTime = endTime; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return "Schedule{" +
                "scheduleId=" + scheduleId +
                ", doctorId=" + doctorId +
                ", date=" + date +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", status='" + status + '\'' +
                '}';
    }
}