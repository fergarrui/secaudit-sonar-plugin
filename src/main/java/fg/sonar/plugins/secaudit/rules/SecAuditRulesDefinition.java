/*
 * Copyright (c) 2016, Fernando Garcia
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package fg.sonar.plugins.secaudit.rules;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.google.gson.Gson;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;
import javax.annotation.Nullable;
import org.sonar.api.rule.RuleStatus;
import org.sonar.api.server.debt.DebtRemediationFunction;
import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.api.server.rule.RulesDefinitionAnnotationLoader;
import org.sonar.api.utils.AnnotationUtils;
import org.sonar.plugins.java.api.CheckRegistrar;

public class SecAuditRulesDefinition implements CheckRegistrar, RulesDefinition {

  public static final String REPOSITORY_KEY = "java-secaudit";
  public static final String LANGUAGE = "java";
  private static final String RULE_RESOURCE_PATH = "/fg.sonar.plugins.secaudit.rules.description";

  private final Gson gson = new Gson();

  @Override
  public void define(Context context) {
    NewRepository repository = context.createRepository(REPOSITORY_KEY, LANGUAGE)
            .setName("SecAuditRepository");
    RulesDefinitionAnnotationLoader rulesDefinitionAnnotationLoader = new RulesDefinitionAnnotationLoader();
    Class[] checks = SecAuditRules.getChecks();
    rulesDefinitionAnnotationLoader.load(repository, checks);
    for (Class ruleClass : checks) {
      populateRule(ruleClass, repository);
    }
    repository.done();
  }

  private void populateRule(Class<?> ruleClass, NewRepository repository) {
    org.sonar.check.Rule ruleAnnotation = AnnotationUtils.getAnnotation(ruleClass, org.sonar.check.Rule.class);
    String ruleKey = ruleAnnotation.key();
    NewRule rule = repository.rule(ruleKey);
    if (rule == null ) {
      throw new IllegalArgumentException("Rule: " + ruleClass.getSimpleName() + " not found in the repository");
    }
    URL resource = SecAuditRulesDefinition.class.getResource(RULE_RESOURCE_PATH + "/" + ruleKey + "_desc.html");
    rule.setHtmlDescription(readResource(resource));
    addMetadata(rule, ruleKey);
  }

  private void addMetadata(NewRule rule, String ruleKey) {
    String json = readRuleMetadata(ruleKey + "_meta.json");
    if (json != null) {
      RuleMetadata metadata = gson.fromJson(json, RuleMetadata.class);
      rule.setSeverity(metadata.defaultSeverity.toUpperCase(Locale.US));
      rule.setName(metadata.title);
      rule.setTags(metadata.tags);
      rule.setStatus(RuleStatus.valueOf(metadata.status.toUpperCase(Locale.US)));
      if (metadata.remediation != null) {
        rule.setDebtRemediationFunction(metadata.remediation.remediationFunction(rule.debtRemediationFunctions()));
        rule.setGapDescription(metadata.remediation.linearDesc);
      }
    }
  }

  private static String readRuleMetadata(String fileName) {
    URL resource = SecAuditRulesDefinition.class.getResource(RULE_RESOURCE_PATH + "/" + fileName);
    if (resource == null) {
      return null;
    }
    try {
      return Resources.toString(resource, Charsets.UTF_8);
    } catch (IOException e) {
      throw new IllegalStateException("Failed to read: " + resource, e);
    }
  }

  private static String readResource(URL resource) {
    try {
      return Resources.toString(resource, StandardCharsets.UTF_8);
    } catch (IOException e) {
      throw new IllegalStateException("Failed to read: " + resource, e);
    }
  }

  @Override
  public void register(RegistrarContext registrarContext) {
    registrarContext.registerClassesForRepository(REPOSITORY_KEY,
            Arrays.asList(SecAuditRules.getChecks()), Collections.emptyList());
  }

  private static class RuleMetadata {
    String title;
    String status;
    @Nullable
    Remediation remediation;

    String[] tags;
    String defaultSeverity;
  }

  private static class Remediation {
    String func;
    String constantCost;
    String linearDesc;
    String linearOffset;
    String linearFactor;

    private DebtRemediationFunction remediationFunction(DebtRemediationFunctions drf) {
      if (func.startsWith("Constant")) {
        return drf.constantPerIssue(constantCost.replace("mn", "min"));
      }
      if ("Linear".equals(func)) {
        return drf.linear(linearFactor.replace("mn", "min"));
      }
      return drf.linearWithOffset(linearFactor.replace("mn", "min"), linearOffset.replace("mn", "min"));
    }
  }
}
