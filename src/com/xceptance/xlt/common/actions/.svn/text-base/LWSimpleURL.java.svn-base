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

/**
 * This is a simple test class for pulling URLs. It is fully configurable using properties.
 */
public class LWSimpleURL extends AbstractLightWeightPageAction
{
    // the action to be executed
    private final CSVBasedURLAction action;

    // the test case reference for property lookup in the actions
    private final AbstractURLTestCase testCase;

    /**
     * @param previousAction
     * @param timerName
     */
    public LWSimpleURL(final AbstractURLTestCase testCase, final CSVBasedURLAction action, final String login, final String password)
    {
        super(action.getName(testCase));

        this.testCase = testCase;
        this.action = action;
        
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
    public LWSimpleURL(final AbstractURLTestCase testCase, final AbstractLightWeightPageAction prevAction, final CSVBasedURLAction action)
    {
        super(prevAction, action.getName(testCase));
        this.action = action;
        this.testCase = testCase;
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
        loadPage(action.getURL(testCase), action.getMethod(), action.getParameters(testCase));
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
        final String text  = action.getText(testCase);        
        
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
            if (pattern == null)
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
}
