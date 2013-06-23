/*
 * File: XltMockWebConnection.java
 * Created on: Nov 11, 2009
 * 
 * Copyright 2009
 * Xceptance Software Technologies GmbH, Germany.
 */
package test.com.xceptance.xlt.common;

import java.io.IOException;

import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.xceptance.xlt.engine.XltWebClient;

/**
 * Mocked web connection which provides the same functionalities as its HtmlUnit counterpart.
 * <p>
 * The only difference is the post-processing of responses performed by the {@link XltWebClient}.
 * </p>
 * 
 * @author Hartmut Arlt (Xceptance Software Technologies GmbH)
 * @version $Id: XltMockWebConnection.java 4715 2010-08-24 13:32:35Z jwerner $
 */
public class XltMockWebConnection extends MockWebConnection
{
    /**
     * XLT web client.
     */
    private final XltWebClient webClient;

    /**
     * Constructor.
     * 
     * @param client
     *            XLT web client to use
     */
    public XltMockWebConnection(final XltWebClient client)
    {
        webClient = client;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebResponse getResponse(final WebRequest settings) throws IOException
    {
        return webClient.processResponse(super.getResponse(settings));
    }
}
