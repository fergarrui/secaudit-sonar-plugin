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
import fg.sonar.plugins.secaudit.rules.checks.util.ExpressionTreeUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.sonar.check.Rule;
import org.sonar.java.matcher.MethodMatcher;
import org.sonar.plugins.java.api.IssuableSubscriptionVisitor;
import org.sonar.plugins.java.api.semantic.Type;
import org.sonar.plugins.java.api.tree.BaseTreeVisitor;
import org.sonar.plugins.java.api.tree.IdentifierTree;
import org.sonar.plugins.java.api.tree.MethodInvocationTree;
import org.sonar.plugins.java.api.tree.MethodTree;
import org.sonar.plugins.java.api.tree.Tree;
import org.sonar.plugins.java.api.tree.VariableTree;

@Rule(key = "XSSCheck")
public class XSSCheck extends IssuableSubscriptionVisitor {

  private static final String HTTP_SERVLET_RESPONSE_NAME = "javax.servlet.http.HttpServletResponse";
  private static final String JAVA_IO_OUTPUTSTREAM_NAME = "java.io.OutputStream";
  private static final String JAVA_IO_PRINTWRITER_NAME = "java.io.PrintWriter";

  private static final MethodMatcher RESPONSE_GET_WRITER_MATCHER = MethodMatcher.create()
          .typeDefinition("javax.servlet.ServletResponse")
          .name("getWriter")
          .withNoParameterConstraint();

  private static final MethodMatcher RESPONSE_GET_OUTSTREAM_MATCHER = MethodMatcher.create()
          .typeDefinition("javax.servlet.ServletResponse")
          .name("getOutputStream")
          .withNoParameterConstraint();

  private static final MethodMatcher PRINT_WRITER_PRINT_MATCHER = MethodMatcher.create()
          .typeDefinition(JAVA_IO_PRINTWRITER_NAME)
          .name("print")
          .withNoParameterConstraint();

  private static final MethodMatcher PRINT_WRITER_PRINTLN_MATCHER = MethodMatcher.create()
          .typeDefinition(JAVA_IO_PRINTWRITER_NAME)
          .name("println")
          .withNoParameterConstraint();

  private static final MethodMatcher PRINT_WRITER_WRITE_MATCHER = MethodMatcher.create()
          .typeDefinition(JAVA_IO_PRINTWRITER_NAME)
          .name("write")
          .withNoParameterConstraint();

  private static final MethodMatcher OUTPUT_STREAM_WRITE_MATCHER = MethodMatcher.create()
          .typeDefinition("java.io.OutputStream")
          .name("write")
          .withNoParameterConstraint();

  private static final Collection<MethodMatcher> outputWriterMatchers =
          ImmutableList.of(PRINT_WRITER_PRINT_MATCHER, PRINT_WRITER_PRINTLN_MATCHER, PRINT_WRITER_WRITE_MATCHER, OUTPUT_STREAM_WRITE_MATCHER);

  private static final Collection<VariableTree> writerCallers = new ArrayList<>();

  @Override
  public List<Tree.Kind> nodesToVisit() {
    return ImmutableList.of(Tree.Kind.METHOD);
  }

  @Override
  public void visitNode(Tree tree) {
    MethodTree methodTree = (MethodTree) tree;
    writerCallers.clear();
    VariableTree httpServletResponseTree = null;
    for (VariableTree variableTree : methodTree.parameters()) {
      if (isHttpServletResponse(variableTree)) {
        httpServletResponseTree = variableTree;
      }
    }
    if (httpServletResponseTree == null) {
      return;
    }
    writerCallers.add(httpServletResponseTree);
    ResponseWritersVisitor responseWritersVisitor = new ResponseWritersVisitor();
    methodTree.accept(responseWritersVisitor);

    writerCallers.addAll(responseWritersVisitor.getResponseWriters());

    MethodXssSearchVisitor methodXssSearchVisitor = new MethodXssSearchVisitor();
    methodTree.accept(methodXssSearchVisitor);
  }

  private static boolean isHttpServletResponse(VariableTree variableTree) {
    return variableTree.type().symbolType().is(HTTP_SERVLET_RESPONSE_NAME);
  }

  class ResponseWritersVisitor extends BaseTreeVisitor {

    private Collection<VariableTree> responseWriters = new ArrayList<>();

    @Override
    public void visitMethodInvocation(MethodInvocationTree methodInvocationTree) {
      if (RESPONSE_GET_WRITER_MATCHER.matches(methodInvocationTree) || RESPONSE_GET_OUTSTREAM_MATCHER.matches(methodInvocationTree)) {
        Tree methodParent = methodInvocationTree.parent();
        if (methodParent.is(Tree.Kind.VARIABLE)) {
          VariableTree variableTree = (VariableTree) methodParent;
          Type variableTypeSymbol = variableTree.type().symbolType();
          if (variableTypeSymbol.is(JAVA_IO_OUTPUTSTREAM_NAME) || variableTypeSymbol.is(JAVA_IO_PRINTWRITER_NAME)) {
            responseWriters.add(variableTree);
          }
        }
      }
      super.visitMethodInvocation(methodInvocationTree);
    }

    public Collection<VariableTree> getResponseWriters() {
      return responseWriters;
    }
  }

  class MethodXssSearchVisitor extends BaseTreeVisitor {

    @Override
    public void visitMethodInvocation(MethodInvocationTree methodInvocationTree) {
      for (MethodMatcher methodMatcher : outputWriterMatchers) {
        if (methodMatcher.matches(methodInvocationTree)) {
          VariableWriterVisitor variableWriterVisitor = new VariableWriterVisitor();
          methodInvocationTree.accept(variableWriterVisitor);
          if (variableWriterVisitor.isFound() && ExpressionTreeUtils.invalidArguments(methodInvocationTree.arguments())) {
            reportIssue(methodInvocationTree, "Potential XSS found. Make sure the output is validated properly if it is coming from an user.");
          }
        }
      }
      super.visitMethodInvocation(methodInvocationTree);
    }
  }

  class VariableWriterVisitor extends BaseTreeVisitor {

    private boolean found = false;

    @Override
    public void visitIdentifier(IdentifierTree identifierTree) {
      if (writerCallers.contains(identifierTree.symbol().declaration())) {
        found = true;
      }
      super.visitIdentifier(identifierTree);
    }

    public boolean isFound() {
      return found;
    }
  }
}
