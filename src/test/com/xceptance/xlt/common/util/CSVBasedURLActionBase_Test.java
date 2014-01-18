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
import java.net.URL;
import java.util.List;
import java.util.regex.PatternSyntaxException;

import junit.framework.Assert;

import org.apache.commons.csv.CSVRecord;
import org.junit.Test;

import test.com.xceptance.xlt.common.tests.TTest;
import bsh.EvalError;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.xceptance.xlt.api.engine.Session;
import com.xceptance.xlt.api.util.XltProperties;
import com.xceptance.xlt.api.validators.HttpResponseCodeValidator;
import com.xceptance.xlt.common.util.CSVBasedURLAction;
import com.xceptance.xlt.common.util.bsh.ParamInterpreter;
import com.xceptance.xlt.engine.SessionImpl;


public class CSVBasedURLActionBase_Test extends AbstractCSVBasedURLAction_Test
{
    @Test
    public void testDefaults() throws IOException
    {
        final List<CSVRecord> records = createRecords(
                                                      "URL",
                                                      "http://localhost:8080/pebble/");

        final CSVBasedURLAction action = new CSVBasedURLAction(records.get(0));

        Assert.assertEquals("Action-1", action.getName());
        Assert.assertEquals(new URL("http://localhost:8080/pebble/"), action.getURL());
        Assert.assertEquals(HttpMethod.GET, action.getMethod());
        Assert.assertNull(action.getParameters(tc));
        Assert.assertTrue(HttpResponseCodeValidator.getInstance().equals(action.getHttpResponseCodeValidator()));
        Assert.assertEquals(null, action.getXPath());
        Assert.assertEquals(null, action.getText());
        Assert.assertEquals(null, action.getRegexp());
        Assert.assertFalse(action.isEncoded());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNoURLFails() throws IOException
    {
        final List<CSVRecord> records = createRecords("test", "anything");
        final CSVBasedURLAction action = new CSVBasedURLAction(records.get(0));
    }    
    
    @Test
    public void testGETWithParameters_NotEncoded() throws IOException
    {
        final List<CSVRecord> records = createRecords(
                                                      "Name,URL,Method,Parameters,ResponseCode,XPath,Text,Encoded",
                                                      "Homepage,\"http://localhost:8080/pebble/\",GET,foo=%20f,200,id('blogName'),\"Pebble Test Suite\",false");

        final CSVBasedURLAction action = new CSVBasedURLAction(records.get(0));

        Assert.assertEquals("Homepage", action.getName());
        Assert.assertEquals(new URL("http://localhost:8080/pebble/"), action.getURL());
        Assert.assertEquals(HttpMethod.GET, action.getMethod());
        Assert.assertTrue(compareParameters(action.getParameters(), "foo=%20f"));
        Assert.assertTrue(HttpResponseCodeValidator.getInstance().equals(action.getHttpResponseCodeValidator()));
        Assert.assertEquals("id('blogName')", action.getXPath());
        Assert.assertEquals("Pebble Test Suite", action.getText());
        Assert.assertEquals(null, action.getRegexp());
        Assert.assertFalse(action.isEncoded());
    }
 
    @Test
    public void testPOST_Encoded() throws IOException
    {
        final List<CSVRecord> records = createRecords(
                "Name,URL,Method,Parameters,ResponseCode,XPath,Text,Encoded",
                "Login,\"http://localhost:8080/pebble/j_acegi_security_check\",POST,\"redirectUrl=%2F&j_username=username&j_password=password\",200,\"id('sidebar')\",\"^Logged in as username.*$\",true");

        final CSVBasedURLAction action = new CSVBasedURLAction(records.get(0));
        
        Assert.assertEquals("Login", action.getName());
        Assert.assertEquals(new URL("http://localhost:8080/pebble/j_acegi_security_check"), action.getURL());
        Assert.assertEquals(HttpMethod.POST, action.getMethod());
        Assert.assertTrue(compareParameters(action.getParameters(), "redirectUrl=/", "j_username=username","j_password=password"));
        Assert.assertTrue(HttpResponseCodeValidator.getInstance().equals(action.getHttpResponseCodeValidator()));
        Assert.assertEquals("id('sidebar')", action.getXPath());
        Assert.assertEquals("^Logged in as username.*$", action.getText());
        Assert.assertEquals(null, action.getRegexp());
        Assert.assertTrue(action.isEncoded());
    }
    
    @Test
    public void testParameterNullAndEncoded() throws IOException
    {
        final List<CSVRecord> records = createRecords(
                "Name,URL,Method,Parameters,ResponseCode,XPath,Text,Encoded",
                "Login,\"http://localhost:8080/pebble/j_acegi_security_check\",POST,\"redirectUrl=\",200,\"id('sidebar')\",\"^Logged in as username.*$\",true");

        final CSVBasedURLAction action = new CSVBasedURLAction(records.get(0));
        Assert.assertEquals("Login", action.getName());
    }   
    
    // ----------------------------------------------------------------------------------------    
    /* Interpreter ON */
    // ----------------------------------------------------------------------------------------
    @Test
    public void testParamInText() throws IOException, EvalError
    {
        final List<CSVRecord> records = createRecords(
                                                      "Name,URL,Method,Parameters,ResponseCode,XPath,Text,Encoded",
                                                      "Homepage,\"http://localhost:8080/pebble/\",GET,foo=bar,200,id('blogName'),\"Pebble ${foobar} Suite${2 + 2}\",false");

        final CSVBasedURLAction action = new CSVBasedURLAction(records.get(0), new ParamInterpreter() );
        action.getInterpreter().set("foobar", "TEST");
        
        Assert.assertEquals("Pebble TEST Suite4", action.getText());
    }

    @Test
    public void testParamInRegExp() throws IOException
    {
        final List<CSVRecord> records = createRecords(
                                                      "Name,URL,Method,Parameters,ResponseCode,RegExp,Text,Encoded",
                                                      "Homepage,\"http://localhost:8080/pebble/\",GET,foo=bar,200,1[a-z]${2 + 2},\"Pebble ${1+1} Suite${2 + 2}\",false");

        final CSVBasedURLAction action = new CSVBasedURLAction(records.get(0), new ParamInterpreter() );
        
        Assert.assertEquals("1[a-z]4", action.getRegexp().pattern());
    }
    
    @Test
    public void testParamInURL() throws IOException
    {
        final List<CSVRecord> records = createRecords(
                                                      "Name,URL,Method,Parameters,ResponseCode,RegExp,Text,Encoded",
                                                      "Homepage,\"http://localhost:${8000 + 80}/pebble/\",GET,foo=bar,200,1[a-z]${2 + 2},\"Pebble ${1+1} Suite${2 + 2}\",false");

        final CSVBasedURLAction action = new CSVBasedURLAction(records.get(0), new ParamInterpreter() );
        
        Assert.assertEquals("http://localhost:8080/pebble/", action.getURL().toString());
    }
    
    @Test
    public void testParamInParams() throws IOException, EvalError
    {
        final List<CSVRecord> records = createRecords(
                                                      "Name,URL,Method,Parameters,ResponseCode,RegExp,Text,Encoded",
        "Homepage,\"http://localhost:${8000 + 80}/pebble/\",GET,${key}=${value},200,1[a-z]${2 + 2},\"Pebble ${1+1} Suite${2 + 2}\",false");

        final CSVBasedURLAction action = new CSVBasedURLAction(records.get(0), new ParamInterpreter() );
        action.getInterpreter().set("key", "mykey");
        action.getInterpreter().set("value", "VALUE");

        Assert.assertEquals("mykey", action.getParameters().get(0).getName());
        Assert.assertEquals("VALUE", action.getParameters().get(0).getValue());
    }
    
    @Test
    public void testParamInXPath() throws IOException, EvalError
    {
        final List<CSVRecord> records = createRecords(
                                                      "Name,URL,Method,Parameters,ResponseCode,XPath,Text,Encoded",
                                                      "Homepage,\"http://localhost:8080/pebble/\",GET,foo=bar,200,id('${name}'),\"Pebble ${foobar} Suite${2 + 2}\",false");

        final CSVBasedURLAction action = new CSVBasedURLAction(records.get(0), new ParamInterpreter() );
        action.getInterpreter().set("name", "myblog");
        
        Assert.assertEquals("id('myblog')", action.getXPath());
    }
    
    // ----------------------------------------------------------------------------------------
    /* Interpreter OFF */
    // ----------------------------------------------------------------------------------------
    @Test
    public void testParamInText_InterpreterOff() throws IOException, EvalError
    {
        final List<CSVRecord> records = createRecords(
                                                      "Name,URL,Method,Parameters,ResponseCode,XPath,Text,Encoded",
                                                      "Homepage,\"http://localhost:8080/pebble/\",GET,foo=bar,200,id('blogName'),\"Pebble ${foobar} Suite${2 + 2}\",false");

        final CSVBasedURLAction action = new CSVBasedURLAction(records.get(0));        
        Assert.assertEquals("Pebble ${foobar} Suite${2 + 2}", action.getText());
    }

    @Test(expected=PatternSyntaxException.class)
    public void testParamInRegExp_InterpreterOff() throws IOException
    {
        final List<CSVRecord> records = createRecords(
                                                      "Name,URL,Method,Parameters,ResponseCode,RegExp,Text,Encoded",
                                                      "Homepage,\"http://localhost:8080/pebble/\",GET,foo=bar,200,1[a-z]${2 + 2},\"Pebble ${1+1} Suite${2 + 2}\",false");

        final CSVBasedURLAction action = new CSVBasedURLAction(records.get(0));
        Assert.assertEquals("1[a-z]${2 + 2}", action.getRegexp().pattern());
    }
    
    @Test
    public void testParamInURL_InterpreterOff() throws IOException
    {
        final List<CSVRecord> records = createRecords(
                                                      "Name,URL,Method,Parameters,ResponseCode,RegExp,Text,Encoded",
                                                      "Homepage,\"http://localhost/${foo}/\",GET,foo=bar,200,1[a-z],\"Pebble ${1+1} Suite${2 + 2}\",false");

        final CSVBasedURLAction action = new CSVBasedURLAction(records.get(0));
        Assert.assertEquals("http://localhost/${foo}/", action.getURL().toString());
    }
    
    @Test
    public void testParamInParams_InterpreterOff() throws IOException, EvalError
    {
        final List<CSVRecord> records = createRecords(
                                                      "Name,URL,Method,Parameters,ResponseCode,RegExp,Text,Encoded",
                                                      "Homepage,\"http://localhost/pebble/\",GET,${key}=${value},200,1[a-z],\"Pebble ${1+1} Suite${2 + 2}\",false");

        final CSVBasedURLAction action = new CSVBasedURLAction(records.get(0));
        Assert.assertEquals("${key}", action.getParameters().get(0).getName());
        Assert.assertEquals("${value}", action.getParameters().get(0).getValue());
    }
    
    @Test
    public void testParamInXPath_InterpreterOff() throws IOException, EvalError
    {
        final List<CSVRecord> records = createRecords(
                                                      "Name,URL,Method,Parameters,ResponseCode,XPath,Text,Encoded",
                                                      "Homepage,\"http://localhost:8080/pebble/\",GET,foo=bar,200,id('${name}'),\"Pebble ${foobar} Suite${2 + 2}\",false");

        final CSVBasedURLAction action = new CSVBasedURLAction(records.get(0));
        Assert.assertEquals("id('${name}')", action.getXPath());
    }
    
    // ----------------------------------------------------------------------------------------
    // Data is null, Interpreter on */
    // ----------------------------------------------------------------------------------------
    @Test
    public void testParamInXPath_InterpreterOn_Null() throws IOException, EvalError
    {
        final List<CSVRecord> records = createRecords("URL", "\"http://localhost:8080/pebble/\"");

        final CSVBasedURLAction action = new CSVBasedURLAction(records.get(0), new ParamInterpreter() );
        Assert.assertNull(action.getXPath());
    }   
    @Test
    public void testParamInRegExp_InterpreterOn_Null() throws IOException, EvalError
    {
        final List<CSVRecord> records = createRecords("URL", "\"http://localhost:8080/pebble/\",,");

        final CSVBasedURLAction action = new CSVBasedURLAction(records.get(0), new ParamInterpreter() );
        Assert.assertNull(action.getRegexp());
    }
    @Test
    public void testParamInText_InterpreterOn_Null() throws IOException, EvalError
    {
        final List<CSVRecord> records = createRecords("URL", "\"http://localhost:8080/pebble/\"");

        final CSVBasedURLAction action = new CSVBasedURLAction(records.get(0), new ParamInterpreter() );
        Assert.assertNull(action.getText());
    }     
    @Test
    public void testParamInParams_InterpreterOn_Null() throws IOException, EvalError
    {
        final List<CSVRecord> records = createRecords("URL", "\"http://localhost:8080/pebble/\"");

        final CSVBasedURLAction action = new CSVBasedURLAction(records.get(0), new ParamInterpreter() );
        Assert.assertNull(action.getParameters());
    } 
    @Test(expected=IllegalArgumentException.class)
    public void testParamInURL_InterpreterOn_Null() throws IOException, EvalError
    {
        final List<CSVRecord> records = createRecords("Text", "Foo");

        final CSVBasedURLAction action = new CSVBasedURLAction(records.get(0), new ParamInterpreter() );
        Assert.assertNull(action.getURL());
    }     

    // ----------------------------------------------------------------------------------------
    // Data is null, Interpreter off
    // ----------------------------------------------------------------------------------------
    @Test
    public void testParamInXPath_Null() throws IOException, EvalError
    {
        final List<CSVRecord> records = createRecords("URL", "\"http://localhost:8080/pebble/\"");

        final CSVBasedURLAction action = new CSVBasedURLAction(records.get(0));
        Assert.assertNull(action.getXPath());
    }   
    @Test
    public void testParamInRegExp_Null() throws IOException, EvalError
    {
        final List<CSVRecord> records = createRecords("URL", "\"http://localhost:8080/pebble/\",,");

        final CSVBasedURLAction action = new CSVBasedURLAction(records.get(0));
        Assert.assertNull(action.getRegexp());
    }
    @Test
    public void testParamInText_Null() throws IOException, EvalError
    {
        final List<CSVRecord> records = createRecords("URL", "\"http://localhost:8080/pebble/\"");

        final CSVBasedURLAction action = new CSVBasedURLAction(records.get(0));
        Assert.assertNull(action.getText());
    }     
    @Test
    public void testParamInParams_Null() throws IOException, EvalError
    {
        final List<CSVRecord> records = createRecords("URL", "\"http://localhost:8080/pebble/\"");

        final CSVBasedURLAction action = new CSVBasedURLAction(records.get(0));
        Assert.assertNull(action.getParameters());
    } 
    @Test(expected=IllegalArgumentException.class)
    public void testParamInURL_Null() throws IOException, EvalError
    {
        final List<CSVRecord> records = createRecords("Text", "Foo");

        final CSVBasedURLAction action = new CSVBasedURLAction(records.get(0));
        Assert.assertNull(action.getURL());
    }     
    
    /* Decoder tests */
    @Test
    public void testDecoder01() throws IOException, EvalError
    {
        final List<CSVRecord> records = createRecords("Name,URL,Method,Parameters,ResponseCode,XPath,Text,Encoded",    
                                                      "SaveArticle," +
                                                      "\"http://localhost:8080/pebble/saveBlogEntry.secureaction#preview\"," +
                                                      "POST," +
                                                      "\"attachmentSize=&attachmentType=&attachmentUrl=&body=%3Cp%3E%0D%0A${DATA.getText(3,6,false)}%0D%0A%3C%2Fp%3E&commentsEnabled=true&date=21-Apr-2013%2011%3A57&entry=${entry = NOW}&excerpt=${DATA.getText(1, false)}&originalPermalink=&persistent=false&submit=Save&subtitle=Sub ${RANDOM.String(25)}&tags=&timeZone=Europe%2FLondon&title=Title ${RANDOM.String(25)}&trackBacksEnabled=true\"," +
                                                      "200,//input[@value='Publish'],,true");
     
        final CSVBasedURLAction action = new CSVBasedURLAction(records.get(0), new ParamInterpreter());
        action.getParameters();
    }

    // ----------------------------------------------------------------------------------------
    // Test that only supported types are replaced 
    // ----------------------------------------------------------------------------------------   
    @Test
    public void testTypeSupport_AllButResponseCode() throws IOException, EvalError
    {
        // test only the supported types
        final List<CSVRecord> records = createRecords(
                                                      "Name,URL,Parameters,XPath,Text,RegExp,Method,Encoded,Type",
                                                      "${k1},\"http://u${k2}.com/\",foo=${k3},id('${k4}'),${k5},${k6},${k7},${8},${k9}");

        final CSVBasedURLAction action = new CSVBasedURLAction(records.get(0), new ParamInterpreter() );        
        for (int i = 1; i <= 9; i++)
        {
            action.getInterpreter().set("k" + i, String.valueOf(i));
        }

        Assert.assertEquals("1", action.getName());        
        Assert.assertEquals("http://u2.com/", action.getURL().toString());        
        Assert.assertEquals("foo=3", action.getParameters().get(0).toString());        
        Assert.assertEquals("id('4')", action.getXPath());        
        Assert.assertEquals("5", action.getText());        
        Assert.assertEquals("6", action.getRegexp().toString());        
        Assert.assertEquals(HttpMethod.GET, action.getMethod());        
        Assert.assertEquals(false, action.isEncoded());        
        Assert.assertEquals(true, action.isAction());        
    }
    
    @Test(expected=NumberFormatException.class)
    public void testTypeSupport_ResponseCode() throws IOException, EvalError
    {
        // test only the supported types
        final List<CSVRecord> records = createRecords(
                                                      "Name,URL,ResponseCode",
                                                      "${k1},\"http://u${k2}.com/\",${k10}");

        final CSVBasedURLAction action = new CSVBasedURLAction(records.get(0), new ParamInterpreter() );        
    }

    // ----------------------------------------------------------------------------------------
    // Check property replacement 
    // ----------------------------------------------------------------------------------------   
    @Test
    public void testPropertyReplacement_XltProperties() throws IOException
    {
        XltProperties.getInstance().setProperty("p1", "1");
        XltProperties.getInstance().setProperty("p2", "2");
        XltProperties.getInstance().setProperty("p3", "3");
        XltProperties.getInstance().setProperty("p4", "4");
        XltProperties.getInstance().setProperty("p5", "5");
        XltProperties.getInstance().setProperty("p6", "6");
        
        // test only the supported types
        final List<CSVRecord> records = createRecords(
                                                      "Name,URL,Parameters,XPath,Text,RegExp",
                                                      "${p1},\"http://u${p2}.com/\",foo=${p3},id('${p4}'),\"${p5}\",${p6}");

        final CSVBasedURLAction action = new CSVBasedURLAction(records.get(0), new ParamInterpreter() );        
        Assert.assertEquals("1", action.getName());        
        Assert.assertEquals("http://u2.com/", action.getURL().toString());        
        Assert.assertEquals("foo=3", action.getParameters().get(0).toString());        
        Assert.assertEquals("id('4')", action.getXPath());        
        Assert.assertEquals("5", action.getText());        
        Assert.assertEquals("6", action.getRegexp().toString());        
    }

    @Test
    public void testPropertyReplacement_FromTestCase() throws IOException
    {
        XltProperties.getInstance().setProperty("test.com.xceptance.xlt.common.tests.TTest.p1", "1");
        XltProperties.getInstance().setProperty("test.com.xceptance.xlt.common.tests.TTest.p2", "2");
        XltProperties.getInstance().setProperty("test.com.xceptance.xlt.common.tests.TTest.p3", "3");
        XltProperties.getInstance().setProperty("test.com.xceptance.xlt.common.tests.TTest.p4", "4");
        XltProperties.getInstance().setProperty("test.com.xceptance.xlt.common.tests.TTest.p5", "5");
        XltProperties.getInstance().setProperty("test.com.xceptance.xlt.common.tests.TTest.p6", "6");
        
        // test only the supported types
        final List<CSVRecord> records = createRecords(
                                                      "Name,URL,Parameters,XPath,Text,RegExp",
                                                      "${p1},\"http://u${p2}.com/\",foo=${p3},id('${p4}'),\"${p5}\",${p6}");

        final CSVBasedURLAction action = new CSVBasedURLAction(records.get(0), new ParamInterpreter() );        
        final TTest tc = new TTest();
        ((SessionImpl)Session.getCurrent()).setUserName("TTUser");
        
        Assert.assertEquals("1", action.getName(tc));        
        Assert.assertEquals("http://u2.com/", action.getURL(tc).toString());        
        Assert.assertEquals("foo=3", action.getParameters(tc).get(0).toString());        
        Assert.assertEquals("id('4')", action.getXPath(tc));        
        Assert.assertEquals("5", action.getText(tc));        
        Assert.assertEquals("6", action.getRegexp(tc).toString());        
    }
    
    /*
     * The regexp will complain, because of $ and {}
     */
    @Test(expected=PatternSyntaxException.class)
    public void testPropertyReplacement_NoSuchProperty() throws IOException
    {
//        XltProperties.getInstance().setProperty("p1", "1");
//        XltProperties.getInstance().setProperty("test.com.xceptance.xlt.common.tests.TTest.p1", "11");
        
        // test only the supported types
        final List<CSVRecord> records = createRecords(
                                                      "Name,URL,Parameters,XPath,Text,RegExp",
                                                      "${ap1},\"http://u${ap2}.com/\",foo=${ap3},id('${ap4}'),${ap5},${ap6}");

        final CSVBasedURLAction action = new CSVBasedURLAction(records.get(0), new ParamInterpreter() );        
        final TTest tc = new TTest();
        ((SessionImpl)Session.getCurrent()).setUserName("TTUser");
        
        Assert.assertEquals("${ap1}", action.getName(tc));        
        Assert.assertEquals("http://u${ap2}.com/", action.getURL(tc).toString());        
        Assert.assertEquals("foo=${ap3}", action.getParameters(tc).get(0).toString());        
        Assert.assertEquals("id('${ap4}')", action.getXPath(tc));        
        Assert.assertEquals("${ap5}", action.getText(tc));        
        Assert.assertEquals("${ap6}", action.getRegexp(tc).toString());        
    }        

    @Test
    public void testPropertyReplacement_PropertyHasBeanShellName() throws IOException, EvalError
    {
        XltProperties.getInstance().setProperty("p1", "1");
        XltProperties.getInstance().setProperty("p2", "2");

        // test only the supported types
        final List<CSVRecord> records = createRecords(
                                                      "Name,URL",
                                                      "${p1},\"http://u${p2}.com/\"");

        final CSVBasedURLAction action = new CSVBasedURLAction(records.get(0), new ParamInterpreter() );        
        action.getInterpreter().set("p1", "bean1");
        action.getInterpreter().set("p2", "bean2");
        
        Assert.assertEquals("1", action.getName(tc));        
        Assert.assertEquals("http://u2.com/", action.getURL(tc).toString());               
    }

}



