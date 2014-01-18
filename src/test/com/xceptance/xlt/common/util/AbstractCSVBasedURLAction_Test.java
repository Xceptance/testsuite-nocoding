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
import java.util.List;

import junit.framework.Assert;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Before;

import test.com.xceptance.xlt.common.tests.TTest;

import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.xceptance.xlt.common.tests.AbstractURLTestCase;

public abstract class AbstractCSVBasedURLAction_Test
{
    /**
     * The test case for utility purposes
     */
    protected AbstractURLTestCase tc;
    
    @Before
    public void setup()
    {
        tc = new TTest();
    }
    
    /**
     * Create records from string
     */
    protected List<CSVRecord> createRecords(final String... records) throws IOException
    {
        final StringBuilder fullRecord = new StringBuilder();
        
        for (final String record:records)
        {
            fullRecord.append(record);
            fullRecord.append("\n");
        }
        
        final CSVFormat csvFormat = CSVFormat.RFC4180.toBuilder().withIgnoreEmptyLines(true).withCommentStart('#').withHeader().build();
        final CSVParser parser = new CSVParser(fullRecord.toString(), csvFormat);
       
        return parser.getRecords();
    }
    
    /**
     * Compares parameter lists
     */
    protected boolean compareParameters(final List<NameValuePair> actual, final String... expected)
    {
        if (actual == null && expected == null)
        {
            return true;
        }
        
        if (expected == null)
        {
            Assert.assertEquals(0, actual.size());
            return true;
        }
        
        Assert.assertEquals(expected.length, actual.size());
        
        for (int i = 0; i < actual.size(); i++)
        {
            Assert.assertEquals(expected[i], actual.get(i).toString());
        }
        
        return true;
    }
    
}



