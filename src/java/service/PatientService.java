package service;

import dao.PatientDAO;
import model.Patient;
import java.sql.SQLException;

public class PatientService {
    private PatientDAO patientDAO = new PatientDAO();

    public boolean addPatient(Patient patient) throws SQLException, ClassNotFoundException {
        return patientDAO.createPatient(patient); 
    }
}