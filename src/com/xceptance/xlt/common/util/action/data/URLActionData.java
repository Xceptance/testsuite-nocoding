package com.xceptance.xlt.common.util.action.data;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jdt.annotation.Nullable;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.api.validators.HttpResponseCodeValidator;
import com.xceptance.xlt.common.util.bsh.ParameterInterpreter;

/**
 * <p>
 * Data Container which holds all necessary information to create and validate a single <b>http request</b>.
 * </p>
 * <ul>
 * <li>Supports automatic & dynamic parameter interpretation via {@link ParameterInterpreter}. <br>
 * </li>
 * <li>Attributes are of type String to allow parameter interpretation and may be parsed to the intended type when they are accessed.</li>
 * <li>Defaults common used Http request values, if not set explicitly.</li>
 * <li>Holds a list of {@link #validations response validations}.</li>
 * <li>Holds a list of {@link #store variables} which should be taken out of the response for dynamic parameter
 * interpretation.</li>
 * </ul>
 * 
 * @author Matthias Mitterreiter
 */

public class URLActionData
{

    /**
     * Http request name.
     */
    private String name;

    /**
     * <p>
     * Type of request. <br>
     * Default: {@link #TYPE_ACTION 'A'}. <br>
     * </p>
     * See {@link #PERMITTEDTYPES permitted request types}.
     */
    private String type;

    /**
     * Intended type: {@link URL} <br>
     * The url used for the request. <br>
     * May be completed with {@link #parameters query parameters}.
     */
    private String url;

    /**
     * <p>
     * Http request method. <br>
     * Default : 'GET'. <br>
     * </p>
     * See {@link #PERMITTEDMETHODS permitted methods}.
     */
    private String method;

    /**
     * <p>
     * Intended type: Boolean <br>
     * Default: 'false'. <br>
     * </p>
     * <p>
     * Allows further encoding of request {@link #parameters} with the declared {@link #encodingType encoding type}.
     * </p>
     */
    private String encodeParameters;

    /**
     * <p>
     * Intended type: Boolean <br>
     * Default: 'false'. <br>
     * </p>
     * <p>
     * Allows further encoding of request {@link #body} with the declared {@link #encodingType encoding type}.
     * </p>
     */
    private String encodeBody;

    /**
     * Intended type: Integer <br>
     * Expected HttpResponseCode. <br>
     * Default : '200'.
     */
    private String httpResponceCode;

    /**
     * <p>
     * Request Body.
     * </p>
     * May be {@link #encodeBody encoded}.
     */
    private String body;

    /**
     * Not implemented yet
     */
    private String encodingType;

    /**
     * Data to validate the Http response.
     */
    private List<URLActionDataValidation> validations = Collections.emptyList();

    /**
     * Data, which should be taken out of the Http response and stored for dynamic parameter interpretation.
     */
    private List<URLActionDataStore> store = Collections.emptyList();

    /**
     * Request parameters. <br>
     * Depends on the {@link #method method}, whether they are GET or POST parameters.
     */
    private List<NameValuePair> parameters = Collections.emptyList();

    /**
     * Additional data for the "Cookie" request header field. <br>
     * Not implemented yet.
     */
    private List<NameValuePair> cookies = Collections.emptyList();

    /**
     * Request headers.
     */
    private List<NameValuePair> headers = Collections.emptyList();

    /**
     * {@link ParameterInterpreter}.
     */
    private ParameterInterpreter interpreter;

    /**
     * <p>
     * Permitted types of requests:
     * <ul>
     * <li> {@link #TYPE_ACTION Action}</li>
     * <li> {@link #TYPE_STATIC Static action}</li>
     * <li> {@link #TYPE_XHR Xhr action}</li>
     * <ul>
     * </p>
     * <p>
     * Different types allow a different handling of the request as well as the response.
     * </p>
     */
    public final static Set<String> PERMITTEDTYPES = new HashSet<String>();

    /**
     * Supported request methods:
     * <ul>
     * <li> {@link #METHOD_GET GET}</li>
     * <li> {@link #METHOD_POST POST}</li>
     * <li> {@link #METHOD_PUT PUT}</li>
     * <li> {@link #METHOD_DELETE DELETE}</li>
     * <li> {@link #METHOD_HEAD HEAD}</li>
     * <li> {@link #METHOD_TRACE TRACE}</li>
     * <li> {@link #METHOD_OPTIONS OPTIONS}</li>
     * <li> {@link #METHOD_CONNECT CONNECT}</li>
     * <ul>
     */
    public final static Set<String> PERMITTEDMETHODS = new HashSet<String>();

