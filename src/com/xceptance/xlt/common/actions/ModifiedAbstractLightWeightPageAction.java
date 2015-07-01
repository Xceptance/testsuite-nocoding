package com.xceptance.xlt.common.actions;

import java.io.IOException;
import java.net.URL;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.xceptance.common.util.ParameterCheckUtils;
import com.xceptance.xlt.api.actions.AbstractLightWeightPageAction;
import com.xceptance.xlt.api.actions.AbstractWebAction;
import com.xceptance.xlt.api.engine.Session;
import com.xceptance.xlt.api.htmlunit.LightWeightPage;
import com.xceptance.xlt.engine.SessionImpl;
import com.xceptance.xlt.engine.XltWebClient;

/**
 * This class is simply a variant of the {@link AbstractLightWeightPageAction}.
 * They distinguish in the method {@link #loadPage(WebRequest)}. In this class it is possible to
 * pass a {@link WebRequest}, which is cozy.
 *
 */
public abstract class ModifiedAbstractLightWeightPageAction extends AbstractWebAction
{

    private LightWeightPage page;

    private URL url;

    protected ModifiedAbstractLightWeightPageAction(final ModifiedAbstractLightWeightPageAction previousAction,
                                                    final String timerName)
    {
        super(previousAction, timerName);
    }
    protected ModifiedAbstractLightWeightPageAction(final String timerName)
    {
        this(null, timerName);
    }
   
    @Override
    public ModifiedAbstractLightWeightPageAction getPreviousAction()
    {
        return (ModifiedAbstractLightWeightPageAction) super.getPreviousAction();
    }

    public LightWeightPage getLightWeightPage()
    {
        return page;
    }
    public String getContent()
    {
        final LightWeightPage page = getLightWeightPage();
        return page != null ? page.getContent() : null;
    }
    
    public int getHttpResponseCode()
    {
        final LightWeightPage page = getLightWeightPage();
        return page != null ? page.getHttpResponseCode() : 0;
    }
    

    public URL getURL()
    {
        return url;
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
            dumpPage(getLightWeightPage());
        }
    }
    
    public void setLightWeightPage(final LightWeightPage page)
    {
        ParameterCheckUtils.isNotNull(page, "page");

        this.page = page;
    }
    
    protected void loadPage(final WebRequest webRequest)
        throws FailingHttpStatusCodeException, IOException
    {
        this.url = webRequest.getUrl();
        setLightWeightPage(((XltWebClient) getWebClient()).getLightWeightPage(webRequest));
    }
    
    private void dumpPage(final LightWeightPage page)
    {
        if (page != null)
        {
            ((SessionImpl) Session.getCurrent()).getRequestHistory().add(page);
        }
    }

}
