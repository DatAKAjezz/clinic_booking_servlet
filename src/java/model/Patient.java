package model;

public class Patient {

    private int patientId;
    private int userId;
    private String fullName;
    private int age;
    private String address;
    private String gender;

    public Patient() {
    }

    public Patient(int patientId, int userId, String fullName, int age, String address, String gender) {
        this.patientId =     patientId;
        this.userId = userId;
        this.fullName = fullName;
        this.age = age;
        this.address = address;
        this.gender = gender;
    }

    // Getters and Setters
    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
