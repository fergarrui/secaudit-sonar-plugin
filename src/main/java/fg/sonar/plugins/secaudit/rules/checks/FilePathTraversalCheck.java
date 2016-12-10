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
import org.sonar.plugins.java.api.IssuableSubscriptionVisitor;
import org.sonar.plugins.java.api.tree.ExpressionTree;
import org.sonar.plugins.java.api.tree.IdentifierTree;
import org.sonar.plugins.java.api.tree.NewClassTree;
import org.sonar.plugins.java.api.tree.Tree;
import org.sonar.plugins.java.api.tree.VariableTree;

@Rule(key = "FilePathTraversalCheck")
public class FilePathTraversalCheck extends IssuableSubscriptionVisitor {

  private static final Collection<String> fileClasses =
          ImmutableList.of("java.io.File", "java.io.FileInputStream", "java.io.FileReader", "java.io.RandomAccessFile",
                  "java.io.FileWriter", "java.io.FileOutputStream");

  @Override
  public List<Tree.Kind> nodesToVisit() {
    return ImmutableList.of(Tree.Kind.NEW_CLASS);
  }

  @Override
  public void visitNode(Tree tree) {
    NewClassTree newClassTree = (NewClassTree)tree;
    if (fileClasses.contains(newClassTree.symbolType().fullyQualifiedName())) {
      if(invalidArguments(newClassTree.arguments())) {
        reportIssue(newClassTree, "A file has been opened. Check that is not receiving any unfiltered parameter.");
      }
    }
  }

  private boolean invalidArguments(List<ExpressionTree> arguments) {
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

  private boolean checkIdentifier(ExpressionTree expressionTree) {
    IdentifierTree identifierTree = (IdentifierTree)expressionTree;
    VariableTree variableTree = (VariableTree) identifierTree.symbol().declaration();
    ExpressionTree initializer = variableTree.initializer();
    return initializer == null || !initializer.is(Tree.Kind.STRING_LITERAL);
  }
}
