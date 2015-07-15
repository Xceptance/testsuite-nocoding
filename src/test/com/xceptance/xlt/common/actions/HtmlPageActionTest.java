package test.com.xceptance.xlt.common.actions;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.xceptance.xlt.common.actions.Downloader;
import com.xceptance.xlt.common.actions.HtmlPageAction;
import com.xceptance.xlt.common.util.action.validation.URLActionDataExecutableResultFactory;
import com.xceptance.xlt.engine.XltWebClient;

public class HtmlPageActionTest
{
    private Downloader downloader;

    private final String name = "test";

    private WebRequest request;

    private final String xceptanceUrlString = "https://www.xceptance.com";

    private URLActionDataExecutableResultFactory factory;

    @Before
    public void setup() throws MalformedURLException
    {
        final XltWebClient client = new XltWebClient();
        client.setTimerName("timeName");
        downloader = new Downloader(client);
        request = new WebRequest(new URL(xceptanceUrlString));
        factory = new URLActionDataExecutableResultFactory();
    }

    @Test
    public void testConstructor() throws Throwable
    {
        final HtmlPageAction action = new HtmlPageAction(name, request, factory);
        downloader = new Downloader((XltWebClient) action.getWebClient());
        action.setDownloader(downloader);
    }

    @Test
    public void testGetHtmlPage() throws Throwable
    {

        final HtmlPageAction action = new HtmlPageAction(name, request, factory);
        downloader = new Downloader((XltWebClient) action.getWebClient());
        action.setDownloader(downloader);
        action.run();

        final HtmlPage page = action.getHtmlPage();
        final List<?> list = page.getByXPath("//*[@id='navigation']/div/nav/div[1]/a/img");
        Assert.assertFalse(list.isEmpty());
    }
}
