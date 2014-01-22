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

import java.util.regex.Pattern;

import junit.framework.Assert;

import org.junit.Test;

import com.gargoylesoftware.htmlunit.WebClient;
import com.xceptance.xlt.common.util.UserAgentUtils;

public class UserAgentUtilsTest
{
    /**
     * Change the agent twice
     */
    @Test
    public void testSetUserAgentUID_true()
    {
        final WebClient webClient = new WebClient();
        final String originalUserAgent = webClient.getBrowserVersion().getUserAgent();
        
        // change it
        UserAgentUtils.setUserAgentUID(webClient, true);
        
        final String newUserAgent1 = webClient.getBrowserVersion().getUserAgent();
        Assert.assertTrue(newUserAgent1.matches("^" + Pattern.quote(originalUserAgent) + " UID/" + "[a-z0-9]{8}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{12}$"));

        // change it again
        UserAgentUtils.setUserAgentUID(webClient, true);
        
        final String newUserAgent2 = webClient.getBrowserVersion().getUserAgent();
        Assert.assertTrue(newUserAgent2.matches("^" + Pattern.quote(originalUserAgent) + " UID/" + "[a-z0-9]{8}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{12}$"));
        Assert.assertFalse(newUserAgent2.equals(newUserAgent1));
    }

    /**
     * Do not change it
     */
    @Test
    public void testSetUserAgentUID_false()
    {
        final WebClient webClient = new WebClient();
        final String originalUserAgent = webClient.getBrowserVersion().getUserAgent();
        
        // change it
        UserAgentUtils.setUserAgentUID(webClient, false);
        
        final String newUserAgent = webClient.getBrowserVersion().getUserAgent();
        Assert.assertTrue(newUserAgent.equals(originalUserAgent));
    }
}
