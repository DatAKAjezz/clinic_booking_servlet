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
            <h1 class="page-title">Quản Lý Lịch Hẹn</h1>
            <div class="user-info">Đăng nhập với vai trò: ${sessionScope.USER.username} (Lễ tân)</div>
            <button class="refresh-btn" onclick="window.location.href = 'StaffDashboardServlet'"><i class="bi bi-arrow-clockwise"></i> Làm mới</button>
            <button class="today-btn" onclick="window.location.href = 'StaffDashboardServlet?today=true'"><i class="bi bi-calendar-day"></i> Lịch hẹn hôm nay</button>
            <button class="add-btn" onclick="window.location.href = 'StaffDashboardServlet?action=add'"><i class="bi bi-plus-circle"></i> Thêm Lịch Hẹn</button>
        </div>

        <!-- Form chỉnh sửa lịch hẹn -->
        <c:if test="${not empty editAppointment}">
            <div class="appointment-form">
                <h2>Chỉnh Sửa Lịch Hẹn</h2>
                <form action="StaffDashboardServlet" method="POST">
                    <input type="hidden" name="action" value="edit">
                    <input type="hidden" name="appointmentId" value="${editAppointment.appointmentId}">
                    <div class="form-group">
                        <label>ID Bệnh Nhân (nếu có):</label>
                        <input type="number" name="patientId" value="${editAppointment.patientId}" placeholder="Nhập ID bệnh nhân (tùy chọn)">
                    </div>
                    <div class="form-group">
                        <label>Tên Khách Vãng Lai:</label>
                        <input type="text" name="guestName" value="${editAppointment.guestName}" placeholder="Nhập tên khách (nếu không có tài khoản)">
                    </div>
                    <div class="form-group">
                        <label>Số Điện Thoại Khách:</label>
                        <input type="text" name="guestPhone" value="${editAppointment.guestPhone}" placeholder="Nhập số điện thoại khách">
                    </div>
                    <div class="form-group">
                        <label>Chọn Bác Sĩ:</label>
                        <select name="doctorId" required>
                            <c:forEach var="doctor" items="${doctors}">
                                <option value="${doctor.doctorId}" ${doctor.doctorId eq editAppointment.doctorId ? 'selected' : ''}>
                                    ${doctor.fullName} - ${doctor.specialty}
                                </option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="form-group">
                        <label>Chọn Khung Giờ:</label>
                        <select name="scheduleId" required>
                            <c:forEach var="schedule" items="${schedules}">
                                <option value="${schedule.scheduleId}" ${schedule.scheduleId eq editAppointment.scheduleId ? 'selected' : ''}>
                                    ${schedule.startTime} - ${schedule.endTime}
                                </option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="form-group">
                        <label>Chọn Dịch Vụ:</label>
                        <select name="serviceId" required>
                            <c:forEach var="service" items="${services}">
                                <option value="${service.serviceId}" ${service.serviceId eq editAppointment.serviceId ? 'selected' : ''}>
                                    ${service.name}
                                </option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="form-group">
                        <label>Ngày Khám:</label>
                        <input type="date" name="appointmentDate" value="${editAppointment.appointmentDate}" required>
                    </div>
                    <div class="form-group">
                        <label>Lý Do Khám:</label>
                        <input type="text" name="reason" value="${editAppointment.reason}" placeholder="Nhập lý do khám" required>
                    </div>
                    <button type="submit" class="submit-btn"><i class="bi bi-pencil-square"></i> Cập Nhật Lịch Hẹn</button>
                </form>
            </div>
        </c:if>

        <!-- Thông báo -->
        <c:if test="${not empty sessionScope.message}">
            <div class="message success"><i class="bi bi-check-circle"></i> ${sessionScope.message}</div>
            <% session.removeAttribute("message"); %>
        </c:if>
        <c:if test="${not empty sessionScope.error}">
            <div class="message error"><i class="bi bi-exclamation-circle"></i> ${sessionScope.error}</div>
            <% session.removeAttribute("error"); %>
        </c:if>

        <!-- Bộ lọc và tìm kiếm -->
        <div class="search-bar">
            <form action="StaffDashboardServlet" method="GET">
                <input type="text" name="searchPatient" placeholder="Tìm theo tên bệnh nhân hoặc khách" value="${param.searchPatient}">
                <input type="text" name="searchDoctor" placeholder="Tìm theo tên bác sĩ" value="${param.searchDoctor}">
                <button type="submit"><i class="bi bi-search"></i> Tìm kiếm</button>
                <input type="hidden" name="statusFilter" value="${param.statusFilter}">
                <input type="hidden" name="dateFilter" value="${param.dateFilter}">
                <input type="hidden" name="page" value="1">
            </form>
        </div>

        <div class="filter-section">
            <form action="StaffDashboardServlet" method="GET">
                <label for="statusFilter">Lọc theo trạng thái: </label>
                <select name="statusFilter" id="statusFilter" onchange="this.form.submit()">
                    <option value="">Tất cả</option>
                    <option value="pending" ${param.statusFilter == 'pending' ? 'selected' : ''}>Đang chờ</option>
                    <option value="confirmed" ${param.statusFilter == 'confirmed' ? 'selected' : ''}>Đã xác nhận</option>
                    <option value="canceled" ${param.statusFilter == 'canceled' ? 'selected' : ''}>Đã hủy</option>
                    <option value="completed" ${param.statusFilter == 'completed' ? 'selected' : ''}>Hoàn thành</option>
                </select>
                <label for="dateFilter" style="margin-left: 20px;">Lọc theo ngày: </label>
                <input type="date" name="dateFilter" id="dateFilter" value="${param.dateFilter}" onchange="this.form.submit()">
                <input type="hidden" name="searchPatient" value="${param.searchPatient}">
                <input type="hidden" name="searchDoctor" value="${param.searchDoctor}">
                <input type="hidden" name="page" value="1">
            </form>
        </div>

        <!-- Danh sách lịch hẹn -->
        <div class="appointments-count"><i class="bi bi-calendar-check"></i> Số lịch hẹn (${requestScope.appointments != null ? requestScope.appointments.size() : 0})</div>
        <c:forEach var="appointment" items="${requestScope.appointments}">
            <div class="appointment-card">
                <div class="session-title"><i class="bi bi-clipboard-pulse"></i> ${appointment.serviceName}</div>
                <div class="appointment-id">ID Lịch Hẹn: ${appointment.appointmentId}</div>
                <div class="patient-info">
                    <div class="patient-name"><i class="bi bi-person-circle"></i> Bệnh Nhân: 
                        <c:choose>
                            <c:when test="${not empty appointment.patientName}">${appointment.patientName}</c:when>
                            <c:otherwise>${appointment.guestName} (${appointment.guestPhone})</c:otherwise>
                        </c:choose>
                    </div>
                    <div class="doctor-name"><i class="bi bi-person-badge"></i> Bác Sĩ: ${appointment.doctorName}</div>
                    <div class="schedule-info"><i class="bi bi-calendar-date"></i> Ngày: ${appointment.appointmentDate}</div>
                    <div class="schedule-info"><i class="bi bi-chat-left-text"></i> Lý Do: ${appointment.reason}</div>
                    <div class="schedule-info"><i class="bi bi-tag"></i> Trạng Thái: 
                        <span class="info-text ${appointment.status == 'pending' ? 'text-warning' : 
                                                 appointment.status == 'confirmed' ? 'text-primary' : 
                                                 appointment.status == 'completed' ? 'text-success' : 
                                                 appointment.status == 'canceled' ? 'text-danger' : ''}">
                            ${appointment.status}
                        </span>
                    </div>
                    <c:if test="${not empty appointment.note}">
                        <div class="schedule-info"><i class="bi bi-journal-text"></i> Ghi Chú: ${appointment.note}</div>
                    </c:if>
                </div>
                <c:if test="${appointment.status == 'pending'}">
                    <form action="StaffDashboardServlet" method="POST" style="display: inline;">
                        <input type="hidden" name="appointmentId" value="${appointment.appointmentId}">
                        <input type="hidden" name="action" value="confirm">
                        <button type="submit" class="confirm-btn" onclick="return confirm('Bạn có chắc muốn xác nhận lịch hẹn này?');"><i class="bi bi-check-circle"></i> Xác Nhận</button>
                    </form>
                    <form action="StaffDashboardServlet" method="POST" style="display: inline;">
                        <input type="hidden" name="appointmentId" value="${appointment.appointmentId}">
                        <input type="hidden" name="action" value="cancel">
                        <button type="submit" class="cancel-btn" onclick="return confirm('Bạn có chắc muốn hủy lịch hẹn này?');"><i class="bi bi-x-circle"></i> Hủy</button>
                    </form>
                </c:if>
                <a href="StaffDashboardServlet?editId=${appointment.appointmentId}" class="edit-btn"><i class="bi bi-pencil-square"></i> Chỉnh Sửa</a>
            </div>
        </c:forEach>

        <c:if test="${empty requestScope.appointments}">
            <div class="appointment-card" style="text-align: center; padding: 40px 20px;">
                <i class="bi bi-calendar-x" style="font-size: 48px; color: #d1d5db; display: block; margin-bottom: 16px;"></i>
                <p style="font-size: 18px; color: #6b7280;">Không tìm thấy lịch hẹn nào.</p>
            </div>
        </c:if>

        <!-- Phân trang -->
        <c:if test="${not empty requestScope.appointments}">
            <div class="pagination">
                <c:if test="${requestScope.currentPage > 1}">
                    <a href="StaffDashboardServlet?page=${requestScope.currentPage - 1}&statusFilter=${param.statusFilter}&searchPatient=${param.searchPatient}&searchDoctor=${param.searchDoctor}&dateFilter=${param.dateFilter}">
                        <i class="bi bi-chevron-left"></i> Trước
                    </a>
                </c:if>
                <c:forEach begin="1" end="${requestScope.totalPages}" var="i">
                    <a href="StaffDashboardServlet?page=${i}&statusFilter=${param.statusFilter}&searchPatient=${param.searchPatient}&searchDoctor=${param.searchDoctor}&dateFilter=${param.dateFilter}" 
                       class="${i == requestScope.currentPage ? 'active' : ''}">${i}</a>
                </c:forEach>
                <c:if test="${requestScope.currentPage < requestScope.totalPages}">
                    <a href="StaffDashboardServlet?page=${requestScope.currentPage + 1}&statusFilter=${param.statusFilter}&searchPatient=${param.searchPatient}&searchDoctor=${param.searchDoctor}&dateFilter=${param.dateFilter}">
                        Tiếp <i class="bi bi-chevron-right"></i>
                    </a>
                </c:if>
            </div>
        </c:if>
    </div>
</body>
</html>