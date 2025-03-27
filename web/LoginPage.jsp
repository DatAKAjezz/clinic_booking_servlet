<%@page import="model.User"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Login</title>
        <link rel="stylesheet" href="<%= request.getContextPath()%>/styles/LoginPage.css"/>
        <style>
            .back-to-home {
                position: absolute;
                top: 20px;
                right: 20px;
                background-color: #6b7280;
                color: white;
                padding: 10px 20px;
                border-radius: 6px;
                text-decoration: none;
                font-size: 14px;
            }
            .back-to-home:hover {
                background-color: #4b5563;
            }
        </style>
    </head>
    <body>

        <%
            User u = (User) session.getAttribute("USER");
            if (u != null) {
                request.getRequestDispatcher("HomePage.jsp").forward(request, response);
            }
        %>

        <div class="login-container">
            <!-- Nút Back to HomePage ở góc phải -->
            <a href="HomePage.jsp" class="back-to-home">Back to HomePage</a>

            <h1 class="title">Login</h1>
            <form action="MainServlet" method="post" accept-charset="UTF-8" class="form">
                <input type="hidden" name="btnAction" value="loginServlet"/>
                <div class="input-group">
                    <label>Username:</label>
                    <input type="text" name="txtusername" class="input" 
                           value="<%= request.getParameter("txtusername") != null ? request.getParameter("txtusername") : ""%>" 
                           required>
                </div>
                <div class="input-group">
                    <label>Password:</label>
                    <input type="password" name="txtpassword" class="input" required>
                </div>
                <%
                    String error = (String) request.getAttribute("ERROR");
                    if (error != null) {
                %>
                <p class="message" style="color: red; text-align: center;"><%= error%></p>
                <% }%>

                <%
                    String msg = (String) request.getAttribute("NOTI");
                    if (msg != null) {
                %>
                <p class="message" style="color: green; text-align: center;"><%= msg%></p>
                <% }%>

                <button type="submit" class="button">Login</button>
            </form>
            <a class="sign-up" href="MainServlet?btnAction=showRegister">Don't have an account? Sign up here</a>
        </div>
    </body>
</html>