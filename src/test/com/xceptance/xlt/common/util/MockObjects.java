package test.com.xceptance.xlt.common.util;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;

import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.xceptance.xlt.api.htmlunit.LightWeightPage;

/**
 * <p>
 * Helper Class. <br>
 * Takes an {@link URL url}, creates a {@link WebRequest}, creates and uses a {@link WebClient} to load the
 * {@link WebResponse}. Can parse the content of the WebResponse into a {@link LightWeightPage}, as well as a
 * {@link HtmlPage} <br>
 * This can be done at one go (via {@link #load()}), or stepwise.
 * </p>
 * Used for Debugging and Testing. <br>
 * 
 * @author matthias mitterreiter
 */

public class MockObjects
{
    public final String regexString = "<title>[\\s\\S]*?</title>";

    public final String regexStringExpected = "<title>A demo html site for this test suite</title>";

    public final String xPathString = "//div[@id='divId']/p";

    public final String xpathStringExpected = "Some content2";

    public final String cookieName1 = "session-id";
        
    public final String cookieName2 = "session-id-time";

    public final String cookieName3 = "x-wl-uid";
    
    public final String cookieValue1 = "11111";

    public final String cookieValue2 = "22222";

    public final String cookieValue3 = "33333";

	public final String urlStringDemoHtml = this.getClass().getResource("DemoHTML.html").toString(); 

    private WebClient client;

    private URL url;
    
    private String urlString = "http://localhost:8080";

    private WebRequest request;

    private WebResponse response;

    public HtmlPage htmlPage;

    private LightWeightPage lightWeightPage;

    public MockObjects()
    {
        initWebClient();
    }

    public MockObjects(final String urlString)
    {
        this();
        setUrlString(urlString);
    }

    public void load()
    {
        initURL();
        initWebRequest();
        initWebResponse();
        initHtmlPage();
        initLightWeightPage();
    }

    public void loadResponse()
    {
        initURL();
        initWebRequest();
        initWebResponse();
    }

    public void loadHtmlPage()
    {
        initHtmlPage();
    }

    public void loadLightWeightPage()
    {
        initLightWeightPage();
    }

    public void initWebClient()
    {
        this.client = new WebClient();

        // load test HTML content
        String content;
        try
        {
            content = IOUtils.toString(getClass().getResourceAsStream("DemoHTML.html"), StandardCharsets.UTF_8);
        }
        catch (Exception ex)
        {
            throw new RuntimeException("Failed to load test HTML", ex);
        }

        // prepare test response headers
        List<NameValuePair> headers = new ArrayList<>();
        headers.add(new NameValuePair("Set-Cookie", cookieName1 + "=" +cookieValue1));
        headers.add(new NameValuePair("Set-Cookie", cookieName2 + "=" +cookieValue2));
        headers.add(new NameValuePair("Set-Cookie", cookieName3 + "=" +cookieValue3));
        
        // set up a web connection that always returns our test content / headers
        MockWebConnection conn = new MockWebConnection();
        conn.setDefaultResponse(content, 200, "OK", "text/html", "UTF-8", headers);
        client.setWebConnection(conn);
    }

    public void initHtmlPage()
    {
        if (urlString != null)
        {
            if (client != null)
            {
                try
                {
                    this.htmlPage = client.getPage(urlString);
                }
                catch (final Exception e)
                {
                    throw new IllegalArgumentException("Failed to load 'HtmlPage': " + e.getMessage(), e);
                }
            }
            else
            {
                throw new IllegalArgumentException("'WebClient' cannot be null");
            }
        }
        else
        {
            throw new IllegalArgumentException("'urlString' cannot be null");
        }
    }

    public void initLightWeightPage()
    {
        this.lightWeightPage = new LightWeightPage(response, "test");
    }

    public void initURL()
    {
        if (urlString != null)
        {
            try
            {
                this.url = new URL(this.urlString);
            }
            catch (final MalformedURLException e)
            {
                throw new IllegalArgumentException("Failed to create URL: " + e.getMessage(), e);
            }
        }
        else
        {
            throw new IllegalArgumentException("'urlString' cannot be null in order to create a URL!");
        }
    }

