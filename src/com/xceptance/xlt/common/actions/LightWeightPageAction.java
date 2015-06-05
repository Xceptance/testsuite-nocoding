package com.xceptance.xlt.common.actions;

import com.gargoylesoftware.htmlunit.WebRequest;
import com.xceptance.xlt.common.util.ParameterUtils;

public class LightWeightPageAction extends ModifiedAbstractHtmlPageAction
{

    protected Downloader downloader;

    protected WebRequest webRequest;
    
    
    protected LightWeightPageAction(final ModifiedAbstractHtmlPageAction previousAction,
                                    final String name,
                                    final WebRequest webRequest,
                                    final Downloader downloader)
    {
        super(previousAction, name);
        setWebRequest(webRequest);
        setDownloader(downloader);

    }
    protected LightWeightPageAction(final String name,
                                    final WebRequest webRequest)
    {
        super(null, name);
        setWebRequest(webRequest);
    
    }
    public void setDownloader(final Downloader downloader)
    {
        ParameterUtils.isNotNull(downloader, "Downloader");
        this.downloader = downloader;
    }

    private void setWebRequest(final WebRequest webRequest)
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
        // TODO Auto-generated method stub

    }

    @Override
    public void preValidate() throws Exception
    {
        // TODO Auto-generated method stub

    }
    public void addRequest(final String url)
    {
        downloader.addRequest(url);
    }

}
