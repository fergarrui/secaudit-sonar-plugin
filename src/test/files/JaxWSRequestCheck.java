import javax.jws.WebMethod;

public class A { // Noncompliant [[sc=14;ec=15]]
  @WebMethod
  public void foo(String s) {
  }
  public String bar(String s) {
  }
}
@WebService
public class B { // Compliant
  public void foo(String s) {
  }
  public String bar(String s) {
  }
}
public class C { // Compliant
  @AnotherAnnotation
  public void foo(String s) {
  }
  public String bar(String s) {
  }
}