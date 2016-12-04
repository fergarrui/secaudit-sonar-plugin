package fg.sonar.plugins.secaudit.rules.checks;

import org.junit.Test;
import org.sonar.java.checks.verifier.JavaCheckVerifier;

public class OScommandCheckTest {

  @Test
  public void test() {
    JavaCheckVerifier.verify("src/test/files/OScommandCheck.java", new OScommandCheck());
  }
}
