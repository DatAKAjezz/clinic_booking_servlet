<%-- 
    Document   : Review
    Created on : Mar 21, 2025, 5:25:06 PM
    Author     : datdat
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css">
    <title>Doctor Reviews</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/styles/Review.css"/>
</head>
<body>
    <div class="sidebar">
        <div class="profile">
            <div class="profile-image"><span>👤</span></div>
            <div class="profile-name">${sessionScope.USER.username}</div>
            <div class="profile-email">${sessionScope.USER.email}</div>
            <form action="MainServlet" method="post">
                <input type="hidden" name="btnAction" value="logout"/>
                <button type="submit" class="logout-btn"><i class="bi bi-box-arrow-right"></i> Log out</button>
            </form>
        </div>
        <div class="menu">
            <a href="HomePage.jsp" class="menu-item"><span class="menu-icon"><i class="bi bi-house"></i></span>Home</a>
            <a href="PatientDashboardServlet" class="menu-item"><span class="menu-icon"><i class="bi bi-calendar"></i></span>My Bookings</a>
            <a href="MedicalHistoryServlet" class="menu-item"><span class="menu-icon"><i class="bi bi-file-medical"></i></span>Medical History</a>
            <a href="Profile.jsp" class="menu-item"><span class="menu-icon"><i class="bi bi-person"></i></span>Profile</a>
        </div>
    </div>

    <div class="main-content">
        <h1 class="page-title">Doctor Reviews</h1>

        <c:if test="${not empty sessionScope.message}">
            <div class="message success"><i class="bi bi-check-circle"></i> ${sessionScope.message}</div>
            <% session.removeAttribute("message"); %>
        </c:if>
        <c:if test="${not empty sessionScope.error}">
            <div class="message error"><i class="bi bi-exclamation-circle"></i> ${sessionScope.error}</div>
            <% session.removeAttribute("error"); %>
        </c:if>

        <!-- Form thêm review -->
        <div class="review-form">
            <h2>Add Your Review</h2>
            <form action="ReviewServlet" method="POST">
                <input type="hidden" name="doctorId" value="${param.doctorId}">
                <div class="form-group">
                    <label for="rating">Rating (1-5):</label>
                    <input type="number" id="rating" name="rating" min="1" max="5" required>
                </div>
                <div class="form-group">
                    <label for="comment">Comment:</label>
                    <textarea id="comment" name="comment" rows="4" placeholder="Enter your comment" required></textarea>
                </div>
                <button type="submit" class="submit-btn"><i class="bi bi-send"></i> Submit Review</button>
            </form>
        </div>

        <!-- Danh sách review -->
        <h2>Existing Reviews</h2>
        <c:forEach var="review" items="${requestScope.reviews}">
            <div class="review-card">
                <div class="rating"><i class="bi bi-star-fill"></i> ${review.rating}/5</div>
                <div class="comment">${review.comment}</div>
                <div class="review-date">Reviewed on: ${review.reviewDate}</div>
            </div>
        </c:forEach>
        <c:if test="${empty requestScope.reviews}">
            <p>No reviews yet for this doctor.</p>
        </c:if>
    </div>
</body>
</html>