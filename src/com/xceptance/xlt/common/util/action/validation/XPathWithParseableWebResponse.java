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
import com.gargoylesoftware.htmlunit.html.HTMLParser;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
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
        final List<HtmlElement> htmlElements = getHtmlElementListByXPath(xPath);
        final List<String> resultList = getStringListFromHtmlElementList(htmlElements);
        return resultList;
    }

    private List<String> getStringListFromHtmlElementList(
                                                          final List<HtmlElement> htmlElements)
    {
        final List<String> resultList = new ArrayList<String>();
        for (final HtmlElement element : htmlElements)
        {
            resultList.add(element.asText());
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

    private List<HtmlElement> getHtmlElementListByXPath(final String xPath)
    {
        final List<HtmlElement> htmlElements = (List<HtmlElement>) htmlPage.getByXPath(xPath);

        return (htmlElements != null ? htmlElements
                                    : Collections.<HtmlElement> emptyList());
    }

    private void createHtmlPageFromWebResponse() throws IOException
    {
        final URL url = webResponse.getWebRequest().getUrl();
        final StringWebResponse response = new StringWebResponse(webResponse.getContentAsString(),
                                                                 url);
        this.htmlPage = HTMLParser.parseHtml(response,
                                             new WebClient().getCurrentWindow());
    }

}
