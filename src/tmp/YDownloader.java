package com.xceptance.xlt.common.actions;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.xceptance.xlt.api.util.XltProperties;
import com.xceptance.xlt.common.actions.StaticContentDownloader;
import com.xceptance.xlt.common.util.action.data.URLActionData;
import com.xceptance.xlt.engine.XltWebClient;

public class YDownloader
{
    private final List<String> urls = new ArrayList<String>();
    
    private final XltWebClient webClient;
    
    
    public YDownloader(final XltWebClient webClient)
    {
        this.webClient=webClient;
    }

    protected void loadRequests(final YAbstractURLTestCase testCase,
                                final URLActionData action) throws Exception
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
                for (final String url : urls)
                {
                    downloader.addRequest(new URL(action.getInterpreter()
                                                        .processDynamicData(url)));
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
    public void addRequest(final String url)
    {
        urls.add(url);
    }
}
