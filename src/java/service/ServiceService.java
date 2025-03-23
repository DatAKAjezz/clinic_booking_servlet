package service;

import dao.ServiceDAO;
import model.Service;
import java.util.List;

public class ServiceService {
    private ServiceDAO serviceDAO = new ServiceDAO();

    public List<Service> getAllServices() {
        return serviceDAO.getAllServices();
    }
}