package com.xceptance.xlt.common.util;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.xpath.XPathExpressionException;

import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.xml.sax.SAXException;

import bsh.EvalError;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.api.validators.HttpResponseCodeValidator;
import com.xceptance.xlt.common.util.bsh.ParameterInterpreter;

public class URLAction
{

    private String name;

    private String type;

    private String url;

    private String method;

    private String encoded;

    private String httpResponceCode;

    private List<URLActionValidation> validations = Collections.emptyList();

    private List<URLActionStore> store = Collections.emptyList();

    private List<NameValuePair> parameters = Collections.emptyList();

    private List<NameValuePair> cookies = Collections.emptyList();

    private List<NameValuePair> headers = Collections.emptyList();

    private ParameterInterpreter interpreter;

    private final static Set<String> PERMITTEDTYPES = new HashSet<String>();

    private final static Set<String> PERMITTEDMETHODS = new HashSet<String>();

    static final String TYPE_ACTION = "A";

    static final String TYPE_STATIC = "S";

    static final String TYPE_XHR = "Xhr";

    static final String METHOD_POST = "POST";

    static final String METHOD_GET = "GET";

    static final String HTML = "html";

    static final String XML = "xml";

    static final String JSON = "json";

    static final HashMap<String, String> CONTENTTYPES = new HashMap<String, String>();

    static
    {
        PERMITTEDTYPES.add(TYPE_ACTION);
        PERMITTEDTYPES.add(TYPE_XHR);
        PERMITTEDTYPES.add(TYPE_STATIC);

        PERMITTEDMETHODS.add(METHOD_GET);
        PERMITTEDMETHODS.add(METHOD_POST);
    }

    private List<NameValuePair> getRawParameters()
    {
        return this.parameters;
    }

    private List<NameValuePair> getRawCookies()
    {
        return this.cookies;
    }

    private List<NameValuePair> getRawHeaders()
    {
        return this.headers;
    }

    private String getRawName()
    {
        return this.name;
    }

    private String getRawType()
    {
        return this.type;
    }

    private String getRawUrl()
    {
        return this.url;
    }

    private String getRawMethod()
    {
        return this.method;
    }

    private String getRawEncoded()
    {
        return this.encoded;
    }

    private String getRawResponseCode()
    {
        return this.httpResponceCode;
    }

    /*
     * @Parameters: Minimal parameters to create a legit URLAction
     */
    public URLAction(String name, String url, final ParameterInterpreter interpreter)
    {
        XltLogger.runTimeLogger.info("[URLAction] Creating new Action ");
        setName(name);
        setUrl(url);
        setType(TYPE_ACTION); // default
        setMethod(METHOD_GET); // default
        setEncoded("false"); // default
        setHttpResponceCode("200");
        setInterpreter(interpreter);
    }

