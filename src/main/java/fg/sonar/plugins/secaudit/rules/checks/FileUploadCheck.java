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
import org.sonar.plugins.java.api.tree.AnnotationTree;
import org.sonar.plugins.java.api.tree.MethodTree;
import org.sonar.plugins.java.api.tree.Tree;
import org.sonar.plugins.java.api.tree.VariableTree;

@Rule(key = "FileUploadCheck")
public class FileUploadCheck extends IssuableSubscriptionVisitor {

  private static final Collection<String> FILE_UPLOAD_TYPES = ImmutableList.of("javax.servlet.http.Part", "org.apache.commons.fileupload.FileItem", "org.springframework.web.multipart.MultipartFile");
  private static final String JERSEY_FORM_DATA_PARAM_ANNOTATION = "com.sun.jersey.multipart.FormDataParam";
  private static final Collection<String> JERSEY_FILE_UPLOAD_TYPES = ImmutableList.of("com.sun.jersey.core.header.FormDataContentDisposition", "java.io.InputStream");

  @Override
  public List<Tree.Kind> nodesToVisit() {
    return ImmutableList.of(Tree.Kind.VARIABLE, Tree.Kind.METHOD);
  }

  @Override
  public void visitNode(Tree tree) {
   if (tree.is(Tree.Kind.VARIABLE)) {
     VariableTree variableTree = (VariableTree)tree;
     if (isFileUploadVariable(variableTree)) {
       reportUpload(tree);
     }
   } else if (tree.is(Tree.Kind.METHOD)) {
     MethodTree methodTree = (MethodTree)tree;
     if (isFileUploadMethod(methodTree)) {
       reportUpload(tree);
     }
   }
  }

  private static boolean isFileUploadVariable(VariableTree variableTree) {
    for (String fileUploadType : FILE_UPLOAD_TYPES) {
      if (variableTree.type().symbolType().is(fileUploadType)) {
        return true;
      }
    }
    return false;
  }

  private static boolean isFileUploadMethod(MethodTree methodTree) {
    for (VariableTree parameter : methodTree.parameters()) {
      if (hasFormDataAnnotation(parameter) && isJerseyFileUploadType(parameter)) {
        return true;
      }
    }
    return false;
  }

  private static boolean isJerseyFileUploadType(VariableTree parameter) {
    for (String jerseyFileUpload : JERSEY_FILE_UPLOAD_TYPES) {
      if (parameter.type().symbolType().is(jerseyFileUpload)) {
        return true;
      }
    }
    return false;
  }

  private static boolean hasFormDataAnnotation(VariableTree parameter) {
    for (AnnotationTree annotationTree : parameter.modifiers().annotations()) {
      if (annotationTree.symbolType().is(JERSEY_FORM_DATA_PARAM_ANNOTATION)) {
        return true;
      }
    }
    return false;
  }

  private void reportUpload(Tree tree) {
    reportIssue(tree, "Processing a server file upload. Some issues must be checked.");
  }
}
