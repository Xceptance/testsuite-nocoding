package tmp;
import java.net.MalformedURLException;
import java.net.URL;

import test.com.xceptance.xlt.common.util.MockWebResponse;

import com.gargoylesoftware.htmlunit.WebResponse;
public class StringWebResponseTest
{
    public static void main(final String args[]) throws MalformedURLException
    {
        final String contentType ="text/xxxxxml";
        final String content = "<x>x</x>";
        final URL url = new URL("http://www.xcepatnce.com");
        final WebResponse response = new MockWebResponse(content, url, contentType);
        System.err.println(response.getContentType());
        System.err.println(response.getContentAsString());
    }
}
