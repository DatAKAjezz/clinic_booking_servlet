package controller;

import model.Appointment;
import model.User;
import model.Doctor;
import model.Schedule;
import model.Service;
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
import java.time.LocalDate;
import java.util.List;

public class StaffDashboardServlet extends HttpServlet {

    private AppointmentService appointmentService = new AppointmentService();
    private DoctorService doctorService = new DoctorService();
    private ScheduleService scheduleService = new ScheduleService();
    private ServiceService serviceService = new ServiceService();
    private static final int PAGE_SIZE = 5;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("USER");
        if (user == null || !"receptionist".equals(user.getRole())) {
            request.setAttribute("ERROR", "Bạn cần đăng nhập với tư cách receptionist.");
            request.getRequestDispatcher("LoginPage.jsp").forward(request, response);
            return;
        }

        String action = request.getParameter("action");

        if ("add".equals(action)) {
            List<Doctor> doctors = doctorService.getAllDoctors();
            List<Schedule> schedules = scheduleService.getAllSchedules();
            List<Service> services = serviceService.getAllServices();

            request.setAttribute("doctors", doctors);
            request.setAttribute("schedules", schedules);
            request.setAttribute("services", services);
            request.getRequestDispatcher("AddAppointment.jsp").forward(request, response);
            return;
        }

        if ("loadSchedules".equals(action)) {
            String doctorIdStr = request.getParameter("doctorId");
            if (doctorIdStr != null && !doctorIdStr.isEmpty()) {
                int doctorId = Integer.parseInt(doctorIdStr);
                List<Schedule> schedules = scheduleService.getAvailableSchedulesByDoctor(doctorId);
                request.setAttribute("schedules", schedules);
                request.setAttribute("selectedDoctorId", doctorId);
            }

            List<Doctor> doctors = doctorService.getAllDoctors();
            List<Service> services = serviceService.getAllServices();
            request.setAttribute("doctors", doctors);
            request.setAttribute("services", services);
            request.getRequestDispatcher("AddAppointment.jsp").forward(request, response);
            return;
        }

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
        if (page < 1) {
            page = 1;
        }

        List<Appointment> appointments;
        int totalAppointments;

        if (showToday) {
            LocalDate today = LocalDate.now();
            appointments = appointmentService.getAllAppointmentsByDate(today.toString(), statusFilter, searchPatient, searchDoctor, page, PAGE_SIZE);
            totalAppointments = appointmentService.getAllAppointmentsCountByDate(today.toString(), statusFilter, searchPatient, searchDoctor);
            request.setAttribute("showingToday", true);
        } else if (dateFilter != null && !dateFilter.isEmpty()) {
            appointments = appointmentService.getAllAppointmentsByDate(dateFilter, statusFilter, searchPatient, searchDoctor, page, PAGE_SIZE);
            totalAppointments = appointmentService.getAllAppointmentsCountByDate(dateFilter, statusFilter, searchPatient, searchDoctor);
        } else {
            appointments = appointmentService.getAllAppointments(statusFilter, searchPatient, searchDoctor, page, PAGE_SIZE);
            totalAppointments = appointmentService.getAllAppointmentsCount(statusFilter, searchPatient, searchDoctor);
        }

        int totalPages = (int) Math.ceil((double) totalAppointments / PAGE_SIZE);
        if (page > totalPages && totalPages > 0) {
            page = totalPages;
        }

        List<Doctor> doctors = doctorService.getAllDoctors();
        List<Schedule> schedules = scheduleService.getAvailableSchedules();
        List<Service> services = serviceService.getAllServices();

        request.setAttribute("appointments", appointments);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("doctors", doctors);
        request.setAttribute("schedules", schedules);
        request.setAttribute("services", services);

        String editId = request.getParameter("editId");
        if (editId != null) {
            try {
                int appointmentId = Integer.parseInt(editId);
                Appointment editAppointment = appointmentService.getAppointmentById(appointmentId);
                request.setAttribute("editAppointment", editAppointment);
            } catch (NumberFormatException e) {
                request.setAttribute("error", "ID lịch hẹn không hợp lệ.");
            }
        }

