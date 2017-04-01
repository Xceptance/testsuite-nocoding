package test.com.xceptance.xlt.common.actions;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.WebRequest;
import com.xceptance.xlt.common.actions.Downloader;
import com.xceptance.xlt.common.actions.XhrLightWeightPageAction;
import com.xceptance.xlt.common.util.action.validation.URLActionDataExecutableResult;
import com.xceptance.xlt.common.util.action.validation.URLActionDataExecutableResultFactory;
import com.xceptance.xlt.engine.XltWebClient;

import test.com.xceptance.xlt.common.util.MockObjects;

public class XhrLightWeightPageActionTest {
	
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
	public void testConstructor()
	{
		final XhrLightWeightPageAction action = new XhrLightWeightPageAction(name, request, factory);
        downloader = new Downloader((XltWebClient) action.getWebClient());
        action.setDownloader(downloader);
        Assert.assertEquals(urlString, action.getUrl().toString());
	}
	
	@Test
	public void testGetResult() throws Throwable
	{
        final XhrLightWeightPageAction action = new XhrLightWeightPageAction(name, request, factory);
        downloader = new Downloader((XltWebClient) action.getWebClient());
        action.setDownloader(downloader);
        action.run();

        final URLActionDataExecutableResult result = action.getResult();
        final List<String> list = result.getByXPath(mockObjects.xPathString);
        Assert.assertEquals(mockObjects.xpathStringExpected, list.get(0));
	}
}
