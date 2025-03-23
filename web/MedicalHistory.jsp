<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css">
    <title>Medical History</title>
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; font-family: Arial, sans-serif; }
        body { display: flex; min-height: 100vh; }
        .sidebar { width: 300px; border-right: 1px solid #e0e0e0; padding: 20px; background-color: #f9f9f9; }
        .main-content { flex: 1; padding: 20px; }
        .profile { text-align: center; margin-bottom: 30px; }
        .profile-image { width: 65px; height: 65px; border-radius: 50%; background-color: #f0f0f0; display: flex; justify-content: center; align-items: center; margin: 0 auto 10px; }
        .profile-name { font-weight: bold; font-size: 18px; }
        .profile-email { color: #777; font-size: 14px; }
        .logout-btn { width: 100%; padding: 10px; background-color: #f0f6ff; border: none; border-radius: 5px; color: #3177d7; cursor: pointer; margin-top: 10px; }
        .menu-item { display: flex; align-items: center; padding: 15px 10px; margin-bottom: 5px; border-radius: 5px; cursor: pointer; text-decoration: none; color: #333; }
        .menu-item.active { background-color: #0069d9; color: white; }
        .menu-icon { margin-right: 10px; }
        .page-title { font-size: 24px; font-weight: bold; margin-bottom: 20px; }
        .history-count { font-size: 18px; margin-bottom: 20px; }
        .history-card { border: 1px solid #e0e0e0; border-radius: 5px; padding: 20px; margin-bottom: 20px; }
        .session-title { font-size: 20px; color: #0069d9; margin-bottom: 15px; }
        .doctor-name { font-weight: bold; }
        .schedule-info { color: #777; margin-bottom: 5px; }
        .message.success { color: green; margin-bottom: 10px; }
        .message.error { color: red; margin-bottom: 10px; }
    </style>
</head>
<body>
    <div class="sidebar">
        <div class="profile">
            <div class="profile-image"><span>👤</span></div>
            <div class="profile-name">${sessionScope.USER.username}</div>
            <div class="profile-email">${sessionScope.USER.email}</div>
            <form action="MainServlet" method="post">
                <input type="hidden" name="btnAction" value="logout"/>
                <button type="submit" class="logout-btn">Log out</button>
            </form>
        </div>
        <div class="menu">
            <a href="HomePage.jsp" class="menu-item"><span class="menu-icon"><i class="bi bi-house"></i></span>Home</a>
            <a href="DoctorDashboardServlet" class="menu-item"><span class="menu-icon"><i class="bi bi-calendar"></i></span>My Appointments</a>
            <a href="MedicalHistoryServlet" class="menu-item active"><span class="menu-icon"><i class="bi bi-file-medical"></i></span>Patient Records</a>
            <a href="Profile.jsp" class="menu-item"><span class="menu-icon"><i class="bi bi-person"></i></span>Profile</a>
        </div>
    </div>

    <div class="main-content">
        <h1 class="page-title">Medical History</h1>
        <div class="history-count">Records (${requestScope.medicalHistory != null ? requestScope.medicalHistory.size() : 0})</div>

        <c:if test="${not empty requestScope.error}">
            <div class="message error">${requestScope.error}</div>
        </c:if>

        <c:forEach var="record" items="${requestScope.medicalHistory}">
            <div class="history-card">
                <div class="session-title">${record.serviceName}</div>
                <div class="appointment-id">Appointment ID: ${record.appointmentId}</div>
                <div class="doctor-info">
                    <div class="doctor-name">${record.doctorName}</div>
                    <div class="schedule-info">Date: ${record.appointmentDate}</div>
                    <div class="schedule-info">Time: ${record.appointmentTime}</div>
                    <div class="schedule-info">Reason: ${record.reason}</div>
                    <div class="schedule-info">Status: ${record.status}</div>
                </div>
            </div>
        </c:forEach>

        <c:if test="${empty requestScope.medicalHistory}">
            <div class="history-card">
                <p>No medical history found for this patient.</p>
            </div>
        </c:if>
    </div>

    <script>
        document.addEventListener('DOMContentLoaded', function () {
            const menuItems = document.querySelectorAll('.menu-item');
            menuItems.forEach(item => {
                item.addEventListener('click', function () {
                    menuItems.forEach(i => i.classList.remove('active'));
                    this.classList.add('active');
                });
            });
        });
    </script>
</body>
</html>