package com.demandware.xlt.util;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;

import com.gargoylesoftware.htmlunit.DefaultPageCreator;
import com.gargoylesoftware.htmlunit.DefaultPageCreator.PageType;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.HTMLParser;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.gargoylesoftware.htmlunit.xml.XmlUtil;
import com.xceptance.common.util.RegExUtils;
import com.xceptance.xlt.api.engine.Session;

/**
 * <h1>XHR configuration</h1>
 * <p>
 * This class provides methods to configure and fire an XHR. To verify the result you can specify status code or simple
 * content checks. Furthermore you have the possibility to parse the response content into an HTML element.
 * </p>
 * <p>
 * By default the XHR is build for method GET and checks for a status code 200.
 * </p>
 * Defaults:
 * <table border=1>
 * <tr>
 * <td>Method</td>
 * <td>GET</td>
 * </tr>
 * <tr>
 * <td>Expected Status Code</td>
 * <td>200</td>
 * </tr>
 * </table>
 */
public class XHR
{
    /** Default status code fail message */
    public static final String DEFAULT_STATUS_CODE_FAIL_MSG = "Unexpected HTTP status code";

    /** Default content assertion fail message */
    public static final String DEFAULT_CONTENT_BLANK_FAIL_MSG = "Response content is blank.";

    /** Default expected response code: 200 */
    public static final int DEFAULT_EXPECTED_STATUS_CODE = 200;

    /**
     * Update modes
     */
    private enum UpdateMode
    {
        REPLACE, APPEND, NONE
    }

    /**
     * Content assertion modes
     */
    private enum ContentAssertion
    {
        JSON_ARRAY, JSON_OBJECT, NOT_BLANK, NONE
    }

    /** Basic URL */
    private String url;

    /** HTTP method */
    private HttpMethod method = HttpMethod.GET;

    /** Explicit URL parameters */
    private final Map<String, String> params = new HashMap<String, String>();

    /** POST parameters. Mutual exclusive to {@link #requestBody} */
    private final Map<String, String> postParams = new HashMap<String, String>();

    /** The request body. Mutual exclusive to {@link #postParams} */
    private String requestBody;
    
    /** Additional headers */
    private final Map<String, String> headers = new HashMap<String, String>();
    
    /** Headers to exclude */
    private final Set<String> delHeaders = new HashSet<String>();

    /** Expected status code. */
    private int expectedStatusCode = DEFAULT_EXPECTED_STATUS_CODE;

    /** Status code fail message */
    private String expectedStatusCodeFailMessage = DEFAULT_STATUS_CODE_FAIL_MSG;

    /** Expected content */
    private ContentAssertion contentAssertion = ContentAssertion.NONE;

    /** content assertion fail message */
    private String contentAssertionFailMessage;

    /** Desired update mode. {@value UpdateMode#NONE} by default */
    private UpdateMode updateMode = UpdateMode.NONE;

    /** HtmlElement to be updated */
    private HtmlElement container;

    /** Is the request cachable or not? XHRs are not cached by default. */
    private boolean isCached = false;

    /**
     * Set the base url
     * 
     * @param url
     * @return XHR configuration
     */
    public XHR url(final String url)
    {
        this.url = url;
        return this;
    }

    /**
     * Set request method GET
     * 
     * @return XHR configuration
     */
    public XHR GET()
    {
        this.method = HttpMethod.GET;
        return this;
    }

    /**
     * Set request method POST
     * 
     * @return XHR configuration
     */
    public XHR POST()
    {
        this.method = HttpMethod.POST;
        return this;
    }

    /**
     * Set request method PUT
     * 
     * @return XHR configuration
     */
    public XHR PUT()
    {
        this.method = HttpMethod.PUT;
        return this;
    }

    /**
     * Set request method DELETE
     * 
     * @return XHR configuration
     */
    public XHR DELETE()
    {
        this.method = HttpMethod.DELETE;
        return this;
    }

