package com.xceptance.xlt.common.util.action.validation;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.xml.xpath.XPath;

import com.gargoylesoftware.htmlunit.StringWebResponse;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.HTMLParser;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.xceptance.xlt.common.util.ParameterUtils;

public class XPathWithWebResponse implements XPathGetable
{
    private WebResponse webResponse;

    private XPath xPath;

    private String xmlString;

    static final HashMap<String, String> HEADERCONTENTTYPES = new HashMap<String, String>();

    static final String JSON = "json";

    static final String HTML = "html";

    static final String XML = "xml";

    static
    {
        HEADERCONTENTTYPES.put("application/json", JSON);
        HEADERCONTENTTYPES.put("text/json", JSON);
        HEADERCONTENTTYPES.put("text/x-json", JSON);

        HEADERCONTENTTYPES.put("text/xml", XML);
        HEADERCONTENTTYPES.put("application/xml", XML);

        HEADERCONTENTTYPES.put("text/html", HTML);
        HEADERCONTENTTYPES.put("text/application", HTML);

    }

    public XPathWithWebResponse(final WebResponse webResponse)
    {
        setWebResponse(webResponse);
    }

    private void setWebResponse(final WebResponse webResponse)
    {
        ParameterUtils.isNotNull(webResponse, "WebResponse");
        this.webResponse = webResponse;
    }

    @Override
    public List<String> getByXPath(final String xPath)
    {
        final List<String> resultList;

        final String contentType = getContentTypeFromResponse();

        if (isXPathable(contentType))
        {
            resultList = getXPathFromWebResponse(xPath);
        }
        else
        {
            throw new IllegalArgumentException("Cannot use XPath on Content of Type: '"
                                               + contentType + "'");
        }
        return resultList;
    }

    private String getContentTypeFromResponse()
    {
        return this.webResponse.getContentType();
    }

    private boolean isXPathable(final String contentType)
    {
        return HEADERCONTENTTYPES.containsKey(contentType);
    }

    private List<String> getXPathFromWebResponse(final String xPath)
    {
        if (this.xmlString == null)
        {
            createXMLFromWebResponseContent();
        }
        if (this.xPath == null)
        {
            //
        }
        return null;
    }

    private void createXMLFromWebResponseContent()
    {

        final String contentType = getContentTypeFromResponse();

        final String bodyContent = getContentFromResponse();

        if (HEADERCONTENTTYPES.get(contentType).equals(JSON))
        {
            this.xmlString = createXMLStringFromJson(bodyContent);
        }
        else if (HEADERCONTENTTYPES.get(contentType).equals(XML))
        {
            this.xmlString = createXMLStringFromXML(bodyContent);
        }
        else if (HEADERCONTENTTYPES.get(contentType).equals(HTML))
        {
            this.xmlString = createXMLStringFromHtml(bodyContent);
        }
        else
        {
            throw new IllegalArgumentException();
        }
    }

    private String getContentFromResponse()
    {
        return this.webResponse.getContentAsString();
        //check for null
    }

    private String createXMLStringFromJson(final String json)
    {
        final String xmlString = org.json.XML.toString(json);
        return "<json>" + xmlString + "</json>";
    }

    private String createXMLStringFromXML(final String xml)
    {
        return xml;
    }

    private String createXMLStringFromHtml(final String html) throws IOException
    {
        final StringWebResponse stringResponse = new StringWebResponse(html, webResponse.getWebRequest().getUrl());
        final HtmlPage page = HTMLParser.parseHtml(stringResponse, new WebClient().getCurrentWindow());
        return null;
    }

    private void initXPath()
    {

    }

}
