package com.xceptance.xlt.common.util;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.gargoylesoftware.htmlunit.util.UrlUtils;
import com.xceptance.common.lang.StringUtils;
import com.xceptance.xlt.api.util.XltLogger;

public class URLActionDataRequestBuilder
{
    public URLActionDataRequestBuilder()
    {
        XltLogger.runTimeLogger.info("Creating new Instance");
    }

    public WebRequest buildRequest(final URLActionData action)
    {
        ParameterUtils.isNotNull(action, "URLAction");
        
        final WebRequest resultRequest;

        try
        {
            resultRequest = createWebRequestFromURLAction(action);
        }
        catch (final Exception e)
        {
            throw new IllegalArgumentException("Failed to create WebRequest for action: "
                                               + action.getName() + e.getMessage(), e);
        }

        return resultRequest;
    }
    private WebRequest createWebRequestFromURLAction(final URLActionData action) throws MalformedURLException, UnsupportedEncodingException{
        
        final WebRequest resultRequest;
        
        resultRequest = createWebRequestFromUrl(action.getUrl());
        fillWebRequestWithMethod(resultRequest, action.getMethod());
        if (action.hasBody())
        {
            fillWebRequestWithBody(resultRequest, action.getBody(),
                                   action.encodeBody());
        }
        fillWebRequestWithHeaders(resultRequest, action.getHeaders());
        fillWebRequestWithCookies(resultRequest, action.getCookies());
        fillWebRequestWithParameters(resultRequest, action.getParameters(),
                                     action.encodeParameters(), action.getMethod());
        return resultRequest;
    }
    
    public WebRequest buildXhrRequest(final URLActionData action, final URL refererUrl)
    {
        final WebRequest resultXhrRequest = buildRequest(action);
        resultXhrRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
        resultXhrRequest.setAdditionalHeader("Referer", refererUrl.toExternalForm());

        return resultXhrRequest;
    }

    private void fillWebRequestWithHeaders(final WebRequest request,
                                           final List<NameValuePair> headers)
    {
        if (!headers.isEmpty())
        {
            for (final NameValuePair header : headers)
            {
                request.setAdditionalHeader(header.getName(), header.getValue());
            }
        }
    }

    private void fillWebRequestWithCookies(final WebRequest request,
                                           final List<NameValuePair> cookies)
    {
        if (!cookies.isEmpty())
        {
            String cookieString = "";
            for (final NameValuePair cookie : cookies)
            {
                cookieString += cookie.getName() + "=" + cookie.getValue() + ";";
            }
            request.setAdditionalHeader("Cookie", cookieString);
        }
    }

    private void fillWebRequestWithMethod(final WebRequest request,
                                          final HttpMethod method)
    {
        request.setHttpMethod(method);
    }

    private WebRequest createWebRequestFromUrl(final URL url)
    {
        return new WebRequest(url);
    }

    private void fillWebRequestWithBody(final WebRequest request,
                                        final String body,
                                        final Boolean encodeBody)
        throws UnsupportedEncodingException
    {
        String resultBody = body;
        if (encodeBody)
        {
            resultBody = encodeRequestBody(body);
        }
        if (request.getHttpMethod().equals(HttpMethod.POST)
            || request.getHttpMethod().equals(HttpMethod.PUT))
        {
            request.setRequestBody(resultBody);
        }
    }

    private void fillWebRequestWithParameters(final WebRequest request,
                                              final List<NameValuePair> parameters,
                                              final boolean encodeParameters,
                                              final HttpMethod httpMethod)
        throws UnsupportedEncodingException, MalformedURLException
    {
        if (!parameters.isEmpty())
        {
            List<NameValuePair> resultParameters = new ArrayList<NameValuePair>(parameters);
            if (encodeParameters)
            {
                resultParameters = encodeRequestParameters(resultParameters);
            }
            if (!httpMethod.equals(HttpMethod.POST) || request.getRequestBody() != null)
            {
                addParametersToUrl(request, resultParameters);
            }
            else
            {
                request.setRequestParameters(resultParameters);
            }
        }

    }

    private void addParametersToUrl(final WebRequest request,
                                    final List<NameValuePair> parameters)
        throws MalformedURLException
    {
        String urlString = request.getUrl().toString();
        urlString = StringUtils.replace(urlString, "&amp;", "&");
        final URL newUrl = new URL(urlString);
        final String oldQueryString = newUrl.getQuery();
        final StringBuilder newQueryString = new StringBuilder();

        final boolean insertLeadingAmp = oldQueryString != null
                                         && !oldQueryString.isEmpty();

        if (insertLeadingAmp)
        {
            newQueryString.append(oldQueryString);
        }
        for (int index = 0; index < parameters.size(); index++)
        {
            final NameValuePair nameValuePair = parameters.get(index);
            /*
             * For each pair except the first, an & has to be appended. For the first pair it depends on whether there
             * is already a query (in which case insertLeadingAmp is true).
             */
            if (index > 0 || insertLeadingAmp)
            {
                newQueryString.append('&');
            }
            newQueryString.append(nameValuePair.getName()).append('=')
                          .append(nameValuePair.getValue());
        }

        final URL newNewUrl = UrlUtils.getUrlWithNewQuery(newUrl,
                                                          newQueryString.toString());
        request.setUrl(newNewUrl);
    }

    private String decodeRequestBody(final String body)
        throws UnsupportedEncodingException
    {
        final String decodedBody = URLDecoder.decode(body, "UTF-8");
        return decodedBody;
    }

    private String encodeRequestBody(final String body)
        throws UnsupportedEncodingException
    {
        final String encodedBody = URLEncoder.encode(body, "UTF-8");
        return encodedBody;
    }
    private List<NameValuePair> encodeRequestParameters(final List<NameValuePair> parameters)
        throws UnsupportedEncodingException
    {
        final List<NameValuePair> encodedParameters = new ArrayList<NameValuePair>();
        for (final NameValuePair parameter : parameters)
        {
            final String encodedName = parameter.getName() != null ? URLEncoder.encode(parameter.getName(),
                                                                                       "UTF-8")
                                                                  : null;
            final String encodedValue = parameter.getValue() != null ? URLEncoder.encode(parameter.getValue(),
                                                                                         "UTF-8")
                                                                    : null;

            final NameValuePair encodedParameter = new NameValuePair(encodedName,
                                                                     encodedValue);

            encodedParameters.add(encodedParameter);

        }
        return encodedParameters;
    }
    private List<NameValuePair> decodeRequestParameters(final List<NameValuePair> parameters)
        throws UnsupportedEncodingException
    {
        final List<NameValuePair> decodedParameters = new ArrayList<NameValuePair>();
        for (final NameValuePair parameter : parameters)
        {
            final String decodedName = parameter.getName() != null ? URLDecoder.decode(parameter.getName(),
                                                                                       "UTF-8")
                                                                  : null;
            final String decodedValue = parameter.getValue() != null ? URLDecoder.decode(parameter.getValue(),
                                                                                         "UTF-8")
                                                                    : null;

            final NameValuePair decodedParameter = new NameValuePair(decodedName,
                                                                     decodedValue);

            decodedParameters.add(decodedParameter);

        }
        return decodedParameters;
    }
}
