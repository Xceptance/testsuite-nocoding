package com.xceptance.xlt.common.actions;

import java.net.URL;

import com.gargoylesoftware.htmlunit.WebRequest;
import com.xceptance.xlt.api.htmlunit.LightWeightPage;
import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.common.util.ParameterUtils;
import com.xceptance.xlt.common.util.action.execution.URLActionDataExecutionable;
import com.xceptance.xlt.common.util.action.validation.URLActionDataExecutableResult;
import com.xceptance.xlt.common.util.action.validation.URLActionDataExecutableResultFactory;

/**
 * All it does, is loading a WebResponse for a passed WebRequest. The WebResponse in form of a {@link LightWeightPage}
 * is wrapped in a {@link URLActionDataExecutableResult}. Additionally static content requests are fired and loaded via
 * {@link Downloader}. Since this class extends {@link ModifiedAbstractLightWeightPageAction}, the response is NOT
 * parsed into the DOM.
 * 
 * @extends {@link ModifiedAbstractLightWeightPageAction}
 * @implements {@link URLActionDataExecutionable}
 * @author matthias mitterreiter
 */

public class LightWeightPageAction extends
    ModifiedAbstractLightWeightPageAction implements URLActionDataExecutionable
{

    /**
     * For downloading static content. To add a request, use {@link #addStaticRequest(URL)}
     */
    protected Downloader downloader;

    /**
     * The WebRequest that is fired.
     */
    protected WebRequest webRequest;

    /**
     * The Wrapper for the WebResponse.
     */
    protected URLActionDataExecutableResult result;

    /**
     * Automatically produces the {@link URLActionDataExecutableResult}
     */
    protected URLActionDataExecutableResultFactory resultFactory;

    /**
     * @param previousAction
     *            : the action that is is executed before.
     * @param name
     *            : name of the action.
     * @param webRequest
     *            : the request that is fired.
     * @param downloader
     *            : the {@link Downloader}.
     * @param resultFactory
     *            : produces the {@link #result}
     */
    public LightWeightPageAction(final LightWeightPageAction previousAction,
                                 final String name,
                                 final WebRequest webRequest,
                                 final Downloader downloader,
                                 final URLActionDataExecutableResultFactory resultFactory)
    {
        super(previousAction, name);
        setWebRequest(webRequest);
        setDownloader(downloader);
        setResultFactory(resultFactory);
        XltLogger.runTimeLogger.debug("Creating new Instance");

    }

    /**
     * This constructor should be used if there is no previous action available. <br>
     * In this case a {@link WebClien} is constructed.
     * 
     * @param name
     *            : name of the action.
     * @param webRequest
     *            : the request that is fired.
     * @param resultFactory
     *            : produces the {@link #result}
     */
    public LightWeightPageAction(final String name,
                                 final WebRequest webRequest,
                                 final URLActionDataExecutableResultFactory resultFactory)
    {
        super(null, name);
        setWebRequest(webRequest);
        setResultFactory(resultFactory);
        XltLogger.runTimeLogger.debug("Creating new Instance");

    }

    private void setResultFactory(final URLActionDataExecutableResultFactory resultFactory)
    {
        ParameterUtils.isNotNull(resultFactory,
                                 "URLActionDataExecutableResultFactory");
        this.resultFactory = resultFactory;
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

        // now download explicitly added static content
        downloader.loadRequests();
    }

    @Override
    protected void postValidate() throws Exception
    {
        this.result = this.resultFactory.getResult(getLightWeightPage());
    }

    @Override
    public void preValidate() throws Exception
    {

    }

    /**
     * Adds a static content request, which gets loaded by the {@link Downloader}.
     */
    public void addRequest(final String url)
    {
        downloader.addRequest(url);
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

    /**
     * Adds a static content request, which gets loaded by the {@link Downloader}.
     */
    @Override
    public void addStaticRequest(final URL url)
    {
        this.downloader.addRequest(url.toString());

    }

    /**
     * @return the url of the {@link WebRequest}.
     */
    @Override
    public URL getUrl()
    {
        return this.webRequest.getUrl();
    }

}
