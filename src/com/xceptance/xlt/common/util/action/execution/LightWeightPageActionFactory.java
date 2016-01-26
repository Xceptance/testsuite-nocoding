package com.xceptance.xlt.common.util.action.execution;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.api.util.XltProperties;
import com.xceptance.xlt.common.actions.Downloader;
import com.xceptance.xlt.common.actions.LightWeightPageAction;
import com.xceptance.xlt.common.actions.ModifiedAbstractHtmlPageAction;
import com.xceptance.xlt.common.actions.ModifiedAbstractLightWeightPageAction;
import com.xceptance.xlt.common.actions.XhrLightWeightPageAction;
import com.xceptance.xlt.common.util.NoCodingPropAdmin;
import com.xceptance.xlt.common.util.ParameterUtils;
import com.xceptance.xlt.common.util.action.validation.URLActionDataExecutableResultFactory;
import com.xceptance.xlt.engine.XltWebClient;

/**
 * Factory Class <br>
 * Produces {@link ModifiedAbstractHtmlPageAction}s, depending on the type of request. <br>
 * Cases:
 * <ul>
 * <li>Request is not a XmlHttpRequest -> use {@link #createPageAction(String, WebRequest)}
 * <li>Request is a XmlHttpRequest -> use {@link #createXhrPageAction(String, WebRequest)}
 * </ul>
 * The response is NOT parsed into the dom. <br>
 * See the execution model of {@link ModifiedAbstractLightWeightPageAction}
 * 
 * @author matthias mitterreiter
 */
public class LightWeightPageActionFactory extends URLActionDataExecutionableFactory
{
    private NoCodingPropAdmin propAdmin;

    private LightWeightPageAction previousAction;

    private final URLActionDataExecutableResultFactory resultFactory;

    /**
     * @param properties
     *            {@link XltProperties} for {@link WebClient} configuration.
     */
    public LightWeightPageActionFactory(final NoCodingPropAdmin propAdmin)
    {
        super();
        setPropertiesAdmin(propAdmin);
        this.resultFactory = new URLActionDataExecutableResultFactory();
        XltLogger.runTimeLogger.debug("Creating new Instance");
    }

    private void setPropertiesAdmin(final NoCodingPropAdmin propAdmin)
    {
        ParameterUtils.isNotNull(propAdmin, "NoCodingPropAdmin");
        this.propAdmin = propAdmin;
    }

    @Override
    public URLActionDataExecutionable createPageAction(final String name, final WebRequest request)
    {
        ParameterUtils.isNotNull(name, "name");
        ParameterUtils.isNotNull(request, "WebRequest");

        LightWeightPageAction action;

        if (this.previousAction == null)
        {
            action = new LightWeightPageAction(name, request, resultFactory);
            previousAction = action;
            action.setDownloader(createDownloader());
            configureWebClient((XltWebClient) action.getWebClient());
        }
        else
        {
            action = new LightWeightPageAction(previousAction, name, request, createDownloader(), resultFactory);
        }
        this.previousAction = action;
        return action;
    }

    private Downloader createDownloader()
    {
        final Boolean userAgentUID = this.propAdmin.getPropertyByKey(NoCodingPropAdmin.USERAGENTUID, false);
        final int threadCount = this.propAdmin.getPropertyByKey(NoCodingPropAdmin.DOWNLOADTHREADS, 1);

        final Downloader downloader = new Downloader((XltWebClient) previousAction.getWebClient(), threadCount, userAgentUID);

        return downloader;

    }

    private void configureWebClient(final XltWebClient webClient)
    {
        this.propAdmin.configWebClient(webClient);
    }

    @Override
    public URLActionDataExecutionable createXhrPageAction(final String name, final WebRequest request)
    {
        ParameterUtils.isNotNull(name, "name");
        ParameterUtils.isNotNull(request, "WebRequest");

        if (previousAction == null)
        {
            throw new IllegalArgumentException("Xhr action cannot be the first action");
        }
        final XhrLightWeightPageAction xhrAction = new XhrLightWeightPageAction(previousAction, name, request, createDownloader(),
                                                                                resultFactory);

        previousAction = xhrAction;
        return xhrAction;
    }
}
