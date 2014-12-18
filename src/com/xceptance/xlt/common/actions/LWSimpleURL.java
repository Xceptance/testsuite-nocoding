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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Assert;

import com.gargoylesoftware.htmlunit.DefaultCredentialsProvider;
import com.xceptance.common.util.RegExUtils;
import com.xceptance.xlt.api.actions.AbstractLightWeightPageAction;
import com.xceptance.xlt.common.tests.AbstractURLTestCase;
import com.xceptance.xlt.common.util.CSVBasedURLAction;
import com.xceptance.xlt.common.util.UserAgentUtils;
import com.xceptance.xlt.engine.XltWebClient;

/**
 * This is a simple test class for pulling URLs. It is fully configurable using properties.
 */
public class LWSimpleURL extends AbstractLightWeightPageAction
{
    // the action to be executed
    protected final CSVBasedURLAction action;

    // the test case reference for property lookup in the actions
    protected final AbstractURLTestCase testCase;

    // Downloader for additional requests belonging to this action (i.e. static content)
    protected final Downloader downloader;

    /**
     * @param previousAction
     * @param timerName
     */
    public LWSimpleURL(final AbstractURLTestCase testCase, final CSVBasedURLAction action, final String login,
        final String password)
    {
        super(action.getName(testCase));

        this.testCase = testCase;
        this.action = action;
        this.downloader = new Downloader((XltWebClient) getWebClient());

        // add credentials, if any
        if (login != null && password != null)
        {
            final DefaultCredentialsProvider credentialsProvider = new DefaultCredentialsProvider();
            credentialsProvider.addCredentials(login, password);

            this.getWebClient().setCredentialsProvider(credentialsProvider);
        }
    }

    /**
     * @param previousAction
     * @param timerName
     */
    public LWSimpleURL(final AbstractURLTestCase testCase, final AbstractLightWeightPageAction prevAction,
        final CSVBasedURLAction action)
    {
        super(prevAction, action.getName(testCase));
        this.action = action;
        this.testCase = testCase;
        this.downloader = new Downloader((XltWebClient) getWebClient());
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.xceptance.xlt.api.actions.AbstractAction#preValidate()
     */
    @Override
    public void preValidate() throws Exception
    {
        // do not prevalidate anything here, assume a correct URL
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.xceptance.xlt.api.actions.AbstractAction#execute()
     */
    @Override
    protected void execute() throws Exception
    {
        // set the user agent UID if required
        UserAgentUtils.setUserAgentUID(this.getWebClient(), testCase.getProperty("userAgent.UID", false));

        loadPage(action.getURL(testCase), action.getMethod(), action.getParameters(testCase));

        downloader.loadRequests(this.testCase, this.action);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.xceptance.xlt.api.actions.AbstractAction#postValidate()
     */
    @Override
    protected void postValidate() throws Exception
    {
        // check response code
        action.getHttpResponseCodeValidator().validate(this.getLightWeightPage());

        final String content = this.getContent();

        final Pattern pattern = action.getRegexp(testCase);
        final String text = action.getText(testCase);

        // check anything else?
        if (pattern != null)
        {
            // ok, find the pattern, should have something at least
            Assert.assertNotNull("Page was totally empty", content);

            final Matcher matcher = pattern.matcher(content);

            Assert.assertTrue(MessageFormat.format("Regexp <{0}> does not match", pattern), matcher.find());

            // shall we check the text as well?
            if (text != null)
            {
                // if we got a group in the pattern, we match the text against the group, otherwise
                // we match in against the entire pattern
                final String actual;
                if (matcher.groupCount() > 0)
                {
                    actual = matcher.group(1);
                }
                else
                {
                    actual = matcher.group();
                }

                final Pattern textPattern = RegExUtils.getPattern(text);
                Assert.assertTrue(MessageFormat.format("Text does not match. Expected:<{0}> but was:<{1}>", text,
                                                       actual), textPattern.matcher(actual).matches());
            }
        }
        else if (text != null)
        {
            // ok, find the pattern, should have something at least
            Assert.assertNotNull("Page was totally empty", content);

            Assert.assertNotNull(MessageFormat.format("Text is not on the page. Expected:<{0}>", text),
                                 RegExUtils.getFirstMatch(content, text));
        }

        // take care of the parameters to fill up the interpreter
        final List<Pattern> regexpGetters = action.getRegExpGetterList(testCase);
        final List<Object> gettersResults = new ArrayList<Object>(regexpGetters.size());
        for (int i = 0; i < regexpGetters.size(); i++)
        {
            final Pattern p = regexpGetters.get(i);
            if (p == null)
            {
                gettersResults.add(null);
                continue;
            }

            // get the elements from the page
            final Matcher matcher = p.matcher(content);

            if (matcher.find())
            {
                final String[] matches = new String[matcher.groupCount() + 1];
                matches[0] = matcher.group();

                for (int x = 1; x <= matcher.groupCount(); x++)
                {
                    matches[x] = matcher.group(x);
                }
                gettersResults.add(matches);
            }
            else
            {
                gettersResults.add(null);
            }
        }
        // send it back for spicing up the interpreter
        action.setRegExpGetterResult(gettersResults);
    }

    /**
     * Add an additional request to the current action.
     * 
     * @param url
     *            request URL
     */
    public void addRequest(final String url)
    {
        downloader.addRequest(url);
    }
}
