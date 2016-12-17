class A {
  void method() {
    int a = 1;
    System.out.println(a);
    System.exit(a); // Noncompliant
    System.exit(1); // Noncompliant
    Runtime.getRuntime().exit(0); // Noncompliant
    Runtime.getRuntime().gc();
    Runtime.getRuntime().exit(a); // Noncompliant
  }
}
