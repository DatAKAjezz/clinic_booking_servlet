package controller;

import dao.DoctorDAO;
import dao.PatientDAO;
import dao.ReceptionistDAO;
import model.User;
import model.Patient;
import model.Receptionist;
import service.UserService;
import service.PatientService;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.servlet.http.HttpSession;

public class MainServlet extends HttpServlet {

    private UserService userService = new UserService();
    private PatientService patientService = new PatientService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("btnAction");
        if (action == null) {
            action = "";
        }

        switch (action) {
            case "login":
                request.getRequestDispatcher("LoginPage.jsp").forward(request, response);
                break;
            case "showRegister":
                request.getRequestDispatcher("RegisterPage.jsp").forward(request, response);
                break;
            case "logout":
                request.getSession().invalidate();
                response.sendRedirect("HomePage.jsp");
                break;
            default:
                response.sendRedirect("HomePage.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String action = request.getParameter("btnAction");
            if (action == null) {
                action = "";
            }

            switch (action) {
                case "loginServlet":
                    String username = request.getParameter("txtusername");
                    String password = request.getParameter("txtpassword");
                    User user = userService.login(username, password);
                    if (user != null) {
                        request.getSession().setAttribute("USER", user);
                        String fullName = "";

                        if ("patient".equals(user.getRole())) {
                            PatientDAO pd = new PatientDAO();
                            fullName = pd.getPatientByUserId(user.getUserId()).getFullName();
                        } else if ("doctor".equals(user.getRole())) {
                            DoctorDAO dd = new DoctorDAO();
                            fullName = dd.getDoctorByUserId(user.getUserId()).getFullName();
                        } else if ("receptionist".equals(user.getRole())) {
                            ReceptionistDAO rd = new ReceptionistDAO();
                            Receptionist receptionist = rd.getReceptionistByUserId(user.getUserId());
                            if (receptionist != null) {
                                fullName = receptionist.getFullName();
                            } else {
                                fullName = "Lễ tân chưa cập nhật thông tin";
                            }
                        }

                        HttpSession session = request.getSession();
                        session.setAttribute("fullName", fullName);

                        switch (user.getRole()) {
                            case "patient":
                                response.sendRedirect("PatientDashboardServlet");
                                break;
                            case "doctor":
                                response.sendRedirect("DoctorDashboardServlet");
                                break;
                            case "receptionist":
                                response.sendRedirect("StaffDashboardServlet");
                                break;
                            default:
                                response.sendRedirect("HomePage.jsp");
                        }
                    } else {
                        request.setAttribute("ERROR", "Tên đăng nhập hoặc mật khẩu không đúng");
                        request.getRequestDispatcher("LoginPage.jsp").forward(request, response);
                    }
                    break;
                case "register":
                    User newUser = new User();
                    newUser.setUsername(request.getParameter("username"));
                    newUser.setPassword(request.getParameter("password"));
                    newUser.setRole("patient"); 
                    newUser.setEmail(request.getParameter("email"));
                    newUser.setPhone(request.getParameter("phone"));

                    if (userService.register(newUser)) {
                        int userId = getLastInsertedUserId();
                        Patient patient = new Patient();
                        patient.setUserId(userId);
                        patient.setFullName(request.getParameter("firstName") + " " + request.getParameter("lastName"));
                        patient.setAge(calculateAge(Date.valueOf(request.getParameter("dateOfBirth"))));
                        patient.setAddress(request.getParameter("address"));
                        patient.setGender(request.getParameter("sex"));

                        if (patientService.addPatient(patient)) {
                            request.setAttribute("NOTI", "Đăng ký thành công! Vui lòng đăng nhập.");
                            request.getRequestDispatcher("LoginPage.jsp").forward(request, response);
                        } else {
                            request.setAttribute("NOTI", "Không thể đăng ký thông tin bệnh nhân.");
                            request.getRequestDispatcher("RegisterPage.jsp").forward(request, response);
                        }
                    } else {
                        request.setAttribute("NOTI", "Tên đăng nhập hoặc email đã tồn tại.");
                        request.getRequestDispatcher("RegisterPage.jsp").forward(request, response);
                    }
                    break;
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error occurred");
        }
    }

    private int getLastInsertedUserId() {
        String sql = "SELECT IDENT_CURRENT('Users') AS last_id";
        try (Connection conn = utils.DBUtils.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt("last_id");
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private int calculateAge(Date dob) {
        java.util.Calendar today = java.util.Calendar.getInstance();
        java.util.Calendar birthDate = java.util.Calendar.getInstance();
        birthDate.setTime(dob);
        int age = today.get(java.util.Calendar.YEAR) - birthDate.get(java.util.Calendar.YEAR);
        if (today.get(java.util.Calendar.DAY_OF_YEAR) < birthDate.get(java.util.Calendar.DAY_OF_YEAR)) {
            age--;
        }
        return age;
    }
}
