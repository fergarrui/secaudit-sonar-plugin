import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequestMapping("/")
class A { // Noncompliant [[sc=7;ec=8]]

  public String method() {
    return "";
  }
}

@Controller
class B { // Noncompliant [[sc=7;ec=8]]

  @RequestMapping("/any")
  public String method() {
    return "";
  }
}

@Controller
@RequestMapping("/")
class C { // Noncompliant [[sc=7;ec=8]]

  @RequestMapping("/any")
  public String method() {
    return "";
  }
}

@RestController
@RequestMapping("/")
class D { // Noncompliant [[sc=7;ec=8]]

  public String method() {
    return "";
  }
}
@AnyAnnotation
class E { // Noncompliant [[sc=7;ec=8]]

  @AnotherAnnotation
  @Another
  @RequestMapping("/any")
  public String method() {
    return "";
  }
}

@RestController
class F { // Compliant

  public String method() {
    return "";
  }
}

class G { // compliant

  public String method() {
    return "";
  }
}
@AnyAnnotation
class H { // Compliant

  public String method() {
    return "";
  }
}

class I { // Compliant

  @AnyAnnotation
  public String method() {
    return "";
  }
}
