package com.xceptance.xlt.common.tests;

import org.junit.Test;

import com.xceptance.xlt.api.actions.AbstractHtmlPageAction;
import com.xceptance.xlt.common.actions.SimpleURL;
import com.xceptance.xlt.common.util.CSVBasedURLAction;
import com.xceptance.xlt.common.util.StaticContentDownloader;
import com.xceptance.xlt.engine.XltWebClient;

/**
 * This is a simple single URL test case. It can be used to create considerable load for simple investigations. Cookie
 * handling, as well as content comparison is handled automatically. See the properties too.
 */
public class TURL extends AbstractURLTestCase
{
    @Test
    public void testURLs() throws Throwable
    {
        // our action tracker to build up a correct chain of pages
        AbstractHtmlPageAction lastAction = null; 
        StaticContentDownloader downloader = null;
         
        try
        {
            // let's loop about the data we have
            for (final CSVBasedURLAction csvBasedAction : csvBasedActions)
            {
                // ok, action or static?
                if (csvBasedAction.isAction())
                {
                    // download all collected static content stuff first
                    if (downloader != null)
                    {
                        downloader.waitForCompletion();
                    }
                    
                    if (lastAction == null)
                    {
                        // our first action, so start the browser too
                        lastAction = new SimpleURL(this, csvBasedAction, login, password);
                    }
                    else
                    {
                        // subsequent actions
                        lastAction = new SimpleURL(this, lastAction, csvBasedAction);
                    }
                    
                    // run it
                    lastAction.run();
                }
                
                // this is the part that deals with the downloads
                if (csvBasedAction.isStaticContent())
                {
                    if (downloader == null)
                    {
                        if (lastAction == null)
                        {
                            // we do not have any action yet, so we have to make one up
                            lastAction = new SimpleURL(this, csvBasedAction, login, password);
                        }
                        downloader = new StaticContentDownloader(((XltWebClient)lastAction.getWebClient()), getProperty("com.xceptance.xlt.staticContent.downloadThreads", 1));
                    }
                    downloader.addRequest(csvBasedAction.getURL(this));
                }
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
