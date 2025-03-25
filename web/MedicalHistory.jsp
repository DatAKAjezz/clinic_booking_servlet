<%@page import="model.User"%>
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
        /* Reset & Base Styles */
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }

        body {
            display: flex;
            min-height: 100vh;
            background-color: #f9fafb;
            color: #1f2937;
        }

        /* Sidebar Styles */
        .sidebar {
            width: 280px;
            border-right: 1px solid #e5e7eb;
            background-color: white;
            padding: 24px 0;
            box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -1px rgba(0, 0, 0, 0.06);
            display: flex;
            flex-direction: column;
            position: sticky;
            top: 0;
            height: 100vh;
            z-index: 10;
        }

        .profile {
            padding: 0 24px 24px 24px;
            border-bottom: 1px solid #e5e7eb;
            margin-bottom: 16px;
        }

        .profile-image {
            width: 64px;
            height: 64px;
            background-color: #f3f4f6;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 28px;
            margin: 0 auto 12px;
        }

        .profile-name {
            font-size: 18px;
            font-weight: 600;
            margin-bottom: 4px;
            text-align: center;
        }

        .profile-email {
            font-size: 14px;
            color: #6b7280;
            margin-bottom: 16px;
            text-align: center;
            word-break: break-all;
        }

        .logout-btn {
            display: flex;
            align-items: center;
            justify-content: center;
            gap: 8px;
            padding: 8px 16px;
            background-color: #f3f4f6;
            color: #1f2937;
            text-decoration: none;
            border-radius: 8px;
            font-size: 14px;
            font-weight: 500;
            transition: all 0.2s;
            width: 100%;
            border: none;
            cursor: pointer;
        }

        .logout-btn:hover {
            background-color: #e5e7eb;
        }

        .menu {
            display: flex;
            flex-direction: column;
            padding: 0 12px;
        }

        .menu-item {
            display: flex;
            align-items: center;
            padding: 12px;
            border-radius: 8px;
            color: #1f2937;
            text-decoration: none;
            margin-bottom: 4px;
            transition: all 0.2s;
        }

        .menu-item:hover {
            background-color: #f3f4f6;
        }

        .menu-item.active {
            background-color: #2563eb;
            color: white;
        }

        .menu-icon {
            margin-right: 12px;
            font-size: 18px;
        }

        /* Main Content Styles */
        .main-content {
            flex: 1;
            padding: 32px;
            max-width: 1200px;
            margin: 0 auto;
            width: 100%;
        }

        .page-title {
            font-size: 28px;
            font-weight: 700;
            color: #1f2937;
            margin-bottom: 20px;
        }

        .history-count {
            font-size: 16px;
            font-weight: 600;
            margin-bottom: 20px;
            color: #6b7280;
        }

        /* History Card Styles */
        .history-card {
            border: 1px solid #e5e7eb;
            border-radius: 8px;
            padding: 24px;
            margin-bottom: 20px;
            background-color: white;
            box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -1px rgba(0, 0, 0, 0.06);
            transition: all 0.2s;
        }

        .history-card:hover {
            box-shadow: 0 10px 15px -3px rgba(0, 0, 0, 0.1), 0 4px 6px -2px rgba(0, 0, 0, 0.05);
        }

        .session-title {
            font-size: 20px;
            font-weight: 600;
            color: #2563eb;
            margin-bottom: 16px;
        }

        .appointment-id {
            font-size: 14px;
            color: #6b7280;
            margin-bottom: 16px;
        }

        .doctor-info {
            margin-bottom: 20px;
        }

        .doctor-name {
            font-size: 18px;
            font-weight: 600;
            margin-bottom: 8px;
        }

        .schedule-info {
            color: #1f2937;
            margin-bottom: 6px;
            display: flex;
            gap: 8px;
        }

        .schedule-info:last-child {
            margin-bottom: 0;
        }

        /* Message Styles */
        .message {
            padding: 12px 16px;
            border-radius: 8px;
            margin-bottom: 20px;
            font-weight: 500;
            position: fixed;
            bottom: 20px;
            right: 20px;
            z-index: 1000;
        }

        .message.success {
            background-color: rgba(16, 185, 129, 0.1);
            color: #065f46;
            border: 1px solid rgba(16, 185, 129, 0.2);
        }

        .message.error {
            background-color: rgba(239, 68, 68, 0.1);
            color: #b91c1c;
            border: 1px solid rgba(239, 68, 68, 0.2);
        }

        /* Status Colors */
        .text-warning { color: #f59e0b; }
        .text-primary { color: #2563eb; }
        .text-success { color: #10b981; }
        .text-danger { color: #ef4444; }

        /* Responsive Styles */
        @media (max-width: 768px) {
            body {
                flex-direction: column;
            }

            .sidebar {
                width: 100%;
                height: auto;
                position: static;
                padding: 16px;
            }

            .profile {
                padding: 0 8px 16px 8px;
                display: flex;
                align-items: center;
                flex-wrap: wrap;
                gap: 16px;
                text-align: left;
            }

            .profile-image {
                margin: 0;
            }

            .menu {
                flex-direction: row;
                overflow-x: auto;
                padding: 8px;
                gap: 8px;
            }

            .menu-item {
                flex-direction: column;
                padding: 12px 8px;
                text-align: center;
                flex: 0 0 auto;
            }

            .menu-icon {
                margin-right: 0;
                margin-bottom: 4px;
            }

            .main-content {
                padding: 16px;
            }
        }
    </style>
</head>
<body>
    <div class="sidebar">
        <div class="profile">
            <div class="profile-image" style="background-image: url(data:image/jpeg;base64,${sessionScope.USER.profilePicture}); background-size: cover; background-position: center"></div>
            <div class="profile-name">${sessionScope.fullName}</div>
            <div class="profile-email">${sessionScope.USER.email}</div>
            <form action="MainServlet" method="post">
                <input type="hidden" name="btnAction" value="logout"/>
                <button type="submit" class="logout-btn"><i class="bi bi-box-arrow-right"></i> Log out</button>
            </form>
        </div>
        <%
            String url = "PatientDashboardServlet";
            User user = (User) session.getAttribute("USER");
            if (user.getRole().equals("doctor")){
                url = "DoctorDashboardServlet";
            }
            else if (user.getRole().equals("StaffDashboardServlet"));
        %>
        <div class="menu">
            <a href="HomePage.jsp" class="menu-item"><span class="menu-icon"><i class="bi bi-house"></i></span>Home</a>
            <a href="<%=url%>" class="menu-item"><span class="menu-icon"><i class="bi bi-calendar"></i></span>My Appointments</a>
            <a href="MedicalHistoryServlet" class="menu-item active"><span class="menu-icon"><i class="bi bi-file-medical"></i></span>Patient Records</a>
            <a href="Profile.jsp" class="menu-item"><span class="menu-icon"><i class="bi bi-person"></i></span>Profile</a>
        </div>
    </div>

    <div class="main-content">
        <h1 class="page-title">Medical History</h1>
        <div class="history-count"><i class="bi bi-file-medical"></i> Records (${requestScope.medicalHistory != null ? requestScope.medicalHistory.size() : 0})</div>

        <c:if test="${not empty requestScope.error}">
            <div class="message error"><i class="bi bi-exclamation-circle"></i> ${requestScope.error}</div>
        </c:if>

        <c:forEach var="record" items="${requestScope.medicalHistory}">
            <div class="history-card">
                <div class="session-title"><i class="bi bi-clipboard-pulse"></i> ${record.serviceName}</div>
                <div class="appointment-id">Appointment ID: ${record.appointmentId}</div>
                <div class="doctor-info">
                    <div class="doctor-name"><i class="bi bi-person-circle"></i> ${record.doctorName}</div>
                    <div class="schedule-info"><i class="bi bi-calendar-date"></i> Date: ${record.appointmentDate}</div>
                    <div class="schedule-info"><i class="bi bi-chat-left-text"></i> Reason: ${record.reason}</div>
                    <div class="schedule-info"><i class="bi bi-tag"></i> Status: 
                        <span class="${record.status == 'pending' ? 'text-warning' : 
                                       record.status == 'confirmed' ? 'text-primary' : 
                                       record.status == 'completed' ? 'text-success' : 
                                       record.status == 'canceled' ? 'text-danger' : ''}">
                            ${record.status}
                        </span>
                    </div>
                </div>
            </div>
        </c:forEach>

        <c:if test="${empty requestScope.medicalHistory}">
            <div class="history-card" style="text-align: center; padding: 40px 20px;">
                <i class="bi bi-file-medical" style="font-size: 48px; color: #d1d5db; display: block; margin-bottom: 16px;"></i>
                <p style="font-size: 18px; color: #6b7280;">No medical history found for this patient.</p>
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