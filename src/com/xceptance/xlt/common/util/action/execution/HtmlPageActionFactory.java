package com.xceptance.xlt.common.util.action.execution;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.api.util.XltProperties;
import com.xceptance.xlt.common.actions.Downloader;
import com.xceptance.xlt.common.actions.HtmlPageAction;
import com.xceptance.xlt.common.actions.ModifiedAbstractHtmlPageAction;
import com.xceptance.xlt.common.actions.XhrHtmlPageAction;
import com.xceptance.xlt.common.util.ParameterUtils;
import com.xceptance.xlt.common.util.action.validation.URLActionDataExecutableResultFactory;
import com.xceptance.xlt.engine.XltWebClient;

/**
 * Factory Class <br>
 * Produces {@link URLActionDataExecutionable}s, depending on the type of request. <br>
 * Cases:
 * <ul>
 * <li>Request is not a XmlHttpRequest -> use {@link #createPageAction(String, WebRequest)}
 * <li>Request is a XmlHttpRequest -> use {@link #createXhrPageAction(String, WebRequest)}
 * </ul>
 * The response is parsed into the dom. <br>
 * See the execution model of {@link ModifiedAbstractHtmlPageAction}
 * 
 * @author matthias mitterreiter
 */

public class HtmlPageActionFactory extends URLActionDataExecutionableFactory
{
    private XltProperties properties;

    private HtmlPageAction previousAction;

    private final URLActionDataExecutableResultFactory resultFactory;

    /**
     * @param properties
     *            {@link XltProperties} for {@link WebClient} configuration.
     */
    public HtmlPageActionFactory(final XltProperties properties)
    {
        super();
        setProperties(properties);
        this.resultFactory = new URLActionDataExecutableResultFactory();
        XltLogger.runTimeLogger.debug("Creating new Instance");
    }

    private void setProperties(final XltProperties properties)
    {
        ParameterUtils.isNotNull(properties, "XltProperties");
        this.properties = properties;
    }

    /**
     * Returns a {@link HtmlPageAction}, that can fire a request.
     * @param name
     *            Name of the URLActionDataExecutionable
     * @param request
     *            the WebRequest that should be fired
     * @return {@link URLActionDataExecutionable}
     *  
     */
    @Override
    public URLActionDataExecutionable createPageAction(final String name,
                                                       final WebRequest request)
    {

        HtmlPageAction action;
        
        ParameterUtils.isNotNull(name, "name");
        ParameterUtils.isNotNull(request, "WebRequest");
        
        
        if (this.previousAction == null)
        {
            action = new HtmlPageAction(name, request, resultFactory);
            this.previousAction = action;

            // bad design createDownloader() depends on action

            action.setDownloader(createDownloader());

        }
        else
        {
            action = new HtmlPageAction(previousAction,
                                        name,
                                        request,
                                        createDownloader(),
                                        resultFactory);
        }
        this.previousAction = action;
        return action;

    }

    private Downloader createDownloader()
    {
        final Boolean userAgentUID = properties.getProperty("userAgent.UID",
                                                            false);
        final int threadCount = properties.getProperty("com.xceptance.xlt.staticContent.downloadThreads",
                                                       1);

        final Downloader downloader = new Downloader((XltWebClient) previousAction.getWebClient(),
                                                     threadCount,
                                                     userAgentUID);

        return downloader;

    }
    /**
     * Returns a {@link XhrHtmlPageAction}, that can fire a request.
     * @param name
     *            Name of the URLActionDataExecutionable
     * @param request
     *            the WebRequest that should be fired
     * @return {@link URLActionDataExecutionable}
     * @throws IllegalArgumentException
     *  if there has not been fired a request before. <br>
     *  Because in this case no WebClient is available. 
     *  
     */
    @Override
    public URLActionDataExecutionable createXhrPageAction(final String name,
                                                          final WebRequest request)
    {
        ParameterUtils.isNotNull(name, "name");
        ParameterUtils.isNotNull(request, "WebRequest");
        
        if (previousAction == null)
        {
            throw new IllegalArgumentException("Xhr action cannot be the first action");
        }
        final XhrHtmlPageAction xhrAction = new XhrHtmlPageAction(previousAction,
                                                                  name,
                                                                  request,
                                                                  createDownloader(),
                                                                  resultFactory);
        previousAction = xhrAction;

        return xhrAction;

    }
}
