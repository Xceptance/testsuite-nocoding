package com.xceptance.xlt.common.tests;

import org.junit.Test;

import com.xceptance.xlt.api.actions.AbstractLightWeightPageAction;
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
        AbstractLightWeightPageAction lastAction = null; 
            
        // let's loop about the data we have
        for (final CSVBasedURLAction csvBasedAction : csvBasedActions)
        {
            if (lastAction == null)
            {
                // our first action, so start the browser too
                lastAction = new LWSimpleURL(this, csvBasedAction, login, password);
            }
            else
            {
                // subsequent actions
                lastAction = new LWSimpleURL(this, lastAction, csvBasedAction);
            }
            
            // run it
            lastAction.run();
        }
    }
}
