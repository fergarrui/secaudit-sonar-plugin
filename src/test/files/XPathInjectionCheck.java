import java.io.ByteArrayInputStream;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import org.jaxen.XPath;
import org.jaxen.dom.DOMXPath;
import org.w3c.dom.Document;
import org.apache.xpath.XPathAPI;
import org.apache.xpath.objects.XObject;
import org.apache.commons.jxpath.JXPathContext;
import net.sf.saxon.om.NamespaceConstant;
import net.sf.saxon.xpath.XPathEvaluator;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.modules.XPathQueryService;

class A {

  String xml = "";


  void jaxen1(String id) throws Exception{
    String xpathExpr = "/users/user[@id='" + id + "']";
    String goodXpathExpr = "/users/user[@id='1']";
    XPath expression = new DOMXPath(xpathExpr); // Noncompliant
    XPath expression2 = new DOMXPath(""); // Compliant
    XPath expression3 = new DOMXPath(goodXpathExpr); // Compliant
  }

  void xmlXpath(String id) throws Exception {
    String xpathExpr = "/users/user[@id='" + id + "']";
    String goodXpathExpr = "/users/user[@id='1']";
    Document doc = getDocument();
    javax.xml.xpath.XPath xpath = XPathFactory.newInstance().newXPath();
    String result = xpath.evaluate(xpathExpr, doc); // Noncompliant
    String result2 = xpath.evaluate(xpathExpr, doc); // Noncompliant
    XPathExpression result3 = xpath.compile(xpathExpr); // Noncompliant
    XPathExpression good1 = xpath.compile(goodXpathExpr); // Compliant
  }

  void xalanXpath(String id) throws  Exception {
    String xpathExpr = "/users/user[@id='" + id + "']";
    String goodXpathExpr = "/users/user[@id='1']";
    Document doc = getDocument();
    XObject xObject = XPathAPI.eval(doc, xpathExpr); // Noncompliant
    XPathAPI.selectNodeIterator(doc,xpathExpr); // Noncompliant
    XPathAPI.selectNodeList(doc,xpathExpr); // Noncompliant
    XPathAPI.selectSingleNode(doc,xpathExpr); // Noncompliant
  }

  void sunXpath(String id) throws  Exception {
    String xpathExpr = "/users/user[@id='" + id + "']";
    String goodXpathExpr = "/users/user[@id='1']";
    Document doc = getDocument();
    com.sun.org.apache.xpath.internal.XPathAPI.eval(doc, xpathExpr); // Noncompliant
    com.sun.org.apache.xpath.internal.XPathAPI.selectNodeIterator(doc,xpathExpr); // Noncompliant
    com.sun.org.apache.xpath.internal.XPathAPI.selectNodeList(doc,xpathExpr); // Noncompliant
    com.sun.org.apache.xpath.internal.XPathAPI.selectSingleNode(doc,xpathExpr); // Noncompliant
  }

  void apacheSecurityXpath(String id) throws  Exception {
    String xpathExpr = "/users/user[@id='" + id + "']";
    String goodXpathExpr = "/users/user[@id='1']";
    Document doc = getDocument();
    org.apache.xml.security.utils.XPathAPI xpathApi = new XalanXPathAPI();
    xpathApi.evaluate(doc, doc, xpathExpr, doc);  // Noncompliant
    xpathApi.selectNodeList(doc, doc, xpathExpr, doc);  // Noncompliant
  }

  void JXPath(String id) {
    String xpathExpr = "/users/user[@id='" + id + "']";
    String goodXpathExpr = "/users/user[@id='1']";
    JXPathContext context = JXPathContext.newContext(new Object());
    JXPathContext.newContext(context).getValue(xpathExpr); // Noncompliant
    JXPathContext.newContext(context).getValue(goodXpathExpr); // Compliant
    context.getValue(xpathExpr); // Noncompliant
    context.getValue(goodXpathExpr); // Compliant
    context.iterate(xpathExpr); // Noncompliant
    context.iterate(goodXpathExpr); // Compliant
    context.getPointer(xpathExpr); // Noncompliant
    context.getPointer(goodXpathExpr); // Compliant
  }

  void jdomXPath(String id) throws Exception {
    String xpathExpr = "/users/user[@id='" + id + "']";
    String goodXpathExpr = "/users/user[@id='1']";
    org.jdom.xpath.XPath.newInstance(xpathExpr); // Noncompliant
    org.jdom.xpath.XPath.newInstance(goodXpathExpr);
  }

  void saxonXpath(String id) throws Exception {
    String xpathExpr = "/users/user[@id='" + id + "']";
    String goodXpathExpr = "/users/user[@id='1']";
    XPathFactory xpathFactory = XPathFactory
            .newInstance(NamespaceConstant.OBJECT_MODEL_SAXON);
    XPathEvaluator xPathEvaluator = (XPathEvaluator) xpathFactory.newXPath();
    xPathEvaluator.compile(xpathExpr); // Noncompliant
    xPathEvaluator.compile(goodXpathExpr);

    javax.xml.xpath.XPath xPath = xpathFactory.newXPath();
    xPath.compile(xpathExpr); // Noncompliant
    xPath.compile(goodXpathExpr); // Compliant
  }

  void xmlDbXPath(String id) throws Exception {
    String xpathExpr = "/users/user[@id='" + id + "']";
    String goodXpathExpr = "/users/user[@id='1']";
    Collection collection = DatabaseManager.getCollection("");
    XPathQueryService xpqs = (XPathQueryService)collection.getService("s1", "1");
    ResourceSet result = xpqs.query(xpathExpr); // Noncompliant
    ResourceSet result2 = xpqs.query(goodXpathExpr);
  }

  // util
  private Document getDocument() throws Exception {
    DocumentBuilderFactory factory =
            DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = factory.newDocumentBuilder();
    ByteArrayInputStream input =  new ByteArrayInputStream(
            xml.toString().getBytes("UTF-8"));
    Document doc = builder.parse(input);
    return doc;
  }
}
