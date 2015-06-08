package test.com.xceptance.xlt.common.util;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.xceptance.xlt.common.util.URLActionData;
import com.xceptance.xlt.common.util.URLActionDataRequestBuilder;
import com.xceptance.xlt.common.util.bsh.ParameterInterpreter;

public class URLActionRequestBuilderTest
{
    private URLActionDataRequestBuilder builder;
    
    ParameterInterpreter interpreter = new ParameterInterpreter(null);
    
    private final String name = "Name";
    
    private final String urlStringEmpty = "https://www.xceptance.com/en/";
    private final String urlStringWithParameters = "https://www.xceptance.com/#q=xeceptance+%2B";
    
    private final String methodPost = "POST";
    private final String methodGet = "GET";
    
    private final String encodeParametersFalse = "false";
    private final String encodeParametersTrue = "true";
    
    private final String encodeBodyFalse = "false";
    private final String encodeBodyTrue = "true";
    
    private final String bodyEmpty = "Body";
    private final String bodyDecoded = "Body?=%$";
    private final String bodyEncoded = "Body%3F%3D%25%24";
    
    private final List<NameValuePair> headers = new ArrayList<NameValuePair>();
    private final NameValuePair header2 = new NameValuePair("HeaderName2", "HeaderValue2");
    private final NameValuePair header1 = new NameValuePair("HeaderName1", "HeaderValue1");
    
    
    private final List<NameValuePair> parametersEmpty = new ArrayList<NameValuePair>();
    private final List<NameValuePair> parametersDecoded = new ArrayList<NameValuePair>();
    private final List<NameValuePair> parametersEncoded = new ArrayList<NameValuePair>();
    
    private final NameValuePair p1Empty = new NameValuePair("parameter1", "parameterValue1");
    private final NameValuePair p2Empty = new NameValuePair("parameter1", "parameterValue1");
    
    private final NameValuePair p1Encoded = new NameValuePair("Parameter%26%25%24Name1", "Parameter%C2%A7%24%25%26%2BValue1");
    private final NameValuePair p2Encoded = new NameValuePair("Parameter%26%25%24Name1", "Parameter%C2%A7%24%25%26%2BValue2");
    
    private final NameValuePair p1Decoded = new NameValuePair("Parameter&%$Name1", "Parameter§$%&+Value1");
    private final NameValuePair p2Decoded = new NameValuePair("Parameter&%$Name2", "Parameter§$%&+Value2");
        
    
    @Before
    public void setup() throws UnsupportedEncodingException
    {
        builder = new URLActionDataRequestBuilder();
        
        headers.add(header1);
        headers.add(header2);
        
        parametersEmpty.add(p1Empty);
        parametersEmpty.add(p2Empty);
        
        parametersEncoded.add(p1Encoded);
        parametersEncoded.add(p2Encoded);
        
        parametersDecoded.add(p1Decoded);
        parametersDecoded.add(p2Decoded);
        
    }  

    @Test
    public void test_GET_Parameters_Decoded(){
        final URLActionData action = new URLActionData(name, urlStringEmpty, interpreter);
        action.setMethod(methodGet);
        action.setEncodeParameters(true);
        action.setParameters(parametersDecoded);
        
        final WebRequest request = builder.buildRequest(action);
        
        Assert.assertEquals("", "");
        System.err.println(request.getUrl().toString());
    }
    }
