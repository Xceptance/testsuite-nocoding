package com.xceptance.xlt.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.annotation.Nullable;
import org.yaml.snakeyaml.Yaml;

import bsh.EvalError;

import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.common.util.ParameterUtils.Reason;
import com.xceptance.xlt.common.util.bsh.ParameterInterpreter;

public class YAMLBasedURLActionListBuilder extends URLActionListBuilder
{
    /*
     * Accepted syntactic keys for the yaml data
     */
    private static final String ACTION = "Action";

    private static final String REQUEST = "Request";

    private static final String RESPONSE = "Response";

    private static final String BODY = "Body";

    private static final String STORE = "Store";

    private static final String SUBREQUESTS = "Subrequests";

    private static final String NAME = "Name";

    private static final String URL = "Url";

    private static final String METHOD = "Method";

    private static final String ENCODED = "Encoded";

    private static final String XHR = "Xhr";

    private static final String PARAMETERS = "Parameters";

    private static final String HTTPCODE = "Httpcode";

    private static final String VALIDATION = "Validate";

    private static final String STATIC = "Static";

    private static final String COOKIES = "Cookies";

    private static final String HEADERS = "Headers";

    private static final String DELETE = "delete";

    /*
     * Default values
     */

    private List<String> d_static = new ArrayList<String>();

    public YAMLBasedURLActionListBuilder(final String filePath,
                                         final ParameterInterpreter interpreter,
                                         final URLActionBuilder builder)
    {
        super(filePath, interpreter, builder);
    }

    public void outline()
    {
        System.err.println("YAMLBasedURLActionListBuilder");
        if (!d_static.isEmpty())
        {
            System.err.println("Static");
            for (final String s : d_static)
            {
                System.err.println("\t" + s);
            }
        }
        this.actionBuilder.outline();
    }

    public List<URLAction> buildURLActions()
    {
        final List<Object> dataList = loadDataFromFile();
        createActionList(dataList);
        return actions;
    }

    @SuppressWarnings("unchecked")
    private List<Object> loadDataFromFile()
    {
        List<Object> resultList = Collections.emptyList();
        try
        {
            final Object o = loadParsedYamlObject();
            if (o != null)
            {
                ParameterUtils.isArrayList(o, "YAML-Data",
                                           "See the no-coding syntax sepecification!");
                resultList = (List<Object>) o;
                XltLogger.runTimeLogger.info(MessageFormat.format("Loading YAML data from file: \"{0}\" ",
                                                                  this.filePath));
            }
            else
            {
                XltLogger.runTimeLogger.warn(MessageFormat.format("Empty file: \"{0}\" ",
                                                                  this.filePath));
            }
        }
        catch (final FileNotFoundException e)
        {
            XltLogger.runTimeLogger.warn(MessageFormat.format("File: \"{0}\" not found!",
                                                              this.filePath));
        }
        return resultList;
    }

    @Nullable
    private Object loadParsedYamlObject() throws FileNotFoundException
    {
        final InputStream input = new FileInputStream(new File(this.filePath));
        final Yaml yaml = new Yaml();
        return yaml.load(input);
    }

    @SuppressWarnings("unchecked")
    private void createActionList(final List<Object> dataList)
    {
        for (final Object listObject : dataList)
        {
            ParameterUtils.isLinkedHashMap(listObject, "List Items",
                                           "See the no-coding syntax sepecification!");

            final LinkedHashMap<String, Object> listItem = (LinkedHashMap<String, Object>) listObject;
            handleListItem(listItem);
        }
    }

