import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

class A {
  void method(FileInputStream fileInputStream) throws IOException, ClassNotFoundException {
    ObjectInputStream ois = new ObjectInputStream(fileInputStream);
    Object o = ois.readObject(); // Noncompliant
    ois.readUnshared(); // Noncompliant
    Object o2 = getObject();
  }
  private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
    Object o = in.readObject(); // compliant, inside a readObject method
  }
  private Object getObject() {
    return new Object();
  }
}
class B {
  public void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
    Object o = in.readObject(); // Noncompliant
  }
}
class C {
  private void readObject(ObjectInputStream in, Object o) throws IOException, ClassNotFoundException {
    Object o1 = in.readObject(); // Noncompliant
    Object o2 = getObject();
  }
  private Object getObject() {
    return new Object();
  }
}
class D {
  private void readObject(Object obj, ObjectInputStream in) throws IOException, ClassNotFoundException {
    Object o = in.readObject(); // Noncompliant
  }
}
class E {
  private void method(ObjectInputStream in) throws IOException, ClassNotFoundException {
    Object o = in.readObject(); // Noncompliant
  }
}
class F {
  private void readObject(Object o) throws IOException, ClassNotFoundException {
    ObjectInputStream ois = null;
    Object o2 = ois.readObject(); // Noncompliant
  }
}
