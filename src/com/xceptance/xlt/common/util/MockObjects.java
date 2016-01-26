package com.xceptance.xlt.common.util;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

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
    private WebClient client;

    private String urlString = "https://www.xceptance.com/en/";

    private URL url;

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
