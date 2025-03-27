package controller;

import model.Appointment;
import model.Doctor;
import model.Schedule;
import model.Service;
import model.User;
import model.Patient;
import service.AppointmentService;
import service.DoctorService;
import service.ScheduleService;
import service.ServiceService;
import dao.PatientDAO; // Thêm import PatientDAO
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.util.List;

public class BookAppointmentServlet extends HttpServlet {

    private DoctorService doctorService = new DoctorService();
    private ScheduleService scheduleService = new ScheduleService();
    private ServiceService serviceService = new ServiceService();
    private AppointmentService appointmentService = new AppointmentService();
    private PatientDAO patientDAO = new PatientDAO();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("USER");
        if (user == null || !"patient".equals(user.getRole())) {
            request.setAttribute("ERROR", "Bạn cần đăng nhập với tư cách patient.");
            request.getRequestDispatcher("LoginPage.jsp").forward(request, response);
            return;
        }

        String doctorIdStr = request.getParameter("doctorId");
        int doctorId = 0;
        if (doctorIdStr != null && !doctorIdStr.isEmpty()) {
            try {
                doctorId = Integer.parseInt(doctorIdStr);
                request.setAttribute("selectedDoctorId", doctorIdStr);
            } catch (NumberFormatException e) {
                doctorId = 0;
            }
        }

        setFormAttributes(request, doctorId);
        request.getRequestDispatcher("BookAppointment.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        User user = (User) request.getSession().getAttribute("USER");
        if (user == null || !"patient".equals(user.getRole())) {
            request.setAttribute("ERROR", "Bạn cần đăng nhập với tư cách patient.");
            request.getRequestDispatcher("LoginPage.jsp").forward(request, response);
            return;
        }

        try {
            String doctorIdStr = request.getParameter("doctorId");
            String scheduleIdStr = request.getParameter("scheduleId");
            String serviceIdStr = request.getParameter("serviceId");
            String dateStr = request.getParameter("appointmentDate");
            String reason = request.getParameter("reason");

            int doctorId = 0;
            int scheduleId = 0;
            int serviceId = 0;
            Date appointmentDate = null;
            boolean hasError = false;

            int userId = user.getUserId();
            Patient patient = patientDAO.getPatientByUserId(userId);
            if (patient == null) {
                request.setAttribute("error", "Không tìm thấy thông tin bệnh nhân. Vui lòng liên hệ quản trị viên.");
                setFormAttributes(request, 0);
                request.getRequestDispatcher("BookAppointment.jsp").forward(request, response);
                return;
            }
            int patientId = patient.getPatientId(); 

            if (isEmptyOrDefault(doctorIdStr, "Chọn Bác Sĩ")) {
                request.setAttribute("doctorIdError", "Bạn cần chọn một bác sĩ.");
                hasError = true;
            } else {
                doctorId = Integer.parseInt(doctorIdStr);
            }
            request.setAttribute("selectedDoctorId", doctorIdStr);

            if (isEmptyOrDefault(scheduleIdStr, "Chọn Khung Giờ")) {
                request.setAttribute("scheduleIdError", "Bạn cần chọn một khung giờ.");
                hasError = true;
            } else {
                scheduleId = Integer.parseInt(scheduleIdStr);
            }
            request.setAttribute("selectedScheduleId", scheduleIdStr);

            if (isEmptyOrDefault(serviceIdStr, "Chọn Dịch Vụ")) {
                request.setAttribute("serviceIdError", "Bạn cần chọn một dịch vụ.");
                hasError = true;
            } else {
                serviceId = Integer.parseInt(serviceIdStr);
            }
            request.setAttribute("selectedServiceId", serviceIdStr);

            if (isEmptyOrDefault(dateStr, null)) {
                request.setAttribute("dateError", "Bạn cần chọn ngày khám.");
                hasError = true;
            } else {
                try {
                    appointmentDate = Date.valueOf(dateStr);
                    Date currentDate = new Date(System.currentTimeMillis());
                    if (appointmentDate.before(currentDate)) {
                        request.setAttribute("dateError", "Ngày khám không được nhỏ hơn ngày hiện tại.");
                        hasError = true;
                    }
                } catch (IllegalArgumentException e) {
                    request.setAttribute("dateError", "Định dạng ngày không hợp lệ. Vui lòng chọn lại.");
                    hasError = true;
                }
            }
            request.setAttribute("selectedDate", dateStr);
            request.setAttribute("selectedReason", reason);

            if (hasError) {
                setFormAttributes(request, doctorId);
                request.getRequestDispatcher("BookAppointment.jsp").forward(request, response);
                return;
            }

            if (appointmentService.isScheduleBooked(scheduleId, appointmentDate)) {
                request.setAttribute("error", "Khung giờ này đã được đặt. Vui lòng chọn khung giờ khác.");
                setFormAttributes(request, doctorId);
                request.getRequestDispatcher("BookAppointment.jsp").forward(request, response);
                return;
            }

            Appointment appointment = new Appointment(
                    0,
                    patientId,
                    doctorId,
                    scheduleId,
                    serviceId,
                    appointmentDate,
                    reason,
                    "pending",
                    null,
                    null 
            );

            if (appointmentService.bookAppointment(appointment)) {
                request.getSession().setAttribute("message", "Đặt lịch thành công!");
                response.sendRedirect("PatientDashboardServlet");
            } else {
                request.setAttribute("error", "Không thể đặt lịch. Vui lòng thử lại.");
                setFormAttributes(request, doctorId);
                request.getRequestDispatcher("BookAppointment.jsp").forward(request, response);
            }

        } catch (Exception e) {
            request.setAttribute("error", "Đã xảy ra lỗi: " + e.getMessage());
            setFormAttributes(request, 0);
            request.getRequestDispatcher("BookAppointment.jsp").forward(request, response);
        }
    }

    private void setFormAttributes(HttpServletRequest request, int doctorId) {
        List<Doctor> doctors = doctorService.getAllDoctors();
        List<Schedule> schedules = (doctorId > 0) 
            ? scheduleService.getAvailableSchedulesByDoctor(doctorId) 
            : scheduleService.getAvailableSchedules();
        List<Service> services = serviceService.getAllServices();

        if (doctors.isEmpty() || schedules.isEmpty() || services.isEmpty()) {
            request.setAttribute("error", "Không có bác sĩ, khung giờ hoặc dịch vụ nào khả dụng.");
        }

        request.setAttribute("doctors", doctors);
        request.setAttribute("schedules", schedules);
        request.setAttribute("services", services);
    }

    private boolean isEmptyOrDefault(String value, String defaultValue) {
        return value == null || value.trim().isEmpty() || (defaultValue != null && defaultValue.equals(value));
    }
}