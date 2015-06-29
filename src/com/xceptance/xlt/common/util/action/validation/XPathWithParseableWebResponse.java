package com.xceptance.xlt.common.util.action.validation;

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

/**
 * <p>
 * Implementation of {@link XPathGetable}. <br>
 * The class is intended to offer ways to select elements by passing a xpath for {@link WebReponse} objects, whose body
 * can be parsed into a {@link HtmlPage}. <br>
 * </p>
 * <ul>
 * <li>Parses WebResponse only if the mime-type is supported (see {@link #HEADERCONTENTTYPES supported types}).
 * <li>Use {@link #isWebResponseParseable(WebResponse)} to see if a response is parsable.
 * <li>Parses automatically and only when needed via {@link #getByXPath(String)}.
 * </ul>
 * 
 * @author matthias mitterreiter
 */
public class XPathWithParseableWebResponse implements XPathGetable
{
    private HtmlPage htmlPage;

    private WebResponse webResponse;

    /**
     * Contains all the supported mime types. Supported mime types so far:
     * <ul>
     * <li>Html:
     * <ul>
     * <li>text/html
     * <li>text/application
     * </ul>
     * </ul>
     */
    static final HashMap<String, String> HEADERCONTENTTYPES = new HashMap<String, String>();

    static final String HTML = "html";

    static final String TEXTHTML = "text/html";

    static final String TEXTAPPLICATION = "text/application";

    static
    {
        HEADERCONTENTTYPES.put(TEXTHTML, HTML);
        HEADERCONTENTTYPES.put(TEXTAPPLICATION, HTML);
    }

    /**
     * @param webResponse
     *            the WebResponse whose body should be parsed.
     */
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

    /**
     * @param webResponse
     * @return true only if webResponse is parsable.
     */
    public static boolean isWebResponseParseable(final WebResponse webResponse)
    {
        final String contentType = webResponse.getContentType();
        return HEADERCONTENTTYPES.containsKey(contentType);
    }

    private String getContentTypeFromResponse(final WebResponse webResponse)
    {
        return webResponse.getContentType();
    }

    /**
     * Parses the {@link WebResponse} in a {@link HtmlPage} only if necessary and only once. <br>
     * The elements get parsed to String via {@link DomNode}.getTextContent(); <br>
     * It depends onto the type of node, what comes back.
     */
    @Override
    public List<String> getByXPath(final String xPath)
    {

        loadDataFromWebResponseIfNecessary();

        final List<DomNode> htmlElements = getHtmlElementListByXPath(xPath);
        final List<String> resultList = getStringListFromHtmlElementList(htmlElements);
        return resultList;
    }

    private List<String> getStringListFromHtmlElementList(final List<DomNode> htmlNodes)
    {
        final List<String> resultList = new ArrayList<String>();
        for (final DomNode node : htmlNodes)
        {
            final String elementAsString = getStringFromHtmlElement(node);
            XltLogger.runTimeLogger.debug("Found Element: " + elementAsString);
            resultList.add(elementAsString);
        }
        return resultList;
    }

    private String getStringFromHtmlElement(final DomNode node)
    {
        try
        {
            final String elementAsString = node.getTextContent();
            return elementAsString;
        }
        catch (final Exception e)
        {
            throw new IllegalArgumentException("Failed to parse DomNode: "
                                               + node.getLocalName()
                                               + " of type: "
                                               + node.getNodeType()
                                               + " to String because: "
                                               + e.getMessage());
        }
    }

    private void loadDataFromWebResponseIfNecessary()
    {
        if (this.htmlPage == null)
        {
            createHtmlPageFromWebResponse();
        }
    }

    private List<DomNode> getHtmlElementListByXPath(final String xPath)
    {
        XltLogger.runTimeLogger.debug("Getting Elements by XPath: " + xPath);

        List<DomNode> htmlElements = Collections.<DomNode> emptyList();

        try
        {
            htmlElements = (List<DomNode>) this.htmlPage.getByXPath(xPath);

            if (htmlElements == null)
            {
                XltLogger.runTimeLogger.debug("No Elements found!, XPath: "
                                              + xPath);
                htmlElements = Collections.<DomNode> emptyList();
            }
        }
        catch (final Exception e)
        {
            throw new IllegalArgumentException("Failed to get Elements by XPath: "
                                               + xPath
                                               + ", Because: "
                                               + e.getMessage());
        }
        return htmlElements;
    }

    private void createHtmlPageFromWebResponse()
    {
        XltLogger.runTimeLogger.debug("Creating HtmlPage from WebResponse");
        try
        {
            final URL url = webResponse.getWebRequest().getUrl();
            final StringWebResponse response = new StringWebResponse(webResponse.getContentAsString(),
                                                                     url);
            this.htmlPage = HTMLParser.parseHtml(response,
                                                 new WebClient().getCurrentWindow());
        }
        catch (final Exception e)
        {
            throw new IllegalArgumentException("Failed to create a HtmlPage from WebResponse, because: "
                                               + e.getMessage());
        }
    }

}
