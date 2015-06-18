package com.xceptance.xlt.common.util.action.validation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.common.util.ParameterUtils;

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

        final List<String> resultList = new ArrayList<String>();
        for (final DomNode element : htmlElements)
        {
            final String elementAsString = element.asText();
            XltLogger.runTimeLogger.debug("Found Element: " + "\"" + elementAsString + "\"");
            resultList.add(elementAsString);
        }
        return resultList;
    }

    private List<DomNode> getHtmlElementListByXPath(final String xPath)
    {
        XltLogger.runTimeLogger.debug("Getting Elements by XPath: " + xPath);
        List<DomNode> htmlElements = (List<DomNode>) this.htmlPage.getByXPath(xPath);
        if (htmlElements == null)
        {
            XltLogger.runTimeLogger.debug("No Elements found!");
            htmlElements = Collections.<DomNode> emptyList();
        }
        return htmlElements;
    }

}
