package com.xceptance.xlt.common.actions;

import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.xceptance.xlt.common.util.action.validation.URLActionDataExecutableResult;
import com.xceptance.xlt.common.util.action.validation.URLActionDataExecutableResultFactory;

/**
 *  See {@link HtmlPageAction}. <br>
 *  Does the same except that the response is NOT parsed into the DOM.
 *  Used for XmlHttpRequests.
 * 
 * @author matthias mitterreiter
 * @extends {@link HtmlPageAction}.
 *
 */

public class XhrHtmlPageAction extends HtmlPageAction
{
    private WebResponse xhrResponse;

    public XhrHtmlPageAction(final HtmlPageAction previousAction,
                             final String name,
                             final WebRequest webRequest,
                             final Downloader downloader,
                             final URLActionDataExecutableResultFactory resultFactory)
    {
        super(previousAction, name, webRequest, downloader, resultFactory);
    }

    public XhrHtmlPageAction(final String name,
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
