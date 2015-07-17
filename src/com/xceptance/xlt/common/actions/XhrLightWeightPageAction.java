package com.xceptance.xlt.common.actions;

import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.xceptance.xlt.common.util.action.validation.URLActionDataExecutableResult;
import com.xceptance.xlt.common.util.action.validation.URLActionDataExecutableResultFactory;

/**
 *  See {@link LightWeightPageAction}. <br>
 *   
 * @author matthias mitterreiter
 * @extends {@link LightWeightPageAction}.
 *
 */
public class XhrLightWeightPageAction extends LightWeightPageAction
{
    private WebResponse xhrResponse;

    public XhrLightWeightPageAction(final LightWeightPageAction previousAction,
                                    final String name,
                                    final WebRequest webRequest,
                                    final Downloader downloader,
                                    final URLActionDataExecutableResultFactory resultFactory)
    {
        super(previousAction, name, webRequest, downloader,resultFactory);
    }

    public XhrLightWeightPageAction(final String name,
                                    final WebRequest webRequest,
                                    final URLActionDataExecutableResultFactory resultFactory)
    {
        super(name, webRequest, resultFactory);
    }

    @Override
    protected void execute() throws Exception
    {
        this.xhrResponse = getWebClient().loadWebResponse(this.webRequest);
    }

    @Override
    protected void postValidate() throws Exception
    {
        this.result = this.resultFactory.getResult(this.xhrResponse);
    }

    @Override
    public URLActionDataExecutableResult getResult()
    {
        return this.result;
    }
}
