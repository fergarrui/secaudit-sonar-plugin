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
import org.sonar.plugins.java.api.IssuableSubscriptionVisitor;
import org.sonar.plugins.java.api.tree.ExpressionTree;
import org.sonar.plugins.java.api.tree.IdentifierTree;
import org.sonar.plugins.java.api.tree.LiteralTree;
import org.sonar.plugins.java.api.tree.MethodInvocationTree;
import org.sonar.plugins.java.api.tree.NewClassTree;
import org.sonar.plugins.java.api.tree.Tree;
import org.sonar.plugins.java.api.tree.UnaryExpressionTree;
import org.sonar.plugins.java.api.tree.VariableTree;

@Rule(key = "InsufficientSessionExpirationCheck")
public class InsufficientSessionExpirationCheck extends IssuableSubscriptionVisitor {

  private static final MethodMatcher SESSION_MAX_INACTIVE_INTERVAL_MATCHER = MethodMatcher.create()
          .typeDefinition("javax.servlet.http.HttpSession")
          .name("setMaxInactiveInterval")
          .withNoParameterConstraint();

  private final static Integer INVALID_SESSION_TIME = 1;

  @Override
  public List<Tree.Kind> nodesToVisit() {
    return ImmutableList.of(Tree.Kind.METHOD_INVOCATION);
  }

  @Override
  public void visitNode(Tree tree) {
    MethodInvocationTree methodInvocationTree = (MethodInvocationTree)tree;
    if (SESSION_MAX_INACTIVE_INTERVAL_MATCHER.matches(methodInvocationTree)) {
      ExpressionTree argument = methodInvocationTree.arguments().get(0);
      checkArgument(argument, methodInvocationTree);
    }
  }

  private void checkArgument(ExpressionTree argument, MethodInvocationTree methodInvocationTree) {
    if (argument.is(Tree.Kind.UNARY_MINUS)) {
      checkUnaryMinus(argument, methodInvocationTree);
    } else if (argument.is(Tree.Kind.NEW_CLASS)) {
      NewClassTree newClassTree = (NewClassTree) argument;
      ExpressionTree newClassArgument = newClassTree.arguments().get(0);
      if (newClassArgument.is(Tree.Kind.UNARY_MINUS)) {
        checkUnaryMinus(newClassArgument, methodInvocationTree);
      }
    } else if (argument.is(Tree.Kind.IDENTIFIER)) {
      IdentifierTree identifierTree = (IdentifierTree)argument;
      Tree declaration = identifierTree.symbol().declaration();
      if (declaration.is(Tree.Kind.VARIABLE)) {
        VariableTree variableTree = (VariableTree) declaration;
        if (variableTree.initializer().is(Tree.Kind.UNARY_MINUS)) {
          checkUnaryMinus(variableTree.initializer(), methodInvocationTree);
        }
      }
    }
  }

  private void checkUnaryMinus(ExpressionTree argument, MethodInvocationTree methodInvocationTree) {
    UnaryExpressionTree unaryExpressionTree = (UnaryExpressionTree)argument;
    ExpressionTree expressionTree = unaryExpressionTree.expression();
    if (expressionTree.is(Tree.Kind.INT_LITERAL)) {
      LiteralTree literalTree = (LiteralTree) expressionTree;
      if (INVALID_SESSION_TIME == Integer.valueOf(literalTree.value())) {
        reportIssue(methodInvocationTree, "Session expiration set to 'never'. Doing that can help attackers.");
      }
    }
  }
}
