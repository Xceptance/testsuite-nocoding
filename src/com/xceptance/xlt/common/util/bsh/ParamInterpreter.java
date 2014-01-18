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
package com.xceptance.xlt.common.util.bsh;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import bsh.EvalError;
import bsh.Interpreter;

import com.xceptance.xlt.api.data.GeneralDataProvider;
import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.api.util.XltProperties;
import com.xceptance.xlt.common.tests.AbstractURLTestCase;

/**
 * Our implementation of the param interpreter, it will set some default
 * data objects for later use, such as NOW and RANDOM.
 
 * @author rschwietzke
 */
public class ParamInterpreter extends Interpreter
{
    private static final long serialVersionUID = -6490310093160560291L;

    /**
     * The pattern to get the inserted param or commands from strings, such as 
     * ${bar = System.currentTimeMillis()}    
     */
    private final static Pattern parameterPattern = Pattern.compile("\\$\\{([^$]*)\\}");
    
    /**
     * Just setup our interpreter and it will be filled with default data objects.
     */
    public ParamInterpreter()
    {
        super();
        
        try
        {
            this.set("NOW", new ParamInterpreterNow());
            this.set("RANDOM", new ParamInterpreterRandom());
            this.set("DATA", GeneralDataProvider.getInstance());
        }
        catch (final EvalError e)
        {
            // nothing should happen here, we just add context
        }
    }
    
    /**
     * Processes dynamic data on the input and use the set interpreter
     * and its state.
     * 
     * @param testCase the currently active testcase, needed for proper property lookup
     * @param input the input to process
     * @return a processed string or the same, if nothing to process
     */
    public String processDynamicData(final AbstractURLTestCase testCase, final String input)
    {
        if (input == null)
        {
            return null;
        }
        
        final List<String> params = new ArrayList<String>();
        
        // get all position out of the string
        final Matcher matcher = parameterPattern.matcher(input);
        while (matcher.find())
        {
            params.add(matcher.group(1));
        }
        
        // did we find something?
        if (params.isEmpty())
        {
            return input;
        }
        
        String output = input;
        
        // ok, go over the data
        for (final String param : params)
        {
            if (param.trim().length() == 0)
            {
                continue;
            }
            
            // first try to map it to a property if a test case is set otherwise ask the properties directly
            final String propertyValue = testCase != null ? testCase.getProperty(param) : XltProperties.getInstance().getProperty(param);
            if (propertyValue != null)
            {
                output = StringUtils.replaceOnce(output, "${" + param + "}", propertyValue);
                continue;
            }
            
            try
            {
                final Object evalResult = this.eval(param);
                if (evalResult != null)
                {
                    output = StringUtils.replaceOnce(output, "${" + param + "}", evalResult.toString());
                }
            }
            catch (final EvalError e)
            {
                XltLogger.runTimeLogger.warn(MessageFormat.format("Unable to process dynamic parameter {0}", "${" + param + "}"), e);
            }
        }
        
        return output;
    }
    
    /**
     * Processes dynamic data on the input and use the set interpreter
     * and its state.
     * 
     * @param input the input to process
     * @return a processed string or the same, if nothing to process
     */
    public String processDynamicData(final String input)
    {
        return processDynamicData(null, input);
    }
}
