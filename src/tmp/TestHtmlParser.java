package tmp;

import java.io.IOException;
import java.net.URL;

import com.gargoylesoftware.htmlunit.StringWebResponse;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HTMLParser;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class TestHtmlParser
{
    public static void main(final String args[]) throws IOException
    {
        final URL url = new URL("http://www.example.com");
        final StringWebResponse response = new StringWebResponse("<title>Test</title>", url);
        final HtmlPage page = HTMLParser.parseHtml(response, new WebClient().getCurrentWindow());
        System.err.println(page.asText());
    }
}
