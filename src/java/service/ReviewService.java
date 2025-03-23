package service;

import dao.ReviewDAO;
import model.Review;
import java.util.List;

public class ReviewService {
    private ReviewDAO reviewDAO = new ReviewDAO();

    public boolean addReview(Review review) {
        return reviewDAO.addReview(review);
    }

    public List<Review> getReviewsByDoctorId(int doctorId) {
        return reviewDAO.getReviewsByDoctorId(doctorId);
    }
}