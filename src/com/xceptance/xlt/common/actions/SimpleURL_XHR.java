/**  
 *  Copyright 2014 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License. 
 *
 */
package com.xceptance.xlt.common.actions;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;

import com.gargoylesoftware.htmlunit.DefaultPageCreator;
import com.gargoylesoftware.htmlunit.DefaultPageCreator.PageType;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.HTMLParser;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.xceptance.common.util.RegExUtils;
import com.xceptance.xlt.api.actions.AbstractHtmlPageAction;
import com.xceptance.xlt.common.tests.AbstractURLTestCase;
import com.xceptance.xlt.common.util.CSVBasedURLAction;
import com.xceptance.xlt.common.util.UserAgentUtils;

/**
 * Performs an AJAX request and parses the response into an container element that can be used for validation.
 */
public class SimpleURL_XHR extends SimpleURL
{
    protected WebResponse response;

    /**
     * @param testCase
     * @param prevAction
     * @param action
     */
    public SimpleURL_XHR(final AbstractURLTestCase testCase, final AbstractHtmlPageAction prevAction,
        final CSVBasedURLAction action)
    {
        super(testCase, prevAction, action);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void execute() throws Exception
    {
        final HtmlPage page = getPreviousAction().getHtmlPage();

        UserAgentUtils.setUserAgentUID(this.getWebClient(), testCase.getProperty("userAgent.UID", false));

        final WebRequest request = createWebRequestSettings(action.getURL(), action.getMethod(), action.getParameters());
        request.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
        request.setAdditionalHeader("Referer", page.getUrl().toExternalForm());
        request.setXHR();

        response = getWebClient().loadWebResponse(request);
        setHtmlPage(page);

        downloader.loadRequests(this.testCase, this.action);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void postValidate() throws Exception
    {
        // response code correct?
        Assert.assertEquals("Response code did not match", action.getHttpResponseCodeValidator().getHttpResponseCode(),
                            response.getStatusCode());

        final HtmlPage page = getHtmlPage();
        final HtmlElement container;
        if (DefaultPageCreator.determinePageType(response.getContentType()) == PageType.HTML)
        {
            container = (HtmlElement) page.createElement("div");
            HTMLParser.parseFragment(container, response.getContentAsString());
        }
        else
        {
            return;
        }

        final String xpath = action.getXPath(testCase);
        final String text = action.getText(testCase);

        // check anything else?
        if (xpath != null)
        {
            // get the elements from the page
            @SuppressWarnings("unchecked")
            final List<HtmlElement> elements = (List<HtmlElement>) container.getByXPath(xpath);

            // verify existence
            Assert.assertFalse("Xpath not found: <" + xpath + ">", elements.isEmpty());

            // shall we check the text as well?
            if (text != null)
            {
                final String actual = elements.get(0).asText().trim();
                Assert.assertNotNull(MessageFormat.format("Text does not match. Expected:<{0}> but was:<{1}>", text,
                                                          actual), RegExUtils.getFirstMatch(actual, text));
            }
        }
        else if (text != null)
        {
            // ok, xpath was null, so we go for the text on the page only
            final String responseString = response.getContentAsString();
            Assert.assertNotNull("Response was totally empty", responseString);

            Assert.assertNotNull(MessageFormat.format("Text is not in response. Expected:<{0}>", text),
                                 RegExUtils.getFirstMatch(responseString, text));
        }

        // take care of the parameters to fill up the interpreter
        final List<String> xpathGetters = action.getXPathGetterList(testCase);
        final List<Object> xpathGettersResults = new ArrayList<Object>(xpathGetters.size());
        for (int i = 0; i < xpathGetters.size(); i++)
        {
            final String xp = xpathGetters.get(i);

            // nothing to do, skip and return empty result
            if (xp == null)
            {
                xpathGettersResults.add(null);
                continue;
            }

            // get the elements from the page
            @SuppressWarnings("unchecked")
            final List<HtmlElement> elements = (List<HtmlElement>) container.getByXPath(xp);

            if (!elements.isEmpty())
            {
                if (elements.size() > 1)
                {
                    // keep the entire list
                    xpathGettersResults.add(elements);
                }
                else
                {
                    // keep only the elements
                    xpathGettersResults.add(elements.get(0));
                }
            }
            else
            {
                xpathGettersResults.add(null);
            }

        }
        // send it back for spicing up the interpreter
        action.setXPathGetterResult(xpathGettersResults);

    }
}