    public void outlineRaw()
    {
        try
        {
            System.err.println("Action: " + getRawName());
            System.err.println("\t" + "Type: " + getRawType());
            System.err.println("\t" + "Method: " + getRawMethod());
            System.err.println("\t" + "URL: " + getRawUrl());
            System.err.println("\t" + "Encoded: " + getRawEncoded());
            System.err.println("\t" + "HttpCode: " + getRawResponseCode());
            if (!parameters.isEmpty())
            {
                List<NameValuePair> parameters = getRawParameters();
                System.err.println("\t" + "Parameters: ");
                for (final NameValuePair nvp : parameters)
                {
                    System.err.println("\t\t" + nvp.getName() + " : " + nvp.getValue());
                }
            }
            if (!headers.isEmpty())
            {
                System.err.println("\t" + "Headers: ");
                List<NameValuePair> headers = getRawHeaders();
                for (NameValuePair nvp : headers)
                {
                    System.err.println("\t\t" + nvp.getName() + " : " + nvp.getValue());
                }
            }
            if (!cookies.isEmpty())
            {
                System.err.println("\t" + "Cookies: ");
                List<NameValuePair> cookies = getRawCookies();
                for (NameValuePair nvp : cookies)
                {
                    System.err.println("\t\t" + nvp.getName() + " : " + nvp.getValue());
                }
            }
            if (!validations.isEmpty())
            {
                System.err.println("\t" + "Validations: ");
                for (final URLActionValidation v : validations)
                {
                    v.outlineRaw();
                }
            }
            if (!store.isEmpty())
            {
                System.err.println("\tStore:");
                for (URLActionStore storeItem : store)
                {
                    storeItem.outlineRaw();
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

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
            System.err.println("\t" + "Is Encoded: " + isEncoded());
            System.err.println("\t" + "HttpCode: " + getResponseCodeValidator().getHttpResponseCode());
            if (!parameters.isEmpty())
            {
                List<NameValuePair> parameters = getParameters();
                System.err.println("\t" + "Parameters: ");
                for (final NameValuePair nvp : parameters)
                {
                    System.err.println("\t\t" + nvp.getName() + " : " + nvp.getValue());
                }
            }
            if (!headers.isEmpty())
            {
                System.err.println("\t" + "Headers: ");
                List<NameValuePair> headers = getHeaders();
                for (NameValuePair nvp : headers)
                {
                    System.err.println("\t\t" + nvp.getName() + " : " + nvp.getValue());
                }
            }
            if (!cookies.isEmpty())
            {
                System.err.println("\t" + "Cookies: ");
                List<NameValuePair> cookies = getCookies();
                for (NameValuePair nvp : cookies)
                {
                    System.err.println("\t\t" + nvp.getName() + " : " + nvp.getValue());
                }
            }
            if (!validations.isEmpty())
            {
                System.err.println("\t" + "Validations: ");
                for (final URLActionValidation v : validations)
                {
                    v.outline();
                }
            }
            if (!store.isEmpty())
            {
                System.err.println("\tStore:");
                for (URLActionStore storeItem : store)
                {
                    storeItem.outline();
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public void setHttpResponceCode(String httpResponceCode)
    {
        if (httpResponceCode != null)
        {
            this.httpResponceCode = httpResponceCode;
            XltLogger.runTimeLogger.info(getSetTagToValueMessage("HttpResponceCode", httpResponceCode));
        }
    }

    public void setUrl(String url)
    {
        this.url = (url != null) ? url : (String) throwIllegalArgumentException("[URLAction] 'Url' cannot be null");
        XltLogger.runTimeLogger.info(getSetTagToValueMessage("URL", url));
    }

    public void setMethod(final String method)
    {
        if (method != null)
        {
            this.method = method;
            XltLogger.runTimeLogger.info(getSetTagToValueMessage("Method", method));
        }
    }

    public void setEncoded(String encoded)
    {
        if (encoded != null)
        {
            this.encoded = encoded;
            XltLogger.runTimeLogger.info(getSetTagToValueMessage("Encoded", encoded));
        }
    }

    public void setType(String type)
    {
        if (type != null)
        {
            this.type = type;
            XltLogger.runTimeLogger.info(getSetTagToValueMessage("Type", type));
        }
    }

    public void setName(final String name)
    {
        this.name = ((name != null) ? name
                                   : (String) throwIllegalArgumentException("[URLAction] 'Name' cannot be null"));
        XltLogger.runTimeLogger.info(MessageFormat.format("[URLAction] Set Action 'Name' to \"{0}\"", name));

    }

    private void setInterpreter(ParameterInterpreter interpreter)
    {

        this.interpreter = (interpreter != null) ? interpreter
                                                : (ParameterInterpreter) throwIllegalArgumentException("[URLAction] 'ParameterInterpreter' cannot be null");
        XltLogger.runTimeLogger.info(getSetNewTagMessage("interpreter"));

    }

    public void setValidations(List<URLActionValidation> validations)
    {
        if (validations != null)
        {
            this.validations = validations;
            XltLogger.runTimeLogger.info(getSetNewTagMessage("interpreter"));
        }
    }

    public void setHeaders(List<NameValuePair> headers)
    {
        if (headers != null)
        {
            this.headers = headers;
            XltLogger.runTimeLogger.info(getSetNewTagMessage("headers"));
        }
    }

    public void setStore(List<URLActionStore> store)
    {
        if (store != null)
        {
            this.store = store;
            XltLogger.runTimeLogger.info(getSetNewTagMessage("store"));
        }
    }

    public void setParameters(List<NameValuePair> parameters)
    {
        if (parameters != null)
        {
            this.parameters = parameters;
            XltLogger.runTimeLogger.info(getSetNewTagMessage("parameters"));
        }
    }

    public void setCookies(List<NameValuePair> cookies)
    {
        if (cookies != null)
        {
            this.cookies = cookies;
            XltLogger.runTimeLogger.info(getSetNewTagMessage("cookies"));
        }
    }

    private String getType()
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

    public URL getUrl() throws MalformedURLException
    {
        return new URL(interpreter.processDynamicData(url));
    }

    public String getUrlString() throws MalformedURLException
    {
        return getUrl().toString();
    }

    public HttpResponseCodeValidator getResponseCodeValidator()
    {
        String dynmaicResponseCode = interpreter.processDynamicData(this.httpResponceCode);
        HttpResponseCodeValidator result = StringUtils.isNotBlank(dynmaicResponseCode) ? new HttpResponseCodeValidator(
                                                                                                                       Integer.parseInt(dynmaicResponseCode))
                                                                                      : HttpResponseCodeValidator.getInstance();
        return result;
    }

    public HttpMethod getMethod()
    {
        HttpMethod result = HttpMethod.GET;

        String dynamicMethod = interpreter.processDynamicData(this.method);

        result = selectMethodFromDynamicData(dynamicMethod);

        return result;
    }

    private HttpMethod selectMethodFromDynamicData(String dynamicMethod)
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
            XltLogger.runTimeLogger.warn(getIllegalValueOfTagDefaultingTo(dynamicMethod, "HttpResponseCode", METHOD_GET));
            result = HttpMethod.GET;
        }
        return result;
    }

    public List<URLActionValidation> getValidations()
    {
        return this.validations;
    }

    public List<URLActionStore> getStore()
    {
        return this.store;
    }

    public List<NameValuePair> getParameters()
    {
        List<NameValuePair> result = new ArrayList<NameValuePair>(parameters.size());

        for (final NameValuePair pair : parameters)
        {
            result.add(getDynamicPair(pair));
        }
        return result;

    }

    private NameValuePair getDynamicPair(NameValuePair pair)
    {
        String name = pair.getName();
        name = name != null ? interpreter.processDynamicData(pair.getName()) : name;
        String value = pair.getValue();
        value = value != null ? interpreter.processDynamicData(pair.getValue()) : value;
        return new NameValuePair(name, value);
    }

    public List<NameValuePair> getCookies()
    {
        List<NameValuePair> result = new ArrayList<NameValuePair>(cookies.size());

        for (final NameValuePair pair : cookies)
        {
            result.add(getDynamicPair(pair));
        }
        return result;
    }

    public List<NameValuePair> getHeaders()
    {
        List<NameValuePair> result = new ArrayList<NameValuePair>(headers.size());

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

    public Boolean getEncodedString()
    {
        Boolean returnBoolean = false; // default
        String dynamicEncoded = interpreter.processDynamicData(this.encoded);
        if (dynamicEncoded.equals("true"))
        {
            returnBoolean = true;
        }
        else if (dynamicEncoded.equals("false"))
        {
            returnBoolean = false;
        }
        else
        {
            // LOG MESSAGE INVALID TYPE -> defaulting
        }
        return returnBoolean;
    }

    public Boolean isEncoded()
    {
        Boolean result = false; // default
        String dynamicEncoded = interpreter.processDynamicData(this.encoded);
        if (dynamicEncoded.equals(true))
        {
            result = true;
        }
        else if (dynamicEncoded.equals(false))
        {
            result = false;
        }
        else
        {
            XltLogger.runTimeLogger.warn(getIllegalValueOfTagDefaultingTo(dynamicEncoded, "Encoded", "false"));
            result = false;
        }
        return result;
    }

    public boolean isStaticContent()
    {
        Boolean result = false; // default
        String dynamicType = interpreter.processDynamicData(this.type);
        if (dynamicType.equals(TYPE_STATIC))
        {
            result = true;
        }
        else if (dynamicType.equals(TYPE_XHR) || dynamicType.equals(TYPE_ACTION))
        {
            result = false;
        }
        else
        {
            XltLogger.runTimeLogger.warn(getIllegalValueOfTagDefaultingTo(dynamicType, "Type", TYPE_ACTION));
            result = false;
        }
        return result;
    }

    public boolean isXHRAction()
    {
        Boolean result = false; // default
        String dynamicType = interpreter.processDynamicData(this.type);
        if (dynamicType.equals(TYPE_XHR))
        {
            result = true;
        }
        else if (dynamicType.equals(TYPE_ACTION) || dynamicType.equals(TYPE_STATIC))
        {
            result = false;
        }
        else
        {
            XltLogger.runTimeLogger.warn(getIllegalValueOfTagDefaultingTo(dynamicType, "Type", TYPE_ACTION));
            ;
            result = false;
        }
        return result;
    }

    public Boolean isAction()
    {
        Boolean result = true; // default
        String dynamicType = interpreter.processDynamicData(this.type);
        if (dynamicType.equals(TYPE_ACTION))
        {
            result = true;
        }
        else if (dynamicType.equals(TYPE_XHR) || dynamicType.equals(TYPE_STATIC))
        {
            result = false;
        }
        else
        {
            XltLogger.runTimeLogger.warn(getIllegalValueOfTagDefaultingTo(dynamicType, "Type", TYPE_ACTION));
            result = true;
        }
        return result;
    }

    public static boolean isPermittedMethod(String s)
    {
        return PERMITTEDMETHODS.contains(s);
    }

    public static boolean isPermittedType(String s)
    {
        return PERMITTEDTYPES.contains(s);
    }

    public void addParameter(NameValuePair nvp)
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

    public void addCookie(NameValuePair cookie)
    {
        if (cookies.isEmpty() && cookie != null)
        {
            cookies = new ArrayList<NameValuePair>();
        }
        if (cookies != null)
        {
            this.cookies.add(cookie);
            XltLogger.runTimeLogger.info(getAddedToTag("Cookie"));
        }
    }

    public void addStore(URLActionStore storeItem)
    {
        if (this.store.isEmpty() && storeItem != null)
        {
            this.store = new ArrayList<URLActionStore>();
        }
        if (storeItem != null)
        {
            this.store.add(storeItem);
            XltLogger.runTimeLogger.info(getAddedToTag("Store-Item"));
        }
    }

    public void addHeader(NameValuePair header)
    {
        if (headers.isEmpty() && header != null)
        {
            headers = new ArrayList<NameValuePair>();
        }
        if (header != null)
        {
            this.headers.add(header);
            XltLogger.runTimeLogger.info(getAddedToTag("Header"));
        }
    }

    public void addValidation(URLActionValidation validation)
    {
        if (validations.isEmpty() && validation != null)
        {
            validations = new ArrayList<URLActionValidation>();
        }
        if (validation != null)
        {
            validations.add(validation);
            XltLogger.runTimeLogger.info(getAddedToTag("Validation"));
        }
    }

    public void validate(HtmlPage page)
    {
        XltLogger.runTimeLogger.info(getStartValidationMessage());
        validateResponseCode(page);
        validateValidationItems(page);
    }
    public void feedParameterInterpreter(HtmlPage page) throws EvalError{
        if(!store.isEmpty()){
            XltLogger.runTimeLogger.info(getStartFeedingInterpreterMessage());
            for(URLActionStore storeItem : store){
                storeItem.feedParameterInterpreter(page);
            }
        }
    }
    public void feedParameterInterpreter(HtmlPage page, WebResponse response) throws XPathExpressionException, EvalError{
        if(!store.isEmpty()){
            XltLogger.runTimeLogger.info(getStartFeedingInterpreterMessage());
            for(URLActionStore storeItem : store){
                storeItem.feedParameterInterpreter(page, response);
            }
        }
    }

    public void validateResponseCode(HtmlPage page)
    {
        XltLogger.runTimeLogger.info(getValidaeResonseCodeMessage());
        this.getResponseCodeValidator().validate(page);
    }

    public void validateValidationItems(HtmlPage page)
    {
        if (!validations.isEmpty())
        {
            for (URLActionValidation validation : validations)
            {
                validation.validate(page);
            }
        }
    }

    public void validate(HtmlPage page, WebResponse response) throws Exception
    {
        XltLogger.runTimeLogger.info(getStartValidationMessage());
        validateResponseCode(response);
        validateValidationItems(page, response);
    }

    public void validateResponseCode(WebResponse response)
    {
        XltLogger.runTimeLogger.info(getValidaeResonseCodeMessage());
        Assert.assertEquals("Response code did not match", getResponseCodeValidator().getHttpResponseCode(),
                            response.getStatusCode());

    }

    private void validateValidationItems(HtmlPage page, WebResponse response)
        throws SAXException, IOException, XPathExpressionException
    {
        if (validations != null)
        {
            for (URLActionValidation validation : validations)
            {
                validation.validate(response, page);
            }
        }
    }

    private Object throwIllegalArgumentException(String message)
    {
        throw new IllegalArgumentException(message);
    }

    private String getSetTagToValueMessage(String tag, String value)
    {
        String message = MessageFormat.format("[URLAction] Action: \"{0}\", Set \"{1}\" to value: \"{2}\"", this.name,
                                              tag, value);
        return message;
    }

    private String getAddedToTag(String tag)
    {
        String message = MessageFormat.format("[URLAction] Action: \"{0}\", Added new \"{1}\"", this.name, tag);
        return message;
    }

    private String getSetNewTagMessage(String tag)
    {
        String message = MessageFormat.format("[URLAction] Action: \"{0}\", Set new \"{1}\"", this.name, tag);
        return message;
    }

    private String getIllegalValueOfTagDefaultingTo(String value, String tag, String defaultValue)
    {
        String message = MessageFormat.format("[URLAction] Action: \"{0}\",  Unsupported value \"{1}\" for \"{2}\", defaulting to \"{3}\"",
                                              this.name, value, tag, defaultValue);
        return message;
    }

    private String getStartValidationMessage()
    {
        String message = MessageFormat.format("[URLAction] Action: \"{0}\", Starting Validation", this.name);
        return message;
    }

    private String getValidaeResonseCodeMessage()
    {
        String message = MessageFormat.format("[URLAction] Action: \"{0}\", Validating Responsecode ", this.name);
        return message;
    }
    private String getStartFeedingInterpreterMessage()
    {
        String message = MessageFormat.format("[URLAction] Action: \"{0}\", Start Feedind the Interpreter ", this.name);
        return message;
    }
}
