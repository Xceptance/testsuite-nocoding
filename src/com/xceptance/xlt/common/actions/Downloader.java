package com.xceptance.xlt.common.actions;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.common.util.ParameterUtils;
import com.xceptance.xlt.engine.XltWebClient;

/**
 * Loads all the static content stuff with a passed {@link XltWebClient}, distributed on some threads. For this the
 * {@link StaticContentDownloader} is used.
 */
public class Downloader
{
    private final List<String> urls = new ArrayList<String>();

    private final XltWebClient webClient;

    private final int threadCount;

    private final boolean userAgentUID;

    /**
     * @param webClient
     *            : the {@link XltWebClient}, that fires the requests.
     */
    public Downloader(final XltWebClient webClient)
    {
        this(webClient, 1, false);
    }

    /**
     * @param webClient
     *            : the {@link XltWebClient}, that fires the requests.
     * @param threadCount
     *            : amount of threads for parallel loading
     * @param userAgentUID
     */
    public Downloader(final XltWebClient webClient, final int threadCount, final boolean userAgentUID)
    {
        ParameterUtils.isNotNull(webClient, "XltWebClient");

        this.userAgentUID = userAgentUID;
        this.threadCount = threadCount < 0 ? 1 : threadCount;
        this.webClient = webClient;
    }

    /**
     * Adds a request.
     * 
     * @param url
     *            : request url
     */
    public void addRequest(final String url)
    {
        ParameterUtils.isNotNull(url, "URL");
        urls.add(url);
        XltLogger.runTimeLogger.debug("Adding Static Request: " + url);
    }

    /**
     * loads all the requests that were previously added via {@link #addRequest(String)}
     * 
     * @throws Exception
     */
    public void loadRequests() throws Exception
    {
        if (!urls.isEmpty())
        {
            // build a static content downloader only when needed
            final StaticContentDownloader downloader = new StaticContentDownloader(webClient, threadCount, userAgentUID);
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
