package tmp;

import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.List;

import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.xceptance.xlt.common.util.MockObjects;

public class TestCookieFunction
{
    public static List<NameValuePair> getCookieByName(final List<NameValuePair> headers,
                                                      final String cookieName)
    {
        final List<HttpCookie> cookies = new ArrayList<HttpCookie>();
        final List<NameValuePair> resultCookies = new ArrayList<NameValuePair>();

        for (final NameValuePair header : headers)
        {
            if (header.getName().equalsIgnoreCase("Set-Cookie"))
            {
                cookies.addAll(HttpCookie.parse(header.getValue()));
            }
        }
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

    public static List<NameValuePair> getCookies(final List<NameValuePair> headers)
    {
        final List<HttpCookie> cookies = new ArrayList<HttpCookie>();
        final List<NameValuePair> resultCookies = new ArrayList<NameValuePair>();

        for (final NameValuePair header : headers)
        {
            if (header.getName().equalsIgnoreCase("Set-Cookie"))
            {
                cookies.addAll(HttpCookie.parse(header.getValue()));
            }
        }
        for (final HttpCookie cookie : cookies)
        {
            resultCookies.add(new NameValuePair(cookie.getName(),
                                                cookie.getValue()));
        }
        return resultCookies;
    }
    public static List<NameValuePair> getCookieCookieByName(final List<NameValuePair> headers,
                                                      final String cookieName)
    {
        final List<HttpCookie> cookies = new ArrayList<HttpCookie>();
        final List<NameValuePair> resultCookies = new ArrayList<NameValuePair>();

        for (final NameValuePair header : headers)
        {
            if (header.getName().equalsIgnoreCase("Set-Cookie"))
            {
                cookies.addAll(HttpCookie.parse(header.getValue()));
            }
        }
        for (final HttpCookie cookie : cookies)
        {
            if (cookie.getName().equals(cookieName))
            {
                resultCookies.add(new NameValuePair(cookie.getName(),
                                                    cookie.getValue()));
            }
        }
        return null;
    }

    public static List<Cookie> getCookiesCookies(final List<NameValuePair> headers)
    {
        final List<HttpCookie> cookies = new ArrayList<HttpCookie>();
        final List<NameValuePair> resultCookies = new ArrayList<NameValuePair>();

        for (final NameValuePair header : headers)
        {
            if (header.getName().equalsIgnoreCase("Set-Cookie"))
            {
                cookies.addAll(HttpCookie.parse(header.getValue()));
            }
        }
        for (final HttpCookie cookie : cookies)
        {
            resultCookies.add(new NameValuePair(cookie.getName(),
                                                cookie.getValue()));
        }
        return null;
    }

    public static void main(final String args[])
    {
        final MockObjects mockObjects = new MockObjects("http://www.amazon.de");
        mockObjects.load();
        final WebResponse response = mockObjects.getResponse();
        final List<NameValuePair> headers = response.getResponseHeaders();

       List<NameValuePair> cookies = TestCookieFunction.getCookies(headers);
        for (final NameValuePair cookie : cookies)
        {
            System.err.println(cookie.getName() + " :bam: " + cookie.getValue());
        }
        
        System.err.println("-------------------------------");
        
        cookies = TestCookieFunction.getCookieByName(headers, "session-id");
        for (final NameValuePair cookie : cookies)
        {
            System.err.println(cookie.getName() + " :bam: " + cookie.getValue());
        }
    }
}
