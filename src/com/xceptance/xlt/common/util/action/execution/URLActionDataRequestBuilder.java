package com.xceptance.xlt.common.util.action.execution;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.auth.Credentials;

import com.gargoylesoftware.htmlunit.FormEncodingType;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.gargoylesoftware.htmlunit.util.UrlUtils;
import com.xceptance.common.lang.StringUtils;
import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.common.util.ParameterUtils;
import com.xceptance.xlt.common.util.action.data.URLActionData;

/**
 * Builder <br>
 * Takes an {@link URLActionData} object and builds a {@link WebRequest}. <br>
 * two alternatives that only differ in the headers:
 * <ul>
 * <li>General WebRequest: -> {@link #buildRequest(URLActionData)}
 * <li>XmlHttpRequest: -> {@link #buildXhrRequest(URLActionData, URL)}
 * </ul>
 * 
 * @author matthias mitterreiter
 */
public class URLActionDataRequestBuilder
{
    public URLActionDataRequestBuilder()
    {
        XltLogger.runTimeLogger.debug("Creating new Instance");
    }

    /**
     * Builds a general WebRequest with the information from action. <br>
     * If you want some additional XmlHttpRequest Headers, <br>
     * use {@link #buildXhrRequest(URLActionData, URL)} instead.
     * 
     * @param action
     *            {@link URLActionData}
     * @return {@link WebRequest}
     * @throws IllegalArgumentException
     *             if failed to create a WebRequest
     */
    public WebRequest buildRequest(final URLActionData action)
    {
        ParameterUtils.isNotNull(action, "URLAction");

        XltLogger.runTimeLogger.debug("Building WebRequest for action: '"
                                      + action.getName() + "'");

        final WebRequest resultRequest;

        try
        {
            resultRequest = createWebRequestFromURLAction(action);
        }
        catch (final Exception e)
        {
            throw new IllegalArgumentException("Failed to create WebRequest for action: "
                                                   + action.getName()
                                                   + " Reason: "
                                                   + e.getMessage(),
                                               e);
        }
        logWebRequest(resultRequest);
        return resultRequest;
    }

    private WebRequest createWebRequestFromURLAction(final URLActionData action)
        throws MalformedURLException, UnsupportedEncodingException
    {

        final WebRequest resultRequest;

        resultRequest = createWebRequestFromUrl(action.getUrl());
        fillWebRequestWithMethod(resultRequest, action.getMethod());
        if (action.hasBody())
        {
            fillWebRequestWithBody(resultRequest,
                                   action.getBody(),
                                   action.encodeBody());
        }
        fillWebRequestWithHeaders(resultRequest, action.getHeaders());
        fillWebRequestWithCookies(resultRequest, action.getCookies());
        fillWebRequestWithParameters(resultRequest,
                                     action.getParameters(),
                                     action.encodeParameters(),
                                     action.getMethod());
        return resultRequest;
    }

    /**
     * Builds a WebRequest with the information from action.<br>
     * Additionally adds some XmlHttpRequest Headers.
     * 
     * @param action
     *            {@link URLActionData}
     * @param refererUrl
     *            the url, the request refers to.
     * @return {@link WebRequest}
     * @throws IllegalArgumentException
     *             if failed to create a WebRequest
     */
    public WebRequest buildXhrRequest(final URLActionData action,
                                      final URL refererUrl)
    {
        XltLogger.runTimeLogger.debug("Building XhrWebRequest for action: "
                                      + action.getName());

        WebRequest resultXhrRequest = null;
        try
        {
            resultXhrRequest = buildXhrRequestFromURLActionData(action,
                                                                refererUrl);
        }
        catch (final Exception e)
        {
            throw new IllegalArgumentException("Failed to create XhrWebRequest for action: "
                                                   + action.getName()
                                                   + e.getMessage(),
                                               e);
        }
        return resultXhrRequest;
    }

