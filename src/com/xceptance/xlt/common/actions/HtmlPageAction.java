package com.xceptance.xlt.common.actions;

import com.gargoylesoftware.htmlunit.WebRequest;
import com.xceptance.xlt.common.util.ParameterUtils;

public class HtmlPageAction extends ModifiedAbstractHtmlPageAction
{
    protected Downloader downloader;

    protected WebRequest webRequest;

    public HtmlPageAction(final HtmlPageAction previousAction,
                          final String name,
                          final WebRequest webRequest,
                          final Downloader downloader)
    {
        super(previousAction, name);
        setDownloader(downloader);
        setWebRequest(webRequest);
    }

    public HtmlPageAction(final String name,
                          final WebRequest webRequest,
                          final Downloader downloader)
    {
        super(name);
        setDownloader(downloader);
        setWebRequest(webRequest);
    }

    private void setDownloader(final Downloader downloader)
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
        //
    }

    @Override
    public void preValidate() throws Exception
    {
        //

    }

    public void addRequest(final String url)
    {
        downloader.addRequest(url);
    }
}
