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
package fg.sonar.plugins.secaudit.rules.checks;

import com.google.common.collect.ImmutableList;
import java.util.List;
import org.sonar.check.Rule;
import org.sonar.java.matcher.MethodMatcher;
import org.sonar.java.matcher.TypeCriteria;
import org.sonar.plugins.java.api.IssuableSubscriptionVisitor;
import org.sonar.plugins.java.api.tree.MethodInvocationTree;
import org.sonar.plugins.java.api.tree.NewClassTree;
import org.sonar.plugins.java.api.tree.Tree;

@Rule(key = "OSCommandCheck",
      name = "",
      tags = {"auditpoint"})
public class OScommandCheck extends IssuableSubscriptionVisitor {

  private static final MethodMatcher RUNTIME_EXEC_MATCHER = MethodMatcher.create()
          .typeDefinition(TypeCriteria.subtypeOf("java.lang.Runtime"))
          .name("exec")
          .addParameter(TypeCriteria.anyType());

  private static final MethodMatcher PROCESS_BUILDER_COMMAND_MATCHER = MethodMatcher.create()
          .typeDefinition(TypeCriteria.subtypeOf("java.lang.ProcessBuilder"))
          .name("command")
          .addParameter(TypeCriteria.anyType());

  private static final MethodMatcher COMMONS_EXEC_MATCHER = MethodMatcher.create()
          .typeDefinition(TypeCriteria.subtypeOf("org.apache.commons.exec.Executor"))
          .name("execute")
          .addParameter(TypeCriteria.anyType());

  private static final List<String> osCommandExecLibraries
          = ImmutableList.of("org.apache.commons.exec.Executor", "java.lang.ProcessBuilder");

  @Override
  public List<Tree.Kind> nodesToVisit() {
    return ImmutableList.of(Tree.Kind.METHOD_INVOCATION, Tree.Kind.NEW_CLASS);
  }

  @Override
  public void visitNode(Tree tree) {
    if (tree.is(Tree.Kind.METHOD_INVOCATION)) {
      MethodInvocationTree methodInvocationTree = (MethodInvocationTree)tree;
      checkMethodInvocation(methodInvocationTree);
    } else if (tree.is(Tree.Kind.NEW_CLASS)) {
      NewClassTree newClassTree = (NewClassTree) tree;
      checkExecNewClass(newClassTree);
    }
  }

  private void checkExecNewClass(NewClassTree newClassTree) {
    for (String execClassName : osCommandExecLibraries) {
      if (newClassTree.symbolType().isSubtypeOf(execClassName)) {
        reportIssue(newClassTree, "Potential command execution.");
      }
    }
  }

  private void checkMethodInvocation(MethodInvocationTree methodInvocationTree) {
    if (RUNTIME_EXEC_MATCHER.matches(methodInvocationTree) || PROCESS_BUILDER_COMMAND_MATCHER.matches(methodInvocationTree)
            || COMMONS_EXEC_MATCHER.matches(methodInvocationTree)) {
      reportIssue(methodInvocationTree, "OS command execution found. Audit is needed here. View description.");
    }
  }
}
