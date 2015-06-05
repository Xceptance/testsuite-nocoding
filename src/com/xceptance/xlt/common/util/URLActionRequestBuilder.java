package com.xceptance.xlt.common.util;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.gargoylesoftware.htmlunit.util.UrlUtils;
import com.xceptance.common.lang.StringUtils;

public class URLActionRequestBuilder
{
    public URLActionRequestBuilder()
    {
    }

    public WebRequest buildRequest(final URLAction action)
    {
        final WebRequest resultRequest;

        try
        {
            resultRequest = createWebRequestFromUrl(action.getUrl());
            fillWebRequestWithMethod(resultRequest, action.getMethod());
            if (action.hasBody())
            {
                fillWebRequestWithBody(resultRequest, action.getBody());
            }
            fillWebRequestWithHeaders(resultRequest, action.getHeaders());
            fillWebRequestWithParameters(resultRequest, action.getParameters(),
                                         action.isParametersEncoded(), action.getMethod());
        }
        catch (final Exception e)
        {
            throw new IllegalArgumentException("Failed to create WebRequest for action: "
                                               + action.getName() + e.getMessage(), e);
        }

        return resultRequest;
    }

    public WebRequest buildXhrRequest(final URLAction action, final URL refererUrl)
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

    private void fillWebRequestWithMethod(final WebRequest request,
                                          final HttpMethod method)
    {
        request.setHttpMethod(method);
    }

    private WebRequest createWebRequestFromUrl(final URL url)
    {
        return new WebRequest(url);
    }

    private void fillWebRequestWithBody(final WebRequest request, final String Body)
    {
        if(request.getHttpMethod().equals(HttpMethod.POST) || request.getHttpMethod().equals(HttpMethod.PUT) ){
            request.setRequestBody(Body);
        }
    }

    private void fillWebRequestWithParameters(final WebRequest request,
                                              List<NameValuePair> parameters,
                                              final boolean encoded,
                                              final HttpMethod httpMethod)
        throws UnsupportedEncodingException, MalformedURLException
    {
        if (!parameters.isEmpty())
        {
            if (encoded)
            {
                parameters = decodeRequestParameters(parameters);
            }
            if (!httpMethod.equals(HttpMethod.POST) || request.getRequestBody() != null)
            {
                addParametersToUrl(request, parameters);
            }
            else
            {
                request.setRequestParameters(parameters);
            }
        }

    }

    private void addParametersToUrl(final WebRequest request,
                                    final List<NameValuePair> parameters) throws MalformedURLException
    {
        String urlString = request.getUrl().toString();
        urlString = StringUtils.replace(urlString, "&amp;", "&");
        final URL newUrl = new URL(urlString);
        final String oldQueryString = newUrl.getQuery();
        final StringBuilder newQueryString = new StringBuilder();
        
        final boolean insertLeadingAmp = oldQueryString != null && !oldQueryString.isEmpty();
        
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
            newQueryString.append(nameValuePair.getName()).append('=').append(nameValuePair.getValue());
        }
        
        final URL  newNewUrl = UrlUtils.getUrlWithNewQuery(newUrl, newQueryString.toString());
        request.setUrl(newNewUrl);
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
