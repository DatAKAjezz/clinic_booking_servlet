<%@page import="model.User"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
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
                box-sizing: border-box;
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

            .input-label {
                padding-bottom: 10px;
            }

            .error-message {
                color: red;
                font-size: 12px;
                margin-top: 5px;
            }
        </style>
    </head>
    <body>

        <%
            User u = (User) session.getAttribute("USER");
            if (u == null || !u.getRole().equals("patient")) {
                request.setAttribute("ERROR", "Bạn cần đăng nhập với tư cách patient để đặt Appointment.");
                request.getRequestDispatcher("LoginPage.jsp").forward(request, response);
            }
        %>

        <jsp:include page="header.jsp" />
        <div class="form-container">
            <h2 class="form-title">Book an Appointment</h2>

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
                    <select name="doctorId" id="doctorId" onchange="window.location.href = 'BookAppointmentServlet?doctorId=' + this.value">
                        <option value="">Select Doctor</option>
                        <c:forEach var="doctor" items="${doctors}">
                            <option value="${doctor.doctorId}"
                                    ${doctor.doctorId eq requestScope.selectedDoctorId ? 'selected' : ''}>
                                ${doctor.fullName} - ${doctor.specialty}
                            </option>
                        </c:forEach>
                    </select>
                    <c:if test="${not empty requestScope.doctorIdError}">
                        <p class="error-message">${requestScope.doctorIdError}</p>
                    </c:if>
                </div>

                <div class="form-group">
                    <label for="scheduleId">Select Time Slot:</label>
                    <select name="scheduleId" id="scheduleId">
                        <option value="">Select Time Slot</option>
                        <c:forEach var="schedule" items="${schedules}">
                            <option value="${schedule.scheduleId}"
                                    ${schedule.scheduleId eq requestScope.selectedScheduleId ? 'selected' : ''}>
                                ${schedule.startTime} - ${schedule.endTime}
                            </option>
                        </c:forEach>
                    </select>
                    <c:if test="${not empty requestScope.scheduleIdError}">
                        <p class="error-message">${requestScope.scheduleIdError}</p>
                    </c:if>
                </div>

                <div class="form-group">
                    <label for="serviceId">Select Service:</label>
                    <select name="serviceId" id="serviceId">
                        <option value="">Select Service</option>
                        <c:forEach var="service" items="${services}">
                            <option value="${service.serviceId}"
                                    ${service.serviceId eq requestScope.selectedServiceId ? 'selected' : ''}>
                                ${service.name} - ${service.price}
                            </option>
                        </c:forEach>
                    </select>
                    <c:if test="${not empty requestScope.serviceIdError}">
                        <p class="error-message">${requestScope.serviceIdError}</p>
                    </c:if>
                </div>

                <div class="form-group">
                    <label for="appointmentDate">Select Date:</label>
                    <input type="date" 
                           id="appointmentDate" 
                           name="appointmentDate" 
                           value="${requestScope.selectedDate}"
                           min="<%= new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date())%>" />
                    <c:if test="${not empty requestScope.dateError}">
                        <p class="error-message">${requestScope.dateError}</p>
                    </c:if>
                </div>

                <div class="form-group">
                    <label for="reason">Reason for Visit:</label>
                    <input type="text" 
                           id="reason" 
                           name="reason" 
                           value="${requestScope.selectedReason}"
                           placeholder="Enter reason" />
                </div>

                <button type="submit">Book Appointment</button>
            </form>

            <c:if test="${not empty sessionScope.message}">
                <div class="message success">${sessionScope.message}</div>
                <% session.removeAttribute("message");%>
            </c:if>
            <c:if test="${not empty requestScope.error}">
                <div class="message error">${requestScope.error}</div>
            </c:if>
        </div>
    </body>
</html>