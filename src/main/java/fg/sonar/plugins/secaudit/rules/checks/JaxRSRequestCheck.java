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
import org.sonar.plugins.java.api.IssuableSubscriptionVisitor;
import org.sonar.plugins.java.api.tree.AnnotationTree;
import org.sonar.plugins.java.api.tree.BaseTreeVisitor;
import org.sonar.plugins.java.api.tree.ClassTree;
import org.sonar.plugins.java.api.tree.IdentifierTree;
import org.sonar.plugins.java.api.tree.MethodTree;
import org.sonar.plugins.java.api.tree.Tree;

@Rule(key = "JaxRSRequestCheck")
public class JaxRSRequestCheck extends IssuableSubscriptionVisitor {

  private static final String JAX_RS_PATH_ANNOTATION = "javax.ws.rs.Path";

  @Override
  public List<Tree.Kind> nodesToVisit() {
    return ImmutableList.of(Tree.Kind.CLASS);
  }

  @Override
  public void visitNode(Tree tree) {
    ClassTree classTree = (ClassTree)tree;
    IdentifierTree classTreeName = classTree.simpleName();
    if ( jaxRsPathAnnotationFound(classTree.modifiers().annotations())) {
      reportRSWSIssue(classTreeName);
    } else {
      JaxRSMethodCheck jaxRSMethodCheck = new JaxRSMethodCheck();
      classTree.accept(jaxRSMethodCheck);
      if (jaxRSMethodCheck.jaxRsPathFound()) {
        reportRSWSIssue(classTreeName);
      }
    }
  }

  private boolean jaxRsPathAnnotationFound(List<AnnotationTree> annotations) {
    for (AnnotationTree annotationTree : annotations) {
      if (JAX_RS_PATH_ANNOTATION.equals(annotationTree.symbolType().fullyQualifiedName())) {
        return true;
      }
    }
    return false;
  }

  private void reportRSWSIssue(Tree tree) {
    reportIssue(tree, "This class is a REST WS. Analysis needed.");
  }

  class JaxRSMethodCheck extends BaseTreeVisitor {

    private boolean jaxRsPathFound = false;

    @Override
    public void visitMethod(MethodTree methodTree) {
      jaxRsPathFound = jaxRsPathAnnotationFound(methodTree.modifiers().annotations());
      super.visitMethod(methodTree);
    }

    public boolean jaxRsPathFound() {
      return jaxRsPathFound;
    }
  }
}

