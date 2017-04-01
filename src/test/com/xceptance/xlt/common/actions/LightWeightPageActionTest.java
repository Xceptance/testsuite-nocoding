package test.com.xceptance.xlt.common.actions;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.WebRequest;

import com.xceptance.xlt.api.htmlunit.LightWeightPage;
import com.xceptance.xlt.common.actions.Downloader;
import com.xceptance.xlt.common.actions.LightWeightPageAction;
import com.xceptance.xlt.common.util.action.validation.URLActionDataExecutableResult;
import com.xceptance.xlt.common.util.action.validation.URLActionDataExecutableResultFactory;
import com.xceptance.xlt.engine.XltWebClient;

import test.com.xceptance.xlt.common.util.MockObjects;

public class LightWeightPageActionTest {
	
    private Downloader downloader;

    private final String name = "test";

    private WebRequest request;

    private URLActionDataExecutableResultFactory factory;
    
    private String urlString;

    private static MockObjects mockObjects;

    @Before
    public void setup() throws MalformedURLException
    {
    	mockObjects = new MockObjects();
        final XltWebClient client = new XltWebClient();
        client.setTimerName("timeName");
        urlString = mockObjects.urlStringDemoHtml;
        downloader = new Downloader(client);
        request = new WebRequest(new URL (urlString));
        factory = new URLActionDataExecutableResultFactory();
    }

    
	@Test
    public void testConstructor() throws Throwable
    {
        final LightWeightPageAction action = new LightWeightPageAction(null, name, request, null, factory);
        downloader = new Downloader((XltWebClient) action.getWebClient());
        action.setDownloader(downloader);
        Assert.assertEquals(urlString, action.getUrl().toString());
    }
	
    @Test
    public void testGetLightWeightHtmlPageContent() throws Throwable
    {

        final LightWeightPageAction action = new LightWeightPageAction(null, name, request, null, factory);
        downloader = new Downloader((XltWebClient) action.getWebClient());
        action.setDownloader(downloader);
        action.run();

        final LightWeightPage page = action.getLightWeightPage();
        Assert.assertFalse(page.getContent().isEmpty());
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testGetResultWithLightWeigtHtmlPage() throws Throwable
    {
        final LightWeightPageAction action = new LightWeightPageAction(null, name, request, null, factory);
        downloader = new Downloader((XltWebClient) action.getWebClient());
        action.setDownloader(downloader);
        action.run();

        final LightWeightPage page = action.getLightWeightPage();
        final URLActionDataExecutableResult result = factory.getResult(page);
        @SuppressWarnings("unused")
		final List<String> something = result.getByXPath(mockObjects.xPathString);
    }
}
