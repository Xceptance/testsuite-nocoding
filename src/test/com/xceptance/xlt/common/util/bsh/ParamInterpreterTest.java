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
package test.com.xceptance.xlt.common.util.bsh;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.xceptance.xlt.common.util.bsh.ParamInterpreter;

/**
 * Test the parameter handling via beanshell.
 * 
 * @author rschwietzke
 *
 */
public class ParamInterpreterTest
{
    private ParamInterpreter interpreter;
    
    @Before
    public void setup()
    {
        interpreter = new ParamInterpreter();
    }
    
    @Test
    public void noParams()
    {
        Assert.assertEquals("No data here.", interpreter.processDynamicData("No data here."));
    }
    
    @Test
    public void emptyParams()
    {
        Assert.assertEquals("No ${} here.", interpreter.processDynamicData("No ${} here."));
    }    

    @Test
    public void emptyParams_Spaces()
    {
        Assert.assertEquals("No ${  } here.", interpreter.processDynamicData("No ${  } here."));
    }   
    
    @Test
    public void javaCall()
    {
        Assert.assertEquals("No 1 here.", interpreter.processDynamicData("No ${Math.abs(-1)} here."));
    }       

    @Test
    public void twoOccurences()
    {
        Assert.assertEquals("No 1 here and 2 here.", interpreter.processDynamicData("No ${Math.abs(-1)} here and ${Math.abs(-2)} here."));
    }       
    
    @Test
    public void keepState()
    {
        Assert.assertEquals("No 1 here and 3 here.", interpreter.processDynamicData("No ${m = Math.abs(-1)} here and ${m + Math.abs(-2)} here."));
    }  
    
    @Test
    public void stateAcrossCalls()
    {
        Assert.assertEquals("No 1 here.", interpreter.processDynamicData("No ${m = Math.abs(-1)} here."));
        Assert.assertEquals("No 2 here.", interpreter.processDynamicData("No ${m = m + Math.abs(-1)} here."));
    }  
 
    @Test
    public void prePopulated()
    {
        Assert.assertEquals("No 1 here and 3 here.", interpreter.processDynamicData("No ${m = Math.abs(-1)} here and ${m + Math.abs(-2)} here."));
    }  

    // ----------------------------------------------------------------------------------------
    /* Error handling */
    // ----------------------------------------------------------------------------------------
    @Test
    public void invalidEnd()
    {
        Assert.assertEquals("Text${a", interpreter.processDynamicData("Text${a"));
    }  
    
    @Test
    public void invalidStart()
    {
        Assert.assertEquals("Text$ {a}", interpreter.processDynamicData("Text$ {a}"));
    }  
    
    @Test
    public void invalidMoreCurlyBraces()
    {
        Assert.assertEquals("Text{", interpreter.processDynamicData("Text${'{'}"));
        Assert.assertEquals("Text}", interpreter.processDynamicData("Text${'}'}"));
        Assert.assertEquals("TTe2t-TA12000", interpreter.processDynamicData("T${java.text.MessageFormat.format(\"Te{0}t\", 2)}-T${java.text.MessageFormat.format(\"A{0}{1}\", 1, 2)}000"));
    }    

}
