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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Before;

import com.xceptance.xlt.api.engine.Session;
import com.xceptance.xlt.api.tests.AbstractTestCase;
import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.api.util.XltProperties;
import com.xceptance.xlt.common.XltConstants;
import com.xceptance.xlt.common.util.CSVBasedURLAction;
import com.xceptance.xlt.common.util.bsh.ParamInterpreter;

/**
 * This is a simple single URL test case. It can be used to create considerable load for simple investigations. Cookie
 * handling, as well as content comparison is handled automatically. See the properties too.
 */
public class AbstractURLTestCase extends AbstractTestCase
{
    /**
     * Our data. This also guards it.
     */
    protected final List<CSVBasedURLAction> csvBasedActions = new ArrayList<CSVBasedURLAction>();
    
    /**
     * Our interpreter engine for that test case
     */
    private final ParamInterpreter interpreter = new ParamInterpreter();
    
    /**
     * In case we have to authenticate the connection first.
     */
    protected String login;

    /**
     * The password for the authentication.
     */
    protected String password;

    /**
     * Loading of the data. There is a state variable used to indicate that we already did that.
     * 
     * @throws IOException
     */
    @Before
    public void loadData() throws IOException
    {
        login = getProperty("login", getProperty("com.xceptance.xlt.auth.userName"));
        password = getProperty("password", getProperty("com.xceptance.xlt.auth.password"));

        // load the data. Ideally we would offload the file searching to
        // XltProperties.getDataFile(String name)
        // or XltProperties.getDataFile(String name, String locale)
        // or XltProperties.getDataFile(String name, Locale locale)
        final String dataDirectory = XltProperties.getInstance().getProperty(XltConstants.XLT_PACKAGE_PATH + ".data.directory", "config" + File.separatorChar + "data");
        final File file = new File(dataDirectory, getProperty("filename", Session.getCurrent().getUserName() + ".csv"));

        BufferedReader br = null;
        boolean incorrectLines = false;
        
        try
        {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));

            // permit # as comment, empty lines, set comma as separator, and activate the header
            final CSVFormat csvFormat = CSVFormat.RFC4180.toBuilder().withIgnoreEmptyLines(true).withCommentStart('#').withHeader().withIgnoreSurroundingSpaces(true).build();
            final CSVParser parser = new CSVParser(br, csvFormat);
            final Iterator<CSVRecord> csvRecords = parser.iterator();

            // verify header fields to avoid problems with incorrect spelling or spaces
            final Map<String, Integer> headerMap = parser.getHeaderMap();
            
            for (final String headerField : headerMap.keySet())
            {
                if (!CSVBasedURLAction.isPermittedHeaderField(headerField))
                {
                    Assert.fail(MessageFormat.format("Unsupported or misspelled header field: {0}", headerField));
                }
            }

            // go over all lines, this is a little odd, because we have to catch the iterator exception
            while (true)
            {
                try
                {
                    final boolean hasNext = csvRecords.hasNext();
                    if (!hasNext)
                    {
                        break;
                    }
                }
                catch(final Exception e)
                {
                    // the plus 1 is meant to correct the increment missing because of the exception
                    throw new RuntimeException(MessageFormat.format("Line at {0} is invalid, because of <{1}>. Line is ignored.",
                                                                    parser.getLineNumber() + 1, e.getMessage()));                   
                }
                
                final CSVRecord csvRecord = csvRecords.next();

                // only take ok lines
                if (csvRecord.isConsistent())
                {
                    // guard against data exceptions
                    try
                    {
                        // do we have an url?
                        if (csvRecord.get(CSVBasedURLAction.URL) != null)
                        {
                            // take it
                            csvBasedActions.add(new CSVBasedURLAction(csvRecord, interpreter));
                        }
                        else
                        {
                            XltLogger.runTimeLogger.error(MessageFormat.format("Line at {0} does not contain any URL. Line is ignored: {1}",
                                                                              parser.getLineNumber(), csvRecord));
                        }
                    }
                    catch(final Exception e)
                    {
                        throw new RuntimeException(MessageFormat.format("Line at {0} is invalid, because of <{2}>. Line is ignored: {1}",
                                                                        parser.getLineNumber(), csvRecord, e.getMessage()));
                    }
                }
                else
                {
                    XltLogger.runTimeLogger.error(MessageFormat.format("Line at {0} has not been correctly formatted. Line is ignored: {1}",
                                                                      parser.getLineNumber(), csvRecord));
                    incorrectLines = true;
                }
            }
        }
        finally
        {
            IOUtils.closeQuietly(br);
        }
        
        // stop if we have anything the is incorrect, avoid half running test cases
        if (incorrectLines)
        {
            throw new RuntimeException("Found incorrectly formatted lines. Stopping here.");
        }
    }
    
    /**
     * Returns the effective key to be used for property lookup via one of the getProperty(...) methods.
     * <p>
     * This method implements the fall-back logic:
     * <ol>
     * <li>user name plus simple key, e.g. TMyRunningTest.password
     * <li>test class name plus simple key, e.g. "com.xceptance.xlt.samples.testsuite.tests.TAuthor.password"</li>
     * <li>simple key, e.g. "password"</li>
     * </ol>
     * 
     * @param bareKey
     *            the bare property key, i.e. without any prefixes
     * @return the first key that produces a result
     */
    protected String getEffectiveKey(final String bareKey)
    {
        String effectiveKey = null;
        final XltProperties xltProperties = XltProperties.getInstance();

        // 1. use the current user name as prefix
        final String userNameQualifiedKey = Session.getCurrent().getUserName() + "." + bareKey;
        if (xltProperties.containsKey(userNameQualifiedKey))
        {
            effectiveKey = userNameQualifiedKey;
        }
        else
        {
            // 2. use the current class name as prefix
            final String classNameQualifiedKey = getTestName() + "." + bareKey;
            if (xltProperties.containsKey(classNameQualifiedKey))
            {
                effectiveKey = classNameQualifiedKey;
            }
            else
            {
                // 3. use the bare key
                effectiveKey = bareKey;
            }
        }

        return effectiveKey;
    }
}
