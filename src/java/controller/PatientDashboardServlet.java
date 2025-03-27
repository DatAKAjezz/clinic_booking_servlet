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
import java.util.List;

public class PatientDashboardServlet extends HttpServlet {
    private AppointmentService appointmentService = new AppointmentService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("USER");
        if (user == null || !"patient".equals(user.getRole())) {
            request.setAttribute("ERROR", "Bạn cần đăng nhập với tư cách patient.");
            request.getRequestDispatcher("LoginPage.jsp").forward(request, response);
            return;
        }

        int patientId = getPatientIdByUserId(user.getUserId());
        if (patientId == -1) {
            response.sendRedirect("HomePage.jsp"); 
            return;
        }

        List<Appointment> bookings = appointmentService.getBookingsByPatientId(patientId);
        request.setAttribute("bookings", bookings);

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