    /**
     * Add an URL parameter
     * 
     * @param key
     *            the parameter's name
     * @param value
     *            the parameter's value
     * @return XHR configuration
     */
    public XHR param(final String key, String value)
    {
        checkParameterKey(key);

        if (key == null)
        {
            Session.logEvent("Parameter value 'null' was converted into empty String for key", key);
            value = StringUtils.EMPTY;
        }

        params.put(key, value);
        return this;
    }

    /**
     * Add URL parameters
     * 
     * @param params
     *            the URL parameters given as name-value pairs
     * @return XHR configuration
     */
    public XHR params(final Map<String, String> params)
    {
        checkParameterKeys(params);

        this.params.putAll(params);

        return this;
    }

    /**
     * Remove an explicit URL parameter. This does not effect the specified URL.
     * 
     * @param key
     *            the parameter's name
     * @return XHR configuration
     */
    public XHR removeParam(final String key)
    {
        checkParameterKey(key);

        params.remove(key);

        return this;
    }

    /**
     * Remove all URL parameters. This effects the explicit URL parameters as well as the parameters of the given URL.
     * 
     * @return XHR configuration
     */
    public XHR removeParams()
    {
        params.clear();
        if (StringUtils.isNotBlank(url))
        {
            // strip query
            final String stripped = RegExUtils.getFirstMatch(url, "(^[^#]*?)\\?", 1);
            if (stripped != null)
            {
                url = stripped;
                try
                {
                    final String ref = Context.getPage().getFullyQualifiedUrl(url).getRef();
                    if (StringUtils.isNotBlank(ref))
                    {
                        url = url + "#" + ref;
                    }
                }
                catch (Exception e)
                {

                }
            }
        }
        return this;
    }

    /**
     * Add POST parameter. This implies that the request body gets dropped.
     * 
     * @param key
     *            the parameter's name
     * @param value
     *            the parameter's value
     * @return XHR configuration
     */
    public XHR postParam(final String key, String value)
    {
        checkParameterKey(key);

        if (key == null)
        {
            Session.logEvent("Parameter value 'null' was converted into empty String for key", key);
            value = StringUtils.EMPTY;
        }

        postParams.put(key, value);
        requestBody = null;

        return this;
    }

    /**
     * Add POST parameters. This implies that the request body gets dropped.
     * 
     * @param postParams
     *            the parameters given as name-value pairs
     * @return XHR configuration
     */
    public XHR postParams(final Map<String, String> postParams)
    {
        checkParameterKeys(postParams);

        // mutual exclusive post params <-> request body
        this.postParams.putAll(postParams);
        requestBody = null;

        return this;
    }

    /**
     * Remove a POST parameter
     * 
     * @param key
     *            the parameter's name
     * @return XHR configuration
     */
    public XHR removePostParam(final String key)
    {
        checkParameterKey(key);

        postParams.remove(key);

        return this;
    }

    /**
     * Remove all POST parameters.
     * 
     * @return XHR configuration
     */
    public XHR removePostParams()
    {
        postParams.clear();
        return this;
    }

    /**
     * Set the request body. This implies that the POST parameters get dropped.
     * 
     * @param requestBody
     *            the request body
     * @return XHR configuration
     */
    public XHR requestBody(final String requestBody)
    {
        this.requestBody = requestBody;
        postParams.clear();

        return this;
    }

    /**
     * Remove the request body.
     * 
     * @return XHR configuration
     */
    public XHR removeRequestBody()
    {
        this.requestBody = null;
        return this;
    }

    /**
     * Set the expected status code.<br>
     * Response code {@value #DEFAULT_EXPECTED_STATUS_CODE} doesn't need to be configured explicitly (it's the default).<br>
     * To disable status code check set it to <code>0</code> or below.
     * 
     * @param expectedStatusCode
     *            expected status code
     * @return
     */
    public XHR expectStatusCode(final int expectedStatusCode)
    {
        this.expectedStatusCode = expectedStatusCode;
        return this;
    }

    /**
     * Set the expected status code and custom assertion fail message. Response code
     * {@value #DEFAULT_EXPECTED_STATUS_CODE} doesn't need to be configured explicitly (it's the default). Default fail
     * message is: {@value #DEFAULT_STATUS_CODE_FAIL_MSG}
     * 
     * @param expectedStatusCode
     *            expected status code
     * @param failMessage
     * @return
     */
    public XHR expectStatusCode(final int expectedStatusCode, final String failMessage)
    {
        this.expectedStatusCodeFailMessage = failMessage;
        return expectStatusCode(expectedStatusCode);
    }

