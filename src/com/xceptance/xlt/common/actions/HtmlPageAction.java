package com.xceptance.xlt.common.actions;

import java.net.URL;

import com.gargoylesoftware.htmlunit.WebRequest;
import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.common.util.ParameterUtils;
import com.xceptance.xlt.common.util.action.execution.URLActionDataExecutable;
import com.xceptance.xlt.common.util.action.validation.URLActionDataExecutableResult;
import com.xceptance.xlt.common.util.action.validation.URLActionDataExecutableResultFactory;

public class HtmlPageAction extends ModifiedAbstractHtmlPageAction
    implements URLActionDataExecutable
{
    protected Downloader downloader;

    protected WebRequest webRequest;

    protected URLActionDataExecutableResult result;

    protected URLActionDataExecutableResultFactory resultFactory;

    public HtmlPageAction(final HtmlPageAction previousAction,
                          final String name,
                          final WebRequest webRequest,
                          final Downloader downloader,
                          final URLActionDataExecutableResultFactory resultFactory)
    {
        super(previousAction, name);
        setDownloader(downloader);
        setWebRequest(webRequest);
        setResultFactory(resultFactory);
        XltLogger.runTimeLogger.debug("Creating new Instance");
    }

    private void setResultFactory(
                                  final URLActionDataExecutableResultFactory resultFactory)
    {
        ParameterUtils.isNotNull(resultFactory, "URLActionDataExecutableResultFactory");
        this.resultFactory = resultFactory;
    }

    public HtmlPageAction(final String name,
                          final WebRequest webRequest,
                          final URLActionDataExecutableResultFactory resultFactory)
    {
        super(null, name);
        setWebRequest(webRequest);
        setResultFactory(resultFactory);
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
        this.result = resultFactory.getResult(getHtmlPage());
    }

    @Override
    public void preValidate() throws Exception
    {

    }

    @Override
    public URLActionDataExecutableResult getResult()
    {
        return this.result;
    }

    @Override
    public void executeAction()
    {
        try
        {
            XltLogger.runTimeLogger.debug("Executing Action");
            this.run();
        }
        catch (final Throwable e)
        {
            throw new IllegalArgumentException("Failed to execute Action: "
                                               + getTimerName() + " - "
                                               + e.getMessage(), e);
        }
    }

    @Override
    public void addStaticRequest(final URL url)
    {
        this.downloader.addRequest(url.toString());
        // UGLY -> change
    }

    @Override
    public URL getUrl()
    {
        return this.webRequest.getUrl();
    }

}
