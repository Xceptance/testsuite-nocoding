package com.xceptance.xlt.common.util.action.validation;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.gargoylesoftware.htmlunit.WebResponse;
import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.common.util.ConcreteNodeList;
import com.xceptance.xlt.common.util.ParameterUtils;

public class XPathWithNonParseableWebResponse implements XPathGetable
{
    private WebResponse webResponse;

    private XPath xPath;

    private Document xmlInputSource;

    static final HashMap<String, String> SUPPORTEDHEADERCONTENTTYPES = new HashMap<String, String>();

    static final String JSON = "json";

    static final String XML = "xml";

    static
    {
        SUPPORTEDHEADERCONTENTTYPES.put("application/json", JSON);
        SUPPORTEDHEADERCONTENTTYPES.put("text/json", JSON);
        SUPPORTEDHEADERCONTENTTYPES.put("text/x-json", JSON);

        SUPPORTEDHEADERCONTENTTYPES.put("text/xml", XML);
        SUPPORTEDHEADERCONTENTTYPES.put("application/xml", XML);
    }

    public XPathWithNonParseableWebResponse(final WebResponse webResponse)
    {
        XltLogger.runTimeLogger.debug("Creating new Instance");
        setWebResponse(webResponse);
    }

    private void setWebResponse(final WebResponse webResponse)
    {
        ParameterUtils.isNotNull(webResponse, "WebResponse");

        final String contentType = webResponse.getContentType();

        if (isXPathable(contentType))
        {
            this.webResponse = webResponse;
        }
        else
        {
            throw new IllegalArgumentException("Parsing content of type: '"
                                               + contentType
                                               + "' is not supported");
        }
    }

    @Override
    public List<String> getByXPath(final String xPath)
    {
        List<String> resultList = new ArrayList<String>();

        try
        {
            resultList = getByXPathFromInputSource(xPath);
        }
        catch (final Exception e)
        {
            throw new IllegalArgumentException("Failed to fetch Elements: "
                                               + e.getMessage(), e);
        }
        return resultList;
    }

    private List<String> getByXPathFromInputSource(final String xPath)
        throws XPathExpressionException, ParserConfigurationException,
        SAXException, IOException
    {
        loadContentFromWebResponseIfNecessary();
        final NodeList nodeList = createNodeListByXPathFromInputSource(xPath);
        final List<String> resultList = createStringListFromNodeList(nodeList);
        return resultList;
    }

    private void loadContentFromWebResponseIfNecessary()
        throws ParserConfigurationException, SAXException, IOException
    {
        loadXMLSourceFromWebResponseIfNecessary();
        createXPathIfNecessary();
    }

    private void loadXMLSourceFromWebResponseIfNecessary()
        throws ParserConfigurationException, SAXException, IOException
    {
        if (this.xmlInputSource == null)
        {
            createXMLSourceFromWebResponseContent();
        }
    }

    private void createXPathIfNecessary()
    {
        if (this.xPath == null)
        {
            createXPath();
        }
    }

    private List<String> createStringListFromNodeList(final NodeList nodeList)
    {
        final List<String> resultList = new ArrayList<String>();

        for (int i = 0; i < nodeList.getLength(); i++)
        {
            final Node n = nodeList.item(i);
            resultList.add(n.getTextContent());
        }
        return resultList;
    }

    private NodeList createNodeListByXPathFromInputSource(final String xPath)

    {
        XltLogger.runTimeLogger.debug("Getting Elements by XPath: " + xPath);
        NodeList list = new ConcreteNodeList();
        try
        {
            list = (NodeList) this.xPath.compile(xPath)
                                        .evaluate(this.xmlInputSource,
                                                  XPathConstants.NODESET);
        }
        catch (final Exception e)
        {
            XltLogger.runTimeLogger.debug("Failed to get Elements: "
                                          + e.getMessage());
        }
        return list;
    }

    private void createXPath()
    {
        XltLogger.runTimeLogger.debug("Creating new XPath");
        final XPathFactory xpathFactory = XPathFactory.newInstance();
        this.xPath = xpathFactory.newXPath();
    }

    private void createXMLSourceFromWebResponseContent()
        throws ParserConfigurationException, SAXException, IOException
    {
        XltLogger.runTimeLogger.debug("Loading content from WebResponse");
        final String contentType = getContentTypeFromWebResponse();

        final String bodyContent = this.webResponse.getContentAsString();

        if (JSON.equals(contentType))
        {
            this.xmlInputSource = createXMLSourceFromJson(bodyContent);
        }
        else if (XML.equals(contentType))
        {
            this.xmlInputSource = createXMLSourceFromXML(bodyContent);
        }
        else
        {
            throw new IllegalArgumentException("This will never happen!");
        }
    }

    private String getContentTypeFromWebResponse()
    {
        final String contentType = this.webResponse.getContentType();
        final String resolvedContentType = SUPPORTEDHEADERCONTENTTYPES.get(contentType);
        return resolvedContentType;

    }

    private Document createXMLSourceFromJson(final String json)
        throws ParserConfigurationException, SAXException, IOException
    {
        XltLogger.runTimeLogger.debug("Converting Json Content to XML");
        String xmlString;
        xmlString = org.json.XML.toString(new JSONObject(json));
        xmlString = "<json>" + xmlString + "</json>";
        System.err.println(xmlString);

        final Document document = createDocumentFromXmlString(xmlString);

        return document;
    }

    private Document createDocumentFromXmlString(final String xmlString)
        throws SAXException, IOException, ParserConfigurationException
    {
        final InputSource source = new InputSource(new StringReader(xmlString));
        final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        final DocumentBuilder db = dbf.newDocumentBuilder();
        final Document document = db.parse(source);
        return document;
    }

    private Document createXMLSourceFromXML(final String xmlString) throws SAXException, IOException, ParserConfigurationException
    {
        XltLogger.runTimeLogger.debug("Loading XML Content");
        final Document document = createDocumentFromXmlString(xmlString);
        return document;
    }

    private boolean isXPathable(final String contentType)
    {
        return SUPPORTEDHEADERCONTENTTYPES.containsKey(contentType);
    }
}
