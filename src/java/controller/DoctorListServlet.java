package controller;

import model.Doctor;
import model.Review;
import service.DoctorService;
import dao.ReviewDAO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DoctorListServlet extends HttpServlet {
    private DoctorService doctorService = new DoctorService();
    private ReviewDAO reviewDAO = new ReviewDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Doctor> doctors = doctorService.getAllDoctors();
        Map<Integer, List<Review>> doctorReviews = reviewDAO.getAllReviews();

        Map<Integer, Double> averageRatings = new HashMap<>();
        for (Doctor doctor : doctors) {
            double avgRating = reviewDAO.getAverageRatingByDoctorId(doctor.getDoctorId(), doctorReviews);
            averageRatings.put(doctor.getDoctorId(), avgRating);
        }

        request.setAttribute("doctors", doctors);
        request.setAttribute("doctorReviews", doctorReviews);
        request.setAttribute("averageRatings", averageRatings);
        request.getRequestDispatcher("DoctorList.jsp").forward(request, response);
    }
}