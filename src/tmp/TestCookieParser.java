package tmp;

import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.List;

import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.xceptance.xlt.common.util.MockObjects;

public class TestCookieParser
{
    public static void main(final String args[])
    {
        final MockObjects mockObjects = new MockObjects("http://www.amazon.de");
        mockObjects.load();
        final WebResponse response = mockObjects.getResponse();
        final List<NameValuePair> headers = response.getResponseHeaders();

        final List<NameValuePair> setCookies = new ArrayList<NameValuePair>();

        System.err.println("-----------------------------------");
        System.err.println("Getting all headers and separate \" Set-Cookie\" headers");
        System.err.println("-----------------------------------");
        for (final NameValuePair header : headers)
        {
            System.err.println(header.getName() + " : " + header.getValue());
            if (header.getName().equalsIgnoreCase("Set-Cookie"))
            {
                setCookies.add(header);
            }
        }
        System.err.println("-----------------------------------");
        System.err.println("Getting SingleCookies:");
        System.err.println("-----------------------------------");

        final List<HttpCookie> cookies = new ArrayList<HttpCookie>();

        for (final NameValuePair setCookie : setCookies)
        {
            cookies.addAll(HttpCookie.parse(setCookie.getValue()));
        }

        for (final HttpCookie cookie : cookies)
        {
            System.err.println(cookie.toString());
        }

    }
}