    /**
     * Type for a general Http Request.
     */
    public static final String TYPE_ACTION = "A";

    /**
     * Type for a Http requests, that loads static content. <br> 
     */
    public static final String TYPE_STATIC = "S";

    /**
     * Type for a XMLHttpRequest
     */
    public static final String TYPE_XHR = "Xhr";

    public static final String METHOD_POST = "POST";

    public static final String METHOD_GET = "GET";

    public static final String METHOD_PUT = "PUT";

    public static final String METHOD_DELETE = "DELETE";

    public static final String METHOD_HEAD = "HEAD";

    public static final String METHOD_TRACE = "TRACE";

    public static final String METHOD_OPTIONS = "OPTIONS";

    static
    {
        PERMITTEDTYPES.add(TYPE_ACTION);
        PERMITTEDTYPES.add(TYPE_XHR);
        PERMITTEDTYPES.add(TYPE_STATIC);

        PERMITTEDMETHODS.add(METHOD_GET);
        PERMITTEDMETHODS.add(METHOD_POST);
        PERMITTEDMETHODS.add(METHOD_DELETE);
        PERMITTEDMETHODS.add(METHOD_PUT);
        PERMITTEDMETHODS.add(METHOD_HEAD);
        PERMITTEDMETHODS.add(METHOD_TRACE);
        PERMITTEDMETHODS.add(METHOD_OPTIONS);
    }

    /**
     * Takes the minimal set of parameters that are necessary to crate a http request. <br>
     * Defaults the rest of the attributes. <br>
     * 
     * @param name
     *            : {@link #name}
     * @param url
     *            : {@link #url}
     * @param interpreter
     *            : {@link #interpreter}
     */
    public URLActionData(final String name,
                         final String url,
                         final ParameterInterpreter interpreter)
    {
        XltLogger.runTimeLogger.debug("Ceating new Instance ");
        setName(name);
        setUrl(url);
        setType(TYPE_ACTION); // default
        setMethod(METHOD_GET); // default
        setEncodeParameters("false"); // default
        setEncodeBody("false"); // default
        setHttpResponceCode("200"); // default
        setInterpreter(interpreter);
    }

    /**
     * For debugging purpose. <br>
     * 'err-streams' the attributes of the object. <br>
     */
    public void outline()
    {
        try
        {
            System.err.println("Action: " + getName());
            System.err.println("\t" + "Is Action: " + isAction());
            System.err.println("\t" + "Is Static Content: " + isStaticContent());
            System.err.println("\t" + "Is Xhr: " + isXHRAction());
            System.err.println("\t" + "Method: " + getMethod());
            System.err.println("\t" + "URL: " + getUrl().toString());
            System.err.println("\t" + "Encode-Parameters: "
                               + encodeParameters());
            System.err.println("\t" + "Encode-Body: " + encodeBody());
            System.err.println("\t"
                               + "HttpCode: "
                               + getResponseCodeValidator().getHttpResponseCode());
            if (body != null)
            {
                System.err.println("\t" + "Body: " + getBody());
            }
            if (!parameters.isEmpty())
            {
                final List<NameValuePair> parameters = getParameters();
                System.err.println("\t" + "Parameters: ");
                for (final NameValuePair nvp : parameters)
                {
                    System.err.println("\t\t" + nvp.getName() + " : "
                                       + nvp.getValue());
                }
            }
            if (!headers.isEmpty())
            {
                System.err.println("\t" + "Headers: ");
                final List<NameValuePair> headers = getHeaders();
                for (final NameValuePair nvp : headers)
                {
                    System.err.println("\t\t" + nvp.getName() + " : "
                                       + nvp.getValue());
                }
            }
            if (!cookies.isEmpty())
            {
                System.err.println("\t" + "Cookies: ");
                final List<NameValuePair> cookies = getCookies();
                for (final NameValuePair nvp : cookies)
                {
                    System.err.println("\t\t" + nvp.getName() + " : "
                                       + nvp.getValue());
                }
            }
            if (!validations.isEmpty())
            {
                System.err.println("\t" + "Validations: ");
                for (final URLActionDataValidation v : validations)
                {
                    v.outline();
                }
            }
            if (!store.isEmpty())
            {
                System.err.println("\tStore:");
                for (final URLActionDataStore storeItem : store)
                {
                    storeItem.outline();
                }
            }
        }
        catch (final Exception e)
        {
            e.printStackTrace();
        }

    }

