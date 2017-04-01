package test.com.xceptance.xlt.common.util.action.execution;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.xceptance.xlt.api.data.GeneralDataProvider;
import com.xceptance.xlt.api.util.XltProperties;
import com.xceptance.xlt.common.util.action.data.URLActionData;
import com.xceptance.xlt.common.util.action.execution.URLActionDataRequestBuilder;
import com.xceptance.xlt.common.util.bsh.ParameterInterpreter;

import test.com.xceptance.xlt.common.util.MockObjects;

public class URLActionDataRequestBuilderTest
{
    @SuppressWarnings("unused")
    private MockObjects mockObjects;

    private URLActionDataRequestBuilder builder;

    private ParameterInterpreter interpreter;

    private XltProperties properties;

    private GeneralDataProvider dataProvider;

    private final String name = "Name";

    private final String urlStringEmpty = "https://www.xceptance.com/?x=%45s#q=xeceptance+%2B";

    @SuppressWarnings("unused")
    private String urlStringWithEncodedParameters;

    private String urlStringWithDecodedParameters;

    private final String methodPost = "POST";

    @SuppressWarnings("unused")
    private final String methodGet = "GET";

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

    private final NameValuePair p2Encoded = new NameValuePair("Parameter%26%25%24Name2", "Parameter%C2%A7%24%25%26%2BValue2");

    private final NameValuePair p1Decoded = new NameValuePair("Parameter&%$Name1", "Parameter§$%&+Value1");

    private final NameValuePair p2Decoded = new NameValuePair("Parameter&%$Name2", "Parameter§$%&+Value2");

    @Before
    public void setup() throws UnsupportedEncodingException
    {
        properties = XltProperties.getInstance();
        dataProvider = GeneralDataProvider.getInstance();
        interpreter = new ParameterInterpreter(properties, dataProvider);

        mockObjects = new MockObjects();

        builder = new URLActionDataRequestBuilder();

        headers.add(header1);
        headers.add(header2);

        parametersEmpty.add(p1Empty);
        parametersEmpty.add(p2Empty);

        parametersEncoded.add(p1Encoded);
        parametersEncoded.add(p2Encoded);

        parametersDecoded.add(p1Decoded);
        parametersDecoded.add(p2Decoded);

        urlStringWithEncodedParameters = "https://www.xceptance.com/?x=%45s&" + parametersEncoded.get(0).getName() + "=" +
                                         parametersEncoded.get(0).getValue() + "&" + parametersEncoded.get(1).getName() + "=" +
                                         parametersEncoded.get(1).getValue() + "#q=xeceptance+%2B";

        urlStringWithDecodedParameters = "https://www.xceptance.com/?x=%45s&" + parametersDecoded.get(0).getName() + "=" +
                                         parametersDecoded.get(0).getValue() + "&" + parametersDecoded.get(1).getName() + "=" +
                                         parametersDecoded.get(1).getValue() + "#q=xeceptance+%2B";

    }

    @Test
    public void test_Get_NoParam__NoBody_()
    {
        final URLActionData action = new URLActionData(name, urlStringEmpty, interpreter);
        final WebRequest request = builder.buildRequest(action);

        Assert.assertEquals(action.getMethod(), request.getHttpMethod());
        Assert.assertEquals(urlStringEmpty, request.getUrl().toString());
        Assert.assertNull(request.getRequestBody());
    }

    @Test
    public void test_POST_NoParam__NoBody_()
    {
        final URLActionData action = new URLActionData(name, urlStringEmpty, interpreter);
        action.setMethod(methodPost);
        final WebRequest request = builder.buildRequest(action);

        Assert.assertEquals(action.getMethod(), request.getHttpMethod());
        Assert.assertEquals(urlStringEmpty, request.getUrl().toString());
        Assert.assertNull(request.getRequestBody());
    }

    @Test
    public void test_Get_Param_EncodeParam_NoBody_()
    {
        final URLActionData action = new URLActionData(name, urlStringEmpty, interpreter);
        action.setEncodeParameters(false);
        action.setParameters(parametersEncoded);

        final WebRequest request = builder.buildRequest(action);

        Assert.assertEquals(action.getMethod(), request.getHttpMethod());
        Assert.assertEquals(urlStringWithDecodedParameters, request.getUrl().toString());
        Assert.assertNull(request.getRequestBody());

    }

    @Test
    public void test_POST_Param_EncodeParam_NoBody_()
    {
        final URLActionData action = new URLActionData(name, urlStringEmpty, interpreter);
        action.setMethod(methodPost);
        action.setEncodeParameters(true);
        action.setParameters(parametersDecoded);

        final WebRequest request = builder.buildRequest(action);

        Assert.assertEquals(action.getMethod(), request.getHttpMethod());
        Assert.assertEquals(urlStringEmpty, request.getUrl().toString());
        Assert.assertNull(request.getRequestBody());

        final List<NameValuePair> requestParameters = request.getRequestParameters();

        for (int i = 0; i < requestParameters.size(); i++)
        {
            Assert.assertEquals(parametersDecoded.get(i).getName(), requestParameters.get(i).getName());
            Assert.assertEquals(parametersDecoded.get(i).getValue(), requestParameters.get(i).getValue());
        }
    }

