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
package test.com.xceptance.xlt.common.actions;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import test.com.xceptance.xlt.common.XltMockWebConnection;

import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.xceptance.xlt.api.actions.AbstractHtmlPageAction;
import com.xceptance.xlt.common.actions.SimpleURL;
import com.xceptance.xlt.common.util.CSVBasedURLAction;
import com.xceptance.xlt.engine.XltWebClient;

/**
 * @author rschwietzke
 */
public class SimpleURLTest
{
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    // dummy page content used for test URLs
    final String response = "<html><head><title>Test</title></head><body><h1>Test 1</h1></body></html>";

    // test URLs
    final String testUrl = "http://www.foobar.com/";

    // test login
    final String login = "login@jaja.com";

    // test pwd
    final String password = "PWD1716!";

    // header
    private final String HEADER = "Name,URL,Method,Parameters,ResponseCode,XPath,Text,Encoded";
    
    /**
     * Create records from string
     */
    private List<CSVRecord> createRecords(final String... records) throws IOException
    {
        final StringBuilder fullRecord = new StringBuilder();
        
        for (final String record:records)
        {
            fullRecord.append(record.replace("{url}", testUrl));
            fullRecord.append("\n");
        }
        
        final CSVFormat csvFormat = CSVFormat.RFC4180.toBuilder().withIgnoreEmptyLines(true).withCommentStart('#').withHeader().build();
        final CSVParser parser = new CSVParser(fullRecord.toString(), csvFormat);
        
        return parser.getRecords();    
    }
    
    /**
     * Mock the action for later execution
     * 
     * @param action
     * @throws IOException 
     */
    private List<AbstractHtmlPageAction> mockIt(final String login, final String password, final String... records) throws IOException
    {
        final List<CSVRecord> csvRecords = createRecords(records);   
        
        final List<AbstractHtmlPageAction> actions = new ArrayList<AbstractHtmlPageAction>();
        
        // our action tracker to build up a correct chain of pages
        AbstractHtmlPageAction lastAction = null; 
            
        // let's loop about the data we have
        for (final CSVRecord csvRecord : csvRecords)
        {
            if (lastAction == null)
            {
                // our first action, so start the browser too
                lastAction = new SimpleURL(null, new CSVBasedURLAction(csvRecord), login, password);
            }
            else
            {
                // subsequent actions
                lastAction = new SimpleURL(null, lastAction, new CSVBasedURLAction(csvRecord));
            }
            
            // create mocked web connection and set the response
            final MockWebConnection conn = new XltMockWebConnection((XltWebClient) lastAction.getWebClient());
            conn.setResponse(new URL(testUrl), response);

            ((XltWebClient) lastAction.getWebClient()).setWebConnection(conn);

            // add
            actions.add(lastAction);
        }
        
        return actions;
    }    

    
    /**********************************************************************************
     * Check names
     * @throws IOException 
     *********************************************************************************/
    @Test
    public void testNames_First() throws IOException
    {
        // Name,URL,Method,Parameters,ResponseCode,XPath,Text,Encoded
        final List<AbstractHtmlPageAction> actions = mockIt(null, null, HEADER, "Foo,{url},,,200,//h1,Test 1,");

        Assert.assertEquals("Timer name does not match.", "Foo", actions.get(0).getTimerName());
    }

    @Test
    public void testNames_NotPassed_First_Empty() throws IOException
    {
        // Name,URL,Method,Parameters,ResponseCode,XPath,Text,Encoded
        final List<AbstractHtmlPageAction> actions = mockIt(null, null, HEADER, ",{url},,,200,//h1,Test 1,");

        Assert.assertEquals("Timer name does not match.", "Action-1", actions.get(0).getTimerName());
    }

    @Test
    public void testNames_NotPassed_First_Null() throws IOException
    {
        // Name,URL,Method,Parameters,ResponseCode,XPath,Text,Encoded
        final List<AbstractHtmlPageAction> actions = mockIt(null, null, "URL", "{url}");

        Assert.assertEquals("Timer name does not match.", "Action-1", actions.get(0).getTimerName());
    }
    
    @Test
    public void testNames_NotPassed_Next_Null() throws IOException
    {
        // Name,URL,Method,Parameters,ResponseCode,XPath,Text,Encoded
        final List<AbstractHtmlPageAction> actions = mockIt(null, null, "URL", "{url}", "{url}");

        Assert.assertEquals("Timer name does not match.", "Action-1", actions.get(0).getTimerName());
        Assert.assertEquals("Timer name does not match.", "Action-2", actions.get(1).getTimerName());
    }
    
    
    /**********************************************************************************
     * Check setting of credentials
     * @throws IOException 
     *********************************************************************************/
    @Test
    public void testCredentials_Set() throws IOException
    {
        final List<AbstractHtmlPageAction> actions = mockIt(login, password, "URL", "{url}");
        final Credentials creds = actions.get(0).getWebClient().getCredentialsProvider().getCredentials(AuthScope.ANY);

        Assert.assertEquals("Login does not match.", login, creds.getUserPrincipal().getName());
        Assert.assertEquals("Password does not match.", password, creds.getPassword());
    }

