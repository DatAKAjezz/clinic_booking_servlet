package controller;

import model.Appointment;
import model.User;
import service.AppointmentService;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class DoctorDashboardServlet extends HttpServlet {
    private AppointmentService appointmentService = new AppointmentService();
    private static final int PAGE_SIZE = 5;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("USER");
        if (user == null || !"doctor".equals(user.getRole())) {
            response.sendRedirect("LoginPage.jsp");
            return;
        }

        int doctorId = getDoctorIdByUserId(user.getUserId());
        if (doctorId == -1) {
            request.setAttribute("error", "Cannot identify doctor.");
            request.getRequestDispatcher("HomePage.jsp").forward(request, response);
            return;
        }

        String statusFilter = request.getParameter("statusFilter");
        String searchPatient = request.getParameter("searchPatient");
        String todayParam = request.getParameter("today");
        String dateFilter = request.getParameter("dateFilter"); // Thêm tham số dateFilter
        boolean showToday = "true".equals(todayParam);
        int page = 1;
        
        try {
            page = Integer.parseInt(request.getParameter("page"));
        } catch (NumberFormatException e) {
            page = 1;
        }
        if (page < 1) page = 1;

        List<Appointment> appointments;
        int totalAppointments;
        
        if (showToday) {
            LocalDate today = LocalDate.now(); // 2025-03-25
            appointments = appointmentService.getAppointmentsByDoctorIdAndDate(
                doctorId, today.toString(), statusFilter, searchPatient, page, PAGE_SIZE);
            totalAppointments = appointmentService.getAppointmentsCountByDoctorIdAndDate(
                doctorId, today.toString(), statusFilter, searchPatient);
            request.setAttribute("showingToday", true);
        } else if (dateFilter != null && !dateFilter.isEmpty()) {
            // Lọc theo ngày được chọn từ input
            appointments = appointmentService.getAppointmentsByDoctorIdAndDate(
                doctorId, dateFilter, statusFilter, searchPatient, page, PAGE_SIZE);
            totalAppointments = appointmentService.getAppointmentsCountByDoctorIdAndDate(
                doctorId, dateFilter, statusFilter, searchPatient);
        } else {
            // Hiển thị tất cả lịch khám
            appointments = appointmentService.getAppointmentsByDoctorId(
                doctorId, statusFilter, searchPatient, page, PAGE_SIZE);
            totalAppointments = appointmentService.getAppointmentsCountByDoctorId(
                doctorId, statusFilter, searchPatient);
        }

        int totalPages = (int) Math.ceil((double) totalAppointments / PAGE_SIZE);
        if (page > totalPages && totalPages > 0) page = totalPages;

        request.setAttribute("appointments", appointments);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.getRequestDispatcher("DoctorDashboard.jsp").forward(request, response);
    }

    // Giữ nguyên doPost và getDoctorIdByUserId
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("USER");
        if (user == null || !"doctor".equals(user.getRole())) {
            response.sendRedirect("LoginPage.jsp");
            return;
        }

        int appointmentId;
        try {
            appointmentId = Integer.parseInt(request.getParameter("appointmentId"));
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("error", "Invalid appointment ID.");
            response.sendRedirect("DoctorDashboardServlet");
            return;
        }

        String action = request.getParameter("action");
        if ("complete".equals(action)) {
            String note = request.getParameter("note");
            if (note == null || note.trim().isEmpty()) {
                request.getSession().setAttribute("error", "Please provide a note before completing the appointment.");
            } else if (appointmentService.completeAppointment(appointmentId, note)) {
                request.getSession().setAttribute("message", "Appointment completed successfully!");
            } else {
                request.getSession().setAttribute("error", "Failed to complete appointment.");
            }
        }

        response.sendRedirect("DoctorDashboardServlet");
    }

    private int getDoctorIdByUserId(int userId) {
        String sql = "SELECT doctor_id FROM Doctors WHERE user_id = ?";
        try (Connection conn = utils.DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("doctor_id");
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return -1;
    }
}