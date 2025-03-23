package org.apache.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import model.User;

public final class HomePage_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.List<String> _jspx_dependants;

  private org.glassfish.jsp.api.ResourceInjector _jspx_resourceInjector;

  public java.util.List<String> getDependants() {
    return _jspx_dependants;
  }

  public void _jspService(HttpServletRequest request, HttpServletResponse response)
        throws java.io.IOException, ServletException {

    PageContext pageContext = null;
    HttpSession session = null;
    ServletContext application = null;
    ServletConfig config = null;
    JspWriter out = null;
    Object page = this;
    JspWriter _jspx_out = null;
    PageContext _jspx_page_context = null;

    try {
      response.setContentType("text/html;charset=UTF-8");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;
      _jspx_resourceInjector = (org.glassfish.jsp.api.ResourceInjector) application.getAttribute("com.sun.appserv.jsp.resource.injector");

      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("<!DOCTYPE html>\n");
      out.write("<html>\n");
      out.write("<head>\n");
      out.write("    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n");
      out.write("    <title>Home Page</title>\n");
      out.write("    <link rel=\"stylesheet\" href=\"");
      out.print( request.getContextPath() );
      out.write("/styles/HomePage.css\"/>\n");
      out.write("</head>\n");
      out.write("<body>\n");
      out.write("    ");
      org.apache.jasper.runtime.JspRuntimeLibrary.include(request, response, "header.jsp", out, false);
      out.write("\n");
      out.write("\n");
      out.write("    <!-- Banner Image -->\n");
      out.write("    <section class=\"banner\">\n");
      out.write("        <div class=\"container\">\n");
      out.write("            <img src=\"");
      out.print( request.getContextPath() );
      out.write("/img/slider.jpg\" alt=\"Medical Services\" class=\"banner-img\">\n");
      out.write("            <div class=\"banner-text\">\n");
      out.write("                <h1>We Provide <span>Medical</span> Services That You Can <span>Trust!</span></h1>\n");
      out.write("                <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Mauris sed nisl pellentesque, faucibus libero eu, gravida quam.</p>\n");
      out.write("                <div class=\"button\">\n");
      out.write("                    <form id =\"form1\" action=\"BookAppointmentServlet\" method=\"GET\">\n");
      out.write("                        <button type=\"submit\" class=\"btn\">Get Appointment</button>\n");
      out.write("                    </form>\n");
      out.write("                    ");
 
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
                    
      out.write("\n");
      out.write("                    \n");
      out.write("                    <form action=\"");
      out.print( dashboardUrl );
      out.write("\" method=\"GET\">\n");
      out.write("                        <button type=\"submit\" class=\"btn primary\">Go to Dashboard</button>\n");
      out.write("                    </form>\n");
      out.write("                    ");
 } else { 
      out.write("\n");
      out.write("                    <a href=\"#\" class=\"btn primary\">Learn More</a>\n");
      out.write("                    ");
 } 
      out.write("\n");
      out.write("                </div>\n");
      out.write("            </div>\n");
      out.write("        </div>\n");
      out.write("    </section>\n");
      out.write("    <!--/ End Banner Image -->\n");
      out.write("\n");
      out.write("    <!-- Info Cards Section -->\n");
      out.write("    <section class=\"info-cards\">\n");
      out.write("        <div class=\"container\">\n");
      out.write("            <div class=\"info-card\">\n");
      out.write("                <h2 class=\"card-title\">Our Doctor</h2>\n");
      out.write("                <p class=\"card-text\">Lorem ipsum sit amet consectetur adipiscing elit. Vivamus et erat in lacus convallis sodales.</p>\n");
      out.write("                <a href=\"#\" class=\"card-link\">LEARN MORE \n");
      out.write("                    <svg xmlns=\"http://www.w3.org/2000/svg\" width=\"24\" height=\"24\" viewBox=\"0 0 24 24\" fill=\"none\" stroke=\"currentColor\" stroke-width=\"2\" stroke-linecap=\"round\" stroke-linejoin=\"round\">\n");
      out.write("                        <line x1=\"5\" y1=\"12\" x2=\"19\" y2=\"12\"></line>\n");
      out.write("                        <polyline points=\"12 5 19 12 12 19\"></polyline>\n");
      out.write("                    </svg>\n");
      out.write("                </a>\n");
      out.write("            </div>\n");
      out.write("\n");
      out.write("            <div class=\"info-card\">\n");
      out.write("                <h2 class=\"card-title\">Schedule</h2>\n");
      out.write("                <p class=\"card-text\">Lorem ipsum sit amet consectetur adipiscing elit. Vivamus et erat in lacus convallis sodales.</p>\n");
      out.write("                <a href=\"#\" class=\"card-link\">LEARN MORE \n");
      out.write("                    <svg xmlns=\"http://www.w3.org/2000/svg\" width=\"24\" height=\"24\" viewBox=\"0 0 24 24\" fill=\"none\" stroke=\"currentColor\" stroke-width=\"2\" stroke-linecap=\"round\" stroke-linejoin=\"round\">\n");
      out.write("                        <line x1=\"5\" y1=\"12\" x2=\"19\" y2=\"12\"></line>\n");
      out.write("                        <polyline points=\"12 5 19 12 12 19\"></polyline>\n");
      out.write("                    </svg>\n");
      out.write("                </a>\n");
      out.write("            </div>\n");
      out.write("\n");
      out.write("            <div class=\"info-card\">\n");
      out.write("                <h2 class=\"card-title\">Medical History</h2>\n");
      out.write("                <table class=\"hours-table\">\n");
      out.write("                    <tr>\n");
      out.write("                        <td>Monday - Friday</td>\n");
      out.write("                        <td>8.00-20.00</td>\n");
      out.write("                    </tr>\n");
      out.write("                    <tr>\n");
      out.write("                        <td>Saturday</td>\n");
      out.write("                        <td>9.00-18.30</td>\n");
      out.write("                    </tr>\n");
      out.write("                    <tr>\n");
      out.write("                        <td>Monday - Thursday</td>\n");
      out.write("                        <td>9.00-15.00</td>\n");
      out.write("                    </tr>\n");
      out.write("                </table>\n");
      out.write("                <a href=\"#\" class=\"card-link\">LEARN MORE \n");
      out.write("                    <svg xmlns=\"http://www.w3.org/2000/svg\" width=\"24\" height=\"24\" viewBox=\"0 0 24 24\" fill=\"none\" stroke=\"currentColor\" stroke-width=\"2\" stroke-linecap=\"round\" stroke-linejoin=\"round\">\n");
      out.write("                        <line x1=\"5\" y1=\"12\" x2=\"19\" y2=\"12\"></line>\n");
      out.write("                        <polyline points=\"12 5 19 12 12 19\"></polyline>\n");
      out.write("                    </svg>\n");
      out.write("                </a>\n");
      out.write("            </div>\n");
      out.write("        </div>\n");
      out.write("    </section>\n");
      out.write("\n");
      out.write("    <!-- About Us Section -->\n");
      out.write("    <section class=\"about-section\">\n");
      out.write("        <div class=\"container\">\n");
      out.write("            <div class=\"about-content\">\n");
      out.write("                <div class=\"about-text\">\n");
      out.write("                    <div class=\"section-header\">\n");
      out.write("                        <span class=\"subtitle\">About Us</span>\n");
      out.write("                        <h2>We Are Always Ready To Help You & Your Family</h2>\n");
      out.write("                    </div>\n");
      out.write("                    <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec sollicitudin molestie malesuada. Praesent sapien massa, convallis a pellentesque nec, egestas non nisi.</p>\n");
      out.write("                    <p>Vivamus suscipit tortor eget felis porttitor volutpat. Sed porttitor lectus nibh.</p>\n");
      out.write("                    <div class=\"stats-row\">\n");
      out.write("                        <div class=\"stat-item\">\n");
      out.write("                            <h3>99%</h3>\n");
      out.write("                            <p>Patient Satisfaction</p>\n");
      out.write("                        </div>\n");
      out.write("                        <div class=\"stat-item\">\n");
      out.write("                            <h3>15+</h3>\n");
      out.write("                            <p>Years of Experience</p>\n");
      out.write("                        </div>\n");
      out.write("                        <div class=\"stat-item\">\n");
      out.write("                            <h3>50+</h3>\n");
      out.write("                            <p>Qualified Doctors</p>\n");
      out.write("                        </div>\n");
      out.write("                    </div>\n");
      out.write("                    <a href=\"#\" class=\"btn\">Learn More</a>\n");
      out.write("                </div>\n");
      out.write("                <div class=\"about-image\">\n");
      out.write("                    <img src=\"");
      out.print( request.getContextPath() );
      out.write("/img/slider3.jpg\" alt=\"Our Medical Team\">\n");
      out.write("                </div>\n");
      out.write("            </div>\n");
      out.write("        </div>\n");
      out.write("    </section>\n");
      out.write("\n");
      out.write("    <!-- Services Section -->\n");
      out.write("    <section class=\"services-section\">\n");
      out.write("        <div class=\"container\">\n");
      out.write("            <div class=\"section-header text-center\">\n");
      out.write("                <span class=\"subtitle\">Our Services</span>\n");
      out.write("                <h2>What We Provide</h2>\n");
      out.write("                <p>We provide quality healthcare services for you and your family</p>\n");
      out.write("            </div>\n");
      out.write("\n");
      out.write("            <div class=\"services-grid\">\n");
      out.write("                <div class=\"service-card\">\n");
      out.write("                    <div class=\"service-icon\">\n");
      out.write("                        <img src=\"");
      out.print( request.getContextPath() );
      out.write("/img/icons/cardiology.png\" alt=\"Cardiology\">\n");
      out.write("                    </div>\n");
      out.write("                    <h3>Cardiology</h3>\n");
      out.write("                    <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit.</p>\n");
      out.write("                </div>\n");
      out.write("\n");
      out.write("                <div class=\"service-card\">\n");
      out.write("                    <div class=\"service-icon\">\n");
      out.write("                        <img src=\"");
      out.print( request.getContextPath() );
      out.write("/img/icons/laboratory.png\" alt=\"Laboratory\">\n");
      out.write("                    </div>\n");
      out.write("                    <h3>Laboratory</h3>\n");
      out.write("                    <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit.</p>\n");
      out.write("                </div>\n");
      out.write("\n");
      out.write("                <div class=\"service-card\">\n");
      out.write("                    <div class=\"service-icon\">\n");
      out.write("                        <img src=\"");
      out.print( request.getContextPath() );
      out.write("/img/icons/emergency.png\" alt=\"Emergency\">\n");
      out.write("                    </div>\n");
      out.write("                    <h3>Emergency</h3>\n");
      out.write("                    <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit.</p>\n");
      out.write("                </div>\n");
      out.write("            </div>\n");
      out.write("        </div>\n");
      out.write("    </section>\n");
      out.write("\n");
      out.write("    <!-- Testimonials Section -->\n");
      out.write("    <section class=\"testimonials-section\">\n");
      out.write("        <div class=\"container\">\n");
      out.write("            <div class=\"section-header text-center\">\n");
      out.write("                <span class=\"subtitle\">Testimonials</span>\n");
      out.write("                <h2>What Our Patients Say</h2>\n");
      out.write("            </div>\n");
      out.write("\n");
      out.write("            <div class=\"testimonials-slider\">\n");
      out.write("                <div class=\"testimonial-card\">\n");
      out.write("                    <div class=\"testimonial-content\">\n");
      out.write("                        <p>\"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus magna justo.\"</p>\n");
      out.write("                    </div>\n");
      out.write("                    <div class=\"testimonial-author\">\n");
      out.write("                        <img src=\"");
      out.print( request.getContextPath() );
      out.write("/img/patient.jpg\" alt=\"Patient\">\n");
      out.write("                        <div class=\"author-info\">\n");
      out.write("                            <h4>John Doe</h4>\n");
      out.write("                            <p>Cardiology Patient</p>\n");
      out.write("                        </div>\n");
      out.write("                    </div>\n");
      out.write("                </div>\n");
      out.write("\n");
      out.write("                <div class=\"testimonial-card\">\n");
      out.write("                    <div class=\"testimonial-content\">\n");
      out.write("                        <p>\"Proin eget tortor risus. Curabitur non nulla sit amet nisl tempus.\"</p>\n");
      out.write("                    </div>\n");
      out.write("                    <div class=\"testimonial-author\">\n");
      out.write("                        <img src=\"");
      out.print( request.getContextPath() );
      out.write("/img/patient2.jpg\" alt=\"Patient\">\n");
      out.write("                        <div class=\"author-info\">\n");
      out.write("                            <h4>Jane Smith</h4>\n");
      out.write("                            <p>Dental Patient</p>\n");
      out.write("                        </div>\n");
      out.write("                    </div>\n");
      out.write("                </div>\n");
      out.write("            </div>\n");
      out.write("        </div>\n");
      out.write("    </section>\n");
      out.write("\n");
      out.write("    ");
      org.apache.jasper.runtime.JspRuntimeLibrary.include(request, response, "footer.jsp", out, false);
      out.write("\n");
      out.write("</body>\n");
      out.write("</html>");
    } catch (Throwable t) {
      if (!(t instanceof SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          out.clearBuffer();
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
        else throw new ServletException(t);
      }
    } finally {
      _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }
}
