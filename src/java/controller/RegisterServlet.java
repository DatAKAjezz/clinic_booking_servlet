package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.Period;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.User;
import model.Patient;
import dao.UserDAO;
import dao.PatientDAO;
import java.sql.Connection;
import utils.DBUtils;

public class RegisterServlet extends HttpServlet {

    private final String REGISTER_PAGE = "RegisterPage.jsp";
    private final String LOGIN_PAGE = "Login.jsp";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            if ("POST".equalsIgnoreCase(request.getMethod()) && "register".equals(request.getParameter("btnAction"))) {
                String firstName = request.getParameter("firstName");
                String lastName = request.getParameter("lastName");
                String phone = request.getParameter("phone");
                String email = request.getParameter("email");
                String dateOfBirth = request.getParameter("dateOfBirth");
                String sex = request.getParameter("sex");
                String address = request.getParameter("address");
                String username = request.getParameter("username");
                String password = request.getParameter("password");
                String url = REGISTER_PAGE;

                // Validation
                if (isEmptyOrNull(username) || isEmptyOrNull(password) || isEmptyOrNull(email) ||
                    isEmptyOrNull(firstName) || isEmptyOrNull(lastName) || isEmptyOrNull(dateOfBirth)) {
                    request.setAttribute("NOTI", "All fields are required!");
                    request.getRequestDispatcher(REGISTER_PAGE).forward(request, response);
                    return;
                }

                // Tính tuổi
                int age;
                try {
                    LocalDate dob = LocalDate.parse(dateOfBirth);
                    LocalDate currentDate = LocalDate.now();
                    age = Period.between(dob, currentDate).getYears();
                    if (age < 0) {
                        request.setAttribute("NOTI", "Invalid date of birth!");
                        request.getRequestDispatcher(REGISTER_PAGE).forward(request, response);
                        return;
                    }
                } catch (Exception e) {
                    request.setAttribute("NOTI", "Invalid date of birth format!");
                    request.getRequestDispatcher(REGISTER_PAGE).forward(request, response);
                    return;
                }

                // Lưu mật khẩu trực tiếp (không mã hóa)
                User user = new User(0, username.trim(), password.trim(), "patient", email.trim(), phone != null ? phone.trim() : null, null);
                Patient patient = new Patient(0, 0, firstName.trim() + " " + lastName.trim(), age, address != null ? address.trim() : null, sex);

                UserDAO userDAO = new UserDAO();
                PatientDAO patientDAO = new PatientDAO();

                try (Connection conn = DBUtils.getConnection()) {
                    conn.setAutoCommit(false);
                    int userId = userDAO.createUser(user);
                    if (userId > 0) {
                        patient.setUserId(userId);
                        if (patientDAO.createPatient(patient)) {
                            conn.commit();
                            request.setAttribute("NOTI", "Register Successfully!");
                            url = LOGIN_PAGE;
                        } else {
                            conn.rollback();
                            request.setAttribute("NOTI", "Failed to create patient profile!");
                        }
                    } else {
                        conn.rollback();
                        request.setAttribute("NOTI", "Username or email already exists!");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    request.setAttribute("NOTI", "Registration failed: " + e.getMessage());
                }

                request.getRequestDispatcher(url).forward(request, response);
            } else {
                request.getRequestDispatcher(REGISTER_PAGE).forward(request, response);
            }
        }
    }

    private boolean isEmptyOrNull(String str) {
        return str == null || str.trim().isEmpty();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Servlet for user registration";
    }
}