import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

class A extends HttpServlet {
  public void doGet(HttpServletRequest request, HttpServletResponse response) {
    String header = request.getHeader("AnyHeader");// Noncompliant [[sc=21;ec=51]]
    request.getCookies(); // Noncompliant [[sc=5;ec=25]]
  }
}
class B extends HttpServlet {
  public void doGet(HttpServletRequest request, HttpServletResponse response) {
    doSomething(request.getHeader("AnyHeader")); // Noncompliant [[sc=17;ec=47]]
    request.getHeader("AnotherHeader");// Noncompliant [[sc=5;ec=39]]
    doSomething(request.getCookies()); // Noncompliant [[sc=17;ec=37]]

  }
}
