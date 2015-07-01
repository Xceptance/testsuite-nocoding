package com.xceptance.xlt.common.actions;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.common.util.ParameterUtils;
import com.xceptance.xlt.engine.XltWebClient;

/**
 * 
 * Loads all the static content stuff with a passed {@link XltWebClient}, 
 * distributed on some threads. For this the {@link StaticContentDownloader} is used.
 */
public class Downloader
{
    private final List<String> urls = new ArrayList<String>();

    private XltWebClient webClient;

    private int threadCount = 1;

    private boolean userAgentUID = false;

    /**
     * 
     * @param webClient : the {@link XltWebClient}, that fires the requests.
     */
    public Downloader(final XltWebClient webClient)
    {
        setWebClient(webClient);
        setUserAgentUID(false); // default
        setThreadCount(1); // default
    }

    /**
     * 
     * @param webClient : the {@link XltWebClient}, that fires the requests.
     * @param threadCount : amount of threads for parallel loading
     * @param userAgentUID
     */
    public Downloader(final XltWebClient webClient,
                      final int threadCount,
                      final boolean userAgentUID)
    {
        setUserAgentUID(userAgentUID);
        setThreadCount(threadCount);
        setWebClient(webClient);
    }

    private void setUserAgentUID(final boolean userAgentUID)
    {
        this.userAgentUID = userAgentUID;
    }

    private void setThreadCount(final int threadCount)
    {
        this.threadCount = threadCount < 0 ? 1 : threadCount;
    }

    private void setWebClient(final XltWebClient webClient)
    {
        ParameterUtils.isNotNull(webClient, "XltWebClient");
        this.webClient = webClient;
    }

    /**
     * Adds a request.
     * @param url : request url
     */
    public void addRequest(final String url)
    {
        ParameterUtils.isNotNull(url, "URL");
        urls.add(url);
        XltLogger.runTimeLogger.debug("Adding Static Request: " + url);
    }

    /**
     * loads all the requests that were previously added via 
     * {@link #addRequest(String)}
     * @throws Exception
     */
    public void loadRequests() throws Exception
    {
        if (!urls.isEmpty())
        {
            // build a static content downloader only when needed
            final StaticContentDownloader downloader = new StaticContentDownloader(
                                                                                   webClient,
                                                                                   threadCount,
                                                                                   userAgentUID);
            try
            {
                // load the additional URLs
                for (final String url : urls)
                {
                    downloader.addRequest(new URL(url));
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
}