    private void handleListItem(final LinkedHashMap<String, Object> listItem)
    {
        final String tagName = determineTagName(listItem);
        switch (tagName)
        {
            case ACTION:
                // handleSingleActionListItem(listItem);
                break;
            case NAME:
                setDefaultName(listItem);
                break;
            case BODY:
                setDefaultBody(listItem);
                break;
            case HTTPCODE:
                setDefaultHttpCode(listItem);
                break;
            case URL:
                setDefaultUrl(listItem);
                break;
            case METHOD:
                setDefaultMethod(listItem);
                break;
            case ENCODED:
                setDefaultEncoded(listItem);
                break;
            case XHR:
                setDefaultXhr(listItem);
                break;
            case PARAMETERS:
                setDefaultParameters(listItem);
                break;
            case COOKIES:
                setDefaultCookies(listItem);
                break;
            case STATIC:
                setDefaultStatic(listItem);
                break;
            case HEADERS:
                setDefaultHeaders(listItem);
                break;
            case STORE:
                setDynamicStoreVariables(listItem);
                break;
            default:
                XltLogger.runTimeLogger.warn(MessageFormat.format("Ignoring invali list argument : \"{0}\", ignore",
                                                                  tagName));
        }
        this.outline();
    }

    private void setDefaultName(final LinkedHashMap<String, Object> nameItem)
    {
        final Object nameObject = nameItem.get(NAME);
        ParameterUtils.isString(nameObject, NAME);
        final String name = (String) nameObject;
        if (name.equals(DELETE))
        {
            actionBuilder.setDefaultName(null);
        }
        else
        {
            actionBuilder.setDefaultName(name);
        }
    }

    private void setDefaultBody(final LinkedHashMap<String, Object> bodyItem)
    {
        final Object bodyObject = bodyItem.get(BODY);
        ParameterUtils.isString(bodyObject, BODY);
        final String body = (String) bodyObject;
        if (body.equals(DELETE))
        {
            actionBuilder.setDefaultBody(null);
        }
        else
        {
            actionBuilder.setDefaultBody(body);
        }
    }

    private void setDefaultHttpCode(final LinkedHashMap<String, Object> codeItem)
    {
        final Object codeObject = codeItem.get(HTTPCODE);
        if (codeObject instanceof Integer)
        {
            final Integer code = (Integer) codeObject;
            actionBuilder.setDefaultHttpResponceCode(code.toString());
        }
        else if (codeObject instanceof String)
        {
            final String code = (String) codeObject;
            if (code.equals(DELETE))
            {
                actionBuilder.setDefaultName(null);
            }
            else
            {
                actionBuilder.setDefaultName(code);
            }
        }
        else
        {
            ParameterUtils.doThrow(HTTPCODE, Reason.UNSUPPORTED_TYPE);
        }
    }

    private void setDefaultUrl(final LinkedHashMap<String, Object> urlItem)
    {
        final Object urlObject = urlItem.get(URL);
        ParameterUtils.isString(urlObject, URL);
        final String url = (String) urlObject;
        if (url.equals(DELETE))
        {
            actionBuilder.setDefaultUrl(null);
        }
        else
        {
            actionBuilder.setDefaultUrl(url);
        }
    }

    private void setDefaultMethod(final LinkedHashMap<String, Object> methodItem)
    {
        final Object methodObject = methodItem.get(METHOD);
        ParameterUtils.isString(methodObject, METHOD);
        final String method = (String) methodObject;
        if (method.equals(DELETE))
        {
            actionBuilder.setDefaultMethod(null);
        }
        else
        {
            actionBuilder.setDefaultMethod(method);
        }
    }

    private void setDefaultEncoded(final LinkedHashMap<String, Object> encodedItem)
    {
        final Object encodedObject = encodedItem.get(ENCODED);
        if (encodedObject instanceof Boolean)
        {
            final Boolean encoded = (Boolean) encodedObject;
            actionBuilder.setDefaultEncoded(encoded.toString());
        }
        else if (encodedObject instanceof String)
        {
            final String encoded = (String) encodedObject;
            if (encoded.equals(DELETE))
            {
                actionBuilder.setDefaultEncoded(null);
            }
            else
            {
                actionBuilder.setDefaultEncoded(encoded);
            }
        }
        else
        {
            ParameterUtils.doThrow(ENCODED, Reason.UNSUPPORTED_TYPE);
        }
    }

