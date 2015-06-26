package tmp;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

public class TestJsonToXML
{
    public static String xmlString = "<json>"
                              + "<currentPage>2</currentPage>"
                              + "<products>"
                              + "<descriptionOverview> When you were young...</descriptionOverview>"
                              + "<showInCarousel>false</showInCarousel>"
                              + "<subCategory>null</subCategory>"
                              + "<descriptionDetail>Makes you think of the good old times </descriptionDetail>"
                              + "<id>43</id>"
                              + "<priceAsString>29.10</priceAsString>"
                              + "<name>Selfmade Mac and Cheese</name>"
                              + "<minimumPrice>29.1</minimumPrice>"
                              + "<imageURL>/assets/img/products/Food/Grillables/Grillables_3.jpg</imageURL>"
                              + "<topCategory>null</topCategory>"
                              + "<showInTopCategorie>true</showInTopCategorie>"
                              + "</products>"
                              + "<products>"
                              + "<descriptionOverview>An honest tribute to fast food.</descriptionOverview>"
                              + "<showInCarousel>false</showInCarousel>"
                              + "<subCategory>null</subCategory>"
                              + "<descriptionDetail>For all who isle at the grocery store</descriptionDetail>"
                              + "<id>46</id>"
                              + "<priceAsString>11.12</priceAsString>"
                              + "<name>Frozen Pizza</name>"
                              + "<minimumPrice>11.12</minimumPrice>"
                              + "<imageURL>/assets/img/products/Food/Grillables/Grillables_6.jpg</imageURL>"
                              + "<topCategory>null</topCategory>"
                              + "<showInTopCategorie>true</showInTopCategorie>"
                              + "</products>" + "</json>";

    public static void main(final String args[])
    {
        try
        {
            final String xml = "<resp><status>good</status><msg>hi</msg></resp>";

            final InputSource source = new InputSource(new StringReader(TestJsonToXML.xmlString));

            final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            final DocumentBuilder db = dbf.newDocumentBuilder();
            final Document document = db.parse(source);

            final XPathFactory xpathFactory = XPathFactory.newInstance();
            final XPath xpath = xpathFactory.newXPath();

            final String msg = xpath.evaluate("/json/currentPage", document);
            final String status = xpath.evaluate("/json/products[3]", document);

            System.out.println( msg + ":" + status);
        }
        catch (final Exception e)
        {
            e.printStackTrace();
        }
    }
}