    /**
     * Add additional header
     * 
     * @param name
     *            header name
     * @param value
     *            header value
     * @return XHR configuration
     */
    public XHR header(final String name, final String value)
    {
        checkParameterKey(name);

        headers.put(name, value);
        delHeaders.remove(name);

        return this;
    }

    /**
     * Add additional headers
     * 
     * @param additionalHeaders
     *            given as name-value pairs
     * @return XHR configuration
     */
    public XHR headers(final Map<String, String> additionalHeaders)
    {
        checkParameterKeys(additionalHeaders);

        headers.putAll(additionalHeaders);
        delHeaders.removeAll(additionalHeaders.keySet());

        return this;
    }

    /**
     * Exclude specific header from XHR
     * 
     * @param name
     *            header name
     * @return XHR configuration
     */
    public XHR removeHeader(final String name)
    {
        headers.remove(name);
        delHeaders.add(name);

        return this;
    }

    /**
     * Exclude specific headers from XHR
     * 
     * @param headerNames
     *            header names
     * @return XHR configuration
     */
    public XHR removeHeaders(final Set<String> headerNames)
    {
        for (final String name : headerNames)
        {
            headers.remove(name);
        }

        delHeaders.addAll(headerNames);

        return this;
    }

    /**
     * Appends the response body to the given element.
     * 
     * @param parent
     *            container the response body gets parsed into
     * @return XHR configuration
     */
    public XHR appendTo(final HtmlElement parent)
    {
        updateMode = UpdateMode.APPEND;
        container = parent;
        
        return this;
    }

    /**
     * Replace the content of the given element with the response body.
     * 
     * @param parent
     *            container the response body gets parsed into (previous children will be removed)
     * @return XHR configuration
     */
    public XHR replaceContentOf(final HtmlElement parent)
    {
        updateMode = UpdateMode.REPLACE;
        container = parent;
        
        return this;
    }

    /**
     * Expect a JSON array as response content
     * 
     * @return XHR configuration
     */
    public XHR expectJsonArray()
    {
        return expectJsonArray(null);
    }

    /**
     * Expect a JSON array as response content
     * 
     * @param contentAssertionFailMessage
     *            if assertion fails then fail with this message
     * @return XHR configuration
     */
    public XHR expectJsonArray(final String contentAssertionFailMessage)
    {
        return expectContent(ContentAssertion.JSON_ARRAY, contentAssertionFailMessage);
    }

    /**
     * Expect a JSON object as response content
     * 
     * @return XHR configuration
     */
    public XHR expectJsonObject()
    {
        return expectJsonObject(null);
    }

    /**
     * Expect a JSON object as response content
     * 
     * @param contentAssertionFailMessage
     *            if assertion fails then fail with this message
     * @return XHR configuration
     */
    public XHR expectJsonObject(final String contentAssertionFailMessage)
    {
        return expectContent(ContentAssertion.JSON_OBJECT, contentAssertionFailMessage);
    }

    /**
     * Expect a non-blank response content
     * 
     * @return XHR configuration
     */
    public XHR expectNotBlank()
    {
        return expectNotBlank(null);
    }

    /**
     * Expect a non-blank response content
     * 
     * @param contentAssertionFailMessage
     *            if assertion fails then fail with this message
     * @return XHR configuration
     */
    public XHR expectNotBlank(final String contentAssertionFailMessage)
    {
        return expectContent(ContentAssertion.NOT_BLANK, contentAssertionFailMessage);
    }

    /**
     * Remove content assertions if any
     * 
     * @return XHR configuration
     */
    public XHR expectNothing()
    {
        return expectStatusCode(-1).expectContent(ContentAssertion.NONE, null);
    }

    private XHR expectContent(final ContentAssertion contentAssertion, final String contentAssertionFailMessage)
    {
        this.contentAssertion = contentAssertion;
        this.contentAssertionFailMessage = contentAssertionFailMessage;
        return this;
    }

