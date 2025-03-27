package model;

public class Receptionist {

    private int receptionistId;
    private int userId;
    private String fullName;
    private int experience;

    public Receptionist() {
    }

    public Receptionist(int receptionistId, int userId, String fullName, int experience) {
        this.receptionistId = receptionistId;
        this.userId = userId;
        this.fullName = fullName;
        this.experience = experience;
    }

    // Getters và Setters
    public int getReceptionistId() {
        return receptionistId;
    }

    public void setReceptionistId(int receptionistId) {
        this.receptionistId = receptionistId;
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

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    @Override
    public String toString() {
        return "Receptionist{"
                + "receptionistId=" + receptionistId
                + ", userId=" + userId
                + ", fullName='" + fullName + '\''
                + ", experience=" + experience
                + '}';
    }
}
