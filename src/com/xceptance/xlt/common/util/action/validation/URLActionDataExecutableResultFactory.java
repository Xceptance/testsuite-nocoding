package com.xceptance.xlt.common.util.action.validation;

import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.xceptance.xlt.api.htmlunit.LightWeightPage;
import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.common.util.ParameterUtils;
import com.xceptance.xlt.common.util.action.execution.URLActionDataExecutionable;

/**
 * Factory <br>
 * Creates {@link URLActionDataExecutableResult} depending on the type of response of the
 * {@link URLActionDataExecutionable}.
 * 
 * @author matthias mitterreiter
 */
public class URLActionDataExecutableResultFactory
{
    public URLActionDataExecutableResultFactory()
    {
        XltLogger.runTimeLogger.debug("Creating new Instance");
    }

    /**
     * @param htmlPage
     * @return a wrapper for a {@link HtmlPage}.
     */
    public URLActionDataExecutableResult getResult(final HtmlPage htmlPage)
    {
        ParameterUtils.isNotNull(htmlPage, "HtmlPage");
        final URLActionDataExecutableResult result = createResultFromHtmlPage(htmlPage);

        return result;
    }

    private URLActionDataExecutableResult createResultFromHtmlPage(final HtmlPage htmlPage)
    {
        final WebResponse webResponse = htmlPage.getWebResponse();
        final XPathWithHtmlPage xPathWithHtmlPage = new XPathWithHtmlPage(htmlPage);
        final URLActionDataExecutableResult result = new URLActionDataExecutableResult(webResponse, xPathWithHtmlPage);
        return result;
    }

    /**
     * @param lightWeightPage
     * @return a wrapper for a {@link LightWeightPage}.
     */
    public URLActionDataExecutableResult getResult(final LightWeightPage lightWeightPage)
    {
        ParameterUtils.isNotNullMessages(lightWeightPage, "LightWeightPage");
        final URLActionDataExecutableResult result = createResultFromLightWeightPage(lightWeightPage);

        return result;
    }

    private URLActionDataExecutableResult createResultFromLightWeightPage(final LightWeightPage lightWeightPage)
    {
        final WebResponse webResponse = lightWeightPage.getWebResponse();
        final XPathWithLightWeightPage xPathWithLightWeightPage = new XPathWithLightWeightPage();
        final URLActionDataExecutableResult result = new URLActionDataExecutableResult(webResponse, xPathWithLightWeightPage);
        return result;
    }

    /**
     * @param webResponse
     * @return a wrapper for a {@link WebResponse}.
     */
    public URLActionDataExecutableResult getResult(final WebResponse webResponse)
    {
        ParameterUtils.isNotNull(webResponse, "WebResponse");
        final URLActionDataExecutableResult result = createResultFromWebResponse(webResponse);

        return result;
    }

    private URLActionDataExecutableResult createResultFromWebResponse(final WebResponse webResponse)
    {
        final XPathGetable xPathGetble = createXPathGetableFromWebResponse(webResponse);
        final URLActionDataExecutableResult result = new URLActionDataExecutableResult(webResponse, xPathGetble);
        return result;
    }

    private XPathGetable createXPathGetableFromWebResponse(final WebResponse webResponse)
    {
        XPathGetable xPathGetble = null;

        if (XPathWithParseableWebResponse.isWebResponseParseable(webResponse))
        {
            xPathGetble = new XPathWithParseableWebResponse(webResponse);
        }
        else
        {
            xPathGetble = new XPathWithNonParseableWebResponse(webResponse);
        }
        return xPathGetble;
    }
}
