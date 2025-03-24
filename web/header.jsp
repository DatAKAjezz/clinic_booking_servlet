<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Header YouMed</title>
        <style>
            /*
    To change this license header, choose License Headers in Project Properties.
    To change this template file, choose Tools | Templates
    and open the template in the editor.
            */
            /* 
                Created on : Mar 21, 2025, 12:14:52 PM
                Author     : datdat
            */
            * {
                margin: 0;
                padding: 0;
                box-sizing: border-box;
            }
            body {
                font-family: Arial, sans-serif;
            }
            .navbar {
                display: flex;
                justify-content: space-between;
                align-items: center;
                background-color: white;
                border-bottom: 2px solid #eeeeee;
                box-shadow: 0px 2px 5px rgba(0, 0, 0, 0.1);
                text-transform: uppercase;
            }
            .logo {
                width: 8%;
                /*height: 130px;*/
                padding-left: 1%;
            }
            .logo img {
                width: 120%;
            }
            .nav-links {
                display: flex;
                list-style: none;
            }
            .nav-links li {
                margin: 0 2em; /* T?ng kho?ng cách gi?a các menu */
                font-size: 0.9rem;
            }
            .nav-links a {
                text-decoration: none;
                color: black;
                transition: color 0.2s ease; 
            }

            /* Style cho form trong menu */
            .nav-links form {
                margin: 0;
                padding: 0;
            }

            .nav-links button {
                background: none;
                border: none;
                font-family: Arial, sans-serif;
                font-size: 0.9rem;
                color: black;
                cursor: pointer;
                text-transform: uppercase;
                padding: 0;
                transition: color 0.2s ease;
            }

            .nav-links button:hover {
                color: #346fdb;
                text-decoration: underline;
            }

            .nav-links a:hover {
                color: #346fdb; 
                text-decoration: underline;
            }
            .login-btn {
                padding: 0.8% 1.5%;
                border: 1px solid #346fdb;
                color: #346fdb;
                font-weight: bold;
                font-size: 0.9rem;
                text-decoration: none;
                border-radius: 5px;
                transition: 0.3s;
                margin-right: 5%;
            }
            .login-btn:hover {
                background: linear-gradient(45deg, #9beaff 1%, #346fdb);
                color: white;
            }
            .user-info {
                align-items: center;
                /* gap: 1rem; */
                margin-right: 5%;
                /* height: 59px; */
                justify-items: center;
            }

            .user-info h2 {
                font-size: 1rem;
                color: #346fdb;
                font-weight: bold;
                padding-bottom: 1rem;
                text-transform: uppercase;
            }

            .logout-btn {
                padding: 0.5rem 1.2rem;
                border: 1px solid #ff4d4d;
                color: #ff4d4d;
                font-weight: bold;
                font-size: 0.9rem;
                text-decoration: none;
                border-radius: 5px;
                transition: 0.3s;
            }

            .logout-btn:hover {
                background: linear-gradient(45deg, #ff9b9b, #ff4d4d);
                color: white;
            }
            .nav-links button:hover, .nav-links a:hover {
                color: #346fdb;
                text-decoration: underline;
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