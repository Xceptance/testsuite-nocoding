package test.com.xceptance.xlt.common.util;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

import junit.framework.Assert;

import org.apache.commons.csv.CSVRecord;
import org.junit.Test;

import com.xceptance.xlt.common.util.CSVBasedURLAction;


public class CSVBasedURLActionDynamicParameters_Test extends AbstractCSVBasedURLAction_Test
{
    // ------------------------------------------------------------------------------------
    // Read all parameters
    // ------------------------------------------------------------------------------------
    @Test
    public void testParameterReading_XPath() throws IOException
    {
        final List<CSVRecord> records = createRecords(
                                                      "Name,URL,xpath1,xpath2,xpath3,xpath4,xpath5,xpath6,xpath7,xpath8,xpath9,xpath10",
                                                      "Homepage,http://www/,p1,p2,p3,p4,p5,p6,p7,p8,p9,p10");

        final CSVBasedURLAction action = new CSVBasedURLAction(records.get(0));
        Assert.assertEquals("Homepage", action.getName());
        Assert.assertEquals("http://www/", action.getURL().toString());

        final List<String> xpaths = action.getXPathGetterList(null);
        for (int i = 0; i < 10; i++)
        {
            Assert.assertEquals("p" + (i + 1), xpaths.get(i));
        }
    }

    @Test
    public void testParameterReading_RegExp() throws IOException
    {
        final List<CSVRecord> records = createRecords(
                                                      "Name,URL,regexp1,regexp2,regexp3,regexp4,regexp5,regexp6,regexp7,regexp8,regexp9,regexp10",
                                                      "Homepage,http://www/,p1,p2,p3,p4,p5,p6,p7,p8,p9,p10");

        final CSVBasedURLAction action = new CSVBasedURLAction(records.get(0));
        Assert.assertEquals("Homepage", action.getName());
        Assert.assertEquals("http://www/", action.getURL().toString());

        final List<Pattern> regexps = action.getRegExpGetterList(null);
        for (int i = 0; i < 10; i++)
        {
            Assert.assertEquals("p" + (i + 1), regexps.get(i).toString());
        }
    }
    
    // ------------------------------------------------------------------------------------
    // Have only a few parameters
    // ------------------------------------------------------------------------------------
    @Test
    public void testParameterReading_XPath_AFew() throws IOException
    {
        final List<CSVRecord> records = createRecords(
                                                      "Name,URL,xpath1,xpath2,xpath9",
                                                      "Homepage,http://www/,p1,p2,p9");

        final CSVBasedURLAction action = new CSVBasedURLAction(records.get(0));
        Assert.assertEquals("Homepage", action.getName());
        Assert.assertEquals("http://www/", action.getURL().toString());

        final List<String> xpaths = action.getXPathGetterList(null);
        Assert.assertEquals("p1", xpaths.get(0));
        Assert.assertEquals("p2", xpaths.get(1));
        Assert.assertEquals(null, xpaths.get(2));
        Assert.assertEquals(null, xpaths.get(3));
        Assert.assertEquals(null, xpaths.get(4));
        Assert.assertEquals(null, xpaths.get(5));
        Assert.assertEquals(null, xpaths.get(6));
        Assert.assertEquals(null, xpaths.get(7));
        Assert.assertEquals("p9", xpaths.get(8));
        Assert.assertEquals(null, xpaths.get(9));
    }

    @Test
    public void testParameterReading_RegExp_AFew() throws IOException
    {
        final List<CSVRecord> records = createRecords(
                                                      "Name,URL,regexp1,regexp2,regexp9",
                                                      "Homepage,http://www/,p1,p2,p9");

        final CSVBasedURLAction action = new CSVBasedURLAction(records.get(0));
        Assert.assertEquals("Homepage", action.getName());
        Assert.assertEquals("http://www/", action.getURL().toString());

        final List<Pattern> regexps = action.getRegExpGetterList(null);
        Assert.assertEquals("p1", regexps.get(0).toString());
        Assert.assertEquals("p2", regexps.get(1).toString());
        Assert.assertEquals(null, regexps.get(2));
        Assert.assertEquals(null, regexps.get(3));
        Assert.assertEquals(null, regexps.get(4));
        Assert.assertEquals(null, regexps.get(5));
        Assert.assertEquals(null, regexps.get(6));
        Assert.assertEquals(null, regexps.get(7));
        Assert.assertEquals("p9", regexps.get(8).toString());
        Assert.assertEquals(null, regexps.get(9));
    }

    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    @Test
    public void testParameterReading_XPath_TooMany() throws IOException
    {
        
    }
    
    @Test
    public void testParameterReadingAndInterpreter_XPath()
    {
        // replace with beanshell values, replace with properties plain and from test case
    }
    
    
}



