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

public class MedicalHistoryServlet extends HttpServlet {

    private AppointmentService appointmentService = new AppointmentService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("USER");
        if (user == null) {
            response.sendRedirect("LoginPage.jsp");
            return;
        }

        List<Appointment> medicalHistory;
        if ("doctor".equals(user.getRole())) {
            int patientId;
            try {
                patientId = Integer.parseInt(request.getParameter("patientId"));
            } catch (NumberFormatException e) {
                request.setAttribute("error", "Invalid patient ID.");
                request.getRequestDispatcher("DoctorDashboardServlet").forward(request, response);
                return;
            }
            medicalHistory = appointmentService.getMedicalHistoryByPatientId(patientId);
            request.setAttribute("patientId", patientId);
        } else if ("patient".equals(user.getRole())) {
            int patientId = getPatientIdByUserId(user.getUserId());
            if (patientId == -1) {
                request.setAttribute("error", "Cannot retrieve patient ID.");
                response.sendRedirect("HomePage.jsp");
                return;
            }
            medicalHistory = appointmentService.getMedicalHistoryByPatientId(patientId);
        } else {
            request.setAttribute("error", "You do not have permission to view medical history.");
            response.sendRedirect("HomePage.jsp");
            return;
        }

        if (medicalHistory == null || medicalHistory.isEmpty()) {
            request.setAttribute("message", "No medical history found.");
        }
        request.setAttribute("medicalHistory", medicalHistory);
        request.getRequestDispatcher("MedicalHistory.jsp").forward(request, response);
    }

    private int getPatientIdByUserId(int userId) {
        String sql = "SELECT patient_id FROM Patients WHERE user_id = ?";
        try (java.sql.Connection conn = utils.DBUtils.getConnection();
             java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            java.sql.ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("patient_id");
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            System.err.println("Error retrieving patientId for userId " + userId + ": " + e.getMessage());
        }
        return -1;
    }
}