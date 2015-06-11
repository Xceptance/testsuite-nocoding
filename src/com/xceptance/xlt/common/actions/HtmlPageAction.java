package com.xceptance.xlt.common.actions;

import java.net.URL;

import com.gargoylesoftware.htmlunit.WebRequest;
import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.common.util.ParameterUtils;
import com.xceptance.xlt.common.util.URLActionDataExecutable;
import com.xceptance.xlt.common.util.URLActionExecutableResult;

public class HtmlPageAction extends ModifiedAbstractHtmlPageAction implements URLActionDataExecutable
{
    protected Downloader downloader;

    protected WebRequest webRequest;
    
    protected URLActionExecutableResult result;

    public HtmlPageAction(final HtmlPageAction previousAction,
                          final String name,
                          final WebRequest webRequest,
                          final Downloader downloader)
    {
        super(previousAction, name);
        setDownloader(downloader);
        setWebRequest(webRequest);
        XltLogger.runTimeLogger.debug("Creating new Instance");
    }

    public HtmlPageAction(final String name, final WebRequest webRequest)
    {
        super(null, name);
        setWebRequest(webRequest);
        XltLogger.runTimeLogger.debug("Creating new Instance");
    }

    public void setDownloader(final Downloader downloader)
    {
        ParameterUtils.isNotNull(downloader, "Downloader");
        this.downloader = downloader;
    }

    protected void setWebRequest(final WebRequest webRequest)
    {
        ParameterUtils.isNotNull(webRequest, "WebRequest");
        this.webRequest = webRequest;

    }

    @Override
    protected void execute() throws Exception
    {
        loadPage(this.webRequest);
    }

    @Override
    protected void postValidate() throws Exception
    {
        this.result = new URLActionExecutableResult(getHtmlPage());
    }

    @Override
    public void preValidate() throws Exception
    {

    }

    @Override
    public URLActionExecutableResult getResult()
    {
        return this.result;
    }
    @Override
    public void executeAction(){
        try
        {
            XltLogger.runTimeLogger.debug("Executing Action");
            this.run();
        }
        catch (final Throwable e)
        {
            throw new IllegalArgumentException("Failed to execute Action: " + getTimerName() + " - " + e.getMessage(), e);
        }
    }

    @Override
    public void addStaticRequest(final URL url)
    {
        this.downloader.addRequest(url.toString());
        //UGLY -> change
    }

    @Override
    public URL getUrl()
    {
        return this.webRequest.getUrl();
    }
    
    
    
    
    
    
}
