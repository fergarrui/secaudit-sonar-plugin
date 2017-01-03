import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

class A {

  void method(FileInputStream fileInputStream) throws IOException, ClassNotFoundException {
    ObjectInputStream ois = new ObjectInputStream(fileInputStream);
    Object o = ois.readObject(); // Noncompliant
    ois.readUnshared(); // Noncompliant
  }
  private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
    Object o = in.readObject(); // compliant, inside a readObject method
  }
}

class B {
  public void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
    Object o = in.readObject(); // Noncompliant
  }
}