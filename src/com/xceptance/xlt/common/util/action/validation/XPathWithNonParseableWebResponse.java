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
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.common.util.ConcreteNodeList;
import com.xceptance.xlt.common.util.ParameterUtils;
/**
 * <p>
 * Implementation of {@link XPathGetable}. <br>
 * The class is intended to offer ways to select elements by passing a xpath for {@link WebReponse} objects, whose body
 * can NOT be parsed into a {@link HtmlPage}. E.g, XML or JSON content. <br>
 * </p>
 * <p>
 * In this case it is tried to offer alternative parsing possibilities to allow
 * the selection of elements by xpath.  
 * If this is also not possible and it is tried to select elements by xpah,
 * it throws an IllegalArgumentException.
 * </p>
 * <ul>
 * <li>Parses WebResponse only if the mime-type is supported (see {@link #SUPPORTEDHEADERCONTENTTYPES supported types}).
 * <li>Use {@link #isXPathable(mime-type)} to see if a response is parsable.
 * <li>Parses automatically and only when needed via {@link #getByXPath(String)}.
 * <li>If the content of a WebResponse is not parsable, but the method {@link #getByXPath(String)} is not calles,
 * nothing happens.
 * </ul>
 * 
 * @author matthias mitterreiter
 */
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

    /**
     * Parses the {@link WebResponse} in a format that allows to select
     * elements by xpath, but only if necessary and only once. <br>
     * The elements then get parsed to Strings <br>
     * @throws IllegalArgumentException
     *  if it is not possible to parse and select.
     * 
     */
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

    /**
     * 
     * @param contentType mime-type of the WebResponse
     * @return true only if the content can be parsed so that elements can be selected by xpath.
     */
    public boolean isXPathable(final String contentType)
    {
        return SUPPORTEDHEADERCONTENTTYPES.containsKey(contentType);
    }
}
