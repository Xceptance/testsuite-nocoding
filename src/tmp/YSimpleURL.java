package com.xceptance.xlt.common.actions;

import java.net.MalformedURLException;
import java.util.List;

import com.gargoylesoftware.htmlunit.CookieManager;
import com.gargoylesoftware.htmlunit.DefaultCredentialsProvider;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.xceptance.xlt.api.actions.AbstractHtmlPageAction;
import com.xceptance.xlt.common.tests.YAbstractURLTestCase;
import com.xceptance.xlt.common.util.UserAgentUtils;
import com.xceptance.xlt.common.util.action.data.URLActionData;
import com.xceptance.xlt.engine.XltWebClient;

public class YSimpleURL extends AbstractHtmlPageAction
{
    protected URLActionData action;

    protected YAbstractURLTestCase testCase;

    protected YDownloader downloader;

    public YSimpleURL(final YAbstractURLTestCase testCase, final URLActionData action, final String login,
        final String password)
    {
        super(action.getName());
        this.action = action;
        this.testCase = testCase;
        this.downloader = new YDownloader((XltWebClient) getWebClient());
        setCredentials(login, password);
    }

    private void setCredentials(final String login, final String password)
    {
        if (login != null && password != null)
        {
            final DefaultCredentialsProvider credentialsProvider = new DefaultCredentialsProvider();
            credentialsProvider.addCredentials(login, password);
            this.getWebClient().setCredentialsProvider(credentialsProvider);
        }
    }

    public YSimpleURL(final YAbstractURLTestCase testCase, final AbstractHtmlPageAction prevAction,
        final URLActionData action)
    {
        super(prevAction, action.getName());
        this.action = action;
        this.testCase = testCase;
        this.downloader = new YDownloader((XltWebClient) getWebClient());
    }

    @Override
    public void preValidate() throws Exception
    {
        // FREELY EMPTY
    }

    @Override
    protected void execute() throws Exception
    {
        addHeadersToWebClient();
        addCookiesToWebClient();
        setUserAgentUID();
        loadHtmlPage();
        removeHeadersFromWebClient();
        loadAdditionalStaticContent();
    }
    protected void addCookiesToWebClient() throws MalformedURLException{
        WebClient webClient = this.getWebClient();
        if (!this.action.getCookies().isEmpty()){
            List<NameValuePair> cookies = action.getCookies();
            CookieManager manager = webClient.getCookieManager();
            
            for (NameValuePair cookie : cookies)
            {
                Cookie newCookie = new Cookie( action.getUrl().toString(), cookie.getName(), cookie.getValue());
                manager.addCookie(newCookie);
                manager.setCookiesEnabled(true);
                
            }
        }
    }
    protected void removeHeadersFromWebClient(){
        WebClient webClient = this.getWebClient();
        if (!this.action.getHeaders().isEmpty())
        {
            List<NameValuePair> headers = action.getHeaders();
            for (NameValuePair header : headers)
            {
                webClient.removeRequestHeader(header.getName());
            }
        }
    }
    protected void addHeadersToWebClient(){
        WebClient webClient = this.getWebClient();
        if (!this.action.getHeaders().isEmpty())
        {
            List<NameValuePair> headers = action.getHeaders();
            for (NameValuePair header : headers)
            {
                webClient.addRequestHeader(header.getName(), header.getValue());
            }
        }
    }
    protected void setUserAgentUID()
    {
        UserAgentUtils.setUserAgentUID(this.getWebClient(), testCase.getProperty("userAgent.UID", false));
    }

    protected void loadHtmlPage() throws MalformedURLException, Exception
    {
        loadPage(action.getUrl(), action.getMethod(), action.getParameters());
    }

    protected void loadAdditionalStaticContent() throws Exception
    {
        downloader.loadRequests(this.testCase, this.action);
    }

    @Override
    protected void postValidate() throws Exception
    {
        final HtmlPage page = getHtmlPage();
        action.validate(page);
        action.feedParameterInterpreter(page);
    }

    public void addRequest(final String url)
    {
        downloader.addRequest(url);
    }
}
