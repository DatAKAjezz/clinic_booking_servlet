<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*, java.text.*" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Book an Appointment</title>
        <style>
            body {
                font-family: Arial, sans-serif;
                background-color: #f5f7fa;
                margin: 0;
                padding: 0;
            }

            .form-container {
                max-width: 600px;
                background: white;
                margin: 4rem auto;
                padding: 25px;
                border-radius: 8px;
                box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.1);
            }

            .form-title {
                text-align: center;
                color: #333;
                margin-bottom: 20px;
            }

            .patient-info {
                background-color: #f8f9fa;
                padding: 15px;
                border-radius: 6px;
                margin-bottom: 20px;
                border-left: 5px solid #346fdb;
            }

            .patient-info h3 {
                margin-bottom: 10px;
                color: #346fdb;
            }

            .form-group {
                margin-bottom: 20px;
            }

            label {
                display: block;
                font-weight: bold;
                margin-bottom: 5px;
                color: #333;
            }

            select, input[type="text"], input[type="date"] {
                width: 100%;
                padding: 10px;
                border: 1px solid #ccc;
                border-radius: 6px;
                font-size: 14px;
                background-color: #f8f9fa;
                transition: 0.3s;
            }

            select:hover, input[type="text"]:hover, input[type="date"]:hover {
                border-color: #346fdb;
            }

            button {
                width: 100%;
                background-color: #346fdb;
                color: white;
                padding: 12px;
                font-size: 16px;
                border: none;
                border-radius: 6px;
                cursor: pointer;
                transition: 0.3s;
            }

            button:hover {
                background-color: #2a5db7;
            }

            .message {
                padding: 12px;
                margin-top: 15px;
                border-radius: 6px;
                font-size: 14px;
                text-align: center;
            }

            .success {
                background-color: #d4edda;
                color: #155724;
            }

            .error {
                background-color: #f8d7da;
                color: #721c24;
            }

            .login-button {
                display: inline-block;
                background-color: #346fdb;
                color: white;
                padding: 10px 15px;
                text-decoration: none;
                border-radius: 6px;
                margin-top: 10px;
                text-align: center;
                width: 100%;
                display: block;
            }

            .login-button:hover {
                background-color: #2a5db7;
            }

            .input-label{
                padding-bottom: 10px;
            }
        </style>
    </head>
    <body>
        <jsp:include page="header.jsp" />
        <div class="form-container">
            <h2 class="form-title">Book an Appointment</h2>
            <c:choose>
                <c:when test="${empty sessionScope.USER}">
                    <div class="message error">
                        <p>You need to log in to book an appointment.</p>
                        <a href="LoginPage.jsp" class="login-button">Log In</a>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="patient-info">
                        <h3>Patient Information</h3>
                        <p class="input-label"><strong>Username:</strong> ${sessionScope.USER.username}</p>
                        <div class="input-label">
                            <label>Email:</label>
                            <input type="text" value="${sessionScope.USER.email}" readonly />
                        </div>
                        <div class="input-label">
                            <label>Phone Number:</label>
                            <input type="text" value="${sessionScope.USER.phone}" readonly />
                        </div>
                    </div>

                    <form action="BookAppointmentServlet" method="POST">
                        <div class="form-group">
                            <label for="doctorId">Select Doctor:</label>
                            <select name="doctorId" id="doctorId" required>
                                <option value="">Select Doctor</option>
                                <c:forEach var="doctor" items="${doctors}">
                                    <option value="${doctor.doctorId}">${doctor.fullName} - ${doctor.specialty}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="form-group">
                            <label for="scheduleId">Select Schedule:</label>
                            <select name="scheduleId" id="scheduleId" required>
                                <option value="">Select Schedule</option>
                                <c:forEach var="schedule" items="${schedules}">
                                    <option value="${schedule.scheduleId}">${schedule.date} ${schedule.startTime} - ${schedule.endTime}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="form-group">
                            <label for="serviceId">Select Service:</label>
                            <select name="serviceId" id="serviceId" required>
                                <option value="">Select Service</option>
                                <c:forEach var="service" items="${services}">
                                    <option value="${service.serviceId}">${service.name} - ${service.price}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="form-group">
                            <label for="appointmentDate">Select Date:</label>
                            <input type="date" id="appointmentDate" name="appointmentDate" required 
                                   min="<%= new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date())%>"/>
                        </div>
                        <div class="form-group">
                            <label for="time">Select Time:</label>
                            <input type="time" id="time" name="time" required />
                        </div>
                        <div class="form-group">
                            <label for="reason">Reason for Visit:</label>
                            <input type="text" id="reason" name="reason" placeholder="Enter reason" required />
                        </div>
                        <button type="submit">Book Appointment</button>
                    </form>
                </c:otherwise>
            </c:choose>
            <c:if test="${not empty message}">
                <div class="message success">${message}</div>
            </c:if>
            <c:if test="${not empty error}">
                <div class="message error">${error}</div>
            </c:if>
        </div>
    </body>
</html>
