<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css">
    <title>Our Doctors</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/styles/DoctorList.css"/>
</head>
<body>
    <%@ include file="header.jsp" %>
    <div class="main-content">
        <h1 class="page-title">Our Doctors</h1>
        <c:forEach var="doctor" items="${requestScope.doctors}">
            <div class="doctor-card">
                <div class="doctor-name"><i class="bi bi-person-circle"></i> ${doctor.fullName}</div>
                <div class="doctor-info">Specialty: ${doctor.specialty}</div>
                <div class="doctor-info">Experience: ${doctor.experience} years</div>
                
                <!-- Hiển thị đánh giá trung bình -->
                <c:set var="avgRating" value="${requestScope.averageRatings[doctor.doctorId]}" />
                <div class="doctor-rating">
                    <i style="color: rgb(252, 3, 61)" class="bi bi-star-fill"></i> Average Rating: 
                    <c:choose>
                        <c:when test="${avgRating > 0}">
                            <fmt:formatNumber value="${avgRating}" maxFractionDigits="1"/> / 5
                        </c:when>
                        <c:otherwise>
                            No ratings yet
                        </c:otherwise>
                    </c:choose>
                </div>
                
                <!-- Danh sách đánh giá -->
                <div class="reviews-section">
                    <h3>Reviews</h3>
                    <c:forEach var="review" items="${requestScope.doctorReviews[doctor.doctorId]}">
                        <div class="review-item">
                            <div class="rating"><i class="bi bi-star-fill"></i> ${review.rating}/5</div>
                            <div class="comment">${review.comment}</div>
                            <div class="review-date">Reviewed on: ${review.reviewDate}</div>
                        </div>
                    </c:forEach>
                    <c:if test="${empty requestScope.doctorReviews[doctor.doctorId]}">
                        <p>No reviews yet for this doctor.</p>
                    </c:if>
                </div>
            </div>
        </c:forEach>
        <c:if test="${empty requestScope.doctors}">
            <p>No doctors available.</p>
        </c:if>
    </div>
</body>
</html>