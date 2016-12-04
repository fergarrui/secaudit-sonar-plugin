package fg.sonar.plugins.secaudit.rules;

import org.junit.Assert;
import org.junit.Test;
import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.api.server.rule.RulesDefinition.Rule;
import org.sonar.api.server.rule.RulesDefinition.Param;

import static org.fest.assertions.Assertions.assertThat;

public class SecAuditRulesDefinitionTest {

  @Test
  public void test() {
    SecAuditRulesDefinition rulesDefinition = new SecAuditRulesDefinition();
    RulesDefinition.Context context = new RulesDefinition.Context();
    rulesDefinition.define(context);
    RulesDefinition.Repository repository = context.repository(SecAuditRulesDefinition.REPOSITORY_KEY);

    Assert.assertEquals("SecAuditRepository",repository.name());
    Assert.assertEquals(repository.language(), "java");
    Assert.assertEquals(repository.rules().size(), SecAuditRules.getChecks().length);

    for ( Rule rule : repository.rules()) {
      for (Param param : rule.params()) {
        assertThat(param.description()).as("description for " + param.key()).isNotEmpty();
      }
    }

  }
}
