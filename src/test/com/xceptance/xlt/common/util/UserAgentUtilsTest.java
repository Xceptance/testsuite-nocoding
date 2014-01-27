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
package test.com.xceptance.xlt.common.util;

import java.io.IOException;
import java.net.URL;

import junit.framework.Assert;

import org.junit.Test;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.WebClient;
import com.xceptance.xlt.common.util.UserAgentUtils;

public class UserAgentUtilsTest
{

    // dummy page content used for test URLs
    final String response = "<html><head><title>Test</title></head><body><h1>Test 1</h1></body></html>";

    // test URLs
    final String testUrl = "http://www.foobar.com/";
    
    /**
     * Change the agent twice
     * @throws IOException 
     * @throws FailingHttpStatusCodeException 
     */
    @Test
    public void testSetUserAgentUID_true() throws FailingHttpStatusCodeException, IOException
    {
        // create mocked web connection 
        final WebClient webClient = new WebClient();
        final MockWebConnection conn = new MockWebConnection();
        conn.setResponse(new URL(testUrl), response);
        webClient.setWebConnection(conn);        
        
        // change it
        UserAgentUtils.setUserAgentUID(webClient, true);
        
        // request it
        webClient.getPage(testUrl);
        
        final String newUserAgent1 = conn.getLastWebRequest().getAdditionalHeaders().get("User-Agent");
        Assert.assertTrue(newUserAgent1.matches("^.* UID/" + "[a-z0-9]{8}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{12}$"));

        // change it again
        UserAgentUtils.setUserAgentUID(webClient, true);

        // request it
        webClient.getPage(testUrl);
        
        final String newUserAgent2 = conn.getLastWebRequest().getAdditionalHeaders().get("User-Agent");
        Assert.assertTrue(newUserAgent2.matches("^.* UID/" + "[a-z0-9]{8}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{12}$"));
        Assert.assertFalse(newUserAgent2.equals(newUserAgent1));
    }

    /**
     * Do not change it
     * @throws IOException 
     * @throws FailingHttpStatusCodeException 
     */
    @Test
    public void testSetUserAgentUID_false() throws FailingHttpStatusCodeException, IOException
    {
        // create mocked web connection 
        final WebClient webClient = new WebClient();
        final MockWebConnection conn = new MockWebConnection();
        conn.setResponse(new URL(testUrl), response);
        webClient.setWebConnection(conn);        

        // change it
        UserAgentUtils.setUserAgentUID(webClient, false);

        webClient.getPage(testUrl);

        final String newUserAgent = conn.getLastWebRequest().getAdditionalHeaders().get("User-Agent");
        Assert.assertNull(newUserAgent);
    }
}
