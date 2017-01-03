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

import org.sonar.plugins.java.api.tree.MethodTree;
import org.sonar.plugins.java.api.tree.Tree;

public class MethodUtils {

  private MethodUtils() {
    throw new IllegalAccessError("Cannot instantiate this utility class.");
  }

  public static MethodTree getParentMethod(Tree tree) {
    Tree result = tree;
    while (result != null && !result.is(Tree.Kind.METHOD)) {
      result = result.parent();
    }
    return (MethodTree) result;
  }
}
