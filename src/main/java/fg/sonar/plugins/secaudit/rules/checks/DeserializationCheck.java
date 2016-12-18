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
import org.sonar.plugins.java.api.tree.MethodInvocationTree;
import org.sonar.plugins.java.api.tree.Tree;

@Rule(key = "DeserializationCheck")
public class DeserializationCheck extends IssuableSubscriptionVisitor {

  private static final MethodMatcher READ_OBJECT_MATCHER = MethodMatcher.create()
          .typeDefinition("java.io.ObjectInputStream")
          .name("readObject")
          .withNoParameterConstraint();

  private static final MethodMatcher READ_UNSHARED_MATCHER = MethodMatcher.create()
          .typeDefinition("java.io.ObjectInputStream")
          .name("readUnshared")
          .withNoParameterConstraint();

  @Override
  public List<Tree.Kind> nodesToVisit() {
    return ImmutableList.of(Tree.Kind.METHOD_INVOCATION);
  }

  @Override
  public void visitNode(Tree tree) {
    MethodInvocationTree methodInvocationTree = (MethodInvocationTree)tree;
    if (READ_OBJECT_MATCHER.matches(methodInvocationTree) || READ_UNSHARED_MATCHER.matches(methodInvocationTree)) {
      reportIssue(methodInvocationTree, "Deserialization found. Check if the source object is untrusted.");
    }
  }
}
