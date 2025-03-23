package service;

import dao.DoctorDAO;
import model.Doctor;
import java.util.List;

public class DoctorService {
    private DoctorDAO doctorDAO = new DoctorDAO();

    public List<Doctor> getAllDoctors() {
        return doctorDAO.getAllDoctors();
    }
}