    private void setDefaultXhr(final LinkedHashMap<String, Object> xhrItem)
    {
        final Object xhrObject = xhrItem.get(XHR);
        if (xhrObject instanceof Boolean)
        {
            final Boolean xhr = (Boolean) xhrObject;
            if (xhr)
            {
                actionBuilder.setDefaultType(URLAction.TYPE_XHR);
            }
            else
            {
                actionBuilder.setDefaultType(URLAction.TYPE_ACTION);
            }
        }
        else if (xhrObject instanceof String)
        {
            final String xhr = (String) xhrObject;
            if (xhr.equals(DELETE))
            {
                actionBuilder.setDefaultType(null);
            }
            else
            {
                actionBuilder.setDefaultType(xhr);
            }
        }
        else
        {
            ParameterUtils.doThrow(XHR, Reason.UNSUPPORTED_TYPE);
        }
    }

    private void setDefaultParameters(final LinkedHashMap<String, Object> parametersItem)
    {
        final Object parametersObject = parametersItem.get(PARAMETERS);
        if (parametersObject instanceof String)
        {
            final String parameters = (String) parametersObject;
            if (parameters.equals(DELETE))
            {
                actionBuilder.setDefaultParameters(Collections.<NameValuePair> emptyList());
            }
            else
            {
                ParameterUtils.doThrow(PARAMETERS, parameters, Reason.UNSUPPORTED_VALUE);
            }
        }
        else if (parametersObject instanceof ArrayList)
        {
            final List<Object> objectList = (ArrayList) parametersObject;
            final List<NameValuePair> newList = new ArrayList<NameValuePair>();
            for (final Object object : objectList)
            {
                ParameterUtils.isLinkedHashMapParam(object, PARAMETERS);
                final LinkedHashMap<Object, Object> lhm = (LinkedHashMap<Object, Object>) object;
                final NameValuePair nvp = createPairfromLinkedHashMap(lhm);
                newList.add(nvp);
            }
            actionBuilder.setParameters(newList);
        }
        else
        {
            ParameterUtils.doThrow(PARAMETERS, Reason.UNSUPPORTED_TYPE);
        }
    }

    private void setDefaultCookies(final LinkedHashMap<String, Object> cookiesItem)
    {
        final Object cookiesObject = cookiesItem.get(COOKIES);
        if (cookiesObject instanceof String)
        {
            final String cookies = (String) cookiesObject;
            if (cookies.equals(DELETE))
            {
                actionBuilder.setDefaultCookies(Collections.<NameValuePair> emptyList());
            }
            else
            {
                ParameterUtils.doThrow(COOKIES, cookies, Reason.UNSUPPORTED_VALUE);
            }
        }
        else if (cookiesObject instanceof ArrayList)
        {
            final List<Object> objectList = (ArrayList) cookiesObject;
            final List<NameValuePair> newList = new ArrayList<NameValuePair>();
            for (final Object object : objectList)
            {
                ParameterUtils.isLinkedHashMapParam(object, COOKIES);
                final LinkedHashMap<Object, Object> lhm = (LinkedHashMap<Object, Object>) object;
                final NameValuePair nvp = createPairfromLinkedHashMap(lhm);
                newList.add(nvp);
            }
            actionBuilder.setCookies(newList);
        }
        else
        {
            ParameterUtils.doThrow(COOKIES, Reason.UNSUPPORTED_TYPE);
        }
    }

    private void setDefaultHeaders(final LinkedHashMap<String, Object> headersItem)
    {
        final Object headersObject = headersItem.get(HEADERS);
        if (headersObject instanceof String)
        {
            final String headers = (String) headersObject;
            if (headers.equals(DELETE))
            {
                actionBuilder.setDefaultHeaders(Collections.<NameValuePair> emptyList());
            }
            else
            {
                ParameterUtils.doThrow(HEADERS, headers, Reason.UNSUPPORTED_VALUE);
            }
        }
        else if (headersObject instanceof ArrayList)
        {
            final List<Object> objectList = (ArrayList) headersObject;
            final List<NameValuePair> newList = new ArrayList<NameValuePair>();
            for (final Object object : objectList)
            {
                ParameterUtils.isLinkedHashMapParam(object, HEADERS);
                final LinkedHashMap<Object, Object> lhm = (LinkedHashMap<Object, Object>) object;
                final NameValuePair nvp = createPairfromLinkedHashMap(lhm);
                newList.add(nvp);
            }
            actionBuilder.setHeaders(newList);
        }
        else
        {
            ParameterUtils.doThrow(HEADERS, Reason.UNSUPPORTED_TYPE);
        }
    }