    public void initWebRequest()
    {
        if (this.url != null)
        {
            this.request = new WebRequest(this.url);
        }
        else
        {
            throw new IllegalArgumentException("'URL' cannot be null in order to create a WebRequest!");
        }
    }

    public void initWebResponse()
    {
        if (request != null)
        {
            if (client != null)
            {
                try
                {
                    this.response = client.loadWebResponse(this.request);
                    printWebResponseHeader(response);
                }
                catch (final IOException e)
                {
                    throw new IllegalArgumentException("Failed to load Response: " + e.getMessage(), e);
                }
            }
            else
            {
                throw new IllegalArgumentException("'WebClient' cannot be null in order to load a WebResponse!");
            }
        }
        else
        {
            throw new IllegalArgumentException("'WebRequest' cannot be null in order to load a WebResponse!");
        }
    }

    public void printWebResponseHeader(final WebResponse response)
    {
        final List<NameValuePair> headers = response.getResponseHeaders();
        System.err.println("------------WebResponse----------------");
        System.err.println("---------------------------------------");
        System.err.println("------------Headers--------------------");
        for (final NameValuePair header : headers)
        {
            System.err.println(header.getName() + " : " + header.getValue());
        }
    }

    public void printWebResponse(final WebResponse response)
    {
        final List<NameValuePair> headers = response.getResponseHeaders();
        System.err.println("------------WebResponse----------------");
        System.err.println("---------------------------------------");
        System.err.println("------------Headers--------------------");
        for (final NameValuePair header : headers)
        {
            System.err.println(header.getName() + " : " + header.getValue());
        }
        System.err.println("------------Body--------------------");
        System.err.println("ContentType: " + response.getContentType());
        System.err.println(response.getContentAsString());
    }

    public WebResponse getResponse()
    {
        return response;
    }

    public HtmlPage getHtmlPage()
    {
        return htmlPage;
    }

    public LightWeightPage getLightWeightPage()
    {
        return lightWeightPage;
    }

    public WebClient getClient()
    {
        return client;
    }

    public String getUrlString()
    {
        return urlString;
    }

    public URL getUrl()
    {
        return url;
    }

    public WebRequest getRequest()
    {
        return request;
    }

    public void setHtmlPage(final HtmlPage htmlPage)
    {
        if (htmlPage != null)
        {
            this.htmlPage = htmlPage;
        }
        else
        {
            throw new IllegalArgumentException("'HtmlPage' cannot be null!");
        }
    }

    public void setLightWeightPage(final LightWeightPage lightWeightPage)
    {
        if (lightWeightPage != null)
        {
            this.lightWeightPage = lightWeightPage;
        }
        else
        {
            throw new IllegalArgumentException("'LightWeightPage' cannot be null!");
        }
    }

    public void setResponse(final WebResponse response)
    {
        if (response != null)
        {
            this.response = response;
        }
        else
        {
            throw new IllegalArgumentException("'WebResponse' cannot be null!");
        }
    }

    public void setClient(final WebClient client)
    {
        if (client != null)
        {
            this.client = client;
        }
        else
        {
            throw new IllegalArgumentException("'WebClient' cannot be null!");
        }
    }

    public void setUrl(final URL url)
    {
        if (url != null)
        {
            this.url = url;
        }
        else
        {
            throw new IllegalArgumentException("'URL' cannot be null!");
        }
    }

    public void setRequest(final WebRequest request)
    {
        if (request != null)
        {
            this.request = request;
        }
        else
        {
            throw new IllegalArgumentException("'WebRequest' cannot be null!");
        }
    }

    public void setUrlString(final String urlString)
    {
        if (urlString != null)
        {
            this.urlString = urlString;
        }
        else
        {
            throw new IllegalArgumentException("'urlString' cannot be null!");
        }
    }
}

