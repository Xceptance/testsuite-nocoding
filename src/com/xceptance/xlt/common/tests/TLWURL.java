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
package com.xceptance.xlt.common.tests;

import org.junit.Test;

import com.xceptance.xlt.common.actions.LWSimpleURL;
import com.xceptance.xlt.common.util.CSVBasedURLAction;

/**
 * This is a simple single URL test case. It can be used to create considerable load for simple investigations. Cookie
 * handling, as well as content comparison is handled automatically. See the properties too. This test cases uses the
 * lightweight action programming pattern to save system resources while testing. This should enable the test to create
 * a lot of load.
 */
public class TLWURL extends AbstractURLTestCase
{
    @Test
    public void testURLs() throws Throwable
    {
        // our action tracker to build up a correct chain of pages
        LWSimpleURL lastAction = null;

        // let's loop about the data we have
        for (final CSVBasedURLAction csvBasedAction : csvBasedActions)
        {
            // ok, action or static?
            if (csvBasedAction.isAction())
            {
                if (lastAction == null)
                {
                    // our first action, so start the browser too
                    lastAction = new LWSimpleURL(this, csvBasedAction, login, password);
                }
                else
                {
                    // Until know just the request URLs were collected. So run the action now.
                    lastAction.run();

                    // subsequent actions
                    lastAction = new LWSimpleURL(this, lastAction, csvBasedAction);
                }
            }

            // this is the part that deals with the static downloads
            if (csvBasedAction.isStaticContent())
            {
                if (lastAction == null)
                {
                    // we do not have any action yet, so we have to make one up
                    lastAction = new LWSimpleURL(this, csvBasedAction, login, password);
                }
                else
                {
                    lastAction.addRequest(csvBasedAction.getURL(this));
                }
            }
        }

        // there might be an action we have collected the requests for but not kicked it off yet
        if (lastAction != null)
        {
            lastAction.run();
        }
    }
}
