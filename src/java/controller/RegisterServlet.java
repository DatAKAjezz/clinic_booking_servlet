    package controller;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Account;
import model.Patient;
import dao.PatientDAO;
import java.util.UUID;

public class RegisterServlet extends HttpServlet {

    private final String REGISTER_PAGE = "RegisterPage.jsp";
    private final String LOGIN_PAGE = "LoginPage.jsp";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            // Chỉ xử lý nếu đây là một POST request và btnAction = register
            if ("POST".equalsIgnoreCase(request.getMethod()) && "register".equals(request.getParameter("btnAction"))) {
                // Get form data
                int id = UUID.randomUUID().hashCode() & Integer.MAX_VALUE;
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

                // Debug: Print all input parameters
                System.out.println("DEBUG - Register Servlet received parameters:");
                System.out.println("firstName: " + firstName);
                System.out.println("lastName: " + lastName);
                System.out.println("username: " + username);
                System.out.println("password: " + (password != null ? "[PROVIDED]" : "null"));
                System.out.println("phone: " + phone);
                System.out.println("email: " + email);
                System.out.println("dateOfBirth: " + dateOfBirth);
                System.out.println("sex: " + sex);
                System.out.println("address: " + address);

                // Validation - check required fields
                if (username == null || username.trim().isEmpty()) {
                    request.setAttribute("NOTI", "Username is required!");
                    System.out.println("ERROR - Username is empty or null");
                    request.getRequestDispatcher(REGISTER_PAGE).forward(request, response);
                    return; // Stop processing
                }

                if (password == null || password.trim().isEmpty()) {
                    request.setAttribute("NOTI", "Password is required!");
                    System.out.println("ERROR - Password is empty or null");
                    request.getRequestDispatcher(REGISTER_PAGE).forward(request, response);
                    return;
                }

                // Create auto ID
                System.out.println("DEBUG - Generated ID: " + id);

                // Create Account object with parameters validated
                Account account = new Account();
                account.setAccountId(id);
                account.setUsername(username.trim());
                account.setPassword(password.trim());

                System.out.println("DEBUG - Created Account object: " + account);
                System.out.println("DEBUG - Account values: ID=" + account.getAccountId()
                        + ", Username=" + account.getUsername()
                        + ", Password=" + (account.getPassword() != null ? "[PROVIDED]" : "null"));

                // Create Patient object and set properties
                Patient patient = new Patient();
                patient.setPatientID(id);
                patient.setFirstName(firstName != null ? firstName.trim() : "");
                patient.setLastName(lastName != null ? lastName.trim() : "");
                patient.setPhone(phone != null ? phone.trim() : "");
                patient.setEmail(email != null ? email.trim() : "");
                patient.setDateOfBirth(dateOfBirth);
                patient.setSex(sex);
                patient.setAddress(address != null ? address.trim() : "");

                System.out.println("DEBUG - Created Patient object with values: " + patient);

                // Initialize DAO to handle data persistence
                PatientDAO patientDAO = new PatientDAO();
                try {
                    System.out.println("DEBUG - Calling createPatient method");
                    boolean result = patientDAO.createPatient(account, patient);
                    System.out.println("DEBUG - createPatient result: " + result);

                    if (result) {
                        url = LOGIN_PAGE;
                        request.setAttribute("NOTI", "Register Successfully!");
                        System.out.println("SUCCESS - Registration completed");
                    } else {
                        request.setAttribute("NOTI", "Registration failed! Please check that all fields are completed correctly and try again.");
                        System.out.println("FAILED - Registration process returned false");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("EXCEPTION - During registration: " + e.getMessage());
                    request.setAttribute("NOTI", "An error occurred during registration: " + e.getMessage());
                }

                // Forward to the specified page
                System.out.println("DEBUG - Forwarding to: " + url);
                request.getRequestDispatcher(url).forward(request, response);
            } else {
                // Nếu không phải POST request hoặc không phải btnAction=register, chỉ hiển thị trang đăng ký
                request.getRequestDispatcher(REGISTER_PAGE).forward(request, response);
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Đối với GET request, chỉ hiển thị trang đăng ký
        request.getRequestDispatcher(REGISTER_PAGE).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
