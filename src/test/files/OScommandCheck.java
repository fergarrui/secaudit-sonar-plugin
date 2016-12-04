import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.Executor;
import org.apache.commons.exec.DefaultExecutor;

class A {
  void method() {
    Runtime runtime = Runtime.getRuntime();
    runtime.exec("ls"); // Noncompliant
  }
  void methodBuilder() {
    Runtime runtime = Runtime.getRuntime();
    ProcessBuilder pb = new ProcessBuilder("ls"); // Noncompliant
    pb.command("ls", "pwd"); // Noncompliant
  }
  void orgApacheCommonsExec() {
    String line = "ls";
    CommandLine cmdLine = CommandLine.parse(line);
    DefaultExecutor executor = new DefaultExecutor(); // Noncompliant
    int exitValue = executor.execute(cmdLine); // Noncompliant
  }
}