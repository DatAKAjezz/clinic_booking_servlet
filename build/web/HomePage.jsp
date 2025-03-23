<%-- 
    Document   : HomePage
    Created on : Feb 23, 2025, 5:06:08 PM
    Author     : PC
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="model.User"%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Home Page</title>
        <link rel="stylesheet" href="<%=request.getContextPath()%>/styles/header.css"/>

        <link rel="stylesheet" href="<%= request.getContextPath()%>/styles/HomePage.css"/>

        <style>
            body {
                background-color: green !important;
            }
        </style>

    </head>
    <body>
        <jsp:include page="header.jsp" />

        <!-- Banner Image -->
        <section class="banner">
            <div class="container">
                <img src="<%= request.getContextPath()%>/img/slider.jpg" alt="Medical Services" class="banner-img">
                <div class="banner-text">
                    <h1>We Provide <span>Medical</span> Services That You Can <span>Trust!</span></h1>
                    <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Mauris sed nisl pellentesque, faucibus libero eu, gravida quam.</p>
                    <div class="button">
                        <form id ="form1" action="BookAppointmentServlet" method="GET">
                            <button type="submit" class="btn">Get Appointment</button>
                        </form>
                        <%
                            User user = (User) session.getAttribute("USER");
                            if (user != null) {
                                String dashboardUrl = "";
                                switch (user.getRole()) {
                                    case "patient":
                                        dashboardUrl = "PatientDashboardServlet";
                                        break;
                                    case "doctor":
                                        dashboardUrl = "DoctorDashboardServlet";
                                        break;
                                    case "receptionist":
                                        dashboardUrl = "StaffDashboardServlet";
                                        break;
                                    default:
                                        dashboardUrl = "HomePage.jsp";
                                }
                        %>

                        <form action="<%= dashboardUrl%>" method="GET">
                            <button type="submit" class="btn primary">Go to Dashboard</button>
                        </form>
                        <% } else { %>
                        <a href="#" class="btn primary">Learn More</a>
                        <% }%>
                    </div>
                </div>
            </div>
        </section>
        <!--/ End Banner Image -->

        <!-- Info Cards Section -->
        <section class="info-cards">
            <div class="container">
                <div class="info-card">
                    <h2 class="card-title">Our Doctor</h2>
                    <p class="card-text">Lorem ipsum sit amet consectetur adipiscing elit. Vivamus et erat in lacus convallis sodales.</p>
                    <a href="#" class="card-link">LEARN MORE 
                        <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                        <line x1="5" y1="12" x2="19" y2="12"></line>
                        <polyline points="12 5 19 12 12 19"></polyline>
                        </svg>
                    </a>
                </div>

                <div class="info-card">
                    <h2 class="card-title">Schedule</h2>
                    <p class="card-text">Lorem ipsum sit amet consectetur adipiscing elit. Vivamus et erat in lacus convallis sodales.</p>
                    <a href="#" class="card-link">LEARN MORE 
                        <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                        <line x1="5" y1="12" x2="19" y2="12"></line>
                        <polyline points="12 5 19 12 12 19"></polyline>
                        </svg>
                    </a>
                </div>

                <div class="info-card">
                    <h2 class="card-title">Medical History</h2>
                    <table class="hours-table">
                        <tr>
                            <td>Monday - Friday</td>
                            <td>8.00-20.00</td>
                        </tr>
                        <tr>
                            <td>Saturday</td>
                            <td>9.00-18.30</td>
                        </tr>
                        <tr>
                            <td>Monday - Thursday</td>
                            <td>9.00-15.00</td>
                        </tr>
                    </table>
                    <a href="#" class="card-link">LEARN MORE 
                        <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                        <line x1="5" y1="12" x2="19" y2="12"></line>
                        <polyline points="12 5 19 12 12 19"></polyline>
                        </svg>
                    </a>
                </div>
            </div>
        </section>

        <!-- About Us Section -->
        <section class="about-section">
            <div class="container">
                <div class="about-content">
                    <div class="about-text">
                        <div class="section-header">
                            <span class="subtitle">About Us</span>
                            <h2>We Are Always Ready To Help You & Your Family</h2>
                        </div>
                        <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec sollicitudin molestie malesuada. Praesent sapien massa, convallis a pellentesque nec, egestas non nisi.</p>
                        <p>Vivamus suscipit tortor eget felis porttitor volutpat. Sed porttitor lectus nibh.</p>
                        <div class="stats-row">
                            <div class="stat-item">
                                <h3>99%</h3>
                                <p>Patient Satisfaction</p>
                            </div>
                            <div class="stat-item">
                                <h3>15+</h3>
                                <p>Years of Experience</p>
                            </div>
                            <div class="stat-item">
                                <h3>50+</h3>
                                <p>Qualified Doctors</p>
                            </div>
                        </div>
                        <a href="#" class="btn">Learn More</a>
                    </div>
                    <div class="about-image">
                        <img src="<%= request.getContextPath()%>/img/slider3.jpg" alt="Our Medical Team">
                    </div>
                </div>
            </div>
        </section>

        <!-- Services Section -->
        <section class="services-section">
            <div class="container">
                <div class="section-header text-center">
                    <span class="subtitle">Our Services</span>
                    <h2>What We Provide</h2>
                    <p>We provide quality healthcare services for you and your family</p>
                </div>

                <div class="services-grid">
                    <div class="service-card">
                        <div class="service-icon">
                            <img src="<%= request.getContextPath()%>/img/icons/cardiology.png" alt="Cardiology">
                        </div>
                        <h3>Cardiology</h3>
                        <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit.</p>
                    </div>

                    <div class="service-card">
                        <div class="service-icon">
                            <img src="<%= request.getContextPath()%>/img/icons/laboratory.png" alt="Laboratory">
                        </div>
                        <h3>Laboratory</h3>
                        <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit.</p>
                    </div>

                    <div class="service-card">
                        <div class="service-icon">
                            <img src="<%= request.getContextPath()%>/img/icons/emergency.png" alt="Emergency">
                        </div>
                        <h3>Emergency</h3>
                        <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit.</p>
                    </div>
                </div>
            </div>
        </section>

        <!-- Testimonials Section -->
        <section class="testimonials-section">
            <div class="container">
                <div class="section-header text-center">
                    <span class="subtitle">Testimonials</span>
                    <h2>What Our Patients Say</h2>
                </div>

                <div class="testimonials-slider">
                    <div class="testimonial-card">
                        <div class="testimonial-content">
                            <p>"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus magna justo."</p>
                        </div>
                        <div class="testimonial-author">
                            <img src="<%= request.getContextPath()%>/img/patient.jpg" alt="Patient">
                            <div class="author-info">
                                <h4>John Doe</h4>
                                <p>Cardiology Patient</p>
                            </div>
                        </div>
                    </div>

                    <div class="testimonial-card">
                        <div class="testimonial-content">
                            <p>"Proin eget tortor risus. Curabitur non nulla sit amet nisl tempus."</p>
                        </div>
                        <div class="testimonial-author">
                            <img src="<%= request.getContextPath()%>/img/patient2.jpg" alt="Patient">
                            <div class="author-info">
                                <h4>Jane Smith</h4>
                                <p>Dental Patient</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </section>

        <jsp:include page="footer.jsp" />
    </body>
</html>