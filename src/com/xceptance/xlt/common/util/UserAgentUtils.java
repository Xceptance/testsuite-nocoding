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
package com.xceptance.xlt.common.util;

import java.util.UUID;

import com.gargoylesoftware.htmlunit.WebClient;

/**
 * Helper for dynamically adjusting the user agent if needed. 
 * 
 * @author Rene Schwietzke (Xceptance Software Technologies GmbH)
 */
public class UserAgentUtils
{
    /**
     * Constant to identify the UID in the user agent string
     */
    private static final String MARKER = " UID/";
    
    /**
     * Adds a random UUID to the user agent. Replaces an existing one
     * if already set.
     * 
     * @param webClient the client to manipulate
     * @param active true when parameter should be inserted
     */
    public static void setUserAgentUID(final WebClient webClient, final boolean active)
    {
        if (active)
        {
            final String userAgent = webClient.getBrowserVersion().getUserAgent();
            webClient.addRequestHeader("User-Agent", userAgent.concat(MARKER).concat(UUID.randomUUID().toString()));
        }
    }
}
