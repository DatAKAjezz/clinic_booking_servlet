<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Header YouMed</title>
</head>
<body>
    <header class="navbar">
        <div class="logo">
            <img src="<%= request.getContextPath()%>/img/Medical Logo.svg" alt="Logo">
        </div>
        <ul class="nav-links">
            <li><a href="HomePage.jsp">About us</a></li>
            <c:if test="${sessionScope.USER.role == 'patient'}">
                <li>
                    <form action="BookAppointmentServlet" method="GET">
                        <button type="submit">Book appointment</button>
                    </form>
                </li>
            </c:if>
            <li><a href="DoctorListServlet">Our doctors</a></li>
            <c:choose>
                <c:when test="${sessionScope.USER.role == 'patient'}">
                    <li><a href="PatientDashboardServlet">Scheduled</a></li>
                    <li><a href="MedicalHistoryServlet">Medical History</a></li>
                </c:when>
                <c:when test="${sessionScope.USER.role == 'doctor'}">
                    <li><a href="DoctorDashboardServlet">Scheduled</a></li>
                    <li><a href="MedicalHistoryServlet">Patient Records</a></li>
                </c:when>
                <c:when test="${sessionScope.USER.role == 'receptionist'}">
                    <li><a href="StaffDashboardServlet">Scheduled</a></li>
                </c:when>
            </c:choose>
            <li><a href="Profile.jsp">Profile</a></li>
        </ul>
        <c:choose>
            <c:when test="${empty sessionScope.USER}">
                <a href="MainServlet?btnAction=login" class="login-btn">Login</a>
            </c:when>
            <c:otherwise>
                <div class="user-info">
                    <h2>Welcome ${sessionScope.USER.username}</h2>
                    <a href="MainServlet?btnAction=logout" class="logout-btn">Logout</a>
                </div>
            </c:otherwise>
        </c:choose>
    </header>
</body>
</html>