import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

class A {

  void method(FileInputStream fileInputStream) throws IOException, ClassNotFoundException {
    ObjectInputStream ois = new ObjectInputStream(fileInputStream);
    Object o = ois.readObject(); // Noncompliant
    ois.readUnshared(); // Noncompliant
  }
}
