<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css">
        <title>Profile</title>
        <link rel="stylesheet" href="<%= request.getContextPath()%>/styles/Profile.css"/>
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
                        <c:if test="${sessionScope.USER.role == 'patient'}">
                    <a href="PatientDashboardServlet" class="menu-item"><span class="menu-icon"><i class="bi bi-calendar"></i></span>My Bookings</a>
                    <a href="MedicalHistoryServlet" class="menu-item"><span class="menu-icon"><i class="bi bi-file-medical"></i></span>Medical History</a>
                        </c:if>
                        <c:if test="${sessionScope.USER.role == 'receptionist'}">
                    <a href="StaffDashboardServlet" class="menu-item"><span class="menu-icon"><i class="bi bi-calendar"></i></span>All Appointments</a>
                        </c:if>
                        <c:if test="${sessionScope.USER.role == 'doctor'}">
                    <a href="DoctorDashboardServlet" class="menu-item"><span class="menu-icon"><i class="bi bi-calendar"></i></span>My Appointments</a>
                    <a href="MedicalHistoryServlet" class="menu-item"><span class="menu-icon"><i class="bi bi-file-medical"></i></span>Patient Records</a>
                        </c:if>
                <a href="Profile.jsp" class="menu-item active"><span class="menu-icon"><i class="bi bi-person"></i></span>Profile</a>
            </div>
        </div>

        <div class="main-content">
            <div class="header">
                <h1 class="page-title">Profile</h1>
                <div class="user-info">Logged in as: ${sessionScope.USER.username} (${sessionScope.USER.role})</div>
            </div>

            <c:if test="${not empty sessionScope.message}">
                <div class="message success"><i class="bi bi-check-circle"></i> ${sessionScope.message}</div>
                <% session.removeAttribute("message"); %>
            </c:if>
            <c:if test="${not empty sessionScope.error}">
                <div class="message error"><i class="bi bi-exclamation-circle"></i> ${sessionScope.error}</div>
                <% session.removeAttribute("error");%>
            </c:if>

            <div class="profile-card">
                <h2 class="session-title"><i class="bi bi-person-circle"></i> Update Profile</h2>
                <form action="UpdateProfileServlet" method="POST" enctype="multipart/form-data">
                    <div class="form-group">
                        <label for="username">Username:</label>
                        <input type="text" id="username" name="username" value="${sessionScope.USER.username}" readonly class="form-input">
                    </div>
                    <div class="form-group">
                        <label for="email">Email:</label>
                        <input type="email" id="email" name="email" value="${sessionScope.USER.email}" required class="form-input">
                    </div>
                    <div class="form-group">
                        <label for="phone">Phone:</label>
                        <input type="text" id="phone" name="phone" value="${sessionScope.USER.phone}" required class="form-input">
                    </div>
                    <div class="form-group">
                        <label for="profilePicture">Profile Picture:</label>
                        <input type="file" id="profilePicture" name="profilePicture" accept="image/*" class="form-input" onchange="previewImage(event)">
                        <c:if test="${not empty sessionScope.USER.profilePicture}">
                            <div class="current-picture">
                                <p>Current Picture:</p>
                                <img src="data:image/jpeg;base64,${sessionScope.USER.profilePicture}" alt="Current Profile Picture" id="previewImage" style="max-width: 150px; margin-top: 10px;">
                            </div>
                        </c:if>
                    </div>
                    <button type="submit" class="update-btn"><i class="bi bi-save"></i> Update Profile</button>
                </form>
            </div>
        </div>

        <script>
            function previewImage(event) {
                const file = event.target.files[0];
                if (file) {
                    const reader = new FileReader();
                    reader.onload = function (e) {
                        const img = document.getElementById('previewImage') || document.createElement('img');
                        img.id = 'previewImage';
                        img.src = e.target.result;
                        img.style.maxWidth = '150px';
                        img.style.marginTop = '10px';
                        const currentPicture = document.querySelector('.current-picture');
                        if (!document.getElementById('previewImage')) {
                            currentPicture.appendChild(img);
                        }
                    };
                    reader.readAsDataURL(file);
                }
            }
        </script>
    </body>
</html>