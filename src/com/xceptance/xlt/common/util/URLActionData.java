package com.xceptance.xlt.common.util;

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

public class URLActionData
{

    private String name;

    private String type;

    private String url;

    private String method;

    private String encodeParameters;

    private String encodeBody;

    private String httpResponceCode;

    private String body;

    private List<URLActionDataValidation> validations = Collections.emptyList();

    private List<URLActionDataStore> store = Collections.emptyList();

    private List<NameValuePair> parameters = Collections.emptyList();

    private List<NameValuePair> cookies = Collections.emptyList();

    private List<NameValuePair> headers = Collections.emptyList();

    private ParameterInterpreter interpreter;

    public final static Set<String> PERMITTEDTYPES = new HashSet<String>();

    public final static Set<String> PERMITTEDMETHODS = new HashSet<String>();

    public static final String TYPE_ACTION = "A";

    public static final String TYPE_STATIC = "S";

    public static final String TYPE_XHR = "Xhr";

    public static final String METHOD_POST = "POST";

    public static final String METHOD_GET = "GET";

    static
    {
        PERMITTEDTYPES.add(TYPE_ACTION);
        PERMITTEDTYPES.add(TYPE_XHR);
        PERMITTEDTYPES.add(TYPE_STATIC);

        PERMITTEDMETHODS.add(METHOD_GET);
        PERMITTEDMETHODS.add(METHOD_POST);
    }

