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
import fg.sonar.plugins.secaudit.rules.checks.util.MethodUtils;
import java.util.List;
import org.sonar.check.Rule;
import org.sonar.java.matcher.MethodMatcher;
import org.sonar.plugins.java.api.IssuableSubscriptionVisitor;
import org.sonar.plugins.java.api.semantic.Symbol;
import org.sonar.plugins.java.api.tree.MethodInvocationTree;
import org.sonar.plugins.java.api.tree.MethodTree;
import org.sonar.plugins.java.api.tree.Tree;
import org.sonar.plugins.java.api.tree.VariableTree;

@Rule(key = "DeserializationCheck")
public class DeserializationCheck extends IssuableSubscriptionVisitor {

  private static final String OBJECT_INPUT_STREAM_FULLY_QUALIFIED = "java.io.ObjectInputStream";

  private static final MethodMatcher READ_OBJECT_MATCHER = MethodMatcher.create()
          .typeDefinition(OBJECT_INPUT_STREAM_FULLY_QUALIFIED)
          .name("readObject")
          .withNoParameterConstraint();

  private static final MethodMatcher READ_UNSHARED_MATCHER = MethodMatcher.create()
          .typeDefinition(OBJECT_INPUT_STREAM_FULLY_QUALIFIED)
          .name("readUnshared")
          .withNoParameterConstraint();

  @Override
  public List<Tree.Kind> nodesToVisit() {
    return ImmutableList.of(Tree.Kind.METHOD_INVOCATION);
  }

  @Override
  public void visitNode(Tree tree) {
    MethodInvocationTree methodInvocationTree = (MethodInvocationTree)tree;
    if (matchesDeserializationMethod(methodInvocationTree) && !isInsideCustomSerialization(methodInvocationTree)) {
      reportIssue(methodInvocationTree, "Deserialization found. Check if the source object is untrusted.");
    }
  }

  private static boolean matchesDeserializationMethod(MethodInvocationTree methodInvocationTree) {
    return READ_OBJECT_MATCHER.matches(methodInvocationTree) || READ_UNSHARED_MATCHER.matches(methodInvocationTree);
  }

  private static boolean isInsideCustomSerialization(MethodInvocationTree methodInvocationTree) {
    MethodTree parentMethodTree = MethodUtils.getParentMethod(methodInvocationTree);
    List<VariableTree> parameters = parentMethodTree.parameters();
    if (parameters.size() != 1) {
      return false;
    }
    VariableTree parameter = parameters.get(0);
    Symbol.MethodSymbol symbol = parentMethodTree.symbol();
    return symbol.isPrivate() && "readObject".equals(symbol.name())
            && parameter.type().symbolType().is(OBJECT_INPUT_STREAM_FULLY_QUALIFIED);
  }
}