    @Test
    public void testCredentials_NotSet() throws IOException
    {
        final List<AbstractHtmlPageAction> actions = mockIt(null, null, "URL", "{url}");
        final Credentials creds = actions.get(0).getWebClient().getCredentialsProvider().getCredentials(AuthScope.ANY);

        Assert.assertNull("There is an credential provider set.", creds);
    }

    /**********************************************************************************
     * Check that xpath and verification text together work.
     *********************************************************************************/

    @Test
    public void testAll_Pass() throws Throwable
    {
        final List<AbstractHtmlPageAction> actions = mockIt(null, null, "URL,XPath,Text", "{url},//h1,\"Test 1\"");
        actions.get(0).run();
    }

    @Test
    public void testAll_Pass_TextIsRegexp() throws Throwable
    {
        final List<AbstractHtmlPageAction> actions = mockIt(null, null, "URL,XPath,Text", "{url},//h1,\"Test [0-9]\"");
        actions.get(0).run();
    }

    @Test
    public void testAll_Fail_TextIsRegexp() throws Throwable
    {
        final List<AbstractHtmlPageAction> actions = mockIt(null, null, "URL,XPath,Text", "{url},//h1,\"Test [0-9]{2,}\"");
        
        thrown.expect(AssertionError.class);
        thrown.expectMessage("Text does not match. Expected:<Test [0-9]{2,}> but was:<Test 1>");

        actions.get(0).run();
    }

    @Test
    public void testAll_Fail_TextDoesNotMatch() throws Throwable
    {
        final List<AbstractHtmlPageAction> actions = mockIt(null, null, "URL,XPath,Text", "{url},//h1,\"Test 2\"");

        thrown.expect(AssertionError.class);
        thrown.expectMessage("Text does not match. Expected:<Test 2> but was:<Test 1>");

        actions.get(0).run();
    }

    /**********************************************************************************
     * Check Xpath only aka exists
     *********************************************************************************/

    @Test
    public void testXPath_Pass_NullText() throws Throwable
    {
        final List<AbstractHtmlPageAction> actions = mockIt(null, null, "URL,XPath", "{url},//h1");
        actions.get(0).run();
    }

    @Test
    public void testXPath_Pass_EmptyText() throws Throwable
    {
        final List<AbstractHtmlPageAction> actions = mockIt(null, null, "URL,XPath,Text", "{url},//h1,");
        actions.get(0).run();
    }

    @Test
    public void testXPath_Fail() throws Throwable
    {
        final List<AbstractHtmlPageAction> actions = mockIt(null, null, "URL,XPath", "{url},//h2");

        thrown.expect(AssertionError.class);
        thrown.expectMessage("Xpath not found: <//h2>");

        actions.get(0).run();
    }

    /**********************************************************************************
     * Check text only
     *********************************************************************************/

    @Test
    public void testText_Path() throws Throwable
    {
        final List<AbstractHtmlPageAction> actions = mockIt(null, null, "URL,Text", "{url},Test 1");
        actions.get(0).run();
    }

    @Test
    public void testText_Fail() throws Throwable
    {
        final List<AbstractHtmlPageAction> actions = mockIt(null, null, "URL,Text", "{url},Test 2");

        thrown.expect(AssertionError.class);
        thrown.expectMessage("Text is not on the page. Expected:<Test 2>");

        actions.get(0).run();
    }

    @Test
    public void testText_Pass_RegExp() throws Throwable
    {
        final List<AbstractHtmlPageAction> actions = mockIt(null, null, "URL,Text", "{url},Test [0-1]");
        actions.get(0).run();
    }

    @Test
    public void testText_Fail_Regexp() throws Throwable
    {
        final List<AbstractHtmlPageAction> actions = mockIt(null, null, "URL,Text", "{url},Test [5-9]");

        thrown.expect(AssertionError.class);
        thrown.expectMessage("Text is not on the page. Expected:<Test [5-9]>");

        actions.get(0).run();
    }

    /**********************************************************************************
     * Response Code only check
     *********************************************************************************/

    @Test
    public void testResponseCodeOnly_Pass() throws Throwable
    {
        final List<AbstractHtmlPageAction> actions = mockIt(null, null, "URL,ResponseCode", "{url},200");
        actions.get(0).run();
    }

    @Test
    public void testResponseCodeOnly_Fail() throws Throwable
    {
        final List<AbstractHtmlPageAction> actions = mockIt(null, null, "URL,ResponseCode", "{url},404");

        thrown.expect(AssertionError.class);
        thrown.expectMessage("Response code does not match expected:<404> but was:<200>");

        actions.get(0).run();
    }
}
