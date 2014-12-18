/**  
 *  Copyright 2014 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License. 
 *
 */
package com.xceptance.xlt.common.actions;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.xceptance.xlt.api.util.XltProperties;
import com.xceptance.xlt.common.tests.AbstractURLTestCase;
import com.xceptance.xlt.common.util.CSVBasedURLAction;
import com.xceptance.xlt.common.util.StaticContentDownloader;
import com.xceptance.xlt.engine.XltWebClient;

public class Downloader
{
    private final List<String> urls = new ArrayList<String>();

    private final XltWebClient webClient;

    public Downloader(final XltWebClient webClient)
    {
        this.webClient = webClient;
    }

    protected void loadRequests(final AbstractURLTestCase testCase, final CSVBasedURLAction action) throws Exception
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
                    downloader.addRequest(new URL(action.getInterpreter().processDynamicData(testCase, url)));
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
