package dao;

import model.Review;
import utils.DBUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReviewDAO {

    // Thêm một review mới
    public boolean addReview(Review review) {
        String sql = "INSERT INTO Reviews (patient_id, doctor_id, rating, comment, review_date) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBUtils.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, review.getPatientId());
            ps.setInt(2, review.getDoctorId());
            ps.setInt(3, review.getRating());
            ps.setString(4, review.getComment());
            ps.setDate(5, review.getReviewDate());
            return ps.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public double getAverageRatingByDoctorId(int doctorId, Map<Integer, List<Review>> doctorReviews){
        double res = 0;
        for(Review review : doctorReviews.get(doctorId)){
            res += review.getRating();
        }
        return res / doctorReviews.get(doctorId).size();
    }

    public Map<Integer, List<Review>> getAllReviews() {

        String sql = "SELECT * FROM Reviews";
        Map<Integer, List<Review>> doctorReviews = new HashMap<>();
        try (Connection conn = DBUtils.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Review review = new Review(rs.getInt("review_id"),
                        rs.getInt("patient_id"),
                        rs.getInt("doctor_id"),
                        rs.getInt("rating"),
                        rs.getString("comment"),
                        rs.getDate("review_date"));
                if (!doctorReviews.containsKey(review.getDoctorId())) doctorReviews.put(review.getDoctorId(), new ArrayList<>());
                doctorReviews.get(review.getDoctorId()).add(review);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return doctorReviews;
    }

    // Lấy danh sách review của một bác sĩ
    public List<Review> getReviewsByDoctorId(int doctorId) {
        List<Review> reviews = new ArrayList<>();
        String sql = "SELECT r.review_id, r.patient_id, r.doctor_id, r.rating, r.comment, r.review_date, "
                + "p.full_name AS patient_name "
                + "FROM Reviews r "
                + "JOIN Patients p ON r.patient_id = p.patient_id "
                + "WHERE r.doctor_id = ?";
        try (Connection conn = DBUtils.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, doctorId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Review review = new Review(
                        rs.getInt("review_id"),
                        rs.getInt("patient_id"),
                        rs.getInt("doctor_id"),
                        rs.getInt("rating"),
                        rs.getString("comment"),
                        rs.getDate("review_date")
                );
                reviews.add(review);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return reviews;
    }
}
