package com.xceptance.xlt.common.actions;

import java.net.URL;

import com.gargoylesoftware.htmlunit.WebRequest;
import com.xceptance.xlt.common.util.ParameterUtils;
import com.xceptance.xlt.common.util.URLActionDataExecutable;
import com.xceptance.xlt.common.util.URLActionDataResult;

public class HtmlPageAction extends ModifiedAbstractHtmlPageAction implements URLActionDataExecutable
{
    protected Downloader downloader;

    protected WebRequest webRequest;
    
    protected URLActionDataResult result;

    public HtmlPageAction(final HtmlPageAction previousAction,
                          final String name,
                          final WebRequest webRequest,
                          final Downloader downloader)
    {
        super(previousAction, name);
        setDownloader(downloader);
        setWebRequest(webRequest);
    }

    public HtmlPageAction(final String name, final WebRequest webRequest)
    {
        super(null, name);
        setWebRequest(webRequest);
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
        this.result = new URLActionDataResult(getHtmlPage());
    }

    @Override
    protected void postValidate() throws Exception
    {
        //
    }

    @Override
    public void preValidate() throws Exception
    {
        //

    }

    @Override
    public URLActionDataResult getResult()
    {
        return this.result;
    }
    @Override
    public void executeURLAction(){
        try
        {
            this.run();
        }
        catch (final Throwable e)
        {
            e.printStackTrace();
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
