package controller;

import model.Appointment;
import model.User;
import service.AppointmentService;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;       // Thêm import này
import java.sql.PreparedStatement; // Thêm import này
import java.sql.ResultSet;       // Thêm import này
import java.sql.SQLException;     // Thêm import này
import java.util.List;

public class PatientDashboardServlet extends HttpServlet {
    private AppointmentService appointmentService = new AppointmentService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Kiểm tra xem user đã đăng nhập và có role là patient không
        User user = (User) request.getSession().getAttribute("USER");
        if (user == null || !"patient".equals(user.getRole())) {
            response.sendRedirect("LoginPage.jsp");
            return;
        }

        // Lấy patient_id từ user_id
        int patientId = getPatientIdByUserId(user.getUserId());
        if (patientId == -1) {
            response.sendRedirect("HomePage.jsp"); // Trường hợp lỗi
            return;
        }

        // Lấy danh sách lịch hẹn của bệnh nhân
        List<Appointment> bookings = appointmentService.getBookingsByPatientId(patientId);
        request.setAttribute("bookings", bookings);

        // Chuyển tiếp đến PatientBookings.jsp
        request.getRequestDispatcher("PatientBookings.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    private int getPatientIdByUserId(int userId) {
        String sql = "SELECT patient_id FROM Patients WHERE user_id = ?";
        try (Connection conn = utils.DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("patient_id");
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return -1;
    }
}