    @Test
    public void test_Get_Param_NoEncodeParam_NoBody_()
    {
        final URLActionData action = new URLActionData(name, urlStringEmpty, interpreter);
        action.setEncodeParameters(true);
        action.setParameters(parametersDecoded);

        final WebRequest request = builder.buildRequest(action);

        Assert.assertEquals(action.getMethod(), request.getHttpMethod());
        Assert.assertEquals(urlStringWithDecodedParameters, request.getUrl().toString());
        Assert.assertNull(request.getRequestBody());
    }

    @Test
    public void test_POST_Param_NoEncodeParam_NoBody_()
    {
        final URLActionData action = new URLActionData(name, urlStringEmpty, interpreter);
        action.setMethod(methodPost);
        action.setEncodeParameters(true);
        action.setParameters(parametersDecoded);

        final WebRequest request = builder.buildRequest(action);

        Assert.assertEquals(action.getMethod(), request.getHttpMethod());
        Assert.assertEquals(urlStringEmpty, request.getUrl().toString());
        Assert.assertNull(request.getRequestBody());

        final List<NameValuePair> requestParameters = request.getRequestParameters();

        for (int i = 0; i < requestParameters.size(); i++)
        {
            Assert.assertEquals(parametersDecoded.get(i).getName(), requestParameters.get(i).getName());
            Assert.assertEquals(parametersDecoded.get(i).getValue(), requestParameters.get(i).getValue());
        }
    }

    @Test
    public void test_Get_NoParam_Body()
    {
        final URLActionData action = new URLActionData(name, urlStringEmpty, interpreter);
        action.setBody(bodyEncoded);

        final WebRequest request = builder.buildRequest(action);

        Assert.assertEquals(action.getMethod(), request.getHttpMethod());
        Assert.assertEquals(urlStringEmpty, request.getUrl().toString());
        Assert.assertNull(request.getRequestBody());
    }

    @Test
    public void test_POST_NoParam_Body_NoEncodedBody()
    {
        final URLActionData action = new URLActionData(name, urlStringEmpty, interpreter);
        action.setBody(bodyEncoded);
        action.setEncodeBody(false);
        action.setMethod(methodPost);
        final WebRequest request = builder.buildRequest(action);

        Assert.assertEquals(action.getMethod(), request.getHttpMethod());
        Assert.assertEquals(urlStringEmpty, request.getUrl().toString());
        Assert.assertEquals(bodyDecoded, request.getRequestBody());
    }

    @Test
    public void test_POST_NoParam_Body_EncodedBody()
    {
        final URLActionData action = new URLActionData(name, urlStringEmpty, interpreter);
        action.setBody(bodyDecoded);
        action.setEncodeBody(true);
        action.setMethod(methodPost);
        final WebRequest request = builder.buildRequest(action);

        Assert.assertEquals(action.getMethod(), request.getHttpMethod());
        Assert.assertEquals(urlStringEmpty, request.getUrl().toString());
        Assert.assertEquals(bodyDecoded, request.getRequestBody());
    }

    @Test
    public void test_POST_Param_EncodeParam_Body_NoEncodedBody()
    {
        final URLActionData action = new URLActionData(name, urlStringEmpty, interpreter);
        action.setMethod(methodPost);

        action.setEncodeParameters(true);
        action.setParameters(parametersDecoded);

        action.setBody(bodyEncoded);
        action.setEncodeBody(false);

        final WebRequest request = builder.buildRequest(action);

        Assert.assertEquals(action.getMethod(), request.getHttpMethod());
        Assert.assertEquals(urlStringWithDecodedParameters, request.getUrl().toString());

        Assert.assertEquals(bodyDecoded, request.getRequestBody());

    }

    @Test
    public void test_POST_Param_EncodeParam_Body_EncodedBody()
    {
        final URLActionData action = new URLActionData(name, urlStringEmpty, interpreter);
        action.setMethod(methodPost);

        action.setEncodeParameters(true);
        action.setParameters(parametersDecoded);

        action.setBody(bodyDecoded);
        action.setEncodeBody(true);

        final WebRequest request = builder.buildRequest(action);

        Assert.assertEquals(action.getMethod(), request.getHttpMethod());
        Assert.assertEquals(urlStringWithDecodedParameters, request.getUrl().toString());
        Assert.assertEquals(bodyDecoded, request.getRequestBody());

    }

    @Test
    public void test_POST_Param_NoEncodeParam_Body_NoEncodeBody()
    {
        final URLActionData action = new URLActionData(name, urlStringEmpty, interpreter);
        action.setMethod(methodPost);

        action.setEncodeParameters(true);
        action.setParameters(parametersDecoded);

        action.setBody(bodyEncoded);
        action.setEncodeBody(false);

        final WebRequest request = builder.buildRequest(action);

        Assert.assertEquals(action.getMethod(), request.getHttpMethod());
        Assert.assertEquals(urlStringWithDecodedParameters, request.getUrl().toString());
        Assert.assertEquals(bodyDecoded, request.getRequestBody());

    }

    @Test
    public void test_POST_Param_NoEncodeParam_Body_EncodeBody()
    {
        final URLActionData action = new URLActionData(name, urlStringEmpty, interpreter);
        action.setMethod(methodPost);

        action.setEncodeParameters(false);
        action.setParameters(parametersEncoded);

        action.setBody(bodyDecoded);
        action.setEncodeBody(true);

        final WebRequest request = builder.buildRequest(action);

        Assert.assertEquals(action.getMethod(), request.getHttpMethod());
        Assert.assertEquals(urlStringWithDecodedParameters, request.getUrl().toString());
        Assert.assertEquals(bodyDecoded, request.getRequestBody());

    }

}
