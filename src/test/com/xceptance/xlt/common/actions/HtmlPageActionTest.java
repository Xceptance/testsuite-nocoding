package test.com.xceptance.xlt.common.actions;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Before;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.WebRequest;
import com.xceptance.xlt.common.actions.Downloader;
import com.xceptance.xlt.common.actions.HtmlPageAction;
import com.xceptance.xlt.engine.XltWebClient;

public class HtmlPageActionTest
{
    private Downloader downloader;
    private final String name = "test";
    private WebRequest request;
    
    @Before
    public void setup() throws MalformedURLException{
        final XltWebClient client = new XltWebClient();
        client.setTimerName("timeName");
        downloader = new Downloader(client);
        
        request = new WebRequest(new URL("http://www.xceptance.com"));
        
    }
    
    @Test
    public void testConstructor() throws Throwable{
        final HtmlPageAction action = new HtmlPageAction(name, request, downloader);
        action.run();
    }
}
