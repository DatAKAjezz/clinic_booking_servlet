<%@page import="model.User"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css">
        <title>My Bookings History</title>
        <link rel="stylesheet" href="<%= request.getContextPath()%>/styles/PatientBookings.css"/>
    </head>
    <body>

        <%
            User u = (User) session.getAttribute("USER");
            if (u == null || !u.getRole().equals("patient")) {
                request.setAttribute("ERROR", "Bạn cần đăng nhập với tư cách patient.");
                request.getRequestDispatcher("LoginPage.jsp").forward(request, response);
            }
        %>

        <div class="sidebar">
            <div class="profile">
                <div class="profile-image" style="background-image: url(data:image/jpeg;base64,${sessionScope.USER.profilePicture}); background-size: cover; background-position: center"></div>
                <div class="profile-name">${sessionScope.fullName}</div>
                <div class="profile-email">${sessionScope.USER.email}</div>
                <form action="MainServlet" method="post">
                    <input type="hidden" name="btnAction" value="logout"/>
                    <a href="MainServlet?btnAction=logout" class="logout-btn"><i class="bi bi-box-arrow-right"></i> Log out</a>
                </form>
            </div>
            <div class="menu">
                <a href="HomePage.jsp" class="menu-item"><span class="menu-icon"><i class="bi bi-house"></i></span>Home</a>
                <a href="PatientDashboardServlet" class="menu-item active"><span class="menu-icon"><i class="bi bi-calendar"></i></span>My Bookings</a>
                <a href="MedicalHistoryServlet" class="menu-item"><span class="menu-icon"><i class="bi bi-file-medical"></i></span>Medical History</a>
                <a href="Profile.jsp" class="menu-item"><span class="menu-icon"><i class="bi bi-person"></i></span>Profile</a>
            </div>
        </div>

        <div class="main-content">
            <div class="header">
                <h1 class="page-title">My Bookings History</h1>
                <button class="refresh-btn" onclick="window.location.href = 'PatientDashboardServlet'"><i class="bi bi-arrow-clockwise"></i> Refresh</button>
            </div>
            <div class="bookings-count"><i class="bi bi-calendar-check"></i> My Bookings (${requestScope.bookings != null ? requestScope.bookings.size() : 0})</div>

            <form action="BookAppointmentServlet" method="GET">
                <button type="submit" class="book-new-btn"><i class="bi bi-plus-circle"></i> Book New Appointment</button>
            </form>

            <c:forEach var="booking" items="${requestScope.bookings}">
                <div class="booking-card">
                    <div class="session-title"><i class="bi bi-clipboard-pulse"></i> ${booking.serviceName}</div>
                    <div class="appointment-id">Appointment ID: ${booking.appointmentId}</div>
                    <div class="doctor-info">
                        <div class="doctor-name"><i class="bi bi-person-circle"></i> ${booking.doctorName}</div>
                        <div class="schedule-info"><i class="bi bi-calendar-date"></i> Date: ${booking.appointmentDate}</div>
                        <div class="schedule-info"><i class="bi bi-chat-left-text"></i> Reason: ${booking.reason}</div>
                        <div class="schedule-info"><i class="bi bi-tag"></i> Status: 
                            <span class="${booking.status == 'pending' ? 'text-warning' : 
                                           booking.status == 'confirmed' ? 'text-primary' : 
                                           booking.status == 'completed' ? 'text-success' : 
                                           booking.status == 'canceled' ? 'text-danger' : ''}">
                                      ${booking.status}
                                  </span>
                            </div>
                        </div>

                        <c:if test="${not empty booking.note}">
                            <div class="schedule-info"><i class="bi bi-journal-text"></i> Ghi Chú: ${booking.note}</div>
                        </c:if>

                        <c:if test="${booking.status == 'pending'}">
                            <form action="CancelBookingServlet" method="post">
                                <input type="hidden" name="appointmentId" value="${booking.appointmentId}">
                                <button type="submit" class="cancel-btn" onclick="return confirm('Are you sure you want to cancel this booking?');"><i class="bi bi-x-circle"></i> Cancel Booking</button>
                            </form>
                        </c:if>
                        <c:if test="${booking.status == 'completed'}">
                            <form action="ReviewServlet" method="GET" style="display:inline;">
                                <input type="hidden" name="doctorId" value="${booking.doctorId}">
                                <button type="submit" class="review-btn"><i class="bi bi-star"></i> Review Doctor</button>
                            </form>
                        </c:if>
                    </div>
                </c:forEach>

                <c:if test="${empty requestScope.bookings}">
                    <div class="booking-card" style="text-align: center; padding: 40px 20px;">
                        <i class="bi bi-calendar-x" style="font-size: 48px; color: #d1d5db; display: block; margin-bottom: 16px;"></i>
                        <p style="font-size: 18px; color: #6b7280;">No bookings found.</p>
                    </div>
                </c:if>

                <c:if test="${not empty sessionScope.message}">
                    <div class="message success"><i class="bi bi-check-circle"></i> ${sessionScope.message}</div>
                    <% session.removeAttribute("message"); %>
                </c:if>
                <c:if test="${not empty sessionScope.error}">
                    <div class="message error"><i class="bi bi-exclamation-circle"></i> ${sessionScope.error}</div>
                    <% session.removeAttribute("error");%>
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