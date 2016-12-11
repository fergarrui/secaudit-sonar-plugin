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
import org.sonar.plugins.java.api.tree.ClassTree;
import org.sonar.plugins.java.api.tree.IdentifierTree;
import org.sonar.plugins.java.api.tree.Tree;
import org.sonar.plugins.java.api.tree.TypeTree;

@Rule(key = "TrustManagerImplementationCheck")
public class TrustManagerImplementationCheck extends IssuableSubscriptionVisitor {

  private static final String TRUST_MANAGER_INTERFACE_NAME = "javax.net.ssl.X509TrustManager";
  private static final String HOSTNAME_VERIFIER_INTERFACE_NAME = "javax.net.ssl.HostnameVerifier";

  @Override
  public List<Tree.Kind> nodesToVisit() {
    return ImmutableList.of(Tree.Kind.CLASS);
  }

  @Override
  public void visitNode(Tree tree) {
    ClassTree classTree = (ClassTree)tree;
    for (TypeTree interfaceTree : classTree.superInterfaces()) {
      IdentifierTree classTreeSimpleName = classTree.simpleName();
      if (interfaceTree.symbolType().is(TRUST_MANAGER_INTERFACE_NAME)) {
        reportIssue(classTreeSimpleName, "TrustManager implementation. A wrong implementation can lead to MITM attacks.");
      } else if (interfaceTree.symbolType().is(HOSTNAME_VERIFIER_INTERFACE_NAME)) {
        reportIssue(classTreeSimpleName, "HostnameVerifier implementation. A wrong implementation can lead to MITM attacks.");
      }
    }

  }
}
