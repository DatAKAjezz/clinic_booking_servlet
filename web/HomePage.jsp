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
        <link rel="stylesheet" href="<%=request.getContextPath()%>/styles/HomePage.css"/>
        <!-- Thêm EmailJS SDK -->
        <script type="text/javascript" src="https://cdn.jsdelivr.net/npm/@emailjs/browser@4/dist/email.min.js"></script>
        <script type="text/javascript">
            (function() {
                // Khởi tạo EmailJS với Public Key của bạn
                emailjs.init("yIDjG3dEiU-ztXmd1"); // Thay YOUR_PUBLIC_KEY bằng Public Key từ EmailJS
            })();
        </script>
    </head>
    <body>
        <jsp:include page="header.jsp" />

        <!-- Banner Image -->
        <section class="banner">
            <div class="container">
                <img src="<%= request.getContextPath()%>/img/slider.jpg" alt="Medical Services" class="banner-img">
                <div class="banner-text">
                    <h1>We Provide <span>Medical</span> Services That You Can <span>Trust!</span></h1>
                    <div class="button">
                        <form id="form1" action="BookAppointmentServlet" method="GET">
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

        <!-- Info Cards Section -->
        <section class="info-cards">
            <div class="container">
                <div class="info-card">
                    <h2 class="card-title">Our Doctor</h2>
                    <p class="card-text">We have a team of experienced and dedicated doctors ready to assist you.</p>
                    <a href="DoctorListServlet" class="card-link">LEARN MORE 
                        <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                        <line x1="5" y1="12" x2="19" y2="12"></line>
                        <polyline points="12 5 19 12 12 19"></polyline>
                        </svg>
                    </a>
                </div>
                <div class="info-card">
                    <h2 class="card-title">Schedule</h2>
                    <p class="card-text">Flexible appointment scheduling to fit your busy lifestyle.</p>
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
                        <tr><td>Monday - Friday</td><td>8.00-20.00</td></tr>
                        <tr><td>Saturday</td><td>9.00-18.30</td></tr>
                        <tr><td>Monday - Thursday</td><td>9.00-15.00</td></tr>
                    </table>
                    <a href="MedicalHistoryServlet" class="card-link">LEARN MORE 
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
                        <p>At our core, we are committed to providing exceptional care and support for you and your loved ones.</p>
                        <p>With compassion and expertise, we strive to make every visit a positive experience.</p>
                        <div class="stats-row">
                            <div class="stat-item"><h3>99%</h3><p>Patient Satisfaction</p></div>
                            <div class="stat-item"><h3>5+</h3><p>Years of Experience</p></div>
                            <div class="stat-item"><h3>50+</h3><p>Qualified Doctors</p></div>
                        </div>
                    </div>
                    <div class="about-image">
                        <img src="<%= request.getContextPath()%>/img/slider3.jpg" alt="Our Medical Team">
                    </div>
                </div>
            </div>
        </section>

        <!-- Contact Us Section -->
        <section class="contact-section">
            <div class="container">
                <div class="section-header text-center">
                    <span class="subtitle">Contact Us</span>
                    <h2>Get In Touch</h2>
                    <p>Send us a message and we'll get back to you as soon as possible!</p>
                </div>
                <form id="contactForm" class="contact-form">
                    <div class="form-group">
                        <label for="name">Name</label>
                        <input type="text" id="name" name="name" required>
                    </div>
                    <div class="form-group">
                        <label for="email">Email</label>
                        <input type="email" id="email" name="email" required>
                    </div>
                    <div class="form-group">
                        <label for="message">Message</label>
                        <textarea id="message" name="message" rows="5" required></textarea>
                    </div>
                    <button type="submit" class="btn">Send Message</button>
                </form>
                <div id="formMessage" style="margin-top: 20px; text-align: center;"></div>
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
                        <div class="service-icon"><img src="<%= request.getContextPath()%>/img/cardiology.jpg" alt="Cardiology"></div>
                        <h3>Cardiology</h3>
                        <p>Expert heart care from diagnosis to treatment and recovery.</p>
                    </div>
                    <div class="service-card">
                        <div class="service-icon"><img src="<%= request.getContextPath()%>/img/laboratory.jpeg" alt="Laboratory"></div>
                        <h3>Laboratory</h3>
                        <p>Advanced diagnostic testing for accurate and timely results.</p>
                    </div>
                    <div class="service-card">
                        <div class="service-icon"><img src="<%= request.getContextPath()%>/img/emergency.jpg" alt="Emergency"></div>
                        <h3>Emergency</h3>
                        <p>24/7 emergency care to handle urgent medical situations.</p>
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
                        <div class="testimonial-content"><p>"The care I received was exceptional. The staff made me feel comfortable and well-informed throughout my treatment."</p></div>
                        <div class="testimonial-author">
                            <img src="<%= request.getContextPath()%>/img/patient.jpg" alt="Patient">
                            <div class="author-info"><h4>John Doe</h4><p>Cardiology Patient</p></div>
                        </div>
                    </div>
                    <div class="testimonial-card">
                        <div class="testimonial-content"><p>"The doctors here are incredibly skilled and caring. I’m grateful for their support during my recovery."</p></div>
                        <div class="testimonial-author">
                            <img src="<%= request.getContextPath()%>/img/patient2.jpg" alt="Patient">
                            <div class="author-info"><h4>Jane Smith</h4><p>Dental Patient</p></div>
                        </div>
                    </div>
                </div>
            </div>
        </section>

        <jsp:include page="footer.jsp" />

        <script type="text/javascript">
            document.getElementById('contactForm').addEventListener('submit', function(event) {
                event.preventDefault();

                const formData = {
                    name: document.getElementById('name').value,
                    email: document.getElementById('email').value,
                    message: document.getElementById('message').value
                };

                emailjs.send('service_2m13k58', 'template_z5cfgci', formData)
                    .then(function(response) {
                        document.getElementById('formMessage').innerHTML = '<p style="color: green;">Message sent successfully!</p>';
                        document.getElementById('contactForm').reset();
                    }, function(error) {
                        document.getElementById('formMessage').innerHTML = '<p style="color: red;">Failed to send message. Please try again.</p>';
                    });
            });
        </script>
    </body>
</html>