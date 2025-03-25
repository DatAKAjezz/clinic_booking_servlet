<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css">
        <title>Staff Dashboard</title>
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
                <a href="StaffDashboardServlet" class="menu-item active"><span class="menu-icon"><i class="bi bi-calendar"></i></span>All Appointments</a>
                <a href="Profile.jsp" class="menu-item"><span class="menu-icon"><i class="bi bi-person"></i></span>Profile</a>
            </div>
        </div>

        <div class="main-content">
            <div class="header">
                <h1 class="page-title">All Appointments</h1>
                <div class="user-info">Logged in as: ${sessionScope.USER.username} (Receptionist)</div>
                <button class="refresh-btn" onclick="window.location.href = 'StaffDashboardServlet'"><i class="bi bi-arrow-clockwise"></i> Refresh</button>
                <button class="today-btn" onclick="window.location.href = 'StaffDashboardServlet?today=true'"><i class="bi bi-calendar-day"></i> Today's Appointments</button>
            </div>
            <div class="appointments-count"><i class="bi bi-calendar-check"></i> Appointments (${requestScope.appointments != null ? requestScope.appointments.size() : 0})</div>

            <c:if test="${not empty sessionScope.message}">
                <div class="message success"><i class="bi bi-check-circle"></i> ${sessionScope.message}</div>
                <% session.removeAttribute("message"); %>
            </c:if>
            <c:if test="${not empty sessionScope.error}">
                <div class="message error"><i class="bi bi-exclamation-circle"></i> ${sessionScope.error}</div>
                <% session.removeAttribute("error");%>
            </c:if>

            <div class="search-bar">
                <form action="StaffDashboardServlet" method="GET">
                    <input type="text" name="searchPatient" placeholder="Search by patient name" value="${param.searchPatient}">
                    <input type="text" name="searchDoctor" placeholder="Search by doctor name" value="${param.searchDoctor}">
                    <button type="submit"><i class="bi bi-search"></i> Search</button>
                    <input type="hidden" name="statusFilter" value="${param.statusFilter}">
                    <input type="hidden" name="dateFilter" value="${param.dateFilter}">
                    <input type="hidden" name="page" value="1">
                </form>
            </div>

            <div class="filter-section">
                <form action="StaffDashboardServlet" method="GET">
                    <label for="statusFilter">Filter by Status: </label>
                    <select name="statusFilter" id="statusFilter" onchange="this.form.submit()">
                        <option value="">All</option>
                        <option value="pending" ${param.statusFilter == 'pending' ? 'selected' : ''}>Pending</option>
                        <option value="confirmed" ${param.statusFilter == 'confirmed' ? 'selected' : ''}>Confirmed</option>
                        <option value="canceled" ${param.statusFilter == 'canceled' ? 'selected' : ''}>Canceled</option>
                        <option value="completed" ${param.statusFilter == 'completed' ? 'selected' : ''}>Completed</option>
                    </select>
                    <label for="dateFilter" style="margin-left: 20px;">Filter by Date: </label>
                    <input type="date" name="dateFilter" id="dateFilter" value="${param.dateFilter}" onchange="this.form.submit()">
                    <input type="hidden" name="searchPatient" value="${param.searchPatient}">
                    <input type="hidden" name="searchDoctor" value="${param.searchDoctor}">
                    <input type="hidden" name="page" value="1">
                </form>
            </div>

            <c:if test="${requestScope.showingToday}">
                <div class="today-info" style="color: #10b981; margin: 10px 0;">
                    <i class="bi bi-calendar-day"></i> Showing appointments for today
                </div>
            </c:if>
            <c:if test="${not empty param.dateFilter && !requestScope.showingToday}">
                <div class="date-info" style="color: #3b82f6; margin: 10px 0;">
                    <i class="bi bi-calendar"></i> Showing appointments for ${param.dateFilter}
                </div>
            </c:if>

            <c:forEach var="appointment" items="${requestScope.appointments}">
                <div class="appointment-card">
                    <div class="session-title"><i class="bi bi-clipboard-pulse"></i> ${appointment.serviceName}</div>
                    <div class="appointment-id">Appointment ID: ${appointment.appointmentId}</div>
                    <div class="patient-info">
                        <div class="patient-name"><i class="bi bi-person-circle"></i> Patient: ${appointment.patientName}</div>
                        <div class="doctor-name"><i class="bi bi-person-badge"></i> Doctor: ${appointment.doctorName}</div>
                        <div class="schedule-info"><i class="bi bi-calendar-date"></i> Date: ${appointment.appointmentDate}</div>
                        <div class="schedule-info"><i class="bi bi-chat-left-text"></i> Reason: ${appointment.reason}</div>
                        <div class="schedule-info"><i class="bi bi-tag"></i> Status: 
                            <span class="info-text ${appointment.status == 'pending' ? 'text-warning' : 
                                                     appointment.status == 'confirmed' ? 'text-primary' : 
                                                     appointment.status == 'completed' ? 'text-success' : 
                                                     appointment.status == 'canceled' ? 'text-danger' : ''}">
                                      ${appointment.status}
                                  </span>
                            </div>
                            <c:if test="${not empty appointment.note}">
                                <div class="schedule-info"><i class="bi bi-journal-text"></i> Note: ${appointment.note}</div>
                            </c:if>
                        </div>
                        <c:if test="${appointment.status == 'pending'}">
                            <form action="StaffDashboardServlet" method="POST" style="display: inline;">
                                <input type="hidden" name="appointmentId" value="${appointment.appointmentId}">
                                <input type="hidden" name="action" value="confirm">
                                <button type="submit" class="confirm-btn" onclick="return confirm('Are you sure you want to confirm this appointment?');"><i class="bi bi-check-circle"></i> Confirm</button>
                            </form>
                            <form action="StaffDashboardServlet" method="POST" style="display: inline;">
                                <input type="hidden" name="appointmentId" value="${appointment.appointmentId}">
                                <input type="hidden" name="action" value="cancel">
                                <button type="submit" class="cancel-btn" onclick="return confirm('Are you sure you want to cancel this appointment?');"><i class="bi bi-x-circle"></i> Cancel</button>
                            </form>
                        </c:if>
                    </div>
                </c:forEach>

                <c:if test="${empty requestScope.appointments}">
                    <div class="appointment-card" style="text-align: center; padding: 40px 20px;">
                        <i class="bi bi-calendar-x" style="font-size: 48px; color: #d1d5db; display: block; margin-bottom: 16px;"></i>
                        <p style="font-size: 18px; color: #6b7280;">No appointments found.</p>
                    </div>
                </c:if>

                <c:if test="${not empty requestScope.appointments}">
                    <div class="pagination">
                        <c:if test="${requestScope.currentPage > 1}">
                            <a href="StaffDashboardServlet?page=${requestScope.currentPage - 1}&statusFilter=${param.statusFilter}&searchPatient=${param.searchPatient}&searchDoctor=${param.searchDoctor}&dateFilter=${param.dateFilter}">
                                <i class="bi bi-chevron-left"></i> Previous
                            </a>
                        </c:if>
                        <c:forEach begin="1" end="${requestScope.totalPages}" var="i">
                            <a href="StaffDashboardServlet?page=${i}&statusFilter=${param.statusFilter}&searchPatient=${param.searchPatient}&searchDoctor=${param.searchDoctor}&dateFilter=${param.dateFilter}" 
                               class="${i == requestScope.currentPage ? 'active' : ''}">${i}</a>
                        </c:forEach>
                        <c:if test="${requestScope.currentPage < requestScope.totalPages}">
                            <a href="StaffDashboardServlet?page=${requestScope.currentPage + 1}&statusFilter=${param.statusFilter}&searchPatient=${param.searchPatient}&searchDoctor=${param.searchDoctor}&dateFilter=${param.dateFilter}">
                                Next <i class="bi bi-chevron-right"></i>
                            </a>
                        </c:if>
                    </div>
                </c:if>
            </div>
        </body>
    </html>