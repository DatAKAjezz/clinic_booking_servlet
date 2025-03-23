package controller;

import model.Review;
import model.User;
import service.ReviewService;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.util.List;

public class ReviewServlet extends HttpServlet {
    private ReviewService reviewService = new ReviewService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("USER");
        if (user == null || !"patient".equals(user.getRole())) {
            response.sendRedirect("LoginPage.jsp");
            return;
        }

        int doctorId;
        try {
            doctorId = Integer.parseInt(request.getParameter("doctorId"));
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid doctor ID.");
            response.sendRedirect("PatientDashboardServlet");
            return;
        }

        List<Review> reviews = reviewService.getReviewsByDoctorId(doctorId);
        request.setAttribute("reviews", reviews);
        request.setAttribute("doctorId", doctorId);
        request.getRequestDispatcher("Review.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("USER");
        if (user == null || !"patient".equals(user.getRole())) {
            response.sendRedirect("LoginPage.jsp");
            return;
        }

        int doctorId;
        int rating;
        try {
            doctorId = Integer.parseInt(request.getParameter("doctorId"));
            rating = Integer.parseInt(request.getParameter("rating"));
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("error", "Invalid input.");
            response.sendRedirect("PatientDashboardServlet");
            return;
        }

        String comment = request.getParameter("comment");
        if (rating < 1 || rating > 5) {
            request.getSession().setAttribute("error", "Rating must be between 1 and 5.");
            response.sendRedirect("PatientDashboardServlet");
            return;
        }

        Review review = new Review(
            0, // reviewId tự sinh
            user.getUserId(), // patientId từ USER
            doctorId,
            rating,
            comment,
            new Date(System.currentTimeMillis()) // Ngày hiện tại
        );

        if (reviewService.addReview(review)) {
            request.getSession().setAttribute("message", "Review submitted successfully!");
        } else {
            request.getSession().setAttribute("error", "Failed to submit review.");
        }
        response.sendRedirect("PatientDashboardServlet");
    }
}