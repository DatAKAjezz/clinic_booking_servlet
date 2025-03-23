package org.apache.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class LoginPage_jsp extends org.apache.jasper.runtime.HttpJspBase
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
      response.setContentType("text/html; charset=UTF-8");
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
      out.write("<!DOCTYPE html>\n");
      out.write("<html>\n");
      out.write("<head>\n");
      out.write("    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n");
      out.write("    <title>Login</title>\n");
      out.write("    <link rel=\"stylesheet\" href=\"");
      out.print( request.getContextPath());
      out.write("/styles/LoginPage.css\"/>\n");
      out.write("</head>\n");
      out.write("<body>\n");
      out.write("    <div class=\"login-container\">\n");
      out.write("        <h1 class=\"title\">Login</h1>\n");
      out.write("        <form action=\"MainServlet\" method=\"post\" accept-charset=\"UTF-8\" class=\"form\">\n");
      out.write("            <input type=\"hidden\" name=\"btnAction\" value=\"loginServlet\"/>\n");
      out.write("            <div class=\"input-group\">\n");
      out.write("                <label>Username:</label>\n");
      out.write("                <input type=\"text\" name=\"txtusername\" class=\"input\" \n");
      out.write("                       value=\"");
      out.print( request.getParameter("txtusername") != null ? request.getParameter("txtusername") : "");
      out.write("\" \n");
      out.write("                       required>\n");
      out.write("            </div>\n");
      out.write("            <div class=\"input-group\">\n");
      out.write("                <label>Password:</label>\n");
      out.write("                <input type=\"password\" name=\"txtpassword\" class=\"input\" required>\n");
      out.write("            </div>\n");
      out.write("            ");
 
                String error = (String) request.getAttribute("ERROR");
                if (error != null) {
            
      out.write("\n");
      out.write("                <p class=\"message\" style=\"color: red; text-align: center;\">");
      out.print( error );
      out.write("</p>\n");
      out.write("            ");
 } 
      out.write("\n");
      out.write("            <button type=\"submit\" class=\"button\">Login</button>\n");
      out.write("        </form>\n");
      out.write("        <a class=\"sign-up\" href=\"MainServlet?btnAction=showRegister\">Don't have an account? Sign up here</a>\n");
      out.write("    </div>\n");
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