    /**
     * Set the caching mode. By default XHR responses are not cached.
     * 
     * @param isCached
     *            <code>true</code> if the XHR response should be cached
     * @return XHR configuration
     */
    public XHR cached(final boolean isCached)
    {
        this.isCached = isCached;
        return this;
    }

    /**
     * Get the URL string
     * 
     * @return URL string
     */
    public String getUrl()
    {
        return url;
    }

    /**
     * Get the HTTP method
     * 
     * @return HTTP method
     */
    public HttpMethod getMethod()
    {
        return method;
    }

    /**
     * Get the explicit URL parameters
     * 
     * @return explicit URL parameters
     */
    public Map<String, String> getParams()
    {
        return params;
    }

    /**
     * Get the POST parameters. Please notice that the POST parameters is mutual exclusive to the request body.
     * 
     * @return POST parameters
     */
    public Map<String, String> getPostParams()
    {
        return postParams;
    }

    /**
     * Get the request body. Please notice that the request body is mutual exclusive to the POST parameters.
     * 
     * @return request body
     */
    public String getRequestBody()
    {
        return requestBody;
    }

    /**
     * Get the expected response code. If no response code is set explicitly the default is
     * {@value #DEFAULT_EXPECTED_STATUS_CODE}.
     * 
     * @return expected response code
     */
    public int getExpectedStatusCode()
    {
        return expectedStatusCode;
    }

    /**
     * Get the fail message. This is the assertion message when the response code validation fails. The default fail
     * message is: {@value #DEFAULT_STATUS_CODE_FAIL_MSG}
     * 
     * @return fail message
     */
    public String getExpectedStatusCodeFailMessage()
    {
        return expectedStatusCodeFailMessage;
    }

    /**
     * Get the additional headers
     * 
     * @return additional headers
     */
    public Map<String, String> getAdditionalHeaders()
    {
        return headers;
    }

    /**
     * Get the header exclusions
     * 
     * @return header exclusions
     */
    public Set<String> getHeaderExclusions()
    {
        return delHeaders;
    }

    /**
     * Performs an XHR call for the configured XHR based on the current page.
     * 
     * @return received response
     * @throws Exception
     */
    public WebResponse fire() throws Exception
    {
        return fireFrom(Context.getPage());
    }

    /**
     * Get caching mode.
     * 
     * @return <code>true</code> if XHR response will be cached, <code>false</code> otherwise
     */
    public boolean isCached()
    {
        return isCached;
    }

