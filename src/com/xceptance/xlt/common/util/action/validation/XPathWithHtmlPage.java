package com.xceptance.xlt.common.util.action.validation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.common.util.ParameterUtils;

/**
 * Implementation of {@link XPathGetable}. <br>
 * Offers a simple way to select select elements of an {@link HtmlPage} by passing a xpath. via
 * {@link #getByXPath(String)}.
 * 
 * @author matthias mitterreiter
 */

public class XPathWithHtmlPage implements XPathGetable
{
    private HtmlPage htmlPage;

    public XPathWithHtmlPage(final HtmlPage htmlPage)
    {
        XltLogger.runTimeLogger.debug("Creating new Instance");
        setHtmlPage(htmlPage);
    }

    private void setHtmlPage(final HtmlPage htmlPage)
    {
        ParameterUtils.isNotNull(htmlPage, "HtmlPage");
        this.htmlPage = htmlPage;
    }

    @Override
    public List<String> getByXPath(final String xPath)
    {
        final List<DomNode> htmlElements = getHtmlElementListByXPath(xPath);
        final List<String> resultList = parseHtmlElementsIntoStrings(htmlElements);

        return resultList;
    }

    private List<String> parseHtmlElementsIntoStrings(final List<DomNode> htmlNodes)
    {
        final List<String> resultList = new ArrayList<String>();

        for (final DomNode node : htmlNodes)
        {
            final String elementAsString = getStringFromHtmlElement(node);
            XltLogger.runTimeLogger.debug("Found Element: " + "\""
                                          + elementAsString + "\"");
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

    @SuppressWarnings("unchecked")
	private List<DomNode> getHtmlElementListByXPath(final String xPath)
    {
        XltLogger.runTimeLogger.debug("Getting Elements by XPath: " + xPath);
        
        List<DomNode> htmlElements = Collections.<DomNode> emptyList();
        
        try
        {
            htmlElements = (List<DomNode>) this.htmlPage.getByXPath(xPath);
            
            if (htmlElements == null)
            {
                XltLogger.runTimeLogger.debug("No Elements found!, XPath: " + xPath);
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

}
