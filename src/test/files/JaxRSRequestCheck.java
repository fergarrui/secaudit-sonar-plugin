import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.PathParam;

@Path("path")
class A { // Noncompliant [[sc=7;ec=8]]

  @GET
  String getEverything() {
    return "{}";
  }

  @GET @Path("{param}")
  String getSomething(@PathParam("param") String param) {
    return "{}";
  }
}
class B { // Noncompliant [[sc=7;ec=8]]

  @GET @Path("{param}")
  String getSomething(@PathParam("param") String param) {
    return "{}";
  }
}
@Path("path")
class C { // Noncompliant [[sc=7;ec=8]]

  @GET @Path("{param}")
  String getSomething(@PathParam("param") String param) {
    return "{}";
  }
}
@Path("path")
class D { // Noncompliant [[sc=7;ec=8]]

  String getSomething(String param) {
    return "{}";
  }
}
class E { // Compliant

  String getSomething(String param) {
    return "{}";
  }
}