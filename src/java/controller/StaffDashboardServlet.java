package controller;

import model.Appointment;
import model.User;
import service.AppointmentService;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class StaffDashboardServlet extends HttpServlet {
    private AppointmentService appointmentService = new AppointmentService();
    private static final int PAGE_SIZE = 5;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("USER");
        if (user == null || !"receptionist".equals(user.getRole())) {
            response.sendRedirect("LoginPage.jsp");
            return;
        }

        String statusFilter = request.getParameter("statusFilter");
        String searchPatient = request.getParameter("searchPatient");
        String searchDoctor = request.getParameter("searchDoctor");
        int page = 1;
        try {
            page = Integer.parseInt(request.getParameter("page"));
        } catch (NumberFormatException e) {
            page = 1;
        }
        if (page < 1) page = 1;

        List<Appointment> appointments = appointmentService.getAllAppointments(statusFilter, searchPatient, searchDoctor, page, PAGE_SIZE);
        int totalAppointments = appointmentService.getAllAppointmentsCount(statusFilter, searchPatient, searchDoctor);
        int totalPages = (int) Math.ceil((double) totalAppointments / PAGE_SIZE);
        if (page > totalPages && totalPages > 0) page = totalPages;

        request.setAttribute("appointments", appointments);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.getRequestDispatcher("StaffDashboard.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("USER");
        if (user == null || !"receptionist".equals(user.getRole())) {
            response.sendRedirect("LoginPage.jsp");
            return;
        }

        String action = request.getParameter("action");
        int appointmentId;
        try {
            appointmentId = Integer.parseInt(request.getParameter("appointmentId"));
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("error", "Invalid appointment ID.");
            response.sendRedirect("StaffDashboardServlet");
            return;
        }

        if ("confirm".equals(action)) {
            if (appointmentService.confirmAppointment(appointmentId)) {
                request.getSession().setAttribute("message", "Appointment confirmed successfully!");
            } else {
                request.getSession().setAttribute("error", "Failed to confirm appointment.");
            }
        } else if ("cancel".equals(action)) {
            int scheduleId = appointmentService.getScheduleIdByAppointmentId(appointmentId);
            if (appointmentService.cancelAppointment(appointmentId)) {
                if (scheduleId != -1 && new service.ScheduleService().updateScheduleStatus(scheduleId, "available")) {
                    request.getSession().setAttribute("message", "Appointment canceled successfully!");
                } else {
                    request.getSession().setAttribute("error", "Failed to update schedule status.");
                }
            } else {
                request.getSession().setAttribute("error", "Failed to cancel appointment.");
            }
        } else if ("complete".equals(action)) {
            String note = request.getParameter("note");
            if (note == null || note.trim().isEmpty()) {
                request.getSession().setAttribute("error", "Please provide a note before completing the appointment.");
            } else if (appointmentService.completeAppointment(appointmentId, note)) {
                request.getSession().setAttribute("message", "Appointment completed successfully!");
            } else {
                request.getSession().setAttribute("error", "Failed to complete appointment.");
            }
        }

        response.sendRedirect("StaffDashboardServlet");
    }
}