package com.xceptance.xlt.common.util.action.execution;

import com.gargoylesoftware.htmlunit.WebRequest;
import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.api.util.XltProperties;
import com.xceptance.xlt.common.actions.Downloader;
import com.xceptance.xlt.common.actions.HtmlPageAction;
import com.xceptance.xlt.common.actions.XhrHtmlPageAction;
import com.xceptance.xlt.common.util.ParameterUtils;
import com.xceptance.xlt.common.util.action.validation.URLActionDataExecutableResultFactory;
import com.xceptance.xlt.engine.XltWebClient;

public class HtmlPageActionFactory extends URLActionDataExecutableFactory
{
    private XltProperties properties;

    private HtmlPageAction previousAction;

    private final URLActionDataExecutableResultFactory resultFactory;

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

    @Override
    public URLActionDataExecutable createPageAction(
                                                    final String name,
                                                    final WebRequest request)
    {

        HtmlPageAction action;

        if (this.previousAction == null)
        {
            action = new HtmlPageAction(name, request, resultFactory);
            previousAction = action;
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

    @Override
    public URLActionDataExecutable createXhrPageAction(
                                                       final String name,
                                                       final WebRequest request)
    {
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
