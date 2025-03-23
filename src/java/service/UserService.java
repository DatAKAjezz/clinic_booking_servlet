package service;

import dao.UserDAO;
import model.User;

public class UserService {
    private UserDAO userDAO = new UserDAO();

    public User login(String username, String password) {
        return userDAO.login(username, password);
    }

    public boolean register(User user) {
        try {
            int userId = userDAO.createUser(user);
            return userId > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}