    /**
     * Sets if NOT null.
     * 
     * @param httpResponceCode
     */
    public void setHttpResponceCode(final String httpResponceCode)
    {
        if (httpResponceCode != null)
        {
            this.httpResponceCode = httpResponceCode;
            XltLogger.runTimeLogger.debug(getSetTagToValueMessage("HttpResponceCode",
                                                                  httpResponceCode));
        }
    }

    /**
     * Sets if NOT null.
     * 
     * @param httpResponceCode
     */
    public void setHttpResponceCode(final Integer httpResponceCode)
    {
        if (httpResponceCode != null)
        {
            this.httpResponceCode = httpResponceCode.toString();
            XltLogger.runTimeLogger.debug(getSetTagToValueMessage("HttpResponceCode",
                                                                  this.httpResponceCode));
        }
    }

    /**
     * Sets if NOT null, otherwise THROWS.
     * 
     * @throws IllegalArgumentException
     * @param url
     */
    public void setUrl(final String url)
    {
        this.url = (url != null) ? url
                                : (String) throwIllegalArgumentException("'Url' cannot be null");
        XltLogger.runTimeLogger.debug(getSetTagToValueMessage("URL", this.url));
    }

    /**
     * Sets if NOT null.
     * 
     * @param method
     */
    public void setMethod(final String method)
    {
        if (method != null)
        {
            this.method = method;
            XltLogger.runTimeLogger.debug(getSetTagToValueMessage("Method",
                                                                  this.method));
        }
    }

    /**
     * Sets if NOT null.
     * 
     * @param encoded
     */
    public void setEncodeParameters(final String encoded)
    {
        if (encoded != null)
        {
            this.encodeParameters = encoded;
            XltLogger.runTimeLogger.debug(getSetTagToValueMessage("encodedParameters",
                                                                  this.encodeParameters));
        }
    }

    /**
     * Sets if NOT null.
     * 
     * @param encoded
     */
    public void setEncodeParameters(final Boolean encoded)
    {
        if (encoded != null)
        {
            this.encodeParameters = encoded.toString();
            XltLogger.runTimeLogger.debug(getSetTagToValueMessage("encodedParameters",
                                                                  this.encodeParameters));
        }
    }

    /**
     * Sets if NOT null.
     * 
     * @param encoded
     */
    public void setEncodeBody(final String encoded)
    {
        if (encoded != null)
        {
            this.encodeBody = encoded;
            XltLogger.runTimeLogger.debug(getSetTagToValueMessage("encodedBody",
                                                                  this.encodeBody));
        }
    }

    /**
     * Sets if NOT null.
     * 
     * @param encoded
     */

    public void setEncodeBody(final Boolean encoded)
    {
        if (encoded != null)
        {
            this.encodeBody = encoded.toString();
            XltLogger.runTimeLogger.debug(getSetTagToValueMessage("encodedBody",
                                                                  this.encodeBody));
        }
    }

    /**
     * Sets if NOT null.
     * 
     * @param type
     */
    public void setType(final String type)
    {
        if (type != null)
        {
            this.type = type;
            XltLogger.runTimeLogger.debug(getSetTagToValueMessage("Type",
                                                                  this.type));
        }
    }

    /**
     * Sets if NOT null, otherwise THROWS.
     * 
     * @param name
     * @throws IllegalArgumentException.
     */
    public void setName(final String name)
    {
        this.name = ((name != null) ? name
                                   : (String) throwIllegalArgumentException("Name' cannot be null"));
        XltLogger.runTimeLogger.debug(MessageFormat.format("Set Action 'Name' to \"{0}\"",
                                                           this.name));

    }

    /**
     * Sets if NOT null, otherwise THROWS.
     * 
     * @param interpreter
     * @throws IllegalArgumentException
     */
    private void setInterpreter(final ParameterInterpreter interpreter)
    {

        this.interpreter = (interpreter != null) ? interpreter
                                                : (ParameterInterpreter) throwIllegalArgumentException(" 'ParameterInterpreter' cannot be null");
        XltLogger.runTimeLogger.debug(getSetNewTagMessage("interpreter"));

    }

    /**
     * Sets if NOT null.
     * @param validations
     */
    public void setValidations(final List<URLActionDataValidation> validations)
    {
        if (validations != null)
        {
            this.validations = validations;
            XltLogger.runTimeLogger.debug(getSetNewTagMessage("validations"));
        }
    }

