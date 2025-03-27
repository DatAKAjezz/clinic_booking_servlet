<%@page import="model.User"%>
<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Register</title>
        <link rel="stylesheet" href="<%= request.getContextPath()%>/styles/RegisterPage.css"/>
    </head>
    <body>
        <%
            User u = (User) session.getAttribute("USER");
            if (u != null) {
                request.getRequestDispatcher("HomePage.jsp").forward(request, response);
            }
        %>

        <div class="register-container">
            <c:if test="${not empty NOTI}">
                <div class="notification" style="color: ${NOTI.contains('successful') ? 'green' : 'red'}; text-align: center;">
                    <p>${NOTI}</p>
                </div>
            </c:if>
            <h2 class="title create-account">Create an account</h2>
            <a class="title login-here" href="MainServlet?btnAction=login">Already have an account? Login here</a>
            <div class="section color-text">
                <p class="required-text">* indicates required field</p>
                <h3 class="section-title">Personal Information</h3>
                <form action="MainServlet" method="post">
                    <input type="hidden" name="btnAction" value="register">
                    <div class="input-group">
                        <label class="heading-input"><span class="required">*</span> First name</label>
                        <input type="text" name="firstName" placeholder="Enter your first name" required>
                    </div>
                    <div class="input-group">
                        <label class="heading-input"><span class="required">*</span> Last name</label>
                        <input type="text" name="lastName" placeholder="Enter your last name" required>
                    </div>
                    <div class="input-group">
                        <label class="heading-input"><span class="required">*</span> Phone</label>
                        <input type="tel" name="phone" placeholder="Enter your phone number" required>
                    </div>
                    <div class="input-group">
                        <label class="heading-input"><span class="required">*</span> Email</label>
                        <input type="email" name="email" placeholder="Enter your email" required>
                    </div>
                    <div class="input-group">
                        <label class="heading-input"><span class="required">*</span> Date of Birth</label>
                        <input type="date" name="dateOfBirth" required>
                    </div>
                    <div class="input-group">
                        <label class="heading-input"><span class="required">*</span> Gender</label> <!-- Đổi từ Sex thành Gender -->
                        <div class="gender-group">
                            <label class="radio-label"><span>Male</span><input type="radio" name="gender" value="Male" required></label> <!-- Đổi name từ sex thành gender -->
                            <label class="radio-label"><span>Female</span><input type="radio" name="gender" value="Female" required></label> <!-- Đổi name từ sex thành gender -->
                        </div>
                    </div>
                    <div class="input-group">
                        <label class="heading-input"><span class="required">*</span> Address</label>
                        <input type="text" name="address" placeholder="Enter your address" required>
                    </div>
                    <h3 class="section-title">Account Security</h3>
                    <div class="input-group">
                        <label class="heading-input"><span class="required">*</span> Username</label>
                        <input type="text" name="username" placeholder="Enter your username" required>
                    </div>
                    <div class="input-group">
                        <label class="heading-input"><span class="required">*</span> Password</label>
                        <input type="password" name="password" id="password" placeholder="Enter your password" required>
                        <p class="small-text">Password must be 8-25 characters long, include at least 1 uppercase, 1 lowercase, 1 number, and 1 special character.</p>
                    </div>
                    <button type="submit" class="register-btn">Create Account</button>
                </form>
            </div>
        </div>
    </body>
</html>