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
package fg.sonar.plugins.secaudit.rules.checks.xpath;

import com.google.common.collect.ImmutableList;
import fg.sonar.plugins.secaudit.rules.checks.util.ExpressionTreeUtils;
import java.util.ArrayList;
import java.util.List;
import org.sonar.check.Rule;
import org.sonar.java.matcher.MethodMatcher;
import org.sonar.plugins.java.api.IssuableSubscriptionVisitor;
import org.sonar.plugins.java.api.tree.ExpressionTree;
import org.sonar.plugins.java.api.tree.MethodInvocationTree;
import org.sonar.plugins.java.api.tree.NewClassTree;
import org.sonar.plugins.java.api.tree.Tree;

import static fg.sonar.plugins.secaudit.rules.checks.xpath.XPathMatchers.matchers;

@Rule(key = "XPathInjectionCheck")
public class XPathInjectionCheck extends IssuableSubscriptionVisitor {

  @Override
  public List<Tree.Kind> nodesToVisit() {
    return ImmutableList.of(Tree.Kind.METHOD_INVOCATION, Tree.Kind.NEW_CLASS);
  }

  @Override
  public void visitNode(Tree tree) {
    if (isInvalidTree(tree)) {
      reportIssue(tree, "XPath expression. Check for injections.");
    }
  }

  private static boolean isInvalidTree(Tree tree) {
    List<ExpressionTree> arguments = new ArrayList<>();
    if (tree.is(Tree.Kind.METHOD_INVOCATION)) {
      MethodInvocationTree methodInvocationTree = (MethodInvocationTree)tree;
      if(isInvalidMethodInvocation(methodInvocationTree)) {
        return false;
      }
      arguments.addAll(methodInvocationTree.arguments());
    } else if (tree.is(Tree.Kind.NEW_CLASS)) {
      NewClassTree newClassTree = (NewClassTree)tree;
      if (!XPathMatchers.matchesConstructor(newClassTree)) {
        return false;
      }
      arguments.addAll(newClassTree.arguments());
    }
    return ExpressionTreeUtils.invalidArguments(arguments);
  }

  private static boolean isInvalidMethodInvocation(MethodInvocationTree methodInvocationTree) {
    boolean isInvalid = true;
    for (MethodMatcher methodMatcher : matchers) {
      isInvalid &= !methodMatcher.matches(methodInvocationTree);
    }
    return isInvalid;
  }
}
