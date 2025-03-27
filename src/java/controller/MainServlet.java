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

                    User user = userService.getUserByUsername(username); 
                    if (user != null && password.equals(user.getPassword())) { // So sánh trực tiếp
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
                    String usernameReg = request.getParameter("username");
                    String plainPassword = request.getParameter("password");
                    String email = request.getParameter("email");
                    String phone = request.getParameter("phone");
                    String firstName = request.getParameter("firstName");
                    String lastName = request.getParameter("lastName");
                    String dateOfBirth = request.getParameter("dateOfBirth");
                    String address = request.getParameter("address");
                    String sex = request.getParameter("sex");

                    if (isEmptyOrNull(usernameReg) || isEmptyOrNull(plainPassword) || isEmptyOrNull(email) ||
                        isEmptyOrNull(firstName) || isEmptyOrNull(lastName) || isEmptyOrNull(dateOfBirth)) {
                        request.setAttribute("NOTI", "Vui lòng điền đầy đủ các trường bắt buộc!");
                        request.getRequestDispatcher("RegisterPage.jsp").forward(request, response);
                        return;
                    }

                    User newUser = new User();
                    newUser.setUsername(usernameReg);
                    newUser.setPassword(plainPassword); // Lưu mật khẩu trực tiếp
                    newUser.setRole("patient");
                    newUser.setEmail(email);
                    newUser.setPhone(phone);

                    if (userService.register(newUser)) {
                        int userId = getLastInsertedUserId();
                        if (userId == -1) {
                            request.setAttribute("NOTI", "Không thể lấy ID người dùng vừa tạo!");
                            request.getRequestDispatcher("RegisterPage.jsp").forward(request, response);
                            return;
                        }

                        Patient patient = new Patient();
                        patient.setUserId(userId);
                        patient.setFullName(firstName + " " + lastName);
                        patient.setAddress(address);
                        patient.setGender(sex);

                        try {
                            Date dob = Date.valueOf(dateOfBirth);
                            patient.setAge(calculateAge(dob));
                            if (patient.getAge() < 0) {
                                request.setAttribute("NOTI", "Ngày sinh không hợp lệ (trong tương lai)!");
                                request.getRequestDispatcher("RegisterPage.jsp").forward(request, response);
                                return;
                            }
                        } catch (IllegalArgumentException e) {
                            request.setAttribute("NOTI", "Ngày sinh không đúng định dạng (yyyy-MM-dd)!");
                            request.getRequestDispatcher("RegisterPage.jsp").forward(request, response);
                            return;
                        }

                        if (patientService.addPatient(patient)) {
                            request.setAttribute("NOTI", "Đăng ký thành công! Vui lòng đăng nhập.");
                            request.getRequestDispatcher("LoginPage.jsp").forward(request, response);
                        } else {
                            request.setAttribute("NOTI", "Không thể đăng ký thông tin bệnh nhân!");
                            request.getRequestDispatcher("RegisterPage.jsp").forward(request, response);
                        }
                    } else {
                        request.setAttribute("NOTI", "Tên đăng nhập hoặc email đã tồn tại!");
                        request.getRequestDispatcher("RegisterPage.jsp").forward(request, response);
                    }
                    break;
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi kết nối cơ sở dữ liệu!");
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

    private boolean isEmptyOrNull(String str) {
        return str == null || str.trim().isEmpty();
    }
}