package service;

import dao.UserDAO;
import model.User;

public class UserService {
    private UserDAO userDAO = new UserDAO();

    public User login(String username, String password) {
        User user = userDAO.getUserByUsername(username);
        if (user != null && password.equals(user.getPassword())) { // So sánh trực tiếp
            return user;
        }
        return null;
    }

    public boolean register(User user) {
        try {
            // Không mã hóa, sử dụng mật khẩu trực tiếp từ user
            int userId = userDAO.createUser(user);
            return userId > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public User getUserByUsername(String username) {
        return userDAO.getUserByUsername(username);
    }
}