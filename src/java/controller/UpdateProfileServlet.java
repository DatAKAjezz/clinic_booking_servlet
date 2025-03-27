package controller;

import dao.UserDAO;
import model.User;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.util.Base64;
import java.io.InputStream;

public class UpdateProfileServlet extends HttpServlet {

    private UserDAO userDAO = new UserDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("USER");
        if (user == null) {
            request.setAttribute("ERROR", "Bạn cần đăng nhập để truy cập trang này.");
            request.getRequestDispatcher("LoginPage.jsp").forward(request, response);
            return;
        }

        String email = request.getParameter("email");
        String phone = request.getParameter("phone");

        Part filePart = request.getPart("profilePicture");
        String profilePicture = user.getProfilePicture();
        if (filePart != null && filePart.getSize() > 0) {
            try (InputStream fileContent = filePart.getInputStream()) {
                byte[] bytes = new byte[(int) filePart.getSize()];
                fileContent.read(bytes);
                profilePicture = Base64.getEncoder().encodeToString(bytes);
            }
        }

        user.setEmail(email);
        user.setPhone(phone);
        user.setProfilePicture(profilePicture);
        boolean updated = userDAO.updateUserProfile(user);

        if (updated) {
            request.getSession().setAttribute("USER", user); 
            request.getSession().setAttribute("message", "Profile updated successfully!");
        } else {
            request.getSession().setAttribute("error", "Failed to update profile.");
        }
        response.sendRedirect("Profile.jsp");
    }
}
