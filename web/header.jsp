<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>YouMed Header</title>
        <style>
            * {
                margin: 0;
                padding: 0;
                box-sizing: border-box;
            }

            body {
                font-family: 'Inter', Arial, sans-serif;
                color: #333;
                line-height: 1.6;
            }

            .navbar {
                display: flex;
                justify-content: space-between;
                align-items: center;
                background-color: white;
                padding: 1rem 5%;
                border-bottom: 1px solid #e0e0e0;
                box-shadow: 0 4px 6px rgba(0, 0, 0, 0.05);
                position: sticky;
                top: 0;
                z-index: 100;
            }

            .logo {
                width: 150px;
                transition: transform 0.3s ease;
            }

            .logo img {
                width: 100%;
                height: auto;
            }

            .logo:hover {
                transform: scale(1.05);
            }

            .nav-links {
                display: flex;
                list-style: none;
                align-items: center;
                gap: 2rem;
            }

            .nav-links li {
                position: relative;
            }

            .nav-links a, .nav-links button {
                text-decoration: none;
                color: #333;
                font-weight: 600;
                text-transform: uppercase;
                font-size: 0.9rem;
                transition: color 0.3s ease;
                background: none;
                border: none;
                cursor: pointer;
            }

            .nav-links a:hover, .nav-links button:hover {
                color: #346fdb;
            }

            .nav-links a::after, .nav-links button::after {
                content: '';
                position: absolute;
                width: 0;
                height: 2px;
                bottom: -5px;
                left: 50%;
                background-color: #346fdb;
                transition: width 0.3s ease, left 0.3s ease;
            }

            .nav-links a:hover::after, .nav-links button:hover::after {
                width: 100%;
                left: 0;
            }

            .login-btn, .logout-btn {
                padding: 0.6rem 1.2rem;
                border: 1px solid #346fdb;
                color: #346fdb;
                border-radius: 5px;
                font-weight: bold;
                text-transform: uppercase;
                transition: all 0.3s ease;
            }

            .login-btn:hover, .logout-btn:hover {
                background: linear-gradient(45deg, #9beaff 1%, #346fdb);
                color: white;
                text-decoration: none;
            }

            .user-info {
                display: flex;
                flex-direction: column;
                align-items: flex-end;
            }

            .user-info h2 {
                font-size: 1rem;
                color: #346fdb;
                font-weight: bold;
                margin-bottom: 0.5rem;
                text-transform: uppercase;
            }
        </style>
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
                        <li><a href="MedicalHistoryServlet">Record</a></li>
                        </c:when>
                        <c:when test="${sessionScope.USER.role == 'receptionist'}">
                        <li><a href="StaffDashboardServlet">Scheduled</a></li>
                        <li><a href="Profile.jsp">Profile</a></li>
                    </c:when>
                </c:choose>
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