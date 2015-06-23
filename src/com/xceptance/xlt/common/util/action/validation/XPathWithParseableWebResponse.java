package com.xceptance.xlt.common.util.action.validation;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.gargoylesoftware.htmlunit.StringWebResponse;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HTMLParser;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.common.util.ParameterUtils;

public class XPathWithParseableWebResponse implements XPathGetable
{
    private HtmlPage htmlPage;

    private WebResponse webResponse;

    static final HashMap<String, String> HEADERCONTENTTYPES = new HashMap<String, String>();

    static final String HTML = "html";

    static
    {
        HEADERCONTENTTYPES.put("text/html", HTML);
        HEADERCONTENTTYPES.put("text/application", HTML);
    }

    public XPathWithParseableWebResponse(final WebResponse webResponse)
    {
        XltLogger.runTimeLogger.debug("Creating new Instance");
        setWebResponse(webResponse);
    }

    private void setWebResponse(final WebResponse webResponse)
    {
        ParameterUtils.isNotNull(webResponse, "WebResponse");
        if (isWebResponseParseable(webResponse))
        {
            this.webResponse = webResponse;
        }
        else
        {
            throw new IllegalArgumentException("WebResponse of Type: '"
                                               + getContentTypeFromResponse(webResponse)
                                               + "' is not parseable!");
        }
    }

    public static boolean isWebResponseParseable(final WebResponse webResponse)
    {
        final String contentType = webResponse.getContentType();
        return HEADERCONTENTTYPES.containsKey(contentType);
    }

    private String getContentTypeFromResponse(final WebResponse webResponse)
    {
        return webResponse.getContentType();
    }

    @Override
    public List<String> getByXPath(final String xPath)
    {
        try
        {
            loadDataFromWebResponseIfNecessary();
        }
        catch (final IOException e)
        {
            throw new IllegalArgumentException("Failed to fetch Elements: "
                                               + e.getMessage(), e);
        }
        final List<DomNode> htmlElements = getHtmlElementListByXPath(xPath);
        final List<String> resultList = getStringListFromHtmlElementList(htmlElements);
        return resultList;
    }

    private List<String> getStringListFromHtmlElementList(
                                                          final List<DomNode> htmlElements)
    {
        final List<String> resultList = new ArrayList<String>();
        for (final DomNode element : htmlElements)
        {
            final String elementAsString = element.getTextContent();
            XltLogger.runTimeLogger.debug("Found Element: " + elementAsString);
            resultList.add(elementAsString);
            /*
             * Problem with images ect.
             */
        }
        return resultList;
    }

    private void loadDataFromWebResponseIfNecessary() throws IOException
    {
        if (this.htmlPage == null)
        {
            createHtmlPageFromWebResponse();
        }
    }

    private List<DomNode> getHtmlElementListByXPath(final String xPath)
    {
        XltLogger.runTimeLogger.debug("Getting Elements by XPath: " + xPath);
        List<DomNode> htmlElements = (List<DomNode>) htmlPage.getByXPath(xPath);
        if(htmlElements == null){
            XltLogger.runTimeLogger.debug("No Elements found!");
            htmlElements = Collections.<DomNode> emptyList();
        }
        return htmlElements;
    }

    private void createHtmlPageFromWebResponse() throws IOException
    {
        XltLogger.runTimeLogger.debug("Creating HtmlPage from WebResponse");
        final URL url = webResponse.getWebRequest().getUrl();
        final StringWebResponse response = new StringWebResponse(webResponse.getContentAsString(),
                                                                 url);
        this.htmlPage = HTMLParser.parseHtml(response,
                                             new WebClient().getCurrentWindow());
    }

}
