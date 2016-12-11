class A {
  String className = "B";

  void method(String badClassName) {
    String classVar = "C";
    Class a = Class.forName("D"); // Compliant
    a.newInstance();
    Class b = Class.forName(badClassName); // Noncompliant [[sc=15;ec=42]]
    b.newInstance();
    Class c = Class.forName(className); // Compliant
    c.newInstance();
    Class d = Class.forName(classVar); // Compliant
    d.newInstance();
    Class e = Class.forName("com.example." + badClassName); // Noncompliant [[sc=15;ec=59]]
    e.newInstance();
    Class f = Class.forName(badClassName, true, this.getClass().getClassLoader()); // Noncompliant [[sc=15;ec=82]]
    f.newInstance();
  }
}