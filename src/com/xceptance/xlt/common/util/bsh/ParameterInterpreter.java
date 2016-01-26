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
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jdt.annotation.Nullable;

import bsh.EvalError;
import bsh.Interpreter;

import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.xceptance.xlt.api.data.GeneralDataProvider;
import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.api.util.XltProperties;

/**
 * Our implementation of the param interpreter, it will set some default data objects for later use, such as NOW and
 * RANDOM ect.
 */
public class ParameterInterpreter extends Interpreter
{
    @Nullable
    private XltProperties properties;

    private static final long serialVersionUID = -6490310093160560291L;

    /**
     * The pattern to get the inserted param or commands from strings, such as ${bar = System.currentTimeMillis()}
     */
    private final static Pattern parameterPattern = Pattern.compile("\\$\\{([^$]*)\\}");

    /**
     * Processes dynamic data on the input and use the set interpreter and its state.
     * 
     * @param testCase
     *            the currently active testcase, needed for proper property lookup
     * @param input
     *            the input to process
     * @return a processed string or the same, if nothing to process
     */
    public ParameterInterpreter(final XltProperties properties, final GeneralDataProvider dataProvider)
    {
        super();
        XltLogger.runTimeLogger.debug("Creating new Instance");
        try
        {
            this.set("NOW", new ParameterInterpreterNow());
            this.set("RANDOM", new ParameterInterpreterRandom());
            this.set("DATA", dataProvider);
            this.set("DATE", new Date());
            this.properties = properties;
        }
        catch (final EvalError e)
        {
            // nothing should happen here, we just add context
        }
    }

    /**
     * Just setup our interpreter and it will be filled with default data objects.
     */

    public void set(final NameValuePair nvp) throws EvalError
    {
        final String name = nvp.getName();
        final String value = nvp.getValue();
        if (name != null)
        {
            XltLogger.runTimeLogger.debug(addVariableMessage(name, value));
            this.set(name, value);
        }
        else
        {
            XltLogger.runTimeLogger.debug("Failed to add variable: \"" + name + "\" = \"" + value + "\", because its identifier was 'null'");
        }
    }

    /**
     * Maps input on predefined value. The value can be defined dynamically during runtime or in the properties. The
     * lookup works the following way and breaks, when a value is found.
     * <ol>
     * <li>Map key on dynamic runtime data</li>
     * <li>Map key on properties</li>
     * </ol>
     * 
     * @param input
     *            : key
     * @return value reference, or if nothing was found the input itself
     */
    @Nullable
    public String processDynamicData(final String input)
    {
        String result = null;

        if (input != null)
        {
            result = input;

            final List<String> params = getPatternMatches(input);

            if (!params.isEmpty())
            {
                for (final String param : params)
                {
                    if (param.trim().length() != 0)
                    {
                        try
                        {
                            final Object evalResult = this.eval(param);
                            if (evalResult != null)
                            {
                                result = StringUtils.replaceOnce(result, "${" + param + "}", evalResult.toString());
                            }
                        }
                        catch (final EvalError e)
                        {
                            XltLogger.runTimeLogger.warn(MessageFormat.format("Unable to process dynamic parameter {0}", "${" + param + "}"),
                                                         e);
                        }
                        // first try to map it to a property if a test case is set otherwise ask the properties directly
                        if (result.equals(input))
                        {
                            final String propertyValue = getPropertyValue(param);

                            if (propertyValue != null)
                            {
                                result = StringUtils.replaceOnce(result, "${" + param + "}", propertyValue);
                            }
                        }
                        // look into variables map
                    }
                }
            }
        }
        return result;
    }

    protected List<String> getPatternMatches(final String input)
    {
        final List<String> result = new ArrayList<String>();
        final Matcher matcher = parameterPattern.matcher(input);

        while (matcher.find())
        {
            result.add(matcher.group(1));
        }
        return result;
    }

    @Nullable
    protected String getPropertyValue(final String propertyName)
    {
        final String propertyValue = properties.getProperty(propertyName);
        return propertyValue;
    }

    protected String addVariableMessage(final String name, final String value)
    {
        final String message = MessageFormat.format("Adding Variables: \"{0}\" = \"{1}\"", name, value);
        return message;

    }
}
