<%-- 
    Document   : AddAppointment
    Created on : Mar 25, 2025, 11:31:44 PM
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
    <title>Thêm Lịch Hẹn</title>
    <link rel="stylesheet" href="<%= request.getContextPath()%>/styles/StaffDashboard.css"/>
</head>
<body>
    <div class="sidebar">
        <div class="profile">
            <div class="profile-image" style="background-image: url(data:image/jpeg;base64,${sessionScope.USER.profilePicture}); background-size: cover; background-position: center"></div>
            <div class="profile-name">${sessionScope.fullName}</div>
            <div class="profile-email">${sessionScope.USER.email}</div>
            <a href="MainServlet?btnAction=logout" class="logout-btn"><i class="bi bi-box-arrow-right"></i> Log out</a>
        </div>
        <div class="menu">
            <a href="HomePage.jsp" class="menu-item"><span class="menu-icon"><i class="bi bi-house"></i></span>Home</a>
            <a href="StaffDashboardServlet" class="menu-item"><span class="menu-icon"><i class="bi bi-calendar"></i></span>All Appointments</a>
            <a href="Profile.jsp" class="menu-item"><span class="menu-icon"><i class="bi bi-person"></i></span>Profile</a>
        </div>
    </div>

    <div class="main-content">
        <div class="header">
            <h1 class="page-title">Thêm Lịch Hẹn Mới</h1>
            <div class="user-info">Đăng nhập với vai trò: ${sessionScope.USER.username} (Lễ tân)</div>
            <button class="back-btn" onclick="window.location.href = 'StaffDashboardServlet'"><i class="bi bi-arrow-left"></i> Quay lại</button>
        </div>

        <!-- Thông báo -->
        <c:if test="${not empty sessionScope.message}">
            <div class="message success"><i class="bi bi-check-circle"></i> ${sessionScope.message}</div>
            <% session.removeAttribute("message"); %>
        </c:if>
        <c:if test="${not empty sessionScope.error}">
            <div class="message error"><i class="bi bi-exclamation-circle"></i> ${sessionScope.error}</div>
            <% session.removeAttribute("error"); %>
        </c:if>

        <!-- Form thêm lịch hẹn -->
        <div class="appointment-form">
            <form action="StaffDashboardServlet" method="POST">
                <input type="hidden" name="action" value="add">
                <div class="form-group">
                    <label>ID Bệnh Nhân (nếu có):</label>
                    <input type="number" name="patientId" placeholder="Nhập ID bệnh nhân (tùy chọn)">
                </div>
                <div class="form-group">
                    <label>Tên Khách Vãng Lai:</label>
                    <input type="text" name="guestName" placeholder="Nhập tên khách (nếu không có tài khoản)">
                </div>
                <div class="form-group">
                    <label>Số Điện Thoại Khách:</label>
                    <input type="text" name="guestPhone" placeholder="Nhập số điện thoại khách">
                </div>
                <div class="form-group">
                    <label>Chọn Bác Sĩ:</label>
                    <select name="doctorId" required>
                        <option value="">Chọn Bác Sĩ</option>
                        <c:forEach var="doctor" items="${doctors}">
                            <option value="${doctor.doctorId}">${doctor.fullName} - ${doctor.specialty}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="form-group">
                    <label>Chọn Khung Giờ:</label>
                    <select name="scheduleId" required>
                        <option value="">Chọn Khung Giờ</option>
                        <c:forEach var="schedule" items="${schedules}">
                            <option value="${schedule.scheduleId}">${schedule.startTime} - ${schedule.endTime}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="form-group">
                    <label>Chọn Dịch Vụ:</label>
                    <select name="serviceId" required>
                        <option value="">Chọn Dịch Vụ</option>
                        <c:forEach var="service" items="${services}">
                            <option value="${service.serviceId}">${service.name}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="form-group">
                    <label>Ngày Khám:</label>
                    <input type="date" name="appointmentDate" required>
                </div>
                <div class="form-group">
                    <label>Lý Do Khám:</label>
                    <input type="text" name="reason" placeholder="Nhập lý do khám" required>
                </div>
                <button type="submit" class="submit-btn"><i class="bi bi-plus-circle"></i> Thêm Lịch Hẹn</button>
            </form>
        </div>
    </div>
</body>
</html>