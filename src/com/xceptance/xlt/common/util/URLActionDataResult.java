package com.xceptance.xlt.common.util;

import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.xceptance.xlt.api.htmlunit.LightWeightPage;

public class URLActionDataResult
{
    private HtmlPage htmlPage = null;

    private LightWeightPage lightHtmlPage = null;

    private WebResponse response = null;

    public URLActionDataResult(final HtmlPage htmlPage)
    {
        ParameterUtils.isNotNull(htmlPage, "HtmlPage");
        this.htmlPage = htmlPage;
    }

    public URLActionDataResult(final LightWeightPage lightHtmlPage)
    {
        ParameterUtils.isNotNull(lightHtmlPage, "LightWeightPage");
        this.lightHtmlPage = lightHtmlPage;
    }

    public URLActionDataResult(final WebResponse response)
    {
        ParameterUtils.isNotNull(response, "WebResponse");
        this.response = response;
    }

    public Boolean isHtmlPage()
    {
        return this.htmlPage != null;
    }

    public Boolean isLightWeightPage()
    {
        return this.lightHtmlPage != null;
    }

    public Boolean isXhrWebResponse()
    {
        return this.response != null;
    }

    public HtmlPage getHtmlPage()
    {
        return this.htmlPage;
    }

    public LightWeightPage getLightHtmlPage()
    {
        return this.lightHtmlPage;
    }

    public WebResponse getResponse()
    {
        return this.response;
    }
}
