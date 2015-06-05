package test.com.xceptance.xlt.common.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.xceptance.xlt.common.util.URLAction;
import com.xceptance.xlt.common.util.bsh.ParameterInterpreter;

public class URLActionTest 
{
    private List<String> types;

    private List<String> methods;

    private ParameterInterpreter interpreter;

    @Before
    public void setup()
    {
        interpreter = new ParameterInterpreter(null);
        methods = new ArrayList<String>();
        methods.addAll(URLAction.PERMITTEDMETHODS);
        types = new ArrayList<String>();
        types.addAll(URLAction.PERMITTEDTYPES);

    }

    @Test
    public void constructorTest()
    {

        @SuppressWarnings({
                "unused", "null"
            })
        final URLAction action = new URLAction("name", "http://www.xceptance.com", interpreter);

    }
    @Test
    public void rightSetup(){
        for(final String type : types){
            for(final String method : methods){
                final URLAction action = new URLAction("name", "http://www.xceptance.com", interpreter);
                
                action.setBody("Body");
                Assert.assertEquals("Body", action.getBody());
                
                action.setCookies(null);
                Assert.assertEquals(Collections.emptyList(), action.getCookies());
                
                action.setEncodedParameters(true);
                Assert.assertEquals(true, action.isParametersEncoded());
                
                action.setHeaders(null);
                Assert.assertEquals(Collections.emptyList(), action.getHeaders());
                
                action.setHttpResponceCode(400);
                Assert.assertEquals(400, action.getResponseCodeValidator().getHttpResponseCode());
                
                action.setMethod(method);
                Assert.assertEquals(method, action.getMethod().toString());
                
                action.setType(type);
                Assert.assertEquals(type, action.getType());
                
                action.setName("name");
                Assert.assertEquals("name", action.getName());
                
                action.setParameters(null);
                Assert.assertEquals(Collections.emptyList(), action.getParameters());
                
                action.setStore(null);
                Assert.assertEquals(Collections.emptyList(), action.getStore());
                
                action.setUrl("http://www.xceptance.com");
                try
                {
                    Assert.assertEquals(new URL("http://www.xceptance.com"), action.getUrl());
                }
                catch (final MalformedURLException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                
                action.setValidations(null);
                Assert.assertEquals(Collections.emptyList(), action.getValidations());
            }
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void wrongSetupName()
    {
        @SuppressWarnings({
                "unused", "null"
            })
        final URLAction action = new URLAction(null, "http://www.xceptance.com", interpreter);
    }
    

    @Test(expected = IllegalArgumentException.class)
    public void wrongSetupUrl()
    {
        @SuppressWarnings({
                "unused", "null"
            })
        final URLAction action = new URLAction("name", null, interpreter);
    }
    @Test(expected = IllegalArgumentException.class)
    public void wrongSetupInterpreter()
    {
        @SuppressWarnings({
                "unused", "null"
            })
        final URLAction action = new URLAction("name", "http://www.xceptance.com", null);
    }
    
    @Test(expected = MalformedURLException.class)
    public void wrongUrl() throws MalformedURLException
    {

        final URLAction action = new URLAction("name", "c", interpreter);

         final URL url = action.getUrl();
    }
    
    
    @Test
    public void defaultValues(){
        final URLAction action = new URLAction("name", "http://www.xceptance.com", interpreter);
        Assert.assertEquals(200, action.getResponseCodeValidator().getHttpResponseCode());
        
        action.setMethod("x");
        Assert.assertEquals(URLAction.METHOD_GET, action.getMethod().toString());
        
        action.setEncodedParameters("x");
        Assert.assertEquals(false, action.isParametersEncoded());
        
        action.setType("x");
        Assert.assertEquals(URLAction.TYPE_ACTION, action.getType());
    }
    
}
