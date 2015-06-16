package com.xceptance.xlt.common.util.action.validation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.xceptance.xlt.common.util.ParameterUtils;

public class XPathWithHtmlPage implements XPathGetable
{
    private HtmlPage htmlPage;

    public XPathWithHtmlPage(final HtmlPage htmlPage)
    {
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
        final List<HtmlElement> htmlElements = getHtmlElementListByXPath(xPath);

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

    private List<HtmlElement> getHtmlElementListByXPath(final String xPath)
    {
        final List<HtmlElement> htmlElements = (List<HtmlElement>) htmlPage.getByXPath(xPath);

        return (htmlElements != null ? htmlElements
                                    : Collections.<HtmlElement> emptyList());
    }

}
