package com.xceptance.xlt.common.actions;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import com.gargoylesoftware.htmlunit.CookieManager;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.xceptance.xlt.api.actions.AbstractHtmlPageAction;
import com.xceptance.xlt.common.tests.YAbstractURLTestCase;
import com.xceptance.xlt.common.util.action.data.URLActionData;

public class YSimpleURL_XHR extends YSimpleURL
{
    protected WebRequest xhrRequest;

    protected WebResponse xhrResponse;

    public YSimpleURL_XHR(final YAbstractURLTestCase testCase, final AbstractHtmlPageAction prevAction,
        final URLActionData action)
    {
        super(testCase, prevAction, action);
    }

    @Override
    protected void execute() throws Exception
    {
        loadHtmlPage();
        setUserAgentUID();
        createXhrWebRequest();
        loadXhrWebResponse();
        loadAdditionalStaticContent();

    }

    @Override
    protected void loadHtmlPage()
    {
        setHtmlPage(getPreviousAction().getHtmlPage());
    }

    private void createXhrWebRequest() throws MalformedURLException
    {
        WebRequest xhrRequest = createWebRequestSettings(action.getUrl(), action.getMethod(), action.getParameters());
        xhrRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
        xhrRequest.setAdditionalHeader("Referer", getHtmlPage().getUrl().toExternalForm());
        if (!this.action.getHeaders().isEmpty())
        {
            List<NameValuePair> headers = action.getHeaders();
            for (NameValuePair header : headers)
            {
                System.err.println("adding header: " + header.getName());
                xhrRequest.setAdditionalHeader(header.getName(), header.getValue());
            }
        }
        if (!this.action.getCookies().isEmpty()){
            List<NameValuePair> cookies = action.getCookies();
            CookieManager manager = this.getWebClient().getCookieManager();
            
            for (NameValuePair cookie : cookies)
            {
                Cookie newCookie = new Cookie( action.getUrl().toString(), cookie.getName(), cookie.getValue());
                manager.addCookie(newCookie);
                manager.setCookiesEnabled(true);
                
            }
        }
        xhrRequest.setXHR();
        xhrRequest.setDocumentRequest();
        this.xhrRequest = xhrRequest;
    }

    private void loadXhrWebResponse() throws IOException
    {
        WebResponse xhrResponse = getWebClient().loadWebResponse(xhrRequest);
        this.xhrResponse = xhrResponse;
    }

    @Override
    protected void postValidate() throws Exception
    {
        final HtmlPage page = getHtmlPage();
        action.validate(page, xhrResponse);
        action.feedParameterInterpreter(page, xhrResponse);
    }

}
