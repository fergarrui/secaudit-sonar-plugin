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
package fg.sonar.plugins.secaudit.rules.checks.util;

import java.util.List;
import org.sonar.plugins.java.api.tree.ExpressionTree;
import org.sonar.plugins.java.api.tree.IdentifierTree;
import org.sonar.plugins.java.api.tree.Tree;
import org.sonar.plugins.java.api.tree.VariableTree;

public class ExpressionTreeUtils {

  private ExpressionTreeUtils() {
    throw new IllegalAccessError("Cannot instantiate this utility class.");
  }

  /**
   * Quite simple checking for dynamic String arguments.
   * @param arguments method arguments
   * @return true if there is at least one invalid argument
   */
  public static boolean invalidArguments(List<ExpressionTree> arguments) {
    boolean areInvalid = false;
    for (ExpressionTree expressionTree : arguments) {
      if (expressionTree.is(Tree.Kind.IDENTIFIER)) {
        areInvalid = checkIdentifier(expressionTree);
      } else if(expressionTree.is(Tree.Kind.STRING_LITERAL)) {
        areInvalid |= false;
      } else {
        areInvalid = true;
      }
    }
    return areInvalid;
  }

  private static boolean checkIdentifier(ExpressionTree expressionTree) {
    IdentifierTree identifierTree = (IdentifierTree)expressionTree;
    VariableTree variableTree = (VariableTree) identifierTree.symbol().declaration();
    ExpressionTree initializer = variableTree.initializer();
    return initializer == null || !initializer.is(Tree.Kind.STRING_LITERAL);
  }
}
