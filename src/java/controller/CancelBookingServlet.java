package controller;

import model.User;
import service.AppointmentService;
import service.ScheduleService;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CancelBookingServlet extends HttpServlet {
    private AppointmentService appointmentService = new AppointmentService();
    private ScheduleService scheduleService = new ScheduleService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("USER");
        if (user == null || !"patient".equals(user.getRole())) {
            request.setAttribute("ERROR", "Bạn cần đăng nhập với tư cách patient.");
            request.getRequestDispatcher("LoginPage.jsp").forward(request, response);
            return;
        }

        int appointmentId;
        try {
            appointmentId = Integer.parseInt(request.getParameter("appointmentId"));
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid appointment ID.");
            response.sendRedirect("PatientDashboardServlet"); 
            return;
        }

        int scheduleId = appointmentService.getScheduleIdByAppointmentId(appointmentId);
        if (appointmentService.cancelAppointment(appointmentId)) {
            if (scheduleId != -1 && scheduleService.updateScheduleStatus(scheduleId, "available")) {
                request.setAttribute("message", "Appointment cancelled successfully!");
            } else {
                request.setAttribute("error", "Failed to update schedule status.");
            }
        } else {
            request.setAttribute("error", "Failed to cancel appointment.");
        }

        response.sendRedirect("PatientDashboardServlet");
    }
}