    /**
     * Sets if NOT null.
     * @param headers
     */
    public void setHeaders(final List<NameValuePair> headers)
    {
        if (headers != null)
        {
            this.headers = headers;
            XltLogger.runTimeLogger.debug(getSetNewTagMessage("headers"));
        }
    }
    /**
     * Sets if NOT null.
     * @param store
     */
    public void setStore(final List<URLActionDataStore> store)
    {
        if (store != null)
        {
            this.store = store;
            XltLogger.runTimeLogger.debug(getSetNewTagMessage("store"));
        }
    }

    /**
     * Sets if NOT null.
     * @param parameters
     */
    public void setParameters(final List<NameValuePair> parameters)
    {
        if (parameters != null)
        {
            this.parameters = parameters;
            XltLogger.runTimeLogger.debug(getSetNewTagMessage("parameters"));
        }
    }
    /**
     * Sets if NOT null.
     * @param cookies
     */
    public void setCookies(final List<NameValuePair> cookies)
    {
        if (cookies != null)
        {
            this.cookies = cookies;
            XltLogger.runTimeLogger.debug(getSetNewTagMessage("cookies"));
        }
    }
    /**
     * Sets if NOT null.
     * @param body
     */
    public void setBody(final String body)
    {
        if (body != null)
        {
            this.body = body;
            XltLogger.runTimeLogger.debug(getSetTagToValueMessage("body",
                                                                  this.body));
        }
    }

    /**
     * @return {@link #body}, after its dynamic interpretation via {@link #interpreter}.
     */
    @Nullable
    public String getBody()
    {
        return interpreter.processDynamicData(this.body);
    }

    /**
     * 
     * @return  The {@link #type type of request}. <br>
     *  if type is unknown, it returns {@link #TYPE_ACTION}. 
     */
    public String getType()
    {
        String result;
        if (isStaticContent())
        {
            result = TYPE_STATIC;
        }
        else if (isXHRAction())
        {
            result = TYPE_XHR;
        }
        else
        {
            result = TYPE_ACTION;
        }
        return result;
    }
    
    /**
     * 
     * @return {@link #url},  after its dynamic interpretation via {@link #interpreter}.
     */
    public URL getUrl()
    {
        try
        {
            return new URL(interpreter.processDynamicData(url));
        }
        catch (final MalformedURLException e)
        {
            throw new IllegalArgumentException("Malformed URL for action"
                                               + getName() + ": "
                                               + e.getMessage(), e);
        }
    }

    /**
     * @return {@link #url},  after its dynamic interpretation via {@link #interpreter}.
     */
    public String getUrlString()
    {
        return getUrl().toString();
    }

    /**
     * @return {@link #httpResponceCode}, after its dynamic interpretation via {@link #interpreter}.
     */
    public HttpResponseCodeValidator getResponseCodeValidator()
    {
        final String dynmaicResponseCode = interpreter.processDynamicData(this.httpResponceCode);
        final HttpResponseCodeValidator result = StringUtils.isNotBlank(dynmaicResponseCode) ? new HttpResponseCodeValidator(Integer.parseInt(dynmaicResponseCode))
                                                                                            : HttpResponseCodeValidator.getInstance();
        return result;
    }

    /**
     * @return {@link #httpResponceCode}, after its dynamic interpretation via {@link #interpreter}.
     */
    public Integer getHttpResponseCode()
    {
        final String dynmaicResponseCode = interpreter.processDynamicData(this.httpResponceCode);
        final Integer resultInteger = dynmaicResponseCode != null ? Integer.parseInt(dynmaicResponseCode)
                                                                 : 200;
        return resultInteger;
    }

    /**
     * @returns {@link #method}, after its dynamic interpretation via {@link #interpreter}.
     * If the value of {@link #method} is unsupported, it gets defaulted to {@link #METHOD_GET GET}.
     */
    public HttpMethod getMethod()
    {
        HttpMethod result = HttpMethod.GET;

        final String dynamicMethod = interpreter.processDynamicData(this.method);

        result = selectMethodFromDynamicData(dynamicMethod);

        return result;
    }

