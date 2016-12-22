import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.RandomAccessFile;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileOutputStream;

class A {

  String attr;

  String fileNameAttr = "file.txt";
  String fileBadAttr = "file.txt" + attr;

  void method(String fileName) throws IOException {

    String fileNameVar = "file.txt";
    String badFileName = "file/" + fileName;

    File a = new File(fileName); // Noncompliant [[sc=14;ec=32]]
    File a2 = new File(fileNameVar);
    File a3 = new File(badFileName); // Noncompliant [[sc=15;ec=36]]
    File a4 = new File(fileNameAttr);
    File a5 = new File(fileNameAttr + "/" + fileName); // Noncompliant [[sc=15;ec=54]]
    File b = new File("dir", fileName); // Noncompliant [[sc=14;ec=39]]
    File b2 = new File("dir", "file.txt");
    File c = new File(a, fileName); // Noncompliant [[sc=14;ec=35]]

    RandomAccessFile f4 = new RandomAccessFile(fileName, "r"); // Noncompliant [[sc=27;ec=62]]
    RandomAccessFile f5 = new RandomAccessFile("file.txt", "r"); // Compliant

    FileReader fr = new FileReader(fileName); // Noncompliant [[sc=21;ec=45]]
    FileReader fr2 = new FileReader(fileNameVar); // Compliant
    FileInputStream fis = new FileInputStream(fileName); // Noncompliant [[sc=27;ec=56]]
    FileInputStream fis2 = new FileInputStream(fileNameAttr); // Compliant

    FileWriter fw = new FileWriter("file.txt"); // Compliant
    FileWriter fw2 = new FileWriter(fileNameVar); // Compliant
    FileWriter fw3 = new FileWriter(fileName); // Noncompliant [[sc=22;ec=46]]
    FileWriter fw4 = new FileWriter(badFileName); // Noncompliant [[sc=22;ec=49]]
    FileWriter fw5 = new FileWriter(a); // Noncompliant [[sc=22;ec=39]]
    FileWriter fw6 = new FileWriter(fileBadAttr); // Noncompliant [[sc=22;ec=49]]
    FileWriter fw7 = new FileWriter("file.txt", true ); // Noncompliant [[sc=22;ec=55]]

    FileOutputStream fos = new FileOutputStream("file.txt"); // Compliant
    FileOutputStream fos1 = new FileOutputStream(a); // Noncompliant [[sc=29;ec=52]]
    FileOutputStream fos2 = new FileOutputStream(fileBadAttr); // Noncompliant [[sc=29;ec=62]]
    FileOutputStream fos3 = new FileOutputStream(fileNameAttr); // Compliant
    Object o = new Object(); // compliant
  }
}
