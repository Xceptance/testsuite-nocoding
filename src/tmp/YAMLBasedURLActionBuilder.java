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

import org.yaml.snakeyaml.Yaml;

import bsh.EvalError;

import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.common.util.bsh.ParameterInterpreter;

public class YAMLBasedURLActionBuilder extends URLActionDataBuilder
{

    /*
     * Accepted syntactic keys for the yaml data
     */
    private static final String ACTION = "Action";

    private static final String REQUEST = "Request";

    private static final String RESPONSE = "Response";

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

    private static final String HEADER = "Headers";

    private static final String DELETE = "delete";

    private String actionInCreation;

    /*
     * Default values
     */
    private String d_name = null;

    private String d_method = null;

    private String d_xhr = null;

    private String d_encoded = null;

    private String d_httpcode = null;

    private String d_url = null;

    private List<NameValuePair> d_parameters = new ArrayList<NameValuePair>();

    private List<NameValuePair> d_cookies = new ArrayList<NameValuePair>();

    private List<NameValuePair> d_headers = new ArrayList<NameValuePair>();

    private List<String> d_static = new ArrayList<String>();

    public void outline()
    {
        if (d_name != null)
            System.err.println("Name: " + d_name);
        if (d_method != null)
            System.err.println("Method: " + d_method);
        if (d_xhr != null)
            System.err.println("Xhr: " + d_xhr);
        if (d_encoded != null)
            System.err.println("Encoded: " + d_encoded);
        if (d_httpcode != null)
            System.err.println("Httpcode: " + d_httpcode);
        if (d_url != null)
            System.err.println("Url: " + d_url);

        if (!d_parameters.isEmpty())
        {
            System.err.println("Parameters: ");
            for (NameValuePair nvp : d_parameters)
            {
                System.err.println("\t" + nvp.getName() + " : " + nvp.getValue());
            }
        }
        if (!d_cookies.isEmpty())
        {
            System.err.println("Cookies: ");
            for (NameValuePair nvp : d_cookies)
            {
                System.err.println("\t" + nvp.getName() + " : " + nvp.getValue());
            }
        }
        if (!d_headers.isEmpty())
        {
            System.err.println("Headers: ");
            for (NameValuePair nvp : d_headers)
            {
                System.err.println("\t" + nvp.getName() + " : " + nvp.getValue());
            }
        }
        if (!d_static.isEmpty())
        {
            System.err.println("Static: ");
            for (String s : d_static)
            {
                System.err.println("\t" + s);
            }
        }
        if (!interpreter.isEmpty())
        {
            System.err.println("Stored Values:");
            interpreter.outline();
        }
    }

    public YAMLBasedURLActionBuilder(final String filePath, ParameterInterpreter interpreter)
    {
        super(filePath, interpreter);
    }

    public List<URLActionData> buildURLActions() throws Exception
    {
        List<Object> dataList = loadDataFromFile();
        createActionList(dataList);

        return this.actions;
    }

    @SuppressWarnings("unchecked")
    private List<Object> loadDataFromFile() throws FileNotFoundException
    {

        List<Object> dataList = Collections.emptyList();

        InputStream input = new FileInputStream(new File(this.filePath));
        Yaml yaml = new Yaml();
        Object o = yaml.load(input);

        if (o != null)
        {
            checkForArrayList(o, getNotOrganizedAsListMessage(ACTION));
            dataList = (List<Object>) o;
            XltLogger.runTimeLogger.info(MessageFormat.format("[YAMLBasedURLActionBuilder] Loading data from file: \"{0}\" ", this.filePath));
        }
        else
        {
            XltLogger.runTimeLogger.warn(MessageFormat.format("[YAMLBasedURLActionBuilder] Empty file: \"{0}\" ", this.filePath));
        }
        return dataList;
    }

    @SuppressWarnings("unchecked")
    private void createActionList(List<Object> dataList)
    {
        for (Object dataObject : dataList)
        {
            if (dataObject instanceof LinkedHashMap)
            {
                LinkedHashMap<String, Object> data = (LinkedHashMap<String, Object>) dataObject;
                handleComplexData(data);
            }
            else if (dataObject instanceof String)
            {
                String data = (String) dataObject;
                if (data.equals(ACTION))
                {
                    createActionFromDefaultValues();
                }
                else
                {
                    throw new IllegalArgumentException(getIllegalTagMessage(data));
                }
            }
            else
            {
                throw new IllegalArgumentException(getIllegalObjectMessage(dataObject));
            }
        }
    }