    /**
     * 
     * @param dynamicMethod
     * @return {@link #method} after its dynamic interpretation via {@link #interpreter}.
     * If its value is unsupported, {@link #method} gets defaulted to {@link #METHOD_GET GET}.
     */
    private HttpMethod selectMethodFromDynamicData(final String dynamicMethod)
    {
        HttpMethod result;
        switch (dynamicMethod)
        {
            case METHOD_GET:
                result = HttpMethod.GET;
                break;
            case METHOD_POST:
                result = HttpMethod.POST;
                break;
            case METHOD_PUT:
                result = HttpMethod.PUT;
                break;
            case METHOD_DELETE:
                result = HttpMethod.DELETE;
                break;
            case METHOD_HEAD:
                result = HttpMethod.HEAD;
                break;
            case METHOD_TRACE:
                result = HttpMethod.TRACE;
                break;
            case METHOD_OPTIONS:
                result = HttpMethod.OPTIONS;
                break;
            default:
                XltLogger.runTimeLogger.warn(getIllegalValueOfTagDefaultingTo(dynamicMethod,
                                                                              "HttpResponseCode",
                                                                              METHOD_GET));
                result = HttpMethod.GET;
        }
        return result;
    }

    /**
     * @return {@link #validations}.
     */
    public List<URLActionDataValidation> getValidations()
    {
        return this.validations;
    }

    /**
     * @return {@link #store}.
     */
    public List<URLActionDataStore> getStore()
    {
        return this.store;
    }

    /**
     * 
     * @return {@link #parameters}, after its dynamic interpretation via {@link #interpreter}.
     */
    public List<NameValuePair> getParameters()
    {
        final List<NameValuePair> result = new ArrayList<NameValuePair>(parameters.size());

        for (final NameValuePair pair : parameters)
        {
            result.add(getDynamicPair(pair));
        }
        return result;

    }

    /**
     * Sends nvp through the {@link #interpreter}.
     */
    private NameValuePair getDynamicPair(final NameValuePair pair)
    {
        String name = pair.getName();
        name = name != null ? interpreter.processDynamicData(pair.getName())
                           : name;
        String value = pair.getValue();
        value = value != null ? interpreter.processDynamicData(pair.getValue())
                             : value;
        return new NameValuePair(name, value);
    }

    /**
     * @return {@link #cookies}, after its dynamic interpretation via {@link #interpreter}.
     */
    public List<NameValuePair> getCookies()
    {
        final List<NameValuePair> result = new ArrayList<NameValuePair>(cookies.size());

        for (final NameValuePair pair : cookies)
        {
            result.add(getDynamicPair(pair));
        }
        return result;
    }

    /**
     * @return {@link #headers}, after its dynamic interpretation via {@link #interpreter}.
     */
    public List<NameValuePair> getHeaders()
    {
        final List<NameValuePair> result = new ArrayList<NameValuePair>(headers.size());

        for (final NameValuePair pair : headers)
        {
            result.add(getDynamicPair(pair));
        }
        return result;
    }

    /**
     * 
     * @return {@link #name} after its dynamic interpretation via {@link #interpreter}.
     */
    public String getName()
    {
        return interpreter.processDynamicData(name);
    }

    public ParameterInterpreter getInterpreter()
    {
        return interpreter;
    }

    /**
     * 
     * @return value of {@link #encodeParameters} after its dynamic interpretation via {@link #interpreter}.
     */
    public Boolean encodeParameters()
    {
        Boolean result = false; // default
        final String dynamicEncoded = interpreter.processDynamicData(this.encodeParameters);
        if ("true".equals(dynamicEncoded))
        {
            result = true;
        }
        else if ("false".equals(dynamicEncoded))
        {
            result = false;
        }
        else
        {
            XltLogger.runTimeLogger.warn(getIllegalValueOfTagDefaultingTo(dynamicEncoded,
                                                                          "encodeParameters",
                                                                          "false"));
            result = false;
        }
        return result;
    }

    /**
     * @return value of {@link #encodeBody} after its dynamic interpretation via {@link #interpreter}.
     */
    public Boolean encodeBody()
    {
        Boolean result = true; // default
        if (this.encodeBody != null)
        {
            final String dynamicEncoded = interpreter.processDynamicData(this.encodeBody);
            if ("true".equals(dynamicEncoded))
            {
                result = true;
            }
            else if ("false".equals(dynamicEncoded))
            {
                result = false;
            }
            else
            {
                XltLogger.runTimeLogger.warn(getIllegalValueOfTagDefaultingTo(dynamicEncoded,
                                                                              "encodeBody",
                                                                              "false"));
                result = false;
            }
        }
        return result;
    }

    /**
     * @return true if {@link #type request} is of type {@link #TYPE_STATIC}.
     */
    public boolean isStaticContent()
    {
        Boolean result = false; // default
        final String dynamicType = interpreter.processDynamicData(this.type);
        if (TYPE_STATIC.equals(dynamicType))
        {
            result = true;
        }
        else if (TYPE_XHR.equals(dynamicType)
                 || TYPE_ACTION.equals(dynamicType))
        {
            result = false;
        }
        else
        {
            XltLogger.runTimeLogger.warn(getIllegalValueOfTagDefaultingTo(dynamicType,
                                                                          "Type",
                                                                          TYPE_ACTION));
            result = false;
        }
        return result;
    }

