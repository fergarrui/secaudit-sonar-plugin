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
package fg.sonar.plugins.secaudit.rules.checks.xpath;

import com.google.common.collect.ImmutableList;
import java.util.Collection;
import org.sonar.java.matcher.MethodMatcher;
import org.sonar.plugins.java.api.semantic.Type;
import org.sonar.plugins.java.api.tree.NewClassTree;

public class XPathMatchers {

  private static final MethodMatcher XML_XPATH_EVAL_MATCHER = MethodMatcher.create()
          .typeDefinition("javax.xml.xpath.XPath")
          .name("evaluate")
          .withNoParameterConstraint();

  private static final MethodMatcher XML_XPATH_COMPILE_MATCHER = MethodMatcher.create()
          .typeDefinition("javax.xml.xpath.XPath")
          .name("compile")
          .withNoParameterConstraint();

  private static final MethodMatcher XALAN_XPATH_EVAL_MATCHER = MethodMatcher.create()
          .typeDefinition("org.apache.xpath.XPathAPI")
          .name("eval")
          .withNoParameterConstraint();

  private static final MethodMatcher XALAN_XPATH_SELECT_NODE_ITER_MATCHER = MethodMatcher.create()
          .typeDefinition("org.apache.xpath.XPathAPI")
          .name("selectNodeIterator")
          .withNoParameterConstraint();

  private static final MethodMatcher XALAN_XPATH_SELECT_NODE_LIST_MATCHER = MethodMatcher.create()
          .typeDefinition("org.apache.xpath.XPathAPI")
          .name("selectNodeList")
          .withNoParameterConstraint();

  private static final MethodMatcher XALAN_XPATH_SELECT_SINGLE_NODE_MATCHER = MethodMatcher.create()
          .typeDefinition("org.apache.xpath.XPathAPI")
          .name("selectSingleNode")
          .withNoParameterConstraint();

  private static final MethodMatcher SUN_XPATH_EVAL_MATCHER = MethodMatcher.create()
          .typeDefinition("com.sun.org.apache.xpath.internal.XPathAPI")
          .name("eval")
          .withNoParameterConstraint();

  private static final MethodMatcher SUN_XPATH_SELECT_NODE_ITER_MATCHER = MethodMatcher.create()
          .typeDefinition("com.sun.org.apache.xpath.internal.XPathAPI")
          .name("selectNodeIterator")
          .withNoParameterConstraint();

  private static final MethodMatcher SUN_XPATH_SELECT_NODE_LIST_MATCHER = MethodMatcher.create()
          .typeDefinition("com.sun.org.apache.xpath.internal.XPathAPI")
          .name("selectNodeList")
          .withNoParameterConstraint();

  private static final MethodMatcher SUNXPATH_SELECT_SINGLE_NODE_MATCHER = MethodMatcher.create()
          .typeDefinition("com.sun.org.apache.xpath.internal.XPathAPI")
          .name("selectSingleNode")
          .withNoParameterConstraint();

  private static final MethodMatcher APACHESEC_XPATH_EVAL_LIST_MATCHER = MethodMatcher.create()
          .typeDefinition("org.apache.xml.security.utils.XPathAPI")
          .name("evaluate")
          .withNoParameterConstraint();

  private static final MethodMatcher APACHESEC_XPATH_SELECT_NODE_LIST_MATCHER = MethodMatcher.create()
          .typeDefinition("org.apache.xml.security.utils.XPathAPI")
          .name("selectNodeList")
          .withNoParameterConstraint();

  private static final MethodMatcher JXPATH_GET_VALUE_MATCHER = MethodMatcher.create()
          .typeDefinition("org.apache.commons.jxpath.JXPathContext")
          .name("getValue")
          .withNoParameterConstraint();

  private static final MethodMatcher JXPATH_ITERATE_MATCHER = MethodMatcher.create()
          .typeDefinition("org.apache.commons.jxpath.JXPathContext")
          .name("iterate")
          .withNoParameterConstraint();

  private static final MethodMatcher JXPATH_GET_POINTER_MATCHER = MethodMatcher.create()
          .typeDefinition("org.apache.commons.jxpath.JXPathContext")
          .name("getPointer")
          .withNoParameterConstraint();

  private static final MethodMatcher JDOM_NEW_INSTANCE_MATCHER = MethodMatcher.create()
          .typeDefinition("org.jdom.xpath.XPath")
          .name("newInstance")
          .withNoParameterConstraint();

  private static final MethodMatcher SAXON_COMPILE_MATCHER = MethodMatcher.create()
          .typeDefinition("net.sf.saxon.xpath.XPathEvaluator")
          .name("compile")
          .withNoParameterConstraint();

  private static final MethodMatcher XMLDB_QUERY_MATCHER = MethodMatcher.create()
          .typeDefinition("org.xmldb.api.modules.XPathQueryService")
          .name("query")
          .withNoParameterConstraint();

  public static final Collection<MethodMatcher> matchers =
          ImmutableList.of(XML_XPATH_EVAL_MATCHER, XML_XPATH_COMPILE_MATCHER, XALAN_XPATH_EVAL_MATCHER, XALAN_XPATH_SELECT_NODE_ITER_MATCHER,
                  XALAN_XPATH_SELECT_NODE_LIST_MATCHER, XALAN_XPATH_SELECT_SINGLE_NODE_MATCHER,SUN_XPATH_EVAL_MATCHER,
                  SUN_XPATH_SELECT_NODE_ITER_MATCHER, SUN_XPATH_SELECT_NODE_LIST_MATCHER, SUNXPATH_SELECT_SINGLE_NODE_MATCHER,
                  APACHESEC_XPATH_EVAL_LIST_MATCHER, APACHESEC_XPATH_SELECT_NODE_LIST_MATCHER,JXPATH_GET_VALUE_MATCHER, JXPATH_ITERATE_MATCHER,
                  JXPATH_GET_POINTER_MATCHER, JDOM_NEW_INSTANCE_MATCHER, SAXON_COMPILE_MATCHER, XMLDB_QUERY_MATCHER);

  private XPathMatchers() {
    throw new IllegalAccessError("Cannot instantiate this utility class.");
  }

  public static boolean matchesConstructor(NewClassTree newClassTree) {
    Type symbolType = newClassTree.symbolType();
    return (symbolType.is("org.jaxen.dom.DOMXPath"))
        && "DOMXPath".equals(symbolType.name());
  }
}
