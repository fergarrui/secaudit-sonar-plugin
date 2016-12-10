import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

class A extends HttpServlet {

  Cookie attr = new Cookie("a","b"); // Noncompliant [[sc=17;ec=36]]
  Cookie attr2;
  String s = new String("a");

  public A() {
    attr2 = new Cookie("a","b"); // Noncompliant [[sc=13;ec=32]]
  }

  public void doGet(HttpServletRequest request, HttpServletResponse response) {
    Cookie a = new Cookie("a", "b"); // Noncompliant [[sc=16;ec=36]]
    String str = new String("a");
    response.addCookie(a);
  }
}
