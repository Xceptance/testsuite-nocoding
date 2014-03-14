package com.xceptance.xlt.common.actions;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.xceptance.xlt.api.util.XltProperties;
import com.xceptance.xlt.common.util.StaticContentDownloader;
import com.xceptance.xlt.engine.XltWebClient;

public class Downloader
{
    private final List<URL> urls = new ArrayList<URL>();

    private final XltWebClient webClient;

    public Downloader(final XltWebClient webClient)
    {
        this.webClient = webClient;
    }

    protected void loadRequests() throws Exception
    {
        if (!urls.isEmpty())
        {
            // build a static content downloader only when needed
            final StaticContentDownloader downloader = new StaticContentDownloader(
                                                                                   webClient,
                                                                                   XltProperties.getInstance()
                                                                                                .getProperty("com.xceptance.xlt.staticContent.downloadThreads",
                                                                                                             1),
                                                                                   XltProperties.getInstance()
                                                                                                .getProperty("userAgent.UID",
                                                                                                             false));
            try
            {
                // load the additional URLs
                for (final URL url : urls)
                {
                    downloader.addRequest(url);
                }
            }
            finally
            {
                // make sure we wait for all resources and clean up too
                if (downloader != null)
                {
                    downloader.waitForCompletion();
                    downloader.shutdown();
                }
            }
        }
    }

    /**
     * Add an additional request to the current action.
     * 
     * @param url
     *            request URL
     */
    public void addRequest(final URL url)
    {
        urls.add(url);
    }
}
