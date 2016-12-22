import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

class A extends HttpServlet {
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    HttpSession s = req.getSession(true);
    s.setMaxInactiveInterval(-1); // Noncompliant [[sc=5;ec=33]]
  }
}
class B extends HttpServlet {
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    HttpSession s = req.getSession(true);
    s.setMaxInactiveInterval(new Integer(-1)); // Noncompliant [[sc=5;ec=46]]
  }
}
class C extends HttpServlet {
  Integer expirationTime = -1;
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    HttpSession s = req.getSession(true);
    s.setMaxInactiveInterval(expirationTime); // Noncompliant [[sc=5;ec=45]]
  }
}
class D extends HttpServlet {
  int expirationTime = -1;
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    HttpSession s = req.getSession(true);
    s.setMaxInactiveInterval(expirationTime); // Noncompliant [[sc=5;ec=45]]
  }
}
class E extends HttpServlet {
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    HttpSession s = req.getSession(true);
    s.setMaxInactiveInterval(100); // Compliant
  }
}
class F extends HttpServlet {
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    HttpSession s = req.getSession(true);
    s.setMaxInactiveInterval(1); // Compliant
  }
}
class G extends HttpServlet {
  Integer expirationTime = 1;
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    HttpSession s = req.getSession(true);
    s.setMaxInactiveInterval(expirationTime); // Compliant
  }
}
class H extends HttpServlet {
  int expirationTime = 1;
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    HttpSession s = req.getSession(true);
    s.setMaxInactiveInterval(expirationTime); // Compliant
  }
}
class I extends HttpServlet {
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    HttpSession s = req.getSession(true);
    s.setMaxInactiveInterval(new Integer(1)); // Compliant
  }
}