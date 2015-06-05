package com.xceptance.xlt.common.actions;

import java.io.IOException;
import java.util.List;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.xceptance.common.util.ParameterCheckUtils;
import com.xceptance.xlt.api.actions.AbstractWebAction;
import com.xceptance.xlt.api.engine.NetworkData;
import com.xceptance.xlt.api.engine.Session;
import com.xceptance.xlt.api.util.XltProperties;
import com.xceptance.xlt.common.XltConstants;
import com.xceptance.xlt.engine.SessionImpl;
import com.xceptance.xlt.engine.XltWebClient;

public abstract class ModifiedAbstractHtmlPageAction extends AbstractWebAction
{

    private static final String PROP_JS_BACKGROUND_ACTIVITY_WAITINGTIME = XltConstants.XLT_PACKAGE_PATH
                                                                          + ".js.backgroundActivity.waitingTime";

    private static final long DEFAULT_JS_BACKGROUND_ACTIVITY_WAITINGTIME = XltProperties.getInstance()
                                                                                        .getProperty(PROP_JS_BACKGROUND_ACTIVITY_WAITINGTIME,
                                                                                                     0);

    private HtmlPage htmlPage;


    protected ModifiedAbstractHtmlPageAction(final ModifiedAbstractHtmlPageAction previousAction,
                                             final String timerName)
    {
        super(previousAction, timerName);
    }

    protected ModifiedAbstractHtmlPageAction(final String timerName)
    {
        this(null, timerName);
    }

    protected void loadPage(final WebRequest webRequest)
        throws FailingHttpStatusCodeException, IOException
    {
        loadPage(webRequest, DEFAULT_JS_BACKGROUND_ACTIVITY_WAITINGTIME);
    }

    protected void loadPage(final WebRequest webRequest, final long waitingTime)
        throws FailingHttpStatusCodeException, IOException
    {
        final Page result = getWebClient().getPage(webRequest);

        this.htmlPage = waitForPageIsComplete(result, waitingTime);
    }

    private HtmlPage waitForPageIsComplete(final Page page, final long waitingTime)
    {
        // wait for any JavaScript background thread to finish
        if (page instanceof SgmlPage)
        {
            final XltWebClient webClient = (XltWebClient) ((SgmlPage) page).getWebClient();
            webClient.waitForBackgroundThreads(page.getEnclosingWindow().getTopWindow()
                                                   .getEnclosedPage(), waitingTime);
        }

        // something might have changed, including a reload via location
        final HtmlPage newHtmlPage = (HtmlPage) page.getEnclosingWindow().getTopWindow()
                                                    .getEnclosedPage();

        // check for any new static content to load
        ((XltWebClient) newHtmlPage.getWebClient()).loadNewStaticContent(newHtmlPage);

        // Feature #471: API: Make the network data available for validation
        collectAndSetNetworkData();

        return newHtmlPage;
    }

    private List<NetworkData> netStats = null;

    private void collectAndSetNetworkData()
    {
        netStats = Session.getCurrent().getNetworkDataManager().getData();
    }

    public HtmlPage getHtmlPage()
    {
        return this.htmlPage;
    }
    protected List<NetworkData> getNetworkDataSet()
    {
        return netStats;
    }
    
    @Override
    public ModifiedAbstractHtmlPageAction getPreviousAction()
    {
        return (ModifiedAbstractHtmlPageAction) super.getPreviousAction();
    }
    
    @Override
    public void run() throws Throwable
    {
        try
        {
            super.run();
        }
        finally
        {
            /*
             * Dump the page not before the very end of this action. This way, all requests that are executed after one
             * of the loadPageByXXX() methods are correctly associated with this action.
             */
            dumpPage(getHtmlPage());
            Session.getCurrent().getNetworkDataManager().clear();
        }
    }
    

    public void setHtmlPage(final HtmlPage htmlPage)
    {
        setHtmlPage(htmlPage, DEFAULT_JS_BACKGROUND_ACTIVITY_WAITINGTIME);
    }
    

    public void setHtmlPage(final HtmlPage htmlPage, final long waitingTime)
    {
        ParameterCheckUtils.isNotNull(htmlPage, "htmlPage");

        this.htmlPage = waitForPageIsComplete(htmlPage, waitingTime);
    }
    
    private void dumpPage(final HtmlPage htmlPage)
    {
        if (htmlPage != null)
        {
            final String timerName = ((XltWebClient) getWebClient()).getTimerName();
            ((SessionImpl) Session.getCurrent()).getRequestHistory().add(timerName, htmlPage);
        }
    }
    
    
}
