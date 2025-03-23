package controller;

import model.Appointment;
import model.Doctor;
import model.Schedule;
import model.Service;
import model.User;
import service.AppointmentService;
import service.DoctorService;
import service.ScheduleService;
import service.ServiceService;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.util.List;

public class BookAppointmentServlet extends HttpServlet {
    private DoctorService doctorService = new DoctorService();
    private ScheduleService scheduleService = new ScheduleService();
    private ServiceService serviceService = new ServiceService();
    private AppointmentService appointmentService = new AppointmentService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("USER");
        if (user == null || !"patient".equals(user.getRole())) {
            response.sendRedirect("LoginPage.jsp");
            return;
        }

        List<Doctor> doctors = doctorService.getAllDoctors();
        List<Schedule> schedules = scheduleService.getAvailableSchedules();
        List<Service> services = serviceService.getAllServices();

        if (doctors.isEmpty() || schedules.isEmpty() || services.isEmpty()) {
            request.setAttribute("error", "No available doctors, schedules, or services.");
            request.getRequestDispatcher("HomePage.jsp").forward(request, response);
            return;
        }

        request.setAttribute("doctors", doctors);
        request.setAttribute("schedules", schedules);
        request.setAttribute("services", services);
        request.getRequestDispatcher("BookAppointment.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("USER");
        if (user == null || !"patient".equals(user.getRole())) {
            response.sendRedirect("LoginPage.jsp");
            return;
        }

        int patientId = user.getUserId();
        int doctorId = Integer.parseInt(request.getParameter("doctorId"));
        int scheduleId = Integer.parseInt(request.getParameter("scheduleId"));
        int serviceId = Integer.parseInt(request.getParameter("serviceId"));
        Date appointmentDate = Date.valueOf(request.getParameter("appointmentDate"));
        Time appointmentTime = Time.valueOf(request.getParameter("appointmentTime"));
        String reason = request.getParameter("reason");

        Appointment appointment = new Appointment(
            0, // appointment_id tự sinh
            patientId,
            doctorId,
            scheduleId,
            serviceId,
            appointmentDate,
            appointmentTime,
            reason,
            "pending" // Trạng thái ban đầu
        );

        if (appointmentService.bookAppointment(appointment)) {
            scheduleService.updateScheduleStatus(scheduleId, "booked");
            request.getSession().setAttribute("message", "Appointment booked successfully!");
            response.sendRedirect("PatientDashboardServlet");
        } else {
            request.setAttribute("error", "Failed to book appointment.");
            request.getRequestDispatcher("BookAppointment.jsp").forward(request, response);
        }
    }
}