    /*
     * Creates URLActions from default values
     */
    private void createActionFromDefaultValues()
    {
        if (d_name == null)
        {
            throw new IllegalArgumentException(getMissingTagMessage(NAME));
        }
        if (d_url == null)
        {
            throw new IllegalArgumentException(getMissingTagMessage(URL));
        }
        URLActionData action = new URLActionData(d_name, d_url, interpreter);
        action.setEncodeParameters(d_encoded);
        action.setCookies(d_cookies);
        action.setParameters(d_parameters);
        action.setHttpResponceCode(d_httpcode);
        if (d_xhr != null)
        {
            if (d_xhr.equalsIgnoreCase("true"))
            {
                action.setType(URLActionData.TYPE_XHR);
            }
            else if (d_xhr.equalsIgnoreCase("false"))
            {
                action.setType(URLActionData.TYPE_ACTION);
            }
            else
            {
                action.setType(URLActionData.TYPE_ACTION);
                //info
            }
            action.setHeaders(d_headers);
            action.setMethod(d_method);
            actions.add(action);
            if (!d_static.isEmpty())
            {
                for (String url : d_static)
                {
                    String nameSubAction = d_name + "- Static";
                    URLActionData subAction = new URLActionData(nameSubAction, url, interpreter);
                    actions.add(subAction);
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void handleComplexData(LinkedHashMap<String, Object> data)
    {
        String tag = getNameOfFirstElementFromLinkedHashMap(data);
        switch (tag)
        {
            case ACTION:
                Object object = data.get(ACTION);
                if (object == null)
                {
                    System.err.println("EMPTY");
                    break;
                }
                checkForLinkedHashMap(object, getNotOrganizedAsHashMapMessage(ACTION));
                LinkedHashMap<String, Object> rawAction = (LinkedHashMap<String, Object>) object;
                URLActionData action = createURLAction(rawAction);
                actions.add(action);
                handleSubrequests(rawAction, action);
                break;

            case NAME:
                setDefaultSingleValue(NAME, data);
                break;
            case HTTPCODE:
                setDefaultSingleValue(HTTPCODE, data);
                break;
            case URL:
                setDefaultSingleValue(URL, data);
                break;
            case STORE:
                try
                {
                    setDynamicStore(data);
                }
                catch (EvalError e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                break;
            case METHOD:
                setDefaultSingleValue(METHOD, data);
                break;
            case ENCODED:
                setDefaultSingleValue(ENCODED, data);
                break;
            case XHR:
                setDefaultSingleValue(XHR, data);
                break;
            case PARAMETERS:
                setDefaultLinkedHashMapValue(PARAMETERS, data);
                break;
            case COOKIES:
                setDefaultLinkedHashMapValue(COOKIES, data);
                break;
            case STATIC:
                setDefaultListValue(STATIC, data);
                break;
            case HEADER:
                setDefaultLinkedHashMapValue(HEADER, data);
                break;
            default:
                XltLogger.runTimeLogger.warn(MessageFormat.format("Ignore invali list argument : \"{0}\", ignore", tag));
        }
        this.outline();
    }

    @SuppressWarnings("unchecked")
    private void setDefaultListValue(String tag, LinkedHashMap<String, Object> data)
    {
        Object o = data.get(tag);
        if (o != null)
        {
            if (o instanceof String && ((String) o).equals(DELETE))
            {
                setDefaultValueToList(tag, Collections.emptyList());
                // do
            }
            else
            {

                checkForArrayList(o, "");
                List<String> defaultList = new ArrayList<String>();
                List<Object> objectList = (List<Object>) o;
                for (Object object : objectList)
                {
                    checkForString(object, " ");
                    String defaultString = (String) object;
                    defaultList.add(defaultString);
                }
                setDefaultValueToList(tag, defaultList);
                // do
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void setDefaultValueToList(String tag, List list)
    {
        switch (tag)
        {
            case STATIC:
                this.d_static = list;
                break;
        }
    }

    @SuppressWarnings(
        {
            "unchecked", "rawtypes"
        })
    private void setDefaultLinkedHashMapValue(String tag, LinkedHashMap<String, Object> data)
    {
        Object o = data.get(tag);
        if (o != null)
        {
            if (o instanceof String && ((String) o).equals(DELETE))
            {
                setDefaultToLinkedHashMap(tag, Collections.EMPTY_LIST);
                XltLogger.runTimeLogger.info(removeDefaultMessage(tag));
            }
            else
            {
                setDefaultToLinkedHashMap(tag, Collections.EMPTY_LIST);
                XltLogger.runTimeLogger.info(setDefaultMessage(tag));
                List<Object> objectList = (ArrayList) o;
                List<NameValuePair> newList = new ArrayList<NameValuePair>();
                for (Object object : objectList)
                {
                    checkForLinkedHashMap(object, getNotOrganizedAsHashMapMessage(tag));
                    LinkedHashMap<Object, Object> lhm = (LinkedHashMap<Object, Object>) object;
                    NameValuePair nvp = createPairfromLinkedHashMap(lhm);
                    newList.add(nvp);
                }
                setDefaultToLinkedHashMap(tag, newList);
            }
        }
    }

    private void setDefaultToLinkedHashMap(String tag, List<NameValuePair> list)
    {
        switch (tag)
        {
            case PARAMETERS:
                d_parameters = list;
                break;
            case COOKIES:
                d_cookies = list;
                break;
            case HEADER:
                d_headers = list;
                break;
        }
    }

    private void setDefaultSingleValue(String tag, LinkedHashMap<String, Object> data)
    {
        Object o = data.get(tag);
        if (o != null)
        {
            checkForString(o, getisNotOfTypeMessage(tag, "Default", "String"));
            String value = (String) o;
            if (value.equals(DELETE))
            {
                XltLogger.runTimeLogger.info(removeDefaultMessage(tag));
                setDefaultValueToTag(null, tag);
            }
            else
            {
                setDefaultValueToTag(value, tag);
                XltLogger.runTimeLogger.info(setDefaultMessage(value, tag));
            }
        }
    }

    private void setDefaultValueToTag(String value, String tag)
    {
        switch (tag)
        {
            case URL:
                d_url = value;
                break;
            case NAME:
                d_name = value;
                break;
            case METHOD:
                d_method = value;
                break;
            case ENCODED:
                d_encoded = value;
                break;
            case XHR:
                d_xhr = value;
                break;
            case HTTPCODE:
                d_httpcode = value;
                break;
            default:
                // do
        }
    }

    @SuppressWarnings(
        {
            "unchecked", "rawtypes"
        })
    private void setDynamicStore(LinkedHashMap<String, Object> data) throws EvalError
    {
        Object o = data.get(STORE);
        if (o != null)
        {
            if (o instanceof String && ((String) o).equals(DELETE))
            {
                XltLogger.runTimeLogger.warn("CANNOT DELETE DATA IN STORE - MISSING FEATRURE");
            }
            else
            {
                checkForArrayList(o, getNotOrganizedAsListMessage(STORE));
                List<Object> store = (ArrayList) o;
                for (Object itemObject : store)
                    try
                    {
                        {
                            checkForLinkedHashMap(itemObject, getNotOrganizedAsHashMapMessage(STORE));
                            LinkedHashMap<Object, Object> lhm = (LinkedHashMap<Object, Object>) itemObject;
                            NameValuePair nvp = createPairfromLinkedHashMap(lhm);
                            interpreter.addVariables(nvp);
                            XltLogger.runTimeLogger.info(setDynamicDataMassage(nvp.getName(), nvp.getValue()));
                        }
                    }
                    catch (Exception e)
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void handleSubrequests(LinkedHashMap<String, Object> rawAction, URLActionData action)
    {
        Object subrequestObject = rawAction.get(SUBREQUESTS);
        if (subrequestObject != null)
        {
            checkForArrayList(subrequestObject, getNotOrganizedAsListMessage(SUBREQUESTS, action.getName()));

            List<Object> subrequests = (List<Object>) subrequestObject;

            for (Object subrequestItem : subrequests)
            {
                checkForLinkedHashMap(subrequestItem, getNotOrganizedAsHashMapMessage(SUBREQUESTS, action.getName()));

                LinkedHashMap<String, Object> subrequest = (LinkedHashMap<String, Object>) subrequestItem;
                createSubrequest(subrequest, action);
            }
        }

    }

    @SuppressWarnings("unchecked")
    private void createSubrequest(LinkedHashMap<String, Object> subrequest, URLActionData action)
    {
        Object staticSubrequestObject = subrequest.get(STATIC);
        if (staticSubrequestObject != null)
        {
            checkForArrayList(staticSubrequestObject,
                              getNotOrganizedAsListMessage("Static Subrequest", action.getName()));

            List<Object> staticSubrequest = (List<Object>) staticSubrequestObject;
            handleStaticSubrequests(action, staticSubrequest);
        }
        else if (d_static != null)
        {
            for (int i = 0; i < d_static.size(); i++)
            {
                String url = d_static.get(i);
                String name = action.getName() + " static-subrequest-" + i;
                URLActionData staticAction = new URLActionData(name, url, interpreter);
                actions.add(staticAction);
            }
        }
        Object xhrSubrequestObject = subrequest.get(XHR);
        if (xhrSubrequestObject != null)
        {
            checkForLinkedHashMap(xhrSubrequestObject,
                                  getNotOrganizedAsHashMapMessage("XHR Subrequests", action.getName()));

            LinkedHashMap<String, Object> xhrSubrequest = (LinkedHashMap<String, Object>) xhrSubrequestObject;
            handleXhrSubrequests(action, xhrSubrequest);
        }
    }

    private void handleStaticSubrequests(URLActionData action, List<Object> staticUrls)
    {
        for (int i = 0; i < staticUrls.size(); i++)
        {
            Object o = staticUrls.get(i);
            checkForString(o, getisNotOfTypeMessage("STATIC Subrequest", action.getName(), "String"));
            String urlString = (String) o;
            String name = action.getName() + "- StaticSubrequest" + i;
            URLActionData staticAction = new URLActionData(name, urlString, interpreter);
            // staticAction.setEncoded(action.isEncoded());
            staticAction.setType(URLActionData.TYPE_STATIC);
            staticAction.setMethod(URLActionData.METHOD_GET);
            actions.add(staticAction);
        }
    }

    private void handleXhrSubrequests(final URLActionData action, LinkedHashMap<String, Object> rawXhrAction)
    {

        String name = getNameFromRawAction(rawXhrAction);
        String url = getUrlFromRawAction(rawXhrAction);

        URLActionData xhrAction = new URLActionData(name, url, interpreter);

        xhrAction.setType(URLActionData.TYPE_XHR);

        LinkedHashMap<String, Object> rawRequest = getRequestFromRawAction(rawXhrAction);
        fillActionWithEncodedData(xhrAction, rawRequest);
        fillActionWithMethodData(xhrAction, rawRequest);
        fillActionWithParameterData(xhrAction, rawRequest);
        fillActionWithOptionalResponseData(xhrAction, rawXhrAction);

        actions.add(xhrAction);

        handleSubrequests(rawXhrAction, xhrAction);
    }

    // fills YAMLBasedURLActionRAW objects with data from rawActionList
    private URLActionData createURLAction(LinkedHashMap<String, Object> rawAction)
    {
        // Necessary data for the creation of an URLAction
        String name = getNameFromRawAction(rawAction);
        String urlString = getUrlFromRawAction(rawAction);

        URLActionData action = new URLActionData(name, urlString, interpreter);

        // optional data for an URLAction

        fillActionWithOptionalRequestData(action, rawAction);

        fillActionWithOptionalResponseData(action, rawAction);

        return action;
    }

    private String getNameFromRawAction(LinkedHashMap<String, Object> rawAction)
    {
        String name;

        Object o = rawAction.get(NAME);

        if (o != null)
        {
            checkForString(o, getMissingTagOrIllegalValueMessage(NAME));
            name = (String) o;
            actionInCreation = name;
        }
        else if (d_name != null)
        {
            name = d_name;
        }
        else
        {
            throw new IllegalArgumentException("");
        }
        return name;
    }

    @SuppressWarnings("unchecked")
    private String getUrlFromRawAction(LinkedHashMap<String, Object> rawAction)
    {
        String url = null;
        Object o = rawAction.get(REQUEST);
        if (o != null)
        {
            checkForLinkedHashMap(o, " ");
            LinkedHashMap<String, Object> request = (LinkedHashMap<String, Object>) o;
            o = request.get(URL);
            if (o != null)
            {
                checkForString(o, "");
                url = (String) o;
            }
        }
        else if (d_url != null)
        {
            url = d_url;
        }
        else
        {
            throw new IllegalArgumentException("url cannot be null");
        }
        return url;
    }

    @SuppressWarnings("unchecked")
    private LinkedHashMap<String, Object> getRequestFromRawAction(LinkedHashMap<String, Object> rawAction)
    {
        Object o = rawAction.get(REQUEST);
        checkForLinkedHashMap(o, getNotOrganizedAsHashMapMessage(REQUEST, actionInCreation));
        LinkedHashMap<String, Object> rawRequest = (LinkedHashMap<String, Object>) o;
        return rawRequest;

    }

    private String getURLStringFromRawRequest(LinkedHashMap<String, Object> rawRequest)
    {
        Object o = rawRequest.get(URL);
        checkForString(o, getMissingTagOrIllegalValueMessage(URL, actionInCreation));
        String urlString = (String) o;
        return urlString;
    }

    @SuppressWarnings("unchecked")
    private void fillActionWithOptionalRequestData(URLActionData action, LinkedHashMap<String, Object> rawAction)
    {
        Object o = rawAction.get(REQUEST);
        if (o != null)
        {
            checkForLinkedHashMap(o, "");
            LinkedHashMap<String, Object> rawRequest = (LinkedHashMap<String, Object>) o;

            fillActionWithEncodedData(action, rawRequest);
            fillActionWithMethodData(action, rawRequest);
            fillActionWithXHRData(action, rawRequest);
            fillActionWithParameterData(action, rawRequest);
            fillActionWithHeaderData(action, rawRequest);
            fillActionWithCookieData(action, rawRequest);
        }
    }

    @SuppressWarnings("unchecked")
    private void fillActionWithHeaderData(URLActionData action, LinkedHashMap<String, Object> rawRequest)
    {
        Object headersObject = rawRequest.get(HEADER);
        if (headersObject == null && d_headers != null)
        {
            action.setHeaders(d_headers);
        }
        else if (headersObject != null)
        {
            checkForArrayList(headersObject, getNotOrganizedAsListMessage(HEADER, action.getName()));
            List<Object> headers = (List<Object>) headersObject;
            fillActionWithHeaders(action, headers);
        }
    }

    @SuppressWarnings("unchecked")
    private void fillActionWithHeaders(URLActionData action, List<Object> parameterList)
    {
        for (Object parameterListItem : parameterList)
        {
            checkForLinkedHashMap(parameterListItem, getNotOrganizedAsHashMapMessage("Parameter", action.getName()));
            LinkedHashMap<Object, Object> parameterItemLinkedHashMap = (LinkedHashMap<Object, Object>) parameterListItem;

            NameValuePair nvp = createPairfromLinkedHashMap(parameterItemLinkedHashMap);
            action.addHeader(nvp);
        }
    }

    @SuppressWarnings("unchecked")
    private void fillActionWithCookieData(URLActionData action, LinkedHashMap<String, Object> rawRequest)
    {
        Object cookiesObject = rawRequest.get(COOKIES);
        if (cookiesObject == null && d_cookies != null)
        {
            action.setCookies(d_cookies);
        }
        else if (cookiesObject != null)
        {
            checkForArrayList(cookiesObject, getNotOrganizedAsListMessage(COOKIES, action.getName()));
            List<Object> cookies = (List<Object>) cookiesObject;
            fillActionWithCookies(action, cookies);
        }

    }

    @SuppressWarnings("unchecked")
    private void fillActionWithCookies(URLActionData action, List<Object> parameterList)
    {
        for (Object parameterListItem : parameterList)
        {
            checkForLinkedHashMap(parameterListItem, getNotOrganizedAsHashMapMessage("Parameter", action.getName()));
            LinkedHashMap<Object, Object> parameterItemLinkedHashMap = (LinkedHashMap<Object, Object>) parameterListItem;

            NameValuePair nvp = createPairfromLinkedHashMap(parameterItemLinkedHashMap);
            action.addCookie(nvp);
        }
    }

    private void fillActionWithEncodedData(URLActionData action, LinkedHashMap<String, Object> rawRequest)
    {
        Object o = rawRequest.get(ENCODED);
        if (o == null && d_encoded != null)
        {
            action.setEncodeParameters(d_encoded);
        }
        else if (o instanceof String)
        {
            String encoded = (String) o;
            action.setEncodeParameters(encoded);
        }
        else
        {
            XltLogger.runTimeLogger.warn(getDefaultingLog(ENCODED, action.getName(), "false"));
        }
    }

    private void fillActionWithMethodData(URLActionData action, LinkedHashMap<String, Object> rawRequest)
    {
        Object o = rawRequest.get(METHOD);
        if (o == null && d_method != null)
        {
            action.setMethod(d_method);
        }
        else if (o instanceof String)
        {
            String method = (String) o;
            if (method.equals(URLActionData.METHOD_GET))
            {
                action.setMethod(URLActionData.METHOD_GET);
            }
            else if (method.equals(URLActionData.METHOD_POST))
            {
                action.setMethod(URLActionData.METHOD_POST);
            }
            else
            {
                XltLogger.runTimeLogger.warn(getDefaultingLog(METHOD, action.getName(), URLActionData.METHOD_GET));
                action.setMethod(URLActionData.METHOD_GET);
            }
        }
        else
        {
            XltLogger.runTimeLogger.warn(getDefaultingLog(METHOD, action.getName(), URLActionData.METHOD_GET));
            action.setMethod(URLActionData.METHOD_GET);
        }
    }

    private void fillActionWithXHRData(URLActionData action, LinkedHashMap<String, Object> rawRequest)
    {
        Object o = rawRequest.get(XHR);
        if (o == null && d_xhr != null)
        {
            if (d_xhr.equals("true"))
            {
                action.setType(URLActionData.TYPE_XHR);
            }
            else if (d_xhr.equals("false"))
            {
                action.setType(URLActionData.TYPE_ACTION);
            }
            else
            {
                // default
            }

        }
        else if (o instanceof Boolean)
        {
            Boolean xhr = (Boolean) o;
            if (xhr == true)
            {
                action.setType(URLActionData.TYPE_XHR);
            }
            else
            {
                action.setType(URLActionData.TYPE_ACTION);
            }
        }
        else
        {
            XltLogger.runTimeLogger.warn(getDefaultingLog(XHR, action.getName(), "false"));
        }
    }

    @SuppressWarnings("unchecked")
    private void fillActionWithParameterData(URLActionData action, LinkedHashMap<String, Object> rawRequest)
    {
        Object parametersObject = rawRequest.get(PARAMETERS);
        if (parametersObject == null && d_parameters != null)
        {
            action.setParameters(d_parameters);
        }
        else if (parametersObject != null)
        {
            checkForArrayList(parametersObject, getNotOrganizedAsListMessage(PARAMETERS, action.getName()));
            List<Object> parameters = (List<Object>) parametersObject;
            fillActionWithParameters(action, parameters);
        }
    }

    @SuppressWarnings("unchecked")
    private void fillActionWithParameters(URLActionData action, List<Object> parameterList)
    {
        for (Object parameterListItem : parameterList)
        {
            checkForLinkedHashMap(parameterListItem, getNotOrganizedAsHashMapMessage("Parameter", action.getName()));
            LinkedHashMap<Object, Object> parameterItemLinkedHashMap = (LinkedHashMap<Object, Object>) parameterListItem;

            NameValuePair nvp = createPairfromLinkedHashMap(parameterItemLinkedHashMap);
            action.addParameter(nvp);
        }
    }

    private void fillActionWithOptionalResponseData(URLActionData action, LinkedHashMap<String, Object> rawAction)
    {
        LinkedHashMap<String, Object> rawResponse = getResponseFromRawAction(rawAction);
        fillActionWithHttpCodeData(action, rawResponse);
        fillActionWithValidationData(action, rawResponse);
        handleStoreData(action, rawResponse);
    }

    @SuppressWarnings("unchecked")
    private void handleStoreData(URLActionData action, LinkedHashMap<String, Object> rawResponse)
    {
        Object storeObject = rawResponse.get(STORE);
        if (storeObject != null)
        {
            checkForArrayList(storeObject, "");
            List<Object> storeList = (List<Object>) storeObject;
            for (Object listItem : storeList)
            {
                checkForLinkedHashMap(listItem, "");

                LinkedHashMap<String, Object> storeItem = (LinkedHashMap<String, Object>) listItem;
                String name = getNameOfFirstElementFromLinkedHashMap(storeItem);
                Object innerObject = storeItem.get(name);
                checkForLinkedHashMap(innerObject, "");

                LinkedHashMap<Object, Object> lhm = (LinkedHashMap<Object, Object>) innerObject;
                NameValuePair nvp = createPairfromLinkedHashMap(lhm);
                URLActionDataStore uas = new URLActionDataStore(name, nvp.getName(), nvp.getValue(), interpreter);
                action.addStore(uas);

            }
        }
    }

    @SuppressWarnings("unchecked")
    private LinkedHashMap<String, Object> getResponseFromRawAction(LinkedHashMap<String, Object> rawAction)
    {
        LinkedHashMap<String, Object> rawResponse = new LinkedHashMap<String, Object>();

        Object o = rawAction.get(RESPONSE);
        if (o != null)
        {
            checkForLinkedHashMap(o, getMissingTagOrIllegalValueMessage(RESPONSE, actionInCreation));
            rawResponse = (LinkedHashMap<String, Object>) o;
        }
        return rawResponse;
    }

    private void fillActionWithHttpCodeData(URLActionData action, LinkedHashMap<String, Object> rawResponse)
    {
        Object o = rawResponse.get(HTTPCODE);
        if (o != null)
        {
            checkForString(o, "");
            String httpcode = (String) o;
            action.setHttpResponceCode(httpcode);
        }
        else if (o == null & d_httpcode != null)
        {
            action.setHttpResponceCode(d_httpcode);
        }
        else
        {
            XltLogger.runTimeLogger.warn(getDefaultingLog(HTTPCODE, action.getName(), "200"));
        }
    }

    @SuppressWarnings("unchecked")
    private void fillActionWithValidationData(URLActionData action, LinkedHashMap<String, Object> rawResponse)
    {
        Object validationsObject = rawResponse.get(VALIDATION);
        if (validationsObject != null)
        {
            checkForArrayList(validationsObject, getNotOrganizedAsListMessage(VALIDATION, action.getName()));
            List<Object> validations = (List<Object>) validationsObject;
            for (Object validationObject : validations)
            {
                checkForLinkedHashMap(validationObject, getNotOrganizedAsHashMapMessage(VALIDATION, action.getName()));
                LinkedHashMap<String, Object> validationItem = (LinkedHashMap<String, Object>) validationObject;

                URLActionDataValidation validation = createValidation(validationItem);

                action.addValidation(validation);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private URLActionDataValidation createValidation(LinkedHashMap<String, Object> rawValidation)
    {
        URLActionDataValidation validation = null;

        String name = getNameOfFirstElementFromLinkedHashMap(rawValidation);

        if (name != null)
        {
            Object rawValidateSubObject = rawValidation.get(name);
            if (rawValidateSubObject != null)
            {
                checkForLinkedHashMap(rawValidateSubObject, getNotOrganizedAsHashMapMessage("Validate Subitem"));

                LinkedHashMap<String, Object> validateListSubItem = (LinkedHashMap<String, Object>) rawValidateSubObject;

                validation = createValidationFromLinkedHashMap(validateListSubItem, name);
            }
            else
            {
                throw new IllegalArgumentException(getMissingTagOrIllegalValueMessage("Validation Subitem"));
            }
        }
        else
        {
            throw new IllegalArgumentException(getMissingTagOrIllegalValueMessage("Validation Name"));
        }

        return validation;
    }

    // super gross function
    private URLActionDataValidation createValidationFromLinkedHashMap(LinkedHashMap<String, Object> rawValidation,
                                                                  String validationName)
    {
        Set<?> entrySet = rawValidation.entrySet();
        Iterator<?> it = entrySet.iterator();
        Map.Entry<?, ?> entry = (Map.Entry<?, ?>) it.next();
        String selectionMode = (String) entry.getKey();
        String selectionContent = (String) entry.getValue();
        String validationMode = null;
        String validationContent = null;

        if (it.hasNext())
        {
            entry = (Map.Entry<?, ?>) it.next();
            validationMode = entry.getKey().toString();
            validationContent = entry.getValue().toString();
        }
        if (validationMode == null)
        {
            validationMode = URLActionDataValidation.EXISTS;
        }
        URLActionDataValidation validation = new URLActionDataValidation(validationName, selectionMode, selectionContent,
                                                                 validationMode, validationContent, interpreter);
        return validation;
    }

    @SuppressWarnings("unchecked")
    private List<Object> getStoreFromRawAction(LinkedHashMap<String, Object> rawAction)
    {
        List<Object> rawStore = Collections.emptyList();
        Object o = rawAction.get(STORE);
        if (o != null)
        {
            checkForArrayList(o, getNotOrganizedAsListMessage(STORE, actionInCreation));
            rawStore = (List<Object>) o;
        }
        return rawStore;
    }

    private NameValuePair createPairfromLinkedHashMap(LinkedHashMap<Object, Object> lhm)
    {
        Set<?> entrySet = lhm.entrySet();
        Iterator<?> it = entrySet.iterator();
        Map.Entry<?, ?> entry = (Map.Entry<?, ?>) it.next();
        String key = entry.getKey() != null ? entry.getKey().toString() : null;
        String value = entry.getValue() != null ? entry.getValue().toString() : null;
        NameValuePair nvp = new NameValuePair(key, value);
        return nvp;
    }

    private String getNameOfFirstElementFromLinkedHashMap(LinkedHashMap<String, Object> lhm)
    {
        Set<?> entrySet = lhm.entrySet();
        Iterator<?> it = entrySet.iterator();
        Map.Entry<?, ?> entry = (Map.Entry<?, ?>) it.next();
        String name = entry.getKey().toString();
        return name;
    }

    private void checkForArrayList(Object o, String errorMessage)
    {
        if (!(o instanceof ArrayList))
        {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    private void checkForLinkedHashMap(Object o, String errorMessage)
    {
        if (!(o instanceof LinkedHashMap))
        {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    private void checkForString(Object o, String errorMessage)
    {
        if (!(o instanceof String))
        {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    private String getDefaultingLog(String tag, String actionName, String defaultValue)
    {
        String message = MessageFormat.format("[YAMLBasedURLActionBuilder] Missing tag: \"{0}\" or illegal value for \"{0}\" in Action: \"{1}\", File: \"{2}\". Defaulting \"{0}\" to \"{3}\" ",
                                              tag, actionName, filePath, defaultValue);
        return message;
    }

    private String getMissingTagMessage(String tag)
    {
        String message = MessageFormat.format("[YAMLBasedURLActionBuilder] Missing tag: \"{0}\" in File : \"{1}\". \n Cannot create Action", tag,
                                              filePath);
        return message;
    }

    private String getDefaultingLog(String tag, String defaultValue)
    {
        String message = MessageFormat.format("[YAMLBasedURLActionBuilder] Missing tag: \"{0}\" or illegal value for \"{0}\" in File: \"{1}\". Defaulting \"{0}\" to \"{2}\" ",
                                              tag, filePath, defaultValue);
        return message;
    }

    private String getMissingTagOrIllegalValueMessage(String tag)
    {
        String message = MessageFormat.format("[YAMLBasedURLActionBuilder] Missing tag: \"{0}\" or illegal value for \"{0}\" in {1}", tag, filePath);
        return message;
    }

    private String getIllegalValueMessage(String tag)
    {
        String message = MessageFormat.format("[YAMLBasedURLActionBuilder] Illegal value for \"{0}\" in {1}", tag, filePath);
        return message;
    }

    private String getIllegalTagMessage(String tag)
    {
        String message = MessageFormat.format("[YAMLBasedURLActionBuilder] Illegal tag \"{0}\" in {1}", tag, filePath);
        return message;
    }

    private String getIllegalObjectMessage(Object o)
    {
        String message = MessageFormat.format("[YAMLBasedURLActionBuilder] Illegal object \"{0}\" in {1}", o.toString(), filePath);
        return message;
    }

    private String getMissingTagOrIllegalValueMessage(String tag, String actionName)
    {
        String message = MessageFormat.format("[YAMLBasedURLActionBuilder] Missing tag: \"{0}\" or illegal value for \"{0}\" in Action: \"{1}\", File: \"{2}\" ",
                                              tag, actionName, filePath);
        return message;
    }

    private String getNotOrganizedAsListMessage(String tag)
    {
        String message = MessageFormat.format("[YAMLBasedURLActionBuilder] \"{0}\" data in \"{1}\" is not organized as a YAML-list!", tag,
                                              filePath);
        return message;
    }

    private String getNotOrganizedAsHashMapMessage(String tag)
    {
        String message = MessageFormat.format("[YAMLBasedURLActionBuilder] \"{0}\" data in \"{1}\" is not organized as an associative YAML-list!",
                                              tag, filePath);
        ;
        return message;
    }

    private String getNotOrganizedAsListMessage(String tag, String actionName)
    {
        String message = MessageFormat.format("[YAMLBasedURLActionBuilder] \"{0}\" data in Action: \"{1}\", File: \"{2}\" is not organized as a YAML-list!",
                                              tag, actionName, filePath);
        return message;
    }

    private String getNotOrganizedAsHashMapMessage(String tag, String actionName)
    {
        String message = MessageFormat.format("[YAMLBasedURLActionBuilder] \"{0}\" data in Action: \"{1}\", File: \"{2}\" is not organized as an associative YAML-list!",
                                              tag, actionName, filePath);
        ;
        return message;
    }

    private String getisNotOfTypeMessage(String tag, String actionName, String type)
    {
        String message = MessageFormat.format("[YAMLBasedURLActionBuilder] \"{0}\" data in Action: \"{1}\", File: \"{2}\" is not of type \"{3}\" !",
                                              tag, actionName, filePath, type);
        return message;
    }

    private String removeDefaultMessage(String tag)
    {
        String message = MessageFormat.format("[YAMLBasedURLActionBuilder] Removing default value for tag: \"{0}\"", tag);
        return message;
    }

    private String removeDefaultMessage(String tag, String value)
    {
        String message = MessageFormat.format("[YAMLBasedURLActionBuilder] Removing default value: \"{0}\" for tag: \"{1}\"", value, tag);
        return message;
    }

    private String setDefaultMessage(String tag, String value)
    {
        String message = MessageFormat.format("[YAMLBasedURLActionBuilder] Setting new default value: \"{0}\" for tag: \"{1}\"", value, tag);
        return message;
    }

    private String setDefaultMessage(String tag)
    {
        String message = MessageFormat.format("[YAMLBasedURLActionBuilder] Setting new default value for tag: \"{0}\"", tag);
        return message;
    }

    private String setDynamicDataMassage(String name, String value)
    {
        String message = MessageFormat.format("[YAMLBasedURLActionBuilder] Adding variable: \"{0} = {1} \"  for dynamic data processing", name,
                                              value);
        return message;
    }
}
