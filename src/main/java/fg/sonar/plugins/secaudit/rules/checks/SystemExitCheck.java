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
import java.util.Collection;
import java.util.List;
import org.sonar.check.Rule;
import org.sonar.java.matcher.MethodMatcher;
import org.sonar.plugins.java.api.IssuableSubscriptionVisitor;
import org.sonar.plugins.java.api.tree.MethodInvocationTree;
import org.sonar.plugins.java.api.tree.Tree;

@Rule(key = "SystemExitCheck")
public class SystemExitCheck extends IssuableSubscriptionVisitor {

  private static final MethodMatcher SYSTEM_EXIT_MATCHER = MethodMatcher.create()
          .typeDefinition("java.lang.System")
          .name("exit")
          .withNoParameterConstraint();

  private static final MethodMatcher RUNTIME_EXIT_MATCHER = MethodMatcher.create()
          .typeDefinition("java.lang.Runtime")
          .name("exit")
          .withNoParameterConstraint();

  private static final Collection<MethodMatcher> MATCHERS = ImmutableList.of(SYSTEM_EXIT_MATCHER, RUNTIME_EXIT_MATCHER);

  @Override
  public List<Tree.Kind> nodesToVisit() {
    return ImmutableList.of(Tree.Kind.METHOD_INVOCATION);
  }

  @Override
  public void visitNode(Tree tree) {
    MethodInvocationTree methodInvocationTree = (MethodInvocationTree)tree;
    for (MethodMatcher methodMatcher : MATCHERS) {
      if (methodMatcher.matches(methodInvocationTree)) {
        reportIssue(methodInvocationTree, "System or Runtime exit detected. In a web application it can lead to DOS attacks.");
      }
    }
  }
}
