package model;

import java.sql.Date;

public class Review {
    private int reviewId;     // review_id
    private int patientId;    // patient_id
    private int doctorId;     // doctor_id
    private int rating;       // rating (1-5)
    private String comment;   // comment (NVARCHAR)
    private Date reviewDate;  // review_date

    public Review() {}

    public Review(int reviewId, int patientId, int doctorId, int rating, String comment, Date reviewDate) {
        this.reviewId = reviewId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.rating = rating;
        this.comment = comment;
        this.reviewDate = reviewDate;
    }

    // Getters và Setters
    public int getReviewId() { return reviewId; }
    public void setReviewId(int reviewId) { this.reviewId = reviewId; }
    public int getPatientId() { return patientId; }
    public void setPatientId(int patientId) { this.patientId = patientId; }
    public int getDoctorId() { return doctorId; }
    public void setDoctorId(int doctorId) { this.doctorId = doctorId; }
    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
    public Date getReviewDate() { return reviewDate; }
    public void setReviewDate(Date reviewDate) { this.reviewDate = reviewDate; }

    @Override
    public String toString() {
        return "Review{" +
                "reviewId=" + reviewId +
                ", patientId=" + patientId +
                ", doctorId=" + doctorId +
                ", rating=" + rating +
                ", comment='" + comment + '\'' +
                ", reviewDate=" + reviewDate +
                '}';
    }
}