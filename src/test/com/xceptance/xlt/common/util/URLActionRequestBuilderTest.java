package test.com.xceptance.xlt.common.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.xceptance.xlt.common.util.URLAction;
import com.xceptance.xlt.common.util.URLActionRequestBuilder;
import com.xceptance.xlt.common.util.bsh.ParameterInterpreter;

public class URLActionRequestBuilderTest
{
    private URLActionRequestBuilder builder;
    
    private URLAction urlActionPostEncoded;
    private URLAction urlActionPost;
    private URLAction urlActionGetEncoded;
    private URLAction urlActionGet;
    private URLAction urlXhrActionPostEncoded;
    private URLAction urlXhrActionPost;
    private URLAction urlXhrActionGetEncoded;
    private URLAction urlXhrActionGet;
    
    ParameterInterpreter interpreter = new ParameterInterpreter(null);
    private final String name = "Name";
    private final String urlString = "https://www.xceptance.com/en/";
    private final String methodPost = "POST";
    private final String methodGet = "GET";
    private final String encodedFalse = "fasle";
    private final String encodedTrue = "true";
    private final String body = "Body";
    private final List<NameValuePair> parameters = new ArrayList<NameValuePair>();
    private final List<NameValuePair> headers = new ArrayList<NameValuePair>();
    private final List<NameValuePair> parametersEncoded = new ArrayList<NameValuePair>();
    private final NameValuePair parameter_1 = new NameValuePair("parameter_Name_1", "parameter_Value_1");
    private final NameValuePair parameter_2 = new NameValuePair("parameter_Name_2", "parameter_Value_2");
    
    private NameValuePair parameter_1_encoded;
    private NameValuePair parameter_2_encoded;
    
    private final NameValuePair header_2 = new NameValuePair("Header_Name_2", "Header_Value_2");
    private final NameValuePair header_1 = new NameValuePair("Header_Name_1", "Header_Value_1");
        
    
    @Before
    public void setup() throws UnsupportedEncodingException
    {
        builder = new URLActionRequestBuilder();
        
        parameter_1_encoded = new NameValuePair(URLEncoder.encode("parameter_Name_1","UTF-8"), URLEncoder.encode("parameter_Value_1", "UTF-8"));
        parameter_2_encoded = new NameValuePair(URLEncoder.encode("parameter_Name_2","UTF-8"), URLEncoder.encode("parameter_Value_2", "UTF-8"));
        
        parameters.add(parameter_1);
        parameters.add(parameter_2);
        
        parametersEncoded.add(parameter_1_encoded);
        parametersEncoded.add(parameter_2_encoded);
        
        headers.add(header_1);
        headers.add(header_2);
        
        urlActionGet = new URLAction(name, urlString, interpreter);
        urlActionGet.setBody(body);
        urlActionGet.setEncodedParameters(encodedFalse);
        urlActionGet.setHeaders(headers);
        urlActionGet.setParameters(parameters);
        urlActionGet.setMethod(methodGet);
        
        urlActionPost = new URLAction(name, urlString, interpreter);
        urlActionPost.setBody(body);
        urlActionPost.setEncodedParameters(encodedFalse);
        urlActionPost.setHeaders(headers);
        urlActionPost.setParameters(parameters);
        urlActionPost.setMethod(methodPost);
        
        urlActionGetEncoded = new URLAction(name, urlString, interpreter);
        urlActionGetEncoded.setBody(body);
        urlActionGetEncoded.setEncodedParameters(encodedTrue);
        urlActionGetEncoded.setHeaders(headers);
        urlActionGetEncoded.setParameters(parametersEncoded);
        urlActionGetEncoded.setMethod(methodPost);
        
        urlActionPostEncoded = new URLAction(name, urlString, interpreter);
        urlActionPostEncoded.setBody(body);
        urlActionPostEncoded.setEncodedParameters(encodedTrue);
        urlActionPostEncoded.setHeaders(headers);
        urlActionPostEncoded.setParameters(parametersEncoded);
        urlActionPostEncoded.setMethod(methodPost);
        
        urlXhrActionGet = new URLAction(name, urlString, interpreter);
        urlXhrActionGet.setType(URLAction.TYPE_XHR);
        urlXhrActionGet.setBody(body);
        urlXhrActionGet.setEncodedParameters(encodedFalse);
        urlXhrActionGet.setHeaders(headers);
        urlXhrActionGet.setParameters(parameters);
        urlXhrActionGet.setMethod(methodGet);
         
        urlXhrActionPost = new URLAction(name, urlString, interpreter);
        urlXhrActionPost.setType(URLAction.TYPE_XHR);
        urlXhrActionPost.setBody(body);
        urlXhrActionPost.setEncodedParameters(encodedFalse);
        urlXhrActionPost.setHeaders(headers);
        urlXhrActionPost.setParameters(parameters);
        urlXhrActionPost.setMethod(methodPost);
        
        urlXhrActionGetEncoded = new URLAction(name, urlString, interpreter);
        urlXhrActionGetEncoded.setType(URLAction.TYPE_XHR);
        urlXhrActionGetEncoded.setBody(body);
        urlXhrActionGetEncoded.setEncodedParameters(encodedTrue);
        urlXhrActionGetEncoded.setHeaders(headers);
        urlXhrActionGetEncoded.setParameters(parametersEncoded);
        urlXhrActionGetEncoded.setMethod(methodGet);
     
        urlXhrActionPostEncoded = new URLAction(name, urlString, interpreter);
        urlXhrActionPostEncoded.setType(URLAction.TYPE_XHR);
        urlXhrActionPostEncoded.setBody(body);
        urlXhrActionPostEncoded.setEncodedParameters(encodedTrue);
        urlXhrActionPostEncoded.setHeaders(headers);
        urlXhrActionPostEncoded.setParameters(parametersEncoded);
        urlXhrActionPostEncoded.setMethod(methodPost);
    }  

    @Test
    public void testBuildRequestWithActionGet()
    {
        final WebRequest request = builder.buildRequest(urlActionGet);
        Assert.assertNotNull(request);
        
    }
    @Test
    public void testBuildRequestWithActionPost()
    {
        final WebRequest request = builder.buildRequest(urlActionPost);
        Assert.assertNotNull(request);
        System.err.println(request.toString());
        System.err.println(request.getRequestBody());
    }
    @Test
    public void testBuildRequestWithActionGetEncoded()
    {
        final WebRequest request = builder.buildRequest(urlActionGetEncoded);
        Assert.assertNotNull(request);
        System.err.println(request.toString());
        System.err.println(request.getRequestBody());
    }
    @Test
    public void testBuildRequestWithActionPostEncoded()
    {
        final WebRequest request = builder.buildRequest(urlActionPostEncoded);
        Assert.assertNotNull(request);
        System.err.println(request.toString());
        System.err.println(request.getRequestBody());
    }
    }