    /**
     * Performs an XHR call for the configured XHR based on given page.
     * 
     * @param page
     *            the current page
     * @return received response
     * @throws Exception
     */
    public WebResponse fireFrom(final HtmlPage page) throws Exception
    {
        // Often an URL string is (relative or absolute) not full qualified (for
        // example '/foo/bar.html'). So a full qualified URL is build first.
        URL pageURL = page.getFullyQualifiedUrl(getUrl());

        // Append additional URL parameters if necessary
        final Map<String, String> params = getParams();
        if (!params.isEmpty())
        {
            String requestUrlString = pageURL.toExternalForm();
            requestUrlString += (pageURL.getQuery() == null ? "?" : "&") + AjaxUtils.paramsToQueryString(params);
            pageURL = page.getFullyQualifiedUrl(requestUrlString);
        }

        // Create the basic request
        final HttpMethod method = getMethod();
        final WebRequest request = new WebRequest(pageURL, method);

        // charset
        final String charset = getContentCharset(page);
        request.setCharset(charset);

        // Add either POST parameters or request body
        if (HttpMethod.POST.equals(method))
        {
            final String requestBody = getRequestBody();
            if (requestBody != null)
            {
                request.setRequestBody(getRequestBody());
            }
            else
            {
                request.setRequestParameters(toNameValuePairs(getPostParams()));
            }
        }

        // Cache
        if (!isCached())
        {
            // If XHR caching is disabled (DEFAULT) set the internal XHR flag.
            request.setXHR();
        }

        // XHR request have an additional header.
        request.getAdditionalHeaders().put("X-Requested-With", "XMLHttpRequest");
        request.getAdditionalHeaders().put("Referer", page.getUrl().toExternalForm());

        // add additional configured headers
        request.getAdditionalHeaders().putAll(getAdditionalHeaders());

        // remove unwanted headers
        for (final String headerName : getHeaderExclusions())
        {
            request.getAdditionalHeaders().remove(headerName);
        }

        // Perform the call
        final WebResponse response = page.getWebClient().loadWebResponse(request);

        // check expected status code
        final int expectedStatusCode = getExpectedStatusCode();
        if (expectedStatusCode > 0)
        {
            Assert.assertEquals(getExpectedStatusCodeFailMessage(), expectedStatusCode, response.getStatusCode());
        }

        // check for expected content type if specified
        if (!ContentAssertion.NONE.equals(contentAssertion))
        {
            final String responseContent = response.getContentAsString();
            switch (contentAssertion)
            {
                case JSON_OBJECT:
                    try
                    {
                        new JSONObject(responseContent);
                    }
                    catch (final JSONException e)
                    {
                        Assert.fail(getContentCheckFailMessage(e.getMessage()));
                    }
                    break;

                case JSON_ARRAY:
                    try
                    {
                        new JSONArray(responseContent);
                    }
                    catch (final JSONException e)
                    {
                        Assert.fail(getContentCheckFailMessage(e.getMessage()));
                    }
                    break;

                case NOT_BLANK:
                    Assert.assertTrue(getContentCheckFailMessage(DEFAULT_CONTENT_BLANK_FAIL_MSG), StringUtils.isNotBlank(responseContent));
                    break;

                default:
                    throw new NoSuchElementException("Unsupported content type check.");
            }
        }

        // update page if necessary
        if (!UpdateMode.NONE.equals(updateMode))
        {
            if (UpdateMode.REPLACE.equals(updateMode))
            {
                container.removeAllChildren();
            }

            HTMLParser.parseFragment(container, response.getContentAsString());
        }

        return response;
    }

    /**
     * Check the key is not blank
     * 
     * @param key
     */
    private void checkParameterKey(final String key)
    {
        if (StringUtils.isBlank(key))
        {
            throw new IllegalArgumentException("Key must not be 'null'.");
        }
    }

    /**
     * Check there's no blank key
     * 
     * @param params
     */
    private void checkParameterKeys(final Map<String, String> params)
    {
        // check not null
        if (params == null)
        {
            throw new IllegalArgumentException("Collection key must not be 'null'.");
        }

        // check keys
        for (final String key : params.keySet())
        {
            checkParameterKey(key);
        }
    }

    /**
     * Get custom fail message or the given default if no custom message is set.
     * 
     * @param defaultMessage
     * @return fail message
     */
    private String getContentCheckFailMessage(final String defaultMessage)
    {
        return StringUtils.isNotBlank(contentAssertionFailMessage) ? contentAssertionFailMessage : defaultMessage;
    }

    /**
     * Convert parameter map to list of NameValuePairs
     * 
     * @param params
     * @return converted parameter collection
     */
    private static List<NameValuePair> toNameValuePairs(final Map<String, String> params)
    {
        final List<NameValuePair> converted = new ArrayList<NameValuePair>();
        for (final Map.Entry<String, String> param : params.entrySet())
        {
            converted.add(new NameValuePair(param.getKey(), param.getValue()));
        }
        return converted;
    }

    /**
     * Determine content charset of given page.
     * 
     * @param page
     * @return
     */
    private static String getContentCharset(final HtmlPage page)
    {
        final WebResponse r = page.getWebResponse();
        String charset = r.getContentCharsetOrNull();
        if (charset == null)
        {
            final String contentType = r.getContentType();

            // xml pages are using a different content type
            if (null != contentType
                && PageType.XML == DefaultPageCreator.determinePageType(contentType))
            {
                return XmlUtil.DEFAULT_CHARSET;
            }

            charset = r.getWebRequest().getCharset();
        }

        if (charset == null)
        {
            charset = "UTF-8";
        }

        return charset;
    }
}
