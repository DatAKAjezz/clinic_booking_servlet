package controller;

import model.Appointment;
import model.User;
import service.AppointmentService;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
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

        // Lấy thông tin lọc và phân trang
        String statusFilter = request.getParameter("statusFilter");
        String searchPatient = request.getParameter("searchPatient");
        String searchDoctor = request.getParameter("searchDoctor");
        String todayParam = request.getParameter("today");
        String dateFilter = request.getParameter("dateFilter");
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
            appointments = appointmentService.getAllAppointmentsByDate(
                today.toString(), statusFilter, searchPatient, searchDoctor, page, PAGE_SIZE);
            totalAppointments = appointmentService.getAllAppointmentsCountByDate(
                today.toString(), statusFilter, searchPatient, searchDoctor);
            request.setAttribute("showingToday", true);
        } else if (dateFilter != null && !dateFilter.isEmpty()) {
            appointments = appointmentService.getAllAppointmentsByDate(
                dateFilter, statusFilter, searchPatient, searchDoctor, page, PAGE_SIZE);
            totalAppointments = appointmentService.getAllAppointmentsCountByDate(
                dateFilter, statusFilter, searchPatient, searchDoctor);
        } else {
            appointments = appointmentService.getAllAppointments(
                statusFilter, searchPatient, searchDoctor, page, PAGE_SIZE);
            totalAppointments = appointmentService.getAllAppointmentsCount(
                statusFilter, searchPatient, searchDoctor);
        }

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
            request.getSession().setAttribute("error", "ID lịch hẹn không hợp lệ.");
            response.sendRedirect("StaffDashboardServlet");
            return;
        }

        if ("confirm".equals(action)) {
            if (appointmentService.confirmAppointment(appointmentId)) {
                request.getSession().setAttribute("message", "Xác nhận lịch hẹn thành công!");
            } else {
                request.getSession().setAttribute("error", "Không thể xác nhận lịch hẹn.");
            }
        } else if ("cancel".equals(action)) {
            int scheduleId = appointmentService.getScheduleIdByAppointmentId(appointmentId);
            if (appointmentService.cancelAppointment(appointmentId)) {
                if (scheduleId != -1 && new service.ScheduleService().updateScheduleStatus(scheduleId, "available")) {
                    request.getSession().setAttribute("message", "Hủy lịch hẹn thành công!");
                } else {
                    request.getSession().setAttribute("error", "Không thể cập nhật trạng thái khung giờ.");
                }
            } else {
                request.getSession().setAttribute("error", "Không thể hủy lịch hẹn.");
            }
        }

        response.sendRedirect("StaffDashboardServlet");
    }
}