        request.getRequestDispatcher("StaffDashboard.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        User user = (User) request.getSession().getAttribute("USER");
        if (user == null || !"receptionist".equals(user.getRole())) {
            request.setAttribute("ERROR", "Bạn cần đăng nhập với tư cách receptionist.");
            request.getRequestDispatcher("LoginPage.jsp").forward(request, response);
            return;
        }

        String action = request.getParameter("action");

        if ("delete".equals(action)) {
            try {
                int appointmentId = Integer.parseInt(request.getParameter("appointmentId"));
                int scheduleId = appointmentService.getScheduleIdByAppointmentId(appointmentId);
                boolean deleted = appointmentService.deleteAppointment(appointmentId);
                if (deleted) {
                    if (scheduleId != -1) {
                        scheduleService.updateScheduleStatus(scheduleId, "available");
                    }
                    request.getSession().setAttribute("message", "Lịch hẹn đã được xóa thành công!");
                } else {
                    request.getSession().setAttribute("error", "Không thể xóa lịch hẹn! Lịch hẹn không tồn tại hoặc đã bị xóa trước đó.");
                }
            } catch (NumberFormatException e) {
                request.getSession().setAttribute("error", "ID lịch hẹn không hợp lệ!");
            }
            response.sendRedirect("StaffDashboardServlet");
            return;
        }

        if ("add".equals(action)) {
            try {
                Integer patientId = request.getParameter("patientId").isEmpty() ? null : Integer.parseInt(request.getParameter("patientId"));
                String guestName = request.getParameter("guestName");
                String guestPhone = request.getParameter("guestPhone");
                int doctorId = Integer.parseInt(request.getParameter("doctorId"));
                int scheduleId = Integer.parseInt(request.getParameter("scheduleId"));
                int serviceId = Integer.parseInt(request.getParameter("serviceId"));
                Date appointmentDate = Date.valueOf(request.getParameter("appointmentDate"));
                String reason = request.getParameter("reason");

                Appointment appointment = new Appointment(0, patientId, doctorId, scheduleId, serviceId, appointmentDate, reason, "pending", guestName, guestPhone);
                boolean success = appointmentService.bookAppointment(appointment);
                if (success) {
                    scheduleService.updateScheduleStatus(scheduleId, "booked");
                    request.getSession().setAttribute("message", "Thêm lịch hẹn thành công!");
                } else {
                    request.getSession().setAttribute("error", "Không thể thêm lịch hẹn!");
                }
            } catch (Exception e) {
                request.getSession().setAttribute("error", "Lỗi: " + e.getMessage());
            }
            response.sendRedirect("StaffDashboardServlet");
            return;
        } else if ("edit".equals(action)) {
            int appointmentId = Integer.parseInt(request.getParameter("appointmentId"));
            String patientIdStr = request.getParameter("patientId");
            String guestName = request.getParameter("guestName");
            String guestPhone = request.getParameter("guestPhone");
            int doctorId = Integer.parseInt(request.getParameter("doctorId"));
            int scheduleId = Integer.parseInt(request.getParameter("scheduleId"));
            int serviceId = Integer.parseInt(request.getParameter("serviceId"));
            Date appointmentDate = Date.valueOf(request.getParameter("appointmentDate"));
            String reason = request.getParameter("reason");

            Integer patientId = patientIdStr != null && !patientIdStr.isEmpty() ? Integer.parseInt(patientIdStr) : null;
            Appointment appointment = new Appointment(appointmentId, patientId, doctorId, scheduleId, serviceId, appointmentDate, reason, "pending", guestName, guestPhone);

            if (appointmentService.updateAppointment(appointment)) {
                request.getSession().setAttribute("message", "Cập nhật lịch hẹn thành công!");
            } else {
                request.getSession().setAttribute("error", "Không thể cập nhật lịch hẹn.");
            }
            response.sendRedirect("StaffDashboardServlet");
        } else if ("confirm".equals(action)) {
            int appointmentId = Integer.parseInt(request.getParameter("appointmentId"));
            if (appointmentService.confirmAppointment(appointmentId)) {
                request.getSession().setAttribute("message", "Xác nhận lịch hẹn thành công!");
            } else {
                request.getSession().setAttribute("error", "Không thể xác nhận lịch hẹn.");
            }
            response.sendRedirect("StaffDashboardServlet");
        } else if ("cancel".equals(action)) {
            int appointmentId = Integer.parseInt(request.getParameter("appointmentId"));
            int scheduleId = appointmentService.getScheduleIdByAppointmentId(appointmentId);
            if (appointmentService.cancelAppointment(appointmentId) && scheduleId != -1) {
                scheduleService.updateScheduleStatus(scheduleId, "available");
                request.getSession().setAttribute("message", "Hủy lịch hẹn thành công!");
            } else {
                request.getSession().setAttribute("error", "Không thể hủy lịch hẹn.");
            }
            response.sendRedirect("StaffDashboardServlet");
        }
    }
}   