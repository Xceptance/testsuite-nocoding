package com.xceptance.xlt.common.util.action.validation;

import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.common.util.ParameterUtils;

/**
 * Offers a gentle way and various methods to access data of a {@link WebRespone}.<br>
 * Access Headers :
 * <ul>
 * <li> {@link #getHeaderByName(String)}
 * <li> {@link #getHeaders()}
 * <li> {@link #getCookie()}, {@link #getCookieByName(String)}, {@link #getHttpCookies()}
 * </ul>
 * Access Body :
 * <ul>
 * <li> {@link #getByRegEx(String)}
 * <li> {@link #getByXPath(String)}
 * </ul>
 * Get response code: {@link #getHttpResponseCode()} <br>
 * Get RAW response {@link #getWebResponse()}
 * 
 * @author matthias mitterreiter
 */
public class URLActionDataExecutableResult
{
    private XPathGetable xPathGetable;

    private WebResponse webResponse;

    /**
     * @param webResponse
     *            the {@link WebResponse}.
     * @param xPathGetable
     *            a {@link XPathGetable}
     */
    public URLActionDataExecutableResult(final WebResponse webResponse,
                                         final XPathGetable xPathGetable)
    {
        XltLogger.runTimeLogger.debug("Creating new Instance");
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

    /**
     * See {@link XPathGetable}.
     * 
     * @param xPath
     *            a XPath pattern
     * @return list of all the found nodes in 'String' representation.
     */
    public List<String> getByXPath(final String xPath)
    {
        return xPathGetable.getByXPath(xPath);
    }

    /**
     * Compiles the pattern and scans the request body for matches.
     * 
     * @param regex
     *            the regex pattern.
     * @return list of all the matches.
     */
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
    /**
     * Compiles the pattern and scans the request body for matches.
     * 
     * @param regex
     *            the regex pattern.
     * @return list of all the matches.
     */
    public List<String> getByRegExGroup(final String regex, final int group)
    {
        final List<String> resultList = new ArrayList<String>();
        final Pattern pattern = Pattern.compile(regex);
        final Matcher matcher = pattern.matcher(webResponse.getContentAsString());
        while (matcher.find())
        {
            resultList.add(matcher.group(group));
        }
        return resultList;
    }

    /**
     * @param headerName
     *            the name of the header
     * @return a list of all the headers with name 'headerName'
     */
    public List<String> getHeaderByName(final String headerName)
    {
        final List<String> resultList = new ArrayList<String>();
        final List<NameValuePair> headers = this.webResponse.getResponseHeaders();
        for (final NameValuePair header : headers)
        {
            if (header.getName().equals(headerName))
            {
                resultList.add(header.getValue());
            }
        }
        return resultList;
    }

    public List<NameValuePair> getHeaders()
    {
        return webResponse.getResponseHeaders();
    }

    /**
     * @return {@link #webResponse}
     */
    public WebResponse getWebResponse()
    {
        return this.webResponse;
    }

    /**
     * @return the http response code
     */
    public int getHttpResponseCode()
    {
        return webResponse.getStatusCode();
    }

    /**
     * @param cookieName
     *            name of the cookie
     * @return all cookies with name "cookieName" of all the "Set-Cookie" headers in {@link NameValuePair}
     *         representation.
     */
    public List<NameValuePair> getCookieByName(final String cookieName)
    {
        final List<HttpCookie> cookies = getSetCookiesFromHeaders();

        final List<NameValuePair> resultCookies = new ArrayList<NameValuePair>();

        for (final HttpCookie cookie : cookies)
        {
            if (cookie.getName().equals(cookieName))
            {
                resultCookies.add(new NameValuePair(cookie.getName(),
                                                    cookie.getValue()));
            }
        }
        return resultCookies;
    }

    /**
     * @param cookieName
     *            name of the cookie
     * @return all cookie values of cookies with name "cookieName" of all the "Set-Cookie" headers in {@link String}
     *         representation.
     */
    public List<String> getCookieAsStringByName(final String cookieName)
    {
        final List<NameValuePair> cookies = this.getCookieByName(cookieName);
        final List<String> resultCookies = new ArrayList<String>();
        for (final NameValuePair cookie : cookies)
        {
            resultCookies.add(cookie.getValue());
        }
        return resultCookies;
    }

    /**
     * @return all cookies of all the "Set-Cookie" headers in {@link NameValuePair} representation.
     */
    public List<NameValuePair> getCookie()
    {
        final List<HttpCookie> cookies = getSetCookiesFromHeaders();

        final List<NameValuePair> resultCookies = new ArrayList<NameValuePair>();

        for (final HttpCookie cookie : cookies)
        {

            resultCookies.add(new NameValuePair(cookie.getName(),
                                                cookie.getValue()));
        }
        return resultCookies;
    }

    private List<HttpCookie> getSetCookiesFromHeaders()
    {
        final List<NameValuePair> headers = this.getHeaders();

        final List<HttpCookie> cookies = new ArrayList<HttpCookie>();
        for (final NameValuePair header : headers)
        {
            if (header.getName().equalsIgnoreCase("Set-Cookie"))
            {
                cookies.addAll(HttpCookie.parse(header.getValue()));
            }
        }
        return cookies;
    }

    /**
     * For debugging purpose! <br>
     * Be careful, {@link HttpCookie} is from "java.net.HttpCookie" and not "HtmlUnit"!
     * 
     * @return List<{@link HttpCookie}>, all cookies from the reponse.
     */
    public List<HttpCookie> getHttpCookies()
    {
        final List<HttpCookie> cookies = getSetCookiesFromHeaders();
        return cookies;
    }

}
