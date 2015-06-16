package test.com.xceptance.xlt.common.util.action.data;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.xceptance.xlt.api.data.GeneralDataProvider;
import com.xceptance.xlt.api.util.XltProperties;
import com.xceptance.xlt.common.util.action.data.URLActionData;
import com.xceptance.xlt.common.util.action.data.URLActionDataBuilder;
import com.xceptance.xlt.common.util.action.data.URLActionDataStore;
import com.xceptance.xlt.common.util.action.data.URLActionDataValidation;
import com.xceptance.xlt.common.util.bsh.ParameterInterpreter;

public class URLActionDataBuilderTest
{
    private ParameterInterpreter interpreter;

    private XltProperties properties;

    private GeneralDataProvider dataProvider;

    private String name;

    private String type;

    private String url;

    private String method;

    private String encodeParameters;
    
    private String encodeBody;

    private String httpResponceCode;

    private String body;

    private List<URLActionDataValidation> validations = new ArrayList<URLActionDataValidation>();

    URLActionDataValidation validation;

    private List<URLActionDataStore> store = Collections.emptyList();

    URLActionDataStore storeItem;

    private List<NameValuePair> parameters = Collections.emptyList();

    private List<NameValuePair> cookies = Collections.emptyList();

    private List<NameValuePair> headers = Collections.emptyList();

    private String d_name;

    private String d_type;

    private String d_url;

    private String d_method;

    private String d_encode_parameters;
    
    private String d_encode_body;

    private String d_httpResponceCode;

    private String d_body;

    private List<URLActionDataValidation> d_validations = Collections.emptyList();

    private List<URLActionDataStore> d_store = Collections.emptyList();

    private List<NameValuePair> d_parameters = Collections.emptyList();

    private List<NameValuePair> d_cookies = Collections.emptyList();

    private List<NameValuePair> d_headers = Collections.emptyList();

    @Before
    public void setup()
    {
        properties = XltProperties.getInstance();
        dataProvider = GeneralDataProvider.getInstance();
        interpreter = new ParameterInterpreter(properties, dataProvider);
        
        validation = new URLActionDataValidation(
                                                 "validation",
                                                 URLActionDataValidation.XPATH,
                                                 "xpath",
                                                 URLActionDataValidation.MATCHES,
                                                 "matcher",
                                                 interpreter);
        
        storeItem = new URLActionDataStore("store",
                                           URLActionDataStore.XPATH,
                                           "some xpath", interpreter);

        name = "name";

        type = URLActionData.TYPE_ACTION;

        url = "http://www.xceptance.com";

        method = URLActionData.METHOD_GET;

        encodeParameters = "true";
        
        encodeBody = "true";

        httpResponceCode = "400";

        body = "body";

        validations = new ArrayList<URLActionDataValidation>();
        validations.add(validation);

        store = new ArrayList<URLActionDataStore>();
        store.add(storeItem);

        parameters = new ArrayList<NameValuePair>();
        parameters.add(new NameValuePair("parameter_1", "parameter_value_1"));

        cookies = new ArrayList<NameValuePair>();
        cookies.add(new NameValuePair("cookie_1", "cookie_value_1"));

        headers = new ArrayList<NameValuePair>();
        headers.add(new NameValuePair("header_1", "header_value_1"));

        d_name = "d_name";

        d_type = URLActionData.TYPE_STATIC;

        d_url = "http://www.blog.xceptance.com";

        d_method = URLActionData.METHOD_POST;

        d_encode_parameters = "false";
        
        d_encode_body = "false";
        
        d_httpResponceCode = "500";

        d_body = "d_body";

        d_validations = new ArrayList<URLActionDataValidation>();

        d_store = new ArrayList<URLActionDataStore>();

        d_parameters = new ArrayList<NameValuePair>();

        d_cookies = new ArrayList<NameValuePair>();

        d_headers = new ArrayList<NameValuePair>();
    }

    @Test
    public void testCorrectActionBuild() throws MalformedURLException
    {
        final URLActionDataBuilder builder = new URLActionDataBuilder();

        builder.setBody(body);
        builder.setCookies(cookies);
        builder.setEncodeParameters(encodeParameters);
        builder.setEncodeBody(encodeBody);
        builder.setHeaders(headers);
        builder.setHttpResponceCode(httpResponceCode);
        builder.setValidations(validations);
        builder.setUrl(url);
        builder.setType(type);
        builder.setStore(store);
        builder.setParameters(parameters);
        builder.setName(name);
        builder.setMethod(method);
        builder.setInterpreter(interpreter);

        builder.setDefaultBody(d_body);
        builder.setDefaultCookies(d_cookies);
        builder.setDefaultEncodeParameters(d_encode_parameters);
        builder.setDefaultEncodeBody(d_encode_body);
        builder.setDefaultHeaders(d_headers);
        builder.setDefaultHttpResponceCode(d_httpResponceCode);
        builder.setDefaultValidations(d_validations);
        builder.setDefaultUrl(d_url);
        builder.setDefaultType(d_type);
        builder.setDefaultStore(d_store);
        builder.setDefaultParameters(d_parameters);
        builder.setDefaultName(d_name);
        builder.setDefaultMethod(d_method);
        builder.setInterpreter(interpreter);

        URLActionData action = builder.build();

        Assert.assertEquals(action.getName(), name);
        Assert.assertEquals(action.getBody(), body);
        Assert.assertEquals(action.getType(), type);
        Assert.assertEquals(action.getMethod().toString(), method);
        Assert.assertEquals(action.getUrlString(), url);
        Assert.assertFalse(action.getValidations().isEmpty());
        Assert.assertFalse(action.getStore().isEmpty());
        Assert.assertFalse(action.getHeaders().isEmpty());
        Assert.assertFalse(action.getParameters().isEmpty());
        Assert.assertFalse(action.getCookies().isEmpty());
        Assert.assertEquals(action.encodeBody().toString(), encodeBody);
        Assert.assertEquals(action.encodeParameters().toString(), encodeParameters);

        builder.setBody(null);
        builder.setCookies(Collections.<NameValuePair> emptyList());
        builder.setEncodeParameters(null);
        builder.setEncodeBody(null);
        builder.setHeaders(Collections.<NameValuePair> emptyList());
        builder.setHttpResponceCode(null);
        builder.setValidations(Collections.<URLActionDataValidation> emptyList());
        builder.setUrl(null);
        builder.setType(null);
        builder.setStore(Collections.<URLActionDataStore> emptyList());
        builder.setParameters(Collections.<NameValuePair> emptyList());
        builder.setName(null);
        builder.setMethod(null);
        builder.setInterpreter(interpreter);

        action = builder.build();

        Assert.assertEquals(action.getName(), d_name);
        Assert.assertEquals(action.getBody(), d_body);
        Assert.assertEquals(action.getType(), d_type);
        Assert.assertEquals(action.getMethod().toString(), d_method);
        Assert.assertEquals(action.getUrlString(), d_url);
        Assert.assertTrue(action.getValidations().isEmpty());
        Assert.assertTrue(action.getStore().isEmpty());
        Assert.assertTrue(action.getHeaders().isEmpty());
        Assert.assertTrue(action.getParameters().isEmpty());
        Assert.assertTrue(action.getCookies().isEmpty());
        Assert.assertEquals(action.encodeBody().toString(), d_encode_body);
        Assert.assertEquals(action.encodeParameters().toString(), d_encode_parameters);

    }

    @Test
    public void constructActionFromDefaults()
    {
        final URLActionDataBuilder builder = new URLActionDataBuilder();
        builder.setDefaultBody(body);
        builder.setDefaultCookies(cookies);
        builder.setDefaultEncodeParameters(encodeParameters);
        builder.setDefaultHeaders(headers);
        builder.setDefaultHttpResponceCode(httpResponceCode);
        builder.setDefaultValidations(validations);
        builder.setDefaultUrl(url);
        builder.setDefaultType(type);
        builder.setDefaultStore(store);
        builder.setDefaultParameters(parameters);
        builder.setDefaultName(name);
        builder.setDefaultMethod(method);
        builder.setInterpreter(interpreter);
        
        final URLActionData action = builder.build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void missingName()
    {
        final URLActionDataBuilder builder = new URLActionDataBuilder();
        builder.setUrl(url);
        builder.setInterpreter(interpreter);
        final URLActionData action = builder.build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void missingUrl()
    {
        final URLActionDataBuilder builder = new URLActionDataBuilder();
        builder.setName(name);
        builder.setInterpreter(interpreter);
        final URLActionData action = builder.build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void missingInterpreter()
    {
        final URLActionDataBuilder builder = new URLActionDataBuilder();
        builder.setUrl(url);
        builder.setName(name);
        final URLActionData action = builder.build();
    }

}
