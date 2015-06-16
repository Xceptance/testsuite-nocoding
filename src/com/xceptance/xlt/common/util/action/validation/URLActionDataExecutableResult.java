package com.xceptance.xlt.common.util.action.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.xceptance.xlt.common.util.ParameterUtils;

public class URLActionDataExecutableResult
{
    private XPathGetable xPathGetable;

    private WebResponse webResponse;

    public URLActionDataExecutableResult(final WebResponse webResponse,
                                         final XPathGetable xPathGetable)
    {
        setWebResponse(webResponse);
        setXPathGetable(xPathGetable);
    }

    private void setWebResponse(final WebResponse webResponse)
    {
        ParameterUtils.isNotNull(webResponse, "WebResponse");
        this.webResponse = webResponse;
    }

    private void setXPathGetable(final XPathGetable xPathGetable)
    {
        ParameterUtils.isNotNull(xPathGetable, "XPathGetable");
        this.xPathGetable = xPathGetable;
    }

    public List<String> getByXPath(final String xPath)
    {
        return xPathGetable.getByXPath(xPath);
    }

    public List<String> getByRegEx(final String regex)
    {
        final List<String> resultList = new ArrayList<String>();
        final Pattern pattern = Pattern.compile(regex);
        final Matcher matcher = pattern.matcher(webResponse.getContentAsString());
        while (matcher.find())
        {
            resultList.add(matcher.group());
        }
        return resultList;
    }

    public List<NameValuePair> getHeaderByName(final String headerName)
    {
        final List<NameValuePair> resultList = new ArrayList<NameValuePair>();
        final List<NameValuePair> headers = this.webResponse.getResponseHeaders();
        for (final NameValuePair header : headers)
        {
            if (header.getName().equals(headerName))
            {
                resultList.add(header);
            }
        }
        return resultList;
    }

    public List<NameValuePair> getHeaders()
    {
        return webResponse.getResponseHeaders();
    }

    public WebResponse getWebResponse()
    {
        return this.webResponse;
    }

    public int getStatusCode()
    {
        return webResponse.getStatusCode();
    }

    private List<NameValuePair> getCookieByName(final String cookieName)
    {
        return null;
        // TODO
    }
}
