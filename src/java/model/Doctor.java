package model;

public class Doctor {

    private int doctorId;
    private int userId;
    private String fullName;
    private String specialty;
    private int experience;

    public Doctor() {
    }

    public Doctor(int doctorId, int userId, String fullName, String specialty, int experience) {
        this.doctorId = doctorId;
        this.userId = userId;
        this.fullName = fullName;
        this.specialty = specialty;
        this.experience = experience;
    }

    // Getters và Setters
    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    @Override
    public String toString() {
        return "Doctor{"
                + "doctorId=" + doctorId
                + ", userId=" + userId
                + ", fullName='" + fullName + '\''
                + ", specialty='" + specialty + '\''
                + ", experience=" + experience
                + '}';
    }
}
