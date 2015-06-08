package com.xceptance.xlt.common.actions;

import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.xceptance.xlt.common.util.URLActionDataResult;

public class XhrHtmlPageAction extends HtmlPageAction
{
    public XhrHtmlPageAction(final HtmlPageAction previousAction,
                          final String name,
                          final WebRequest webRequest,
                          final Downloader downloader)
    {
        super(previousAction, name, webRequest, downloader);
    }

    public XhrHtmlPageAction(final String name, final WebRequest webRequest)
    {
        super(name, webRequest);

    }
    
    @Override
    protected void execute() throws Exception
    {
        final WebResponse xhrResponse = getWebClient().loadWebResponse(this.webRequest);
        result = new URLActionDataResult(xhrResponse);
    }
    @Override
    public URLActionDataResult getResult(){
        return this.result;
    }
}
