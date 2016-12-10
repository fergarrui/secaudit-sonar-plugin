import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServlet;

class C extends HttpServlet {
  protected void doGet(HttpServletRequest request, HttpServletResponse response, String urlParam) throws ServletException, IOException {
    String url = request.getParameter("url");
    String url2 = request.getParameter("url2");
    if ( whatever() ) {
      response.sendRedirect(url); // Noncompliant [[sc=7;ec=33]]
    }
    if (whatever2()) {
      response.sendRedirect(url2); // Noncompliant [[sc=7;ec=34]]
    }
    if (whatever3()) {
      response.sendRedirect(urlParam); // Noncompliant [[sc=7;ec=38]]
    }
    if (whatever4()) {
      response.sendRedirect("path/" + urlParam); // Noncompliant [[sc=7;ec=48]]
    }
    if (whatever6()) {
      response.sendRedirect(getUrl()); // Noncompliant [[sc=7;ec=38]]
    }
    if (whatever7()) {
      response.sendRedirect("web"); // Compliant
    }
  }

  private String getUrl() {

  }
}