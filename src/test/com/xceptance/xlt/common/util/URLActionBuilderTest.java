package test.com.xceptance.xlt.common.util;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.xceptance.xlt.common.util.URLAction;
import com.xceptance.xlt.common.util.URLActionBuilder;
import com.xceptance.xlt.common.util.URLActionStore;
import com.xceptance.xlt.common.util.URLActionValidation;
import com.xceptance.xlt.common.util.bsh.ParameterInterpreter;

public class URLActionBuilderTest
{
    private ParameterInterpreter interpreter = new ParameterInterpreter(null);

    private String name;

    private String type;

    private String url;

    private String method;

    private String encoded;

    private String httpResponceCode;

    private String body;

    private List<URLActionValidation> validations = new ArrayList<URLActionValidation>();

    URLActionValidation validation = new URLActionValidation("validation",
                                                             URLActionValidation.XPATH,
                                                             "xpath",
                                                             URLActionValidation.MATCHES,
                                                             "matcher", interpreter);

    private List<URLActionStore> store = Collections.emptyList();
    
    URLActionStore storeItem = new URLActionStore("store", URLActionStore.XPATH, "some xpath", interpreter);

    private List<NameValuePair> parameters = Collections.emptyList();

    private List<NameValuePair> cookies = Collections.emptyList();

    private List<NameValuePair> headers = Collections.emptyList();

    private String d_name;

    private String d_type;

    private String d_url;

    private String d_method;

    private String d_encoded;

    private String d_httpResponceCode;

    private String d_body;

    private List<URLActionValidation> d_validations = Collections.emptyList();

    private List<URLActionStore> d_store = Collections.emptyList();

    private List<NameValuePair> d_parameters = Collections.emptyList();

    private List<NameValuePair> d_cookies = Collections.emptyList();

    private List<NameValuePair> d_headers = Collections.emptyList();

    @Before
    public void setup()
    {
        interpreter = new ParameterInterpreter(null);

        name = "name";
        
        type = URLAction.TYPE_ACTION;

        url = "http://www.xceptance.com";

        method = URLAction.METHOD_GET;

        encoded = "true";

        httpResponceCode = "400";

        body = "body";

        validations = new ArrayList<URLActionValidation>();
        validations.add(validation);

        store = new ArrayList<URLActionStore>();
        store.add(storeItem);

        parameters = new ArrayList<NameValuePair>();
        parameters.add(new NameValuePair("parameter_1", "parameter_value_1"));

        cookies = new ArrayList<NameValuePair>();
        cookies.add(new NameValuePair("cookie_1", "cookie_value_1"));
        
        headers = new ArrayList<NameValuePair>();
        headers.add(new NameValuePair("header_1", "header_value_1"));

        d_name = "d_name";

        d_type = URLAction.TYPE_STATIC;

        d_url = "http://www.blog.xceptance.com";

        d_method = URLAction.METHOD_POST;

        d_encoded = "fale";

        d_httpResponceCode = "500";

        d_body = "d_body";

        d_validations = new ArrayList<URLActionValidation>();

        d_store = new ArrayList<URLActionStore>();

        d_parameters = new ArrayList<NameValuePair>();

        d_cookies = new ArrayList<NameValuePair>();

        d_headers = new ArrayList<NameValuePair>();
    }

    @Test
    public void testCorrectActionBuild() throws MalformedURLException
    {
        final URLActionBuilder builder = new URLActionBuilder();

        builder.setBody(body);
        builder.setCookies(cookies);
        builder.setEncoded(encoded);
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
        builder.setDefaultEncoded(d_encoded);
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
        
        URLAction action = builder.build();

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

        builder.setBody(null);
        builder.setCookies(Collections.<NameValuePair> emptyList());
        builder.setEncoded(null);
        builder.setHeaders(Collections.<NameValuePair> emptyList());
        builder.setHttpResponceCode(null);
        builder.setValidations(Collections.<URLActionValidation> emptyList());
        builder.setUrl(null);
        builder.setType(null);
        builder.setStore(Collections.<URLActionStore> emptyList());
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


    }

    @Test
    public void constructActionFromDefaults()
    {
        final URLActionBuilder builder = new URLActionBuilder();
        builder.setDefaultBody(body);
        builder.setDefaultCookies(cookies);
        builder.setDefaultEncoded(encoded);
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
    }

    @Test(expected = IllegalArgumentException.class)
    public void missingName()
    {
        final URLActionBuilder builder = new URLActionBuilder();
        builder.setUrl(url);
        builder.setInterpreter(interpreter);
        final URLAction action = builder.build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void missingUrl()
    {
        final URLActionBuilder builder = new URLActionBuilder();
        builder.setName(name);
        builder.setInterpreter(interpreter);
        final URLAction action = builder.build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void missingInterpreter()
    {
        final URLActionBuilder builder = new URLActionBuilder();
        builder.setUrl(url);
        builder.setName(name);
        final URLAction action = builder.build();
    }

}