    private void setDefaultStatic(final LinkedHashMap<String, Object> headersItem)
    {
        final Object staticObject = headersItem.get(STATIC);
        if (staticObject instanceof String)
        {
            final String staticString = (String) staticObject;
            if (staticString.equals(DELETE))
            {
                this.d_static = Collections.emptyList();
            }
            else
            {
                ParameterUtils.doThrow(STATIC, staticString, Reason.UNSUPPORTED_VALUE);
            }
        }
        else if (staticObject instanceof ArrayList)
        {
            final List<Object> objectList = (ArrayList) staticObject;
            final List<String> newList = new ArrayList<String>();
            for (final Object object : objectList)
            {
                ParameterUtils.isString(object, STATIC);
                final String staticUrl = (String) object;
                newList.add(staticUrl);
            }
            this.d_static = newList;
        }
        else
        {
            ParameterUtils.doThrow(HEADERS, Reason.UNSUPPORTED_TYPE);
        }
    }

    private void setDynamicStoreVariables(final LinkedHashMap<String, Object> headersItem)
    {
        final Object storeObject = headersItem.get(STORE);
        if (storeObject instanceof String)
        {
            final String storeString = (String) storeObject;
            if (storeString.equals(DELETE))
            {
                XltLogger.runTimeLogger.warn("CANNOT DELETE DATA IN STORE (YET)");
            }
            else
            {
                ParameterUtils.doThrow(STORE, storeString, Reason.UNSUPPORTED_VALUE);
            }
        }
        else if (storeObject instanceof ArrayList)
        {
            final List<Object> objectList = (ArrayList) storeObject;
            final List<NameValuePair> newList = new ArrayList<NameValuePair>();
            for (final Object object : objectList)
            {
                ParameterUtils.isLinkedHashMapParam(object, STORE);
                final LinkedHashMap<Object, Object> lhm = (LinkedHashMap<Object, Object>) object;
                final NameValuePair nvp = createPairfromLinkedHashMap(lhm);
                System.err.println(nvp.getName() + " : " + nvp.getValue());
                try
                {
                    this.interpreter.set(nvp);
                }
                catch (final EvalError e)
                {
                    // We just Set Values, so NP
                }
            }
        }
        else
        {
            ParameterUtils.doThrow(STORE, Reason.UNSUPPORTED_TYPE);
        }
    }

    private void handleSingleActionListItem(final LinkedHashMap<String, Object> listItem)
    {
        /*
         * final Object object = data.get(ACTION); if (object == null) { System.err.println("EMPTY"); break; }
         * checkForLinkedHashMap(object, getNotOrganizedAsHashMapMessage(ACTION)); final LinkedHashMap<String, Object>
         * rawAction = (LinkedHashMap<String, Object>) object; final URLAction action = createURLAction(rawAction);
         * actions.add(action); handleSubrequests(rawAction, action);
         */
    }

    private String determineTagName(final LinkedHashMap<String, Object> tag)
    {
        return getNameOfFirstElementFromLinkedHashMap(tag);
    }

    private NameValuePair createPairfromLinkedHashMap(final LinkedHashMap<Object, Object> lhm)
    {
        final Set<?> entrySet = lhm.entrySet();
        final Iterator<?> it = entrySet.iterator();
        final Map.Entry<?, ?> entry = (Map.Entry<?, ?>) it.next();
        final String key = entry.getKey() != null ? entry.getKey().toString() : null;
        final String value = entry.getValue() != null ? entry.getValue().toString()
                                                     : null;
        final NameValuePair nvp = new NameValuePair(key, value);
        return nvp;
    }

    private String getNameOfFirstElementFromLinkedHashMap(final LinkedHashMap<String, Object> lhm)
    {
        final Set<?> entrySet = lhm.entrySet();
        final Iterator<?> it = entrySet.iterator();
        final Map.Entry<?, ?> entry = (Map.Entry<?, ?>) it.next();
        final String name = entry.getKey().toString();
        return name;
    }

}
