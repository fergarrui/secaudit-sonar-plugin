import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

class A extends HttpServlet { // Noncompliant [[sc=7;ec=8]]
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
          throws IOException, ServletException {

  }
}
class B { // Compliant
  public void doGet(HttpServletRequest request, HttpServletResponse response)
          throws IOException, ServletException {

  }
}

class C extends OtherClass { // Compliant
  public void doGet(HttpServletRequest request, HttpServletResponse response)
          throws IOException, ServletException {

  }
}
