import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;

class A extends HttpServlet {

  String good = "Good";
  String bad;

  @Override
  void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    PrintWriter pw = resp.getWriter();

    pw.print(bad); // Noncompliant [[sc=5;ec=18]]
    pw.print(req.getParameter("a")); // Noncompliant [[sc=5;ec=36]]
    pw.print(good);
    pw.print("Good");
    pw.println(bad); // Noncompliant [[sc=5;ec=20]]
    pw.println(good);
    pw.println("Good");

    resp.getWriter().print(bad); // Noncompliant [[sc=5;ec=32]]
    resp.getWriter().print(good);
    resp.getWriter().print("Good");

    resp.getWriter().println(bad); // Noncompliant [[sc=5;ec=34]]
    resp.getWriter().println(good);
    resp.getWriter().println("Good");

    resp.getWriter().write(bad); // Noncompliant [[sc=5;ec=32]]
    resp.getWriter().write(good);
    resp.getWriter().write("Good");
    resp.getWriter().write(req.getParameter("a")); // Noncompliant [[sc=5;ec=50]]

    resp.getOutputStream().write("Good".getBytes()); // Noncompliant  [[sc=5;ec=52]] - TODO remove this FP
    resp.getOutputStream().write(req.getParameter("a").getBytes()); // Noncompliant [[sc=5;ec=67]]

    OutputStream out = resp.getOutputStream();
    out.write("Good".getBytes()); // Noncompliant  [[sc=5;ec=33]] - TODO remove this FP
    out.write(req.getParameter("a").getBytes()); // Noncompliant [[sc=5;ec=48]]

    OutputStream out2 = getOutputStream();
    out2.write(req.getParameter("a").getBytes()); // Compliant - not used by response

    PrintWriter pw2 = new PrintWriter(out2);
    pw2.write(bad); // Compliant - not used by response
    pw2 = resp.getWriter();
    pw2.print(bad); // TODO fix this False negative

  }

  private OutputStream getOutputStream() {
    return null;
  }
}
