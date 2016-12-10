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
import org.sonar.plugins.java.api.tree.MethodTree;
import org.sonar.plugins.java.api.tree.Tree;

@Rule(key = "SpringControllerCheck")
public class SpringControllerCheck extends IssuableSubscriptionVisitor {

  private static final String SPRING_REQUEST_MAPPER_NAME = "org.springframework.web.bind.annotation.RequestMapping";

  @Override
  public List<Tree.Kind> nodesToVisit() {
    return ImmutableList.of(Tree.Kind.CLASS);
  }

  @Override
  public void visitNode(Tree tree) {
    ClassTree classTree = (ClassTree)tree;
    if (hasRequestMappingAnnotation(classTree.modifiers().annotations())) {
      reportSpringControllerIssue(classTree);
    } else {
      SpringMethodsVisitor springMethodsVisitor = new SpringMethodsVisitor();
      classTree.accept(springMethodsVisitor);
      if (springMethodsVisitor.requestMappingFound()) {
        reportSpringControllerIssue(classTree);
      }
    }
  }

  private static boolean hasRequestMappingAnnotation(List<AnnotationTree> annotations) {
    for (AnnotationTree annotationTree : annotations) {
      if (SPRING_REQUEST_MAPPER_NAME.equals(annotationTree.symbolType().fullyQualifiedName())) {
        return true;
      }
    }
    return false;
  }

  private void reportSpringControllerIssue(ClassTree classTree) {
    reportIssue(classTree.simpleName(), "Spring MVC Controller found. Requests can be processed from here.");
  }

  class SpringMethodsVisitor extends BaseTreeVisitor {

    private boolean requestMappingFound = false;

    @Override
    public void visitMethod(MethodTree methodTree) {
      requestMappingFound = hasRequestMappingAnnotation(methodTree.modifiers().annotations());
      super.visitMethod(methodTree);
    }

    public boolean requestMappingFound() {
      return requestMappingFound;
    }
  }
}
