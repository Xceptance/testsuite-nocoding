package test.com.xceptance.xlt.common.util.action;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.xceptance.xlt.common.actions.Downloader;
import com.xceptance.xlt.engine.XltWebClient;

public class DownloaderTest
{
    private static  XltWebClient client;

    private static boolean userAgentUID;

    private static int threadCount;

    private static final List<String> urls = new ArrayList<String>();

    @BeforeClass
    public static void setup()
    {
        userAgentUID = false;
        threadCount = 1;
        client = new XltWebClient();

        client.setTimerName("Test"); // NECESSARY OR CRASH

        urls.add("https://www.xceptance.com/css/font-awesome.min.css?1432903128");
        urls.add("https://www.xceptance.com/images/xceptance-logo-transparent-202px.png");
        urls.add("https://www.xceptance.com/js/jquery-1.11.1.min.js");
    }

    @Test
    public void testConstructors()
    {
        @SuppressWarnings("unused")
		Downloader downloader;
        downloader = new Downloader(client);
        downloader = new Downloader(client, threadCount, userAgentUID);
    }

    @Test
    public void testEmptyUrls()
    {
        Downloader downloader;
        downloader = new Downloader(client);
        try
        {
            downloader.loadRequests();
        }
        catch (final Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void testLinearDownload() throws Exception
    {
        client.setTimerName("Test");
        // client.getPage("https://www.xceptance.com/en/");

        final Downloader downloader = new Downloader(client);
        for (final String url : urls)
        {
            downloader.addRequest(url);
        }
        downloader.loadRequests();
    }

    @Test
    public void testParallelDownload() throws Exception
    {
        client.setTimerName("Test");
        // client.getPage("https://www.xceptance.com/en/");

        final Downloader downloader = new Downloader(client, urls.size(), false);
        for (final String url : urls)
        {
            downloader.addRequest(url);
        }
        downloader.loadRequests();
    }

    @Test
    public void testWithUAUID() throws Exception
    {
        client.setTimerName("Test");
        // client.getPage("https://www.xceptance.com/en/");

        final Downloader downloader = new Downloader(client, urls.size(), true);
        for (final String url : urls)
        {
            downloader.addRequest(url);
        }
        downloader.loadRequests();
    }
}