    private WebRequest buildXhrRequestFromURLActionData(final URLActionData action,
                                                        final URL refererUrl)
        throws UnsupportedEncodingException, MalformedURLException
    {
        final WebRequest resultXhrRequest = createWebRequestFromUrl(action.getUrl());
        fillWebRequestWithMethod(resultXhrRequest, action.getMethod());
        if (action.hasBody())
        {
            fillWebRequestWithBody(resultXhrRequest,
                                   action.getBody(),
                                   action.encodeBody());
        }
        fillWebRequestWithHeaders(resultXhrRequest, action.getHeaders());
        resultXhrRequest.setAdditionalHeader("X-Requested-With",
                                             "XMLHttpRequest");
        resultXhrRequest.setAdditionalHeader("Referer",
                                             refererUrl.toExternalForm());
        fillWebRequestWithCookies(resultXhrRequest, action.getCookies());
        fillWebRequestWithParameters(resultXhrRequest,
                                     action.getParameters(),
                                     action.encodeParameters(),
                                     action.getMethod());
        logWebRequest(resultXhrRequest);
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
                cookieString = cookieString + cookie.getName() + "="
                               + cookie.getValue() + ";";
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
        if (!encodeBody)
        {
            resultBody = decodeRequestBody(body);
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
            if (!encodeParameters)
            {
                resultParameters = decodeRequestParameters(resultParameters);
            }
            if (!httpMethod.equals(HttpMethod.POST)
                || request.getRequestBody() != null)
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
             * For each pair except the first, an & has to be appended. For the first pair it depends
             *  on whether there is already a query (in which case insertLeadingAmp is true).
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

    /**
     * For debugging purpose. <br>
     * 'err-streams' the attributes of the WebRequest. <br>
     * 
     * @param request
     *            The {@link WebRequest} you want to err stream.
     */
    public void logWebRequest(final WebRequest request)
    {
        XltLogger.runTimeLogger.debug("------------Webrequest------------");
        final String url = request.getUrl().toString();
        if (url != null)
        {
            XltLogger.runTimeLogger.debug("URL: " + url);
        }
        final HttpMethod method = request.getHttpMethod();
        if (method != null)
        {
            XltLogger.runTimeLogger.debug("Method : " + method);
        }
        final String charSet = request.getCharset();
        if (charSet != null)
        {
            XltLogger.runTimeLogger.debug("Chraset: " + charSet);
        }
        final String proxy = request.getProxyHost();
        if (proxy != null)
        {
            XltLogger.runTimeLogger.debug("Proxy: " + proxy);
        }
        final String body = request.getRequestBody();
        if (body != null)
        {
            XltLogger.runTimeLogger.debug("Body: " + body);
        }
        final FormEncodingType encoding = request.getEncodingType();
        if (encoding != null)
        {
            final String encodingString = encoding.toString();
            XltLogger.runTimeLogger.debug("Encoding: " + encodingString);
        }
        final URL originalUrl = request.getOriginalURL();
        if (originalUrl != null)
        {
            final String originalUrlString = originalUrl.toString();
            XltLogger.runTimeLogger.debug("OriginalUrl: " + originalUrlString);
        }
        final List<NameValuePair> parameters = request.getRequestParameters();
        if (!(parameters == null) && !(parameters.isEmpty()))
        {
            XltLogger.runTimeLogger.debug("Parameters: ");
            for (final NameValuePair parameter : parameters)
            {
                XltLogger.runTimeLogger.debug("\t" + parameter.getName()
                                              + " = " + parameter.getValue());
            }
        }
        final Map<String, String> headers = request.getAdditionalHeaders();
        if (!(headers == null))
        {
            XltLogger.runTimeLogger.debug("Headers: ");
            for (final Map.Entry<String, String> entry : headers.entrySet())
            {
                XltLogger.runTimeLogger.debug("\t" + entry.getKey() + " : "
                                              + entry.getValue());
            }
        }

        final Credentials credentials = request.getCredentials();
        if (credentials != null)
        {
            XltLogger.runTimeLogger.debug("Credentials: "
                                          + credentials.toString());
        }

    }
}