    /**
     * @return true if {@link #type request} is of type {@link #TYPE_XHR}.
     */
    public boolean isXHRAction()
    {
        Boolean result = false; // default
        final String dynamicType = interpreter.processDynamicData(this.type);
        if (TYPE_XHR.equals(dynamicType))
        {
            result = true;
        }
        else if (TYPE_ACTION.equals(dynamicType)
                 || TYPE_STATIC.equals(dynamicType))
        {
            result = false;
        }
        else
        {
            XltLogger.runTimeLogger.warn(getIllegalValueOfTagDefaultingTo(dynamicType,
                                                                          "Type",
                                                                          TYPE_ACTION));
            ;
            result = false;
        }
        return result;
    }

    /**
     * @return true if {@link #type request} is of type {@link #TYPE_ACTION}.
     */
    public Boolean isAction()
    {
        Boolean result = true; // default
        final String dynamicType = interpreter.processDynamicData(this.type);
        if (TYPE_ACTION.equals(dynamicType))
        {
            result = true;
        }
        else if (TYPE_XHR.equals(dynamicType)
                 || TYPE_STATIC.equals(dynamicType))
        {
            result = false;
        }
        else
        {
            XltLogger.runTimeLogger.warn(getIllegalValueOfTagDefaultingTo(dynamicType,
                                                                          "Type",
                                                                          TYPE_ACTION));
            result = true;
        }
        return result;
    }

    /**
     * 
     * @return true if {@link #body} is set.
     */
    public Boolean hasBody()
    {
        Boolean b = false;
        if (getBody() != null)
        {
            b = true;
        }
        return b;
    }

    /**
     * @param method
     * @return if ({@link #PERMITTEDMETHODS method} is permitted) ? true : false. 
     */
    public static boolean isPermittedMethod(final String method)
    {
        return PERMITTEDMETHODS.contains(method);
    }
    /**
     * 
     * @param type
     * @return if ({@link #PERMITTEDTYPES type} is permitted) ? true : false. 
     */
    
    public static boolean isPermittedType(final String type)
    {
        return PERMITTEDTYPES.contains(type);
    }

    /**
     * Add request parameter if NOT null.
     * @param nvp
     */
    public void addParameter(final NameValuePair nvp)
    {
        if (parameters.isEmpty() && nvp != null)
        {
            parameters = new ArrayList<NameValuePair>();
        }
        if (nvp != null)
        {
            parameters.add(nvp);
            XltLogger.runTimeLogger.debug(getAddedToTag("Parameter"));
        }
    }

    /**
     * Dirty way of throwing a IllegalArgumentException with the passed message. 
     * @param message
     * @return nothing.
     */
    private Object throwIllegalArgumentException(final String message)
    {
        throw new IllegalArgumentException(message);
    }

    /**
     * 
     * @param tag
     * @param value
     * @return Formated message.
     */
    private String getSetTagToValueMessage(final String tag, final String value)
    {
        final String message = MessageFormat.format("Action: \"{0}\", Set \"{1}\" to value: \"{2}\"",
                                                    this.name,
                                                    tag,
                                                    value);
        return message;
    }

    /**
     * 
     * @param tag name
     * @return Formated message.
     */
    private String getAddedToTag(final String tag)
    {
        final String message = MessageFormat.format("Action: \"{0}\", Added new \"{1}\"",
                                                    this.name,
                                                    tag);
        return message;
    }

    /**
     * @param tag
     * @return Formated error message.
     */
    private String getSetNewTagMessage(final String tag)
    {
        final String message = MessageFormat.format("Action: \"{0}\", Set new \"{1}\"",
                                                    this.name,
                                                    tag);
        return message;
    }

    /**
     * @param value
     * @param tag name
     * @param defaultValue
     * @return Formated error message.
     */
    private String getIllegalValueOfTagDefaultingTo(final String value,
                                                    final String tag,
                                                    final String defaultValue)
    {
        final String message = MessageFormat.format(" Action: \"{0}\",  Unsupported value \"{1}\" for \"{2}\", defaulting to \"{3}\"",
                                                    this.name,
                                                    value,
                                                    tag,
                                                    defaultValue);
        return message;
    }
}
