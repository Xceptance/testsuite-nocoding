package test.com.xceptance.xlt.common.util.action.validation;

import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.xceptance.xlt.api.htmlunit.LightWeightPage;
import com.xceptance.xlt.common.util.ParameterUtils;
import com.xceptance.xlt.common.util.action.validation.URLActionDataExecutableResult;
import com.xceptance.xlt.common.util.action.validation.XPathGetable;
import com.xceptance.xlt.common.util.action.validation.XPathWithHtmlPage;
import com.xceptance.xlt.common.util.action.validation.XPathWithLightWeightPage;
import com.xceptance.xlt.common.util.action.validation.XPathWithNonParseableWebResponse;
import com.xceptance.xlt.common.util.action.validation.XPathWithParseableWebResponse;

public class URLActionDataExecutableResultFactory
{
    public URLActionDataExecutableResultFactory()
    {

    }

    URLActionDataExecutableResult getResult(final HtmlPage htmlPage)
    {
        ParameterUtils.isNotNull(htmlPage, "HtmlPage");
        final WebResponse webResponse = htmlPage.getWebResponse();
        final XPathWithHtmlPage xPathWithHtmlPage = new XPathWithHtmlPage(htmlPage);
        return null;
    }

    URLActionDataExecutableResult getResult(
                                            final LightWeightPage lightWeightPage)
    {
        ParameterUtils.isNotNullMessages(lightWeightPage, "LightWeightPage");
        final WebResponse webResponse = lightWeightPage.getWebResponse();
        final XPathWithLightWeightPage xPathWithLightWeightPage = new XPathWithLightWeightPage();
        return null;
    }

    URLActionDataExecutableResult getResult(final WebResponse webResponse)
    {
        ParameterUtils.isNotNull(webResponse, "WebResponse");
        XPathGetable xPathGetble = null;
        if (XPathWithParseableWebResponse.isWebResponseParseable(webResponse))
        {
            xPathGetble = new XPathWithParseableWebResponse(webResponse);
        }
        else{
            xPathGetble= new XPathWithNonParseableWebResponse(webResponse);
        }
        return null;
    }
}