    /*
     * @Parameters: Minimal parameters to create a legit URLAction
     */
    public URLActionData(final String name,
                     final String url,
                     final ParameterInterpreter interpreter)
    {
        XltLogger.runTimeLogger.info("Start creating new URLAction ");
        setName(name);
        setUrl(url);
        setType(TYPE_ACTION); // default
        setMethod(METHOD_GET); // default
        setEncodeParameters("true"); // default
        setEncodeBody("true"); //default
        setHttpResponceCode("200"); //default
        setInterpreter(interpreter);
    }

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
            System.err.println("\t" + "Encode-Parameters: " + encodeParameters());
            System.err.println("\t" + "Encode-Body: " + encodeBody());
            System.err.println("\t" + "HttpCode: "
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
                    System.err.println("\t\t" + nvp.getName() + " : " + nvp.getValue());
                }
            }
            if (!headers.isEmpty())
            {
                System.err.println("\t" + "Headers: ");
                final List<NameValuePair> headers = getHeaders();
                for (final NameValuePair nvp : headers)
                {
                    System.err.println("\t\t" + nvp.getName() + " : " + nvp.getValue());
                }
            }
            if (!cookies.isEmpty())
            {
                System.err.println("\t" + "Cookies: ");
                final List<NameValuePair> cookies = getCookies();
                for (final NameValuePair nvp : cookies)
                {
                    System.err.println("\t\t" + nvp.getName() + " : " + nvp.getValue());
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

    public void setHttpResponceCode(final String httpResponceCode)
    {
        if (httpResponceCode != null)
        {
            this.httpResponceCode = httpResponceCode;
            XltLogger.runTimeLogger.info(getSetTagToValueMessage("HttpResponceCode",
                                                                 httpResponceCode));
        }
    }

    public void setHttpResponceCode(final Integer httpResponceCode)
    {
        if (httpResponceCode != null)
        {
            this.httpResponceCode = httpResponceCode.toString();
            XltLogger.runTimeLogger.info(getSetTagToValueMessage("HttpResponceCode",
                                                                 this.httpResponceCode));
        }
    }

    public void setUrl(final String url)
    {
        this.url = (url != null) ? url
                                : (String) throwIllegalArgumentException("'Url' cannot be null");
        XltLogger.runTimeLogger.info(getSetTagToValueMessage("URL", this.url));
    }

    public void setMethod(final String method)
    {
        if (method != null)
        {
            this.method = method;
            XltLogger.runTimeLogger.info(getSetTagToValueMessage("Method", this.method));
        }
    }

    public void setEncodeParameters(final String encoded)
    {
        if (encoded != null)
        {
            this.encodeParameters = encoded;
            XltLogger.runTimeLogger.info(getSetTagToValueMessage("encodedParameters",
                                                                 this.encodeParameters));
        }
    }

    public void setEncodeParameters(final Boolean encoded)
    {
        if (encoded != null)
        {
            this.encodeParameters = encoded.toString();
            XltLogger.runTimeLogger.info(getSetTagToValueMessage("encodedParameters",
                                                                 this.encodeParameters));
        }
    }
    public void setEncodeBody(final String encoded)
    {
        if (encoded != null)
        {
            this.encodeBody = encoded;
            XltLogger.runTimeLogger.info(getSetTagToValueMessage("encodedBody",
                                                                 this.encodeBody));
        }
    }

    public void setEncodeBody(final Boolean encoded)
    {
        if (encoded != null)
        {
            this.encodeBody = encoded.toString();
            XltLogger.runTimeLogger.info(getSetTagToValueMessage("encodedBody",
                                                                 this.encodeBody));
        }
    }

    public void setType(final String type)
    {
        if (type != null)
        {
            this.type = type;
            XltLogger.runTimeLogger.info(getSetTagToValueMessage("Type", this.type));
        }
    }

    public void setName(final String name)
    {
        this.name = ((name != null) ? name
                                   : (String) throwIllegalArgumentException("Name' cannot be null"));
        XltLogger.runTimeLogger.info(MessageFormat.format("Set Action 'Name' to \"{0}\"",
                                                          this.name));

    }

    private void setInterpreter(final ParameterInterpreter interpreter)
    {

        this.interpreter = (interpreter != null) ? interpreter
                                                : (ParameterInterpreter) throwIllegalArgumentException(" 'ParameterInterpreter' cannot be null");
        XltLogger.runTimeLogger.info(getSetNewTagMessage("interpreter"));

    }

    public void setValidations(final List<URLActionDataValidation> validations)
    {
        if (validations != null)
        {
            this.validations = validations;
            XltLogger.runTimeLogger.info(getSetNewTagMessage("validations"));
        }
    }

    public void setHeaders(final List<NameValuePair> headers)
    {
        if (headers != null)
        {
            this.headers = headers;
            XltLogger.runTimeLogger.info(getSetNewTagMessage("headers"));
        }
    }

    public void setStore(final List<URLActionDataStore> store)
    {
        if (store != null)
        {
            this.store = store;
            XltLogger.runTimeLogger.info(getSetNewTagMessage("store"));
        }
    }

    public void setParameters(final List<NameValuePair> parameters)
    {
        if (parameters != null)
        {
            this.parameters = parameters;
            XltLogger.runTimeLogger.info(getSetNewTagMessage("parameters"));
        }
    }

    public void setCookies(final List<NameValuePair> cookies)
    {
        if (cookies != null)
        {
            this.cookies = cookies;
            XltLogger.runTimeLogger.info(getSetNewTagMessage("cookies"));
        }
    }

    public void setBody(final String body)
    {
        if (body != null)
        {
            this.body = body;
            XltLogger.runTimeLogger.info(getSetTagToValueMessage("body", this.body));
        }
    }

    @Nullable
    public String getBody()
    {
        return interpreter.processDynamicData(this.body);
    }

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

    public URL getUrl()
    {
        try
        {
            return new URL(interpreter.processDynamicData(url));
        }
        catch (final MalformedURLException e)
        {
            throw new IllegalArgumentException("Malformed URL for action" + getName() + ": " + e.getMessage(), e);
        }
    }

    public String getUrlString() throws MalformedURLException
    {
        return getUrl().toString();
    }

    public HttpResponseCodeValidator getResponseCodeValidator()
    {
        final String dynmaicResponseCode = interpreter.processDynamicData(this.httpResponceCode);
        final HttpResponseCodeValidator result = StringUtils.isNotBlank(dynmaicResponseCode) ? new HttpResponseCodeValidator(
                                                                                                                             Integer.parseInt(dynmaicResponseCode))
                                                                                            : HttpResponseCodeValidator.getInstance();
        return result;
    }

    public HttpMethod getMethod()
    {
        HttpMethod result = HttpMethod.GET;

        final String dynamicMethod = interpreter.processDynamicData(this.method);

        result = selectMethodFromDynamicData(dynamicMethod);

        return result;
    }

    private HttpMethod selectMethodFromDynamicData(final String dynamicMethod)
    {
        HttpMethod result;
        if (dynamicMethod.equals(METHOD_GET))
        {
            result = HttpMethod.GET;
        }
        else if (dynamicMethod.equals(METHOD_POST))
        {
            result = HttpMethod.POST;
        }
        else
        {
            XltLogger.runTimeLogger.warn(getIllegalValueOfTagDefaultingTo(dynamicMethod,
                                                                          "HttpResponseCode",
                                                                          METHOD_GET));
            result = HttpMethod.GET;
        }
        return result;
    }

    public List<URLActionDataValidation> getValidations()
    {
        return this.validations;
    }

    public List<URLActionDataStore> getStore()
    {
        return this.store;
    }

    public List<NameValuePair> getParameters()
    {
        final List<NameValuePair> result = new ArrayList<NameValuePair>(parameters.size());

        for (final NameValuePair pair : parameters)
        {
            result.add(getDynamicPair(pair));
        }
        return result;

    }

    private NameValuePair getDynamicPair(final NameValuePair pair)
    {
        String name = pair.getName();
        name = name != null ? interpreter.processDynamicData(pair.getName()) : name;
        String value = pair.getValue();
        value = value != null ? interpreter.processDynamicData(pair.getValue()) : value;
        return new NameValuePair(name, value);
    }

    public List<NameValuePair> getCookies()
    {
        final List<NameValuePair> result = new ArrayList<NameValuePair>(cookies.size());

        for (final NameValuePair pair : cookies)
        {
            result.add(getDynamicPair(pair));
        }
        return result;
    }

    public List<NameValuePair> getHeaders()
    {
        final List<NameValuePair> result = new ArrayList<NameValuePair>(headers.size());

        for (final NameValuePair pair : headers)
        {
            result.add(getDynamicPair(pair));
        }
        return result;
    }

    public String getName()
    {
        return interpreter.processDynamicData(name);
    }

    public ParameterInterpreter getInterpreter()
    {
        return interpreter;
    }


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

    public Boolean encodeBody()
    {
        Boolean result = false; // default
        if (this.body != null)
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
    
    public boolean isStaticContent()
    {
        Boolean result = false; // default
        final String dynamicType = interpreter.processDynamicData(this.type);
        if (TYPE_STATIC.equals(dynamicType))
        {
            result = true;
        }
        else if (TYPE_XHR.equals(dynamicType) || TYPE_ACTION.equals(dynamicType))
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

    public boolean isXHRAction()
    {
        Boolean result = false; // default
        final String dynamicType = interpreter.processDynamicData(this.type);
        if (TYPE_XHR.equals(dynamicType))
        {
            result = true;
        }
        else if (TYPE_ACTION.equals(dynamicType) || TYPE_STATIC.equals(dynamicType))
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

    public Boolean isAction()
    {
        Boolean result = true; // default
        final String dynamicType = interpreter.processDynamicData(this.type);
        if (TYPE_ACTION.equals(dynamicType))
        {
            result = true;
        }
        else if (TYPE_XHR.equals(dynamicType) || TYPE_STATIC.equals(dynamicType))
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

    public Boolean hasBody()
    {
        Boolean b = false;
        if (getBody() != null)
        {
            b = true;
        }
        return b;
    }

    public static boolean isPermittedMethod(final String s)
    {
        return PERMITTEDMETHODS.contains(s);
    }

    public static boolean isPermittedType(final String s)
    {
        return PERMITTEDTYPES.contains(s);
    }

    public void addParameter(final NameValuePair nvp)
    {
        if (parameters.isEmpty() && nvp != null)
        {
            parameters = new ArrayList<NameValuePair>();
        }
        if (nvp != null)
        {
            parameters.add(nvp);
            XltLogger.runTimeLogger.info(getAddedToTag("Parameter"));
        }
    }

    private Object throwIllegalArgumentException(final String message)
    {
        throw new IllegalArgumentException(message);
    }

    private String getSetTagToValueMessage(final String tag, final String value)
    {
        final String message = MessageFormat.format("Action: \"{0}\", Set \"{1}\" to value: \"{2}\"",
                                                    this.name, tag, value);
        return message;
    }

    private String getAddedToTag(final String tag)
    {
        final String message = MessageFormat.format("Action: \"{0}\", Added new \"{1}\"",
                                                    this.name, tag);
        return message;
    }

    private String getSetNewTagMessage(final String tag)
    {
        final String message = MessageFormat.format("Action: \"{0}\", Set new \"{1}\"",
                                                    this.name, tag);
        return message;
    }

    private String getIllegalValueOfTagDefaultingTo(final String value,
                                                    final String tag,
                                                    final String defaultValue)
    {
        final String message = MessageFormat.format(" Action: \"{0}\",  Unsupported value \"{1}\" for \"{2}\", defaulting to \"{3}\"",
                                                    this.name, value, tag, defaultValue);
        return message;
    }
}
