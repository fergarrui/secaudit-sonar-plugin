import java.io.ByteArrayInputStream;
import java.io.InputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

class A {

  String xml = "<xml></xml>";

  void documentParseXML() {
    DocumentBuilderFactory factory =
            DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = factory.newDocumentBuilder();

    ByteArrayInputStream input =  new ByteArrayInputStream(
            xml.toString().getBytes("UTF-8"));
    Document doc = builder.parse(input); // Noncompliant [[sc=5;ec=41]]
  }

  void xmlStreamReaderParseXml() {
    XMLInputFactory factory = XMLInputFactory.newFactory();
    InputStream inputStream = new ByteArrayInputStream(xml.getBytes());
    XMLStreamReader reader = factory.createXMLStreamReader(inputStream); // Noncompliant [[sc=5;ec=73]]
  }

  void saxParserParseXml() {
    SAXParserFactory factory = SAXParserFactory.newInstance();
    InputStream inputStream = new ByteArrayInputStream(xml.getBytes());
    SAXParser saxParser =  factory.newSAXParser(); // Noncompliant [[sc=5;ec=51]]
    saxParser.parse(inputStream, new SaxHandler());
  }

  void xmlReaderParseXml() {
    XMLReader xmlReader = XMLReaderFactory.createXMLReader(); // Noncompliant [[sc=5;ec=62]]
    InputStream inputStream = new ByteArrayInputStream(xml.getBytes());
    xmlReader.setContentHandler(new SaxHandler());
    xmlReader.parse(new InputSource(inputStream));
  }

  static class SaxHandler extends DefaultHandler {
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
      System.out.println(new String(ch, start, length));
    }
  }
}
