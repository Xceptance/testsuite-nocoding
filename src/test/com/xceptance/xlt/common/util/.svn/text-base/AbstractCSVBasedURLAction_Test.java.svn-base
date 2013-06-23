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



