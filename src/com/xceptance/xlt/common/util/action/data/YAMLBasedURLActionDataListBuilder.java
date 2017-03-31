package com.xceptance.xlt.common.util.action.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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
import com.xceptance.xlt.common.util.Constants;
import com.xceptance.xlt.common.util.ParameterUtils;
import com.xceptance.xlt.common.util.ParameterUtils.Reason;
import com.xceptance.xlt.common.util.bsh.ParameterInterpreter;

/**
 * Implementation of the {@link URLActionDataListBuilder} for files of type 'YAML'.
 * <ul>
 * <li>Takes a file of type yaml and build a List<{@link #URLActionData}> from it.
 * <li>The syntax of the file must follow the yaml 1.1 specification.
 * <li>The structure of the data is determined within this class and described in syntax.yml
 * <li>The names of the tags, whose values should be parsed into a URLActionData are also determined here.
 * <li>Since the used yaml parser ({@link #Yaml SnakeYaml})returns a monstrous {@link #HashMap}, this class is quite
 * busy with slaughtering this HashMap in small tasty pieces, doing some nasty type checking and converting, as well as
 * error handling. Therefore the structure and quality of the code is not very charming, but it works.
 * <li>If you want to change the names of the tags, you can do it easily.
 * <li>If you want to change the general structure of the data, you better write a new Builder and think about SRP
 * (Single responsibility principle).
 * </ul>
 * 
 * @author matthias mitterreiter
 */
public class YAMLBasedURLActionDataListBuilder extends URLActionDataListBuilder
{

    protected final URLActionDataValidationBuilder validationBuilder;

    protected final URLActionDataStoreBuilder storeBuilder;

    static private final String SPECIFICATION = "YAMLSyntaxSpecification.txt";

    static private final String SEESPEC = "See " + SPECIFICATION + " for the correct Syntax!";
    
    private String actionName = null;

    /**
     * Default static URLs
     */
    private List<String> d_static = new ArrayList<String>();

    /**
     * @param filePath
     *            : path to the yaml file.
     * @param interpreter
     *            : {@link ParameterInterpreter}
     * @param actionBuilder
     *            : {@link URLActionDataBuilder}
     * @param validationBuilder
     *            :{@link URLActionDataValidationBuilder }
     * @param storeBuilder
     *            : {@link URLActionDataStoreBuilder }
     */
    public YAMLBasedURLActionDataListBuilder(final String filePath, final ParameterInterpreter interpreter,
                                             final URLActionDataBuilder actionBuilder,
                                             final URLActionDataValidationBuilder validationBuilder,
                                             final URLActionDataStoreBuilder storeBuilder)
    {
        super(filePath, interpreter, actionBuilder);

        ParameterUtils.isNotNull(storeBuilder, "URLActionStoreBuilder");
        ParameterUtils.isNotNull(validationBuilder, "URLActionStoreBuilder");

        this.storeBuilder = storeBuilder;
        this.validationBuilder = validationBuilder;
    }

    /**
     * For debugging purpose. <br>
     * 'err-streams' the attributes of the object. <br>
     */
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

        if (!actions.isEmpty())
        {
            System.err.println("URLAction actions:");
            for (final URLActionData action : actions)
            {
                action.outline();
            }
        }
    }

    /**
     * Parses the data of the yaml file into List<{@link #URLActionData}>.
     * 
     * @return List<{@link #URLActionData}>
     */
    @Override
	public List<URLActionData> buildURLActionDataList()
    {
        try
        {
            @SuppressWarnings("unchecked")
            final List<Object> dataList = (List<Object>) getOrParseData();
            createActionList(dataList);
            final List<URLActionData> copy = new ArrayList<>(actions);
            actions.clear();

            return copy;
        }
        catch (final Exception e)
        {
            throw new IllegalArgumentException(new StringBuilder("Failed to parse file '").append(filePath).append("' as YAML. ").append(e.getMessage()).toString(), e);
        }
    }
    
    
    @Override
    protected Object parseData() throws IOException
    {

    	
        final InputStream input = new FileInputStream(new File(this.filePath));
        final Yaml yaml = new Yaml();
        Object o = yaml.load(input); 
        if (o != null)
        {
            ParameterUtils.isArrayListMessage(o, "YAML-Data", "See the no-coding syntax sepecification!");
            XltLogger.runTimeLogger.info(MessageFormat.format("Loading YAML data from file: \"{0}\" ", this.filePath));
        }
        else
        {
            o = Collections.emptyList();
            XltLogger.runTimeLogger.warn(MessageFormat.format("Empty file: \"{0}\" ", this.filePath));
        }

        return o;
    }
    
    private void checkForInvalidTags (final LinkedHashMap<String, Object> lhm, String parentItemName)
    {
        final Set<?> entrySet = lhm.entrySet();
        final Iterator<?> it = entrySet.iterator();
        if (parentItemName.equals(Constants.ACTION) || parentItemName.equals(Constants.XHR))
        {
            final Object nameObject = lhm.get(Constants.NAME);
            if (nameObject == null)
            {
            	throw new IllegalArgumentException(MessageFormat.format("Name of \"{0}\" cannot be Null", parentItemName));
            }
            else
            {
            	actionName = nameObject.toString();
            }
        }

        for (int i = 0; i < entrySet.size(); i++)
        {  	      	
            final Map.Entry<?, ?> entry = (Map.Entry<?, ?>) it.next();
    		String itemName =  entry.getKey().toString();
			XltLogger.runTimeLogger.debug("Handling tag: " + itemName);
    		switch (parentItemName) {
			case Constants.ACTION:
				if (!Constants.isPermittedActionItem(itemName))
				{
					throw new IllegalArgumentException(MessageFormat.format("Invalid tag: \"{0}\" at Action \"{1}\". \"{0}\" is not a valid child of \"Action\"", itemName, actionName));
				}
				break;
			case Constants.REQUEST:
				if (!Constants.isPermittedRequestItem(itemName))
				{
					throw new IllegalArgumentException(MessageFormat.format("Invalid tag: \"{0}\" at Action \"{1}\". \"{0}\" is not a valid child of \"Request\"", itemName, actionName));
				}
				break;
			case Constants.RESPONSE:
				if (!Constants.isPermittedResponseItem(itemName))
				{
					throw new IllegalArgumentException(MessageFormat.format("Invalid tag: \"{0}\" at Action \"{1}\". \"{0}\" is not a valid child of \"Response\"", itemName, actionName));
				}
				break;
			case Constants.SUBREQUESTS:
                if (!(entrySet.size() <= 1))
                {
                	throw new IllegalArgumentException(MessageFormat.format("Incorrect syntax at item: \"Subrequest\" of Aciton: \"{0}\". Please mind the whitespaces.", actionName));
                }
                else if (!Constants.isPermittedSubRequestItem(itemName))
				{
					throw new IllegalArgumentException(MessageFormat.format("Invalid tag: \"{0}\" at Action \"{1}\". \"{0}\" is not a valid child of \"Subrequests\"", itemName, actionName));
				}
				break;
			case Constants.XHR:
				if (!Constants.isPermittedActionItem(itemName))
				{
					throw new IllegalArgumentException(MessageFormat.format("Invalid tag: \"{0}\" at Action \"{1}\". \"{0}\" is not a valid child of \"Xhr\"", itemName, actionName));
				}
				break;
			default:
				throw new IllegalArgumentException(MessageFormat.format("Key: \"{0}\" is not a valid Tag", itemName));
			} 
        }
    }
    
    @SuppressWarnings("unchecked")
    private void createActionList(final List<Object> dataList)
    {
        XltLogger.runTimeLogger.info("Start building URLAction list");
        for (final Object listObject : dataList)
        {
            ParameterUtils.isLinkedHashMapMessage(listObject, "YAML - List", SEESPEC);
            final LinkedHashMap<String, Object> listItem = (LinkedHashMap<String, Object>) listObject;
            handleListItem(listItem);
        }
        XltLogger.runTimeLogger.info("Finished building URLAction list");
    }

    private void handleListItem(final LinkedHashMap<String, Object> listItem)
    {
        final String itemName = determineTagName(listItem);
        XltLogger.runTimeLogger.debug("Handling tag: " + itemName);
        if (!Constants.isPermittedListItem(itemName))
        {
            throw new IllegalArgumentException(MessageFormat.format("Invalid list item: \"{0}\"", itemName));
        }
        switch (itemName)
        {
            case Constants.ACTION:
                handleActionListItem(listItem);
                break;
            case Constants.NAME:
                setDefaultName(listItem);
                break;
            case Constants.BODY:
                setDefaultBody(listItem);
                break;
            case Constants.HTTPCODE:
                setDefaultHttpCode(listItem);
                break;
            case Constants.URL:
                setDefaultUrl(listItem);
                break;
            case Constants.METHOD:
                setDefaultMethod(listItem);
                break;
            case Constants.ENCODEPARAMETERS:
                setDefaultEncodeParameters(listItem);
                break;
            case Constants.ENCODEBODY:
                setDefaultEncodeBody(listItem);
                break;
            case Constants.XHR:
                setDefaultXhr(listItem);
                break;
            case Constants.PARAMETERS:
                setDefaultParameters(listItem);
                break;
            case Constants.COOKIES:
                setDefaultCookies(listItem);
                break;
            case Constants.STATIC:
                setDefaultStatic(listItem);
                break;
            case Constants.HEADERS:
                setDefaultHeaders(listItem);
                break;
            case Constants.STORE:
                setDynamicStoreVariables(listItem);
                break;
            default:
                break;
        }
    }

    private void setDefaultName(final LinkedHashMap<String, Object> nameItem)
    {
        final Object nameObject = nameItem.get(Constants.NAME);
        ParameterUtils.isString(nameObject, Constants.NAME);
        final String name = (String) nameObject;
        if (name.equals(Constants.DELETE))
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
        final Object bodyObject = bodyItem.get(Constants.BODY);
        ParameterUtils.isString(bodyObject, Constants.BODY);
        final String body = (String) bodyObject;
        if (body.equals(Constants.DELETE))
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
        final Object codeObject = codeItem.get(Constants.HTTPCODE);
        if (codeObject instanceof Integer)
        {
            final Integer code = (Integer) codeObject;
            actionBuilder.setDefaultHttpResponceCode(code.toString());
        }
        else if (codeObject instanceof String)
        {
            final String code = (String) codeObject;
            if (code.equals(Constants.DELETE))
            {
                actionBuilder.setDefaultHttpResponceCode(null);
            }
            else
            {
                actionBuilder.setDefaultHttpResponceCode(code);
            }
        }
        else
        {
            ParameterUtils.doThrow(Constants.HTTPCODE, Reason.UNSUPPORTED_TYPE);
        }
    }

    private void setDefaultUrl(final LinkedHashMap<String, Object> urlItem)
    {
        final Object urlObject = urlItem.get(Constants.URL);
        ParameterUtils.isString(urlObject, Constants.URL);
        final String url = (String) urlObject;
        if (url.equals(Constants.DELETE))
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
        final Object methodObject = methodItem.get(Constants.METHOD);
        ParameterUtils.isString(methodObject, Constants.METHOD);
        final String method = (String) methodObject;
        if (method.equals(Constants.DELETE))
        {
            actionBuilder.setDefaultMethod(null);
        }
        else
        {
            actionBuilder.setDefaultMethod(method);
        }
    }

    private void setDefaultEncodeParameters(final LinkedHashMap<String, Object> encodedItem)
    {
        final Object encodedObject = encodedItem.get(Constants.ENCODEPARAMETERS);
        if (encodedObject instanceof Boolean)
        {
            final Boolean encoded = (Boolean) encodedObject;
            actionBuilder.setDefaultEncodeParameters(encoded.toString());
        }
        else if (encodedObject instanceof String)
        {
            final String encoded = (String) encodedObject;
            if (encoded.equals(Constants.DELETE))
            {
                actionBuilder.setDefaultEncodeParameters(null);
            }
            else
            {
                actionBuilder.setDefaultEncodeParameters(encoded);
            }
        }
        else
        {
            ParameterUtils.doThrow(Constants.ENCODEPARAMETERS, Reason.UNSUPPORTED_TYPE);
        }
    }

    private void setDefaultEncodeBody(final LinkedHashMap<String, Object> encodedItem)
    {
        final Object encodedObject = encodedItem.get(Constants.ENCODEBODY);
        if (encodedObject instanceof Boolean)
        {
            final Boolean encoded = (Boolean) encodedObject;
            actionBuilder.setDefaultEncodeBody(encoded.toString());
        }
        else if (encodedObject instanceof String)
        {
            final String encoded = (String) encodedObject;
            if (encoded.equals(Constants.DELETE))
            {
                actionBuilder.setDefaultEncodeBody(null);
            }
            else
            {
                actionBuilder.setDefaultEncodeBody(encoded);
            }
        }
        else
        {
            ParameterUtils.doThrow(Constants.ENCODEBODY, Reason.UNSUPPORTED_TYPE);
        }
    }

    private void setDefaultXhr(final LinkedHashMap<String, Object> xhrItem)
    {
        final Object xhrObject = xhrItem.get(Constants.XHR);
        if (xhrObject instanceof Boolean)
        {
            final Boolean xhr = (Boolean) xhrObject;
            if (xhr)
            {
                actionBuilder.setDefaultType(URLActionData.TYPE_XHR);
            }
            else
            {
                actionBuilder.setDefaultType(URLActionData.TYPE_ACTION);
            }
        }
        else if (xhrObject instanceof String)
        {
            final String xhr = (String) xhrObject;
            if (xhr.equals(Constants.DELETE))
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
            ParameterUtils.doThrow(Constants.XHR, Reason.UNSUPPORTED_TYPE);
        }
    }

    private void setDefaultParameters(final LinkedHashMap<String, Object> parametersItem)
    {
        final Object parametersObject = parametersItem.get(Constants.PARAMETERS);
        if (parametersObject instanceof String)
        {
            final String parameters = (String) parametersObject;
            if (parameters.equals(Constants.DELETE))
            {
                actionBuilder.setDefaultParameters(Collections.<NameValuePair>emptyList());
            }
            else
            {
                ParameterUtils.doThrow(Constants.PARAMETERS, parameters, Reason.UNSUPPORTED_VALUE);
            }
        }
        else if (parametersObject instanceof ArrayList)
        {
            @SuppressWarnings("unchecked")
            final List<Object> objectList = (ArrayList<Object>) parametersObject;
            final List<NameValuePair> newList = new ArrayList<NameValuePair>();
            for (final Object object : objectList)
            {
                ParameterUtils.isLinkedHashMap(object, Constants.PARAMETERS);
                @SuppressWarnings("unchecked")
                final LinkedHashMap<Object, Object> lhm = (LinkedHashMap<Object, Object>) object;
                newList.add(createPairfromLinkedHashMap(lhm));
            }
            actionBuilder.setDefaultParameters(newList);
        }
        else
        {
            ParameterUtils.doThrow(Constants.PARAMETERS, Reason.UNSUPPORTED_TYPE);
        }
    }

    private void setDefaultCookies(final LinkedHashMap<String, Object> cookiesItem)
    {
        final Object cookiesObject = cookiesItem.get(Constants.COOKIES);
        if (cookiesObject instanceof String)
        {
            final String cookies = (String) cookiesObject;
            if (cookies.equals(Constants.DELETE))
            {
                actionBuilder.setDefaultCookies(Collections.<NameValuePair>emptyList());
            }
            else
            {
                ParameterUtils.doThrow(Constants.COOKIES, cookies, Reason.UNSUPPORTED_VALUE);
            }
        }
        else if (cookiesObject instanceof ArrayList)
        {
            @SuppressWarnings("unchecked")
            final List<Object> objectList = (ArrayList<Object>) cookiesObject;
            final List<NameValuePair> newList = new ArrayList<NameValuePair>();
            for (final Object object : objectList)
            {
                ParameterUtils.isLinkedHashMap(object, Constants.COOKIES);
                @SuppressWarnings("unchecked")
                final LinkedHashMap<Object, Object> lhm = (LinkedHashMap<Object, Object>) object;
                newList.add(createPairfromLinkedHashMap(lhm));
            }
            actionBuilder.setDefaultCookies(newList);
        }
        else
        {
            ParameterUtils.doThrow(Constants.COOKIES, Reason.UNSUPPORTED_TYPE);
        }
    }

    private void setDefaultHeaders(final LinkedHashMap<String, Object> headersItem)
    {
        final Object headersObject = headersItem.get(Constants.HEADERS);
        if (headersObject instanceof String)
        {
            final String headersString = (String) headersObject;
            if (headersString.equals(Constants.DELETE))
            {

                actionBuilder.setDefaultHeaders(Collections.<NameValuePair>emptyList());

            }
            else
            {
                ParameterUtils.doThrow(Constants.HEADERS, headersString, Reason.UNSUPPORTED_VALUE);
            }
        }
        else if (headersObject instanceof ArrayList)
        {
            @SuppressWarnings("unchecked")
            final List<Object> objectList = (ArrayList<Object>) headersObject;
            final List<NameValuePair> newList = new ArrayList<NameValuePair>();
            for (final Object object : objectList)
            {
                ParameterUtils.isLinkedHashMap(object, Constants.HEADERS);
                @SuppressWarnings("unchecked")
                final LinkedHashMap<Object, Object> lhm = (LinkedHashMap<Object, Object>) object;
                newList.add(createPairfromLinkedHashMap(lhm));
            }
            actionBuilder.setDefaultHeaders(newList);
        }
        else
        {
            ParameterUtils.doThrow(Constants.HEADERS, Reason.UNSUPPORTED_TYPE);
        }
    }

    private void setDefaultStatic(final LinkedHashMap<String, Object> headersItem)
    {
        final Object staticObject = headersItem.get(Constants.STATIC);
        if (staticObject instanceof String)
        {
            final String staticString = (String) staticObject;
            if (staticString.equals(Constants.DELETE))
            {
                this.d_static = Collections.emptyList();
            }
            else
            {
                ParameterUtils.doThrow(Constants.STATIC, staticString, Reason.UNSUPPORTED_VALUE);
            }
        }
        else if (staticObject instanceof ArrayList)
        {
            @SuppressWarnings("unchecked")
            final List<Object> objectList = (ArrayList<Object>) staticObject;
            final List<String> newList = new ArrayList<String>();
            for (final Object object : objectList)
            {
                ParameterUtils.isString(object, Constants.STATIC);
                newList.add((String) object);
            }
            this.d_static = newList;
        }
        else
        {
            ParameterUtils.doThrow(Constants.HEADERS, Reason.UNSUPPORTED_TYPE);
        }
    }

    private void setDynamicStoreVariables(final LinkedHashMap<String, Object> headersItem)
    {
        final Object storeObject = headersItem.get(Constants.STORE);
        if (storeObject instanceof String)
        {
            final String storeString = (String) storeObject;
            if (storeString.equals(Constants.DELETE))
            {
                XltLogger.runTimeLogger.warn("CANNOT DELETE DATA IN STORE (YET)");
            }
            else
            {
                ParameterUtils.doThrow(Constants.STORE, storeString, Reason.UNSUPPORTED_VALUE);
            }
        }
        else if (storeObject instanceof ArrayList)
        {
            @SuppressWarnings("unchecked")
            final List<Object> objectList = (ArrayList<Object>) storeObject;
            @SuppressWarnings("unused")
            final List<NameValuePair> newList = new ArrayList<NameValuePair>();
            for (final Object object : objectList)
            {
                ParameterUtils.isLinkedHashMap(object, Constants.STORE);
                @SuppressWarnings("unchecked")
                final LinkedHashMap<Object, Object> lhm = (LinkedHashMap<Object, Object>) object;
                final NameValuePair nvp = createPairfromLinkedHashMap(lhm);
                final NameValuePair nvp2 = new NameValuePair(interpreter.processDynamicData(nvp.getName()),
                                                             interpreter.processDynamicData(nvp.getValue()));
                try
                {
                    this.interpreter.set(nvp2);
                }
                catch (final EvalError e)
                {
                    // We just Set Values, so NP
                }
            }
        }
        else
        {
            ParameterUtils.doThrow(Constants.STORE, Reason.UNSUPPORTED_TYPE);
        }
    }

    private void handleActionListItem(final LinkedHashMap<String, Object> listItem)
    {
        final Object actionObject = listItem.get(Constants.ACTION);
        ParameterUtils.isNotNull(actionObject, Constants.ACTION);
        ParameterUtils.isLinkedHashMapMessage(actionObject, Constants.ACTION, "Missing Content");
        @SuppressWarnings("unchecked")
        final LinkedHashMap<String, Object> rawAction = (LinkedHashMap<String, Object>) actionObject;
        checkForInvalidTags(rawAction, Constants.ACTION);

        fillURLActionBuilder(rawAction);
        final URLActionData action = actionBuilder.build();
        this.actions.add(action);

        handleSubrequests(rawAction);

    }

    private void handleSubrequests(final LinkedHashMap<String, Object> rawAction)
    {
        final Object subrequestObject = rawAction.get(Constants.SUBREQUESTS);
        if (subrequestObject != null)
        {
            ParameterUtils.isArrayListMessage(subrequestObject, Constants.SUBREQUESTS, "");

            @SuppressWarnings("unchecked")
            final List<Object> subrequests = (List<Object>) subrequestObject;

            for (final Object subrequestItem : subrequests)
            {
                ParameterUtils.isLinkedHashMapMessage(subrequestItem, Constants.STATIC, "");

                @SuppressWarnings("unchecked")
                final LinkedHashMap<String, Object> subrequest = (LinkedHashMap<String, Object>) subrequestItem;
                checkForInvalidTags(subrequest, Constants.SUBREQUESTS);
                createSubrequest(subrequest);
            }
        }
        else
        {
        	XltLogger.runTimeLogger.warn(MessageFormat.format("Subrequest of Action: \"{0}\" is null", rawAction.get(Constants.NAME)));
        }
    }

    private void createSubrequest(final LinkedHashMap<String, Object> subrequest)
    {
        final Object staticSubrequestObject = subrequest.get(Constants.STATIC);
        if (staticSubrequestObject != null)
        {
            ParameterUtils.isArrayListMessage(staticSubrequestObject, Constants.SUBREQUESTS, "");
            @SuppressWarnings("unchecked")
            final List<Object> staticSubrequest = (List<Object>) staticSubrequestObject;
            handleStaticSubrequests(staticSubrequest);
        }
        else if (!d_static.isEmpty())
        {
            for (int i = 0; i < d_static.size(); i++)
            {
                actionBuilder.reset();
                actionBuilder.setUrl(d_static.get(i));
                actionBuilder.setType(URLActionData.TYPE_STATIC);
                actionBuilder.setMethod(URLActionData.METHOD_GET);
                actionBuilder.setName("static-subrequest" + i);
                actionBuilder.setInterpreter(this.interpreter);
                actions.add(actionBuilder.build());
            }
        }
        final Object xhrSubrequestObject = subrequest.get(Constants.XHR);
        if (xhrSubrequestObject != null)
        {
            ParameterUtils.isLinkedHashMapMessage(xhrSubrequestObject, Constants.SUBREQUESTS, "");
            @SuppressWarnings("unchecked")
            final LinkedHashMap<String, Object> xhrSubrequest = (LinkedHashMap<String, Object>) xhrSubrequestObject;
            handleXhrSubrequests(xhrSubrequest);
        }
    }

    private void handleXhrSubrequests(final LinkedHashMap<String, Object> xhrSubrequest)
    {
    	checkForInvalidTags(xhrSubrequest, Constants.XHR);
        actionBuilder.reset();
        fillURLActionBuilder(xhrSubrequest);
        actionBuilder.setType(URLActionData.TYPE_XHR);
        final URLActionData xhrAction = actionBuilder.build();
        actions.add(xhrAction);
        handleSubrequests(xhrSubrequest);
    }

    private void handleStaticSubrequests(final List<Object> staticUrls)
    {
        for (int i = 0; i < staticUrls.size(); i++)
        {
            final Object o = staticUrls.get(i);
            ParameterUtils.isStringMessage(o, Constants.STATIC, "");

            final String urlString = (String) o;

            actionBuilder.reset();
            actionBuilder.setType(URLActionData.TYPE_STATIC);
            actionBuilder.setMethod(URLActionData.METHOD_GET);
            actionBuilder.setUrl(urlString);
            actionBuilder.setName("static-subrequest" + i);
            actionBuilder.setInterpreter(this.interpreter);
            actions.add(actionBuilder.build());
        }
    }  
    
    private void fillURLActionBuilder(final LinkedHashMap<String, Object> rawAction)
    {
        actionBuilder.reset();

        actionBuilder.setInterpreter(this.interpreter);
        fillUrlActionBuilderWithName(rawAction);
        fillUrlActionBuilderWithRequestData(rawAction);
        fillUrlActionBuilderWithResponseData(rawAction);
    }

    private void fillUrlActionBuilderWithName(final LinkedHashMap<String, Object> rawAction)
    {
        final Object nameObject = rawAction.get(Constants.NAME);
        if (nameObject != null)
        {
            ParameterUtils.isString(nameObject, Constants.NAME);
            final String name = (String) nameObject;
            actionBuilder.setName(name);
        }

    }

    private void fillUrlActionBuilderWithRequestData(final LinkedHashMap<String, Object> rawAction)
    {
        final Object requestObject = rawAction.get(Constants.REQUEST);
        if (requestObject != null)
        {
            ParameterUtils.isLinkedHashMapMessage(requestObject, Constants.REQUEST, "");
            @SuppressWarnings("unchecked")
            final LinkedHashMap<String, Object> rawRequest = (LinkedHashMap<String, Object>) requestObject;
            checkForInvalidTags(rawRequest, Constants.REQUEST);
            
            fillURLActionBuilderWithBodyData(rawRequest);
            fillURLActionBuilderWithHeaderData(rawRequest);
            fillURLActionBuilderWithEncodeParametersData(rawRequest);
            fillURLActionBuilderWithEncodeBodyData(rawRequest);
            fillURLActionBuilderWithMethodData(rawRequest);
            fillURLActionBuilderWithParameterData(rawRequest);
            fillURLActionBuilderWithCookieData(rawRequest);
            fillURLActionBuilderWithXhrData(rawRequest);
            fillURLActionBuilderWithUrlData(rawRequest);
        }
    }

    private void fillURLActionBuilderWithBodyData(final LinkedHashMap<String, Object> rawRequest)
    {
        final Object bodyObject = rawRequest.get(Constants.BODY);
        if (bodyObject != null)
        {
            ParameterUtils.isString(bodyObject, Constants.BODY);
            final String body = (String) bodyObject;
            actionBuilder.setBody(body);
        }
    }

    private void fillURLActionBuilderWithHeaderData(final LinkedHashMap<String, Object> rawRequest)
    {
        final Object headersObject = rawRequest.get(Constants.HEADERS);
        if (headersObject != null)
        {
            if (headersObject instanceof ArrayList)
            {
                @SuppressWarnings(
                    {
                        "unchecked"
                    })
                final List<Object> objectList = (ArrayList<Object>) headersObject;

                final List<NameValuePair> newList = new ArrayList<NameValuePair>();

                for (final Object object : objectList)
                {
                    ParameterUtils.isLinkedHashMap(object, Constants.HEADERS);
                    @SuppressWarnings("unchecked")
                    final LinkedHashMap<Object, Object> lhm = (LinkedHashMap<Object, Object>) object;
                    final NameValuePair nvp = createPairfromLinkedHashMap(lhm);
                    newList.add(nvp);
                }
                actionBuilder.setHeaders(newList);
            }
            else
            {
                ParameterUtils.doThrow(Constants.HEADERS, Reason.UNSUPPORTED_TYPE);
            }
        }
    }

    private void fillURLActionBuilderWithParameterData(final LinkedHashMap<String, Object> rawRequest)
    {
        final Object parametersObject = rawRequest.get(Constants.PARAMETERS);
        if (parametersObject != null)
        {
            if (parametersObject instanceof ArrayList)
            {
                @SuppressWarnings("unchecked")
                final List<Object> objectList = (ArrayList<Object>) parametersObject;
                final List<NameValuePair> newList = new ArrayList<NameValuePair>();
                for (final Object object : objectList)
                {
                    ParameterUtils.isLinkedHashMap(object, Constants.PARAMETERS);
                    @SuppressWarnings("unchecked")
                    final LinkedHashMap<Object, Object> lhm = (LinkedHashMap<Object, Object>) object;
                    final NameValuePair nvp = createPairfromLinkedHashMap(lhm);
                    newList.add(nvp);
                }
                actionBuilder.setParameters(newList);
            }
            else
            {
                ParameterUtils.doThrow(Constants.PARAMETERS, Reason.UNSUPPORTED_TYPE);
            }
        }
    }

    private void fillURLActionBuilderWithCookieData(final LinkedHashMap<String, Object> rawRequest)
    {
        final Object cookiesObject = rawRequest.get(Constants.COOKIES);
        if (cookiesObject != null)
        {
            if (cookiesObject instanceof ArrayList)
            {
                @SuppressWarnings("unchecked")
                final List<Object> objectList = (ArrayList<Object>) cookiesObject;
                final List<NameValuePair> newList = new ArrayList<NameValuePair>();
                for (final Object object : objectList)
                {
                    ParameterUtils.isLinkedHashMap(object, Constants.COOKIES);
                    @SuppressWarnings("unchecked")
                    final LinkedHashMap<Object, Object> lhm = (LinkedHashMap<Object, Object>) object;
                    final NameValuePair nvp = createPairfromLinkedHashMap(lhm);
                    newList.add(nvp);
                }
                actionBuilder.setCookies(newList);
            }
            else
            {
                ParameterUtils.doThrow(Constants.COOKIES, Reason.UNSUPPORTED_TYPE);
            }
        }
    }

    private void fillURLActionBuilderWithXhrData(final LinkedHashMap<String, Object> rawRequest)
    {
        final Object xhrObject = rawRequest.get(Constants.XHR);
        if (xhrObject != null)
        {
            if (xhrObject instanceof Boolean)
            {
                final Boolean xhr = (Boolean) xhrObject;
                if (xhr)
                {
                    actionBuilder.setType(URLActionData.TYPE_XHR);
                }
                else
                {
                    actionBuilder.setType(URLActionData.TYPE_ACTION);
                }
            }
            else if (xhrObject instanceof String)
            {
                final String xhr = (String) xhrObject;

                actionBuilder.setType(xhr);
            }
            else
            {
                ParameterUtils.doThrow(Constants.XHR, Reason.UNSUPPORTED_TYPE);
            }
        }
    }

    private void fillURLActionBuilderWithMethodData(final LinkedHashMap<String, Object> rawRequest)
    {
        final Object methodObject = rawRequest.get(Constants.METHOD);
        if (methodObject != null)
        {
            ParameterUtils.isString(methodObject, Constants.METHOD);
            final String method = (String) methodObject;
            actionBuilder.setMethod(method);
        }
    }

    private void fillURLActionBuilderWithUrlData(final LinkedHashMap<String, Object> rawRequest)
    {
        final Object urlObject = rawRequest.get(Constants.URL);
        if (urlObject != null)
        {
            ParameterUtils.isString(urlObject, Constants.URL);
            final String url = (String) urlObject;
            actionBuilder.setUrl(url);
        }
    }

    private void fillURLActionBuilderWithEncodeParametersData(final LinkedHashMap<String, Object> rawRequest)
    {
        final Object encodedObject = rawRequest.get(Constants.ENCODEPARAMETERS);
        if (encodedObject != null)
        {
            if (encodedObject instanceof Boolean)
            {
                final Boolean encoded = (Boolean) encodedObject;
                actionBuilder.setEncodeParameters(encoded.toString());
            }
            else if (encodedObject instanceof String)
            {
                final String encoded = (String) encodedObject;
                actionBuilder.setEncodeParameters(encoded);
            }
            else
            {
                ParameterUtils.doThrow(Constants.ENCODEPARAMETERS, Reason.UNSUPPORTED_TYPE);
            }
        }
    }

    private void fillURLActionBuilderWithEncodeBodyData(final LinkedHashMap<String, Object> rawRequest)
    {
        final Object encodedObject = rawRequest.get(Constants.ENCODEBODY);
        if (encodedObject != null)
        {
            if (encodedObject instanceof Boolean)
            {
                final Boolean encoded = (Boolean) encodedObject;
                actionBuilder.setEncodeBody(encoded.toString());
            }
            else if (encodedObject instanceof String)
            {
                final String encoded = (String) encodedObject;
                actionBuilder.setEncodeBody(encoded);
            }
            else
            {
                ParameterUtils.doThrow(Constants.ENCODEBODY, Reason.UNSUPPORTED_TYPE);
            }
        }
    }

    private void fillUrlActionBuilderWithResponseData(final LinkedHashMap<String, Object> rawAction)
    {
        final Object responseObject = rawAction.get(Constants.RESPONSE);
        if (responseObject != null)
        {
            ParameterUtils.isLinkedHashMapMessage(responseObject, Constants.RESPONSE, "");
            @SuppressWarnings("unchecked")
            final LinkedHashMap<String, Object> rawResponse = (LinkedHashMap<String, Object>) responseObject;
            checkForInvalidTags(rawResponse, Constants.RESPONSE);
            
            fillURLActionBuilderWithHttpResponseCodeData(rawResponse);
            fillURLActionBuilderWithValidationData(rawResponse);
            fillURLActionBuilderWithStoreData(rawResponse);
        }
    }

    private void fillURLActionBuilderWithHttpResponseCodeData(final LinkedHashMap<String, Object> rawResponse)
    {
        final Object codeObject = rawResponse.get(Constants.HTTPCODE);
        if (codeObject != null)
        {
            if (codeObject instanceof Integer)
            {
                final Integer code = (Integer) codeObject;
                actionBuilder.setHttpResponceCode(code.toString());
            }
            else if (codeObject instanceof String)
            {
                final String code = (String) codeObject;
                actionBuilder.setHttpResponceCode(code);
            }
            else
            {
                ParameterUtils.doThrow(Constants.HTTPCODE, Reason.UNSUPPORTED_TYPE);
            }
        }
    }

    private void fillURLActionBuilderWithValidationData(final LinkedHashMap<String, Object> rawResponse)
    {
        final Object validationsObject = rawResponse.get(Constants.VALIDATION);
        if (validationsObject != null)
        {
            ParameterUtils.isArrayListMessage(validationsObject, Constants.VALIDATION, "");
            @SuppressWarnings("unchecked")
            final List<Object> validations = (List<Object>) validationsObject;
            for (final Object validationObject : validations)
            {
                ParameterUtils.isLinkedHashMapMessage(validationObject, Constants.VALIDATION, "");
                @SuppressWarnings("unchecked")
                final LinkedHashMap<String, Object> validationItem = (LinkedHashMap<String, Object>) validationObject;
                if (!(validationItem.entrySet().size() <= 1))
                {
                	throw new IllegalArgumentException(MessageFormat.format("Incorrect syntax at \"Validate\" of Aciton: \"{0}\". Please mind the whitespaces.", actionName));
                }
                fillURLActionValidationBuilder(validationItem);
                final URLActionDataValidation validation = validationBuilder.build();
                actionBuilder.addValidation(validation);
            }
        }
    }

    private void fillURLActionValidationBuilder(final LinkedHashMap<String, Object> rawValidationItem)
    {
        validationBuilder.reset();

        final String validationName = getNameOfFirstElementFromLinkedHashMap(rawValidationItem);

        validationBuilder.setName(validationName);
        validationBuilder.setInterpreter(this.interpreter);

        final Object rawValidateSubObject = rawValidationItem.get(validationName);
        if (rawValidateSubObject != null)
        {
            ParameterUtils.isLinkedHashMapMessage(rawValidateSubObject, validationName, "");
            @SuppressWarnings("unchecked")
            final LinkedHashMap<String, Object> validateListSubItem = (LinkedHashMap<String, Object>) rawValidateSubObject;
            fillURLActionValidationBuilderWithDataFromLinkedHashMap(validateListSubItem);
        }
        else
        {
            throw new IllegalArgumentException (MessageFormat.format("Validation Item: \"{0}\" at Action: \"{1}\" failed because: \"{2}\"", validationName, actionName, Reason.UNCOMPLETE));
        }
    }

    private void fillURLActionValidationBuilderWithDataFromLinkedHashMap(final LinkedHashMap<String, Object> rawValidateSubItem)
    {
        final Set<?> entrySet = rawValidateSubItem.entrySet();
	    if (entrySet.size() >2) 
	    { 
	    	throw new IllegalArgumentException (MessageFormat.format("Validation Item: \"{0}\" of Action: \"{1}\" has too manny selection- or validationMode ", validationBuilder.getName(), actionName)); 
	    }         
        final Iterator<?> it = entrySet.iterator();
        Map.Entry<?, ?> entry = (Map.Entry<?, ?>) it.next();
        final String selectionMode = (String) entry.getKey();
        final String selectionContent = (String) entry.getValue();
        String validationMode = null;
        String validationContent = null;

        if (it.hasNext())
        {
            entry = (Map.Entry<?, ?>) it.next();
            validationMode = entry.getKey().toString();
            Object validationContentObject = entry.getValue();
		    switch (validationMode) 
		    { 
		    case Constants.EXISTS: 
		    	if (validationContentObject == null)
		    	{
					  throw new IllegalArgumentException(MessageFormat.format("Validation Item: \"{0}\" of Action: \"{1}\". Validation content of \"{2}\" can not be null", validationBuilder.getName(), actionName, validationMode)); 
		    	}
		    	else
		    	{
					if (validationContentObject.toString().equals("false"))
					{
						  throw new IllegalArgumentException(MessageFormat.format("Validation Item: \"{0}\" of Action: \"{1}\". Validation content of \"{2}\" can not be \"false\"", validationBuilder.getName(), actionName, validationMode)); 						
					}
		            XltLogger.runTimeLogger.warn("Validation mode \"Exists\" is implicit in every validation. The exlpicit delacration of this mode is deprecated and will be removed soon.");
					validationContent = validationContentObject.toString();             
		    	}
				break; 
		    default: 
			    if (!Constants.isPermittedValidationMode(validationMode)) 
			    { 
			    	throw new IllegalArgumentException(MessageFormat.format("Invalid ValidationMode: \"{0}\" at Validation Item: \"{1}\" of Action \"{2}\"", validationMode, validationBuilder.getName(), actionName)); 
			    } 
			    else if (validationContentObject == null) 
			    { 
			    	throw new IllegalArgumentException(MessageFormat.format("Validation Item: \"{0}\" of Action: \"{1}\" have more than one selection- or validationMode ", validationBuilder.getName(), actionName, validationMode)); 
			    }  
				validationContent = validationContentObject.toString();             
			    break;
		    }
        }
        if (validationMode == null)
        {
            validationMode = URLActionDataValidation.EXISTS;
        }
        validationBuilder.setSelectionMode(selectionMode);
        validationBuilder.setValidationContent(validationContent);
        validationBuilder.setValidationMode(validationMode);
        validationBuilder.setSelectionContent(selectionContent);
    }

    private void fillURLActionBuilderWithStoreData(final LinkedHashMap<String, Object> rawResponse)
    {
        final Object storeObject = rawResponse.get(Constants.STORE);
        if (storeObject != null)
        {
            ParameterUtils.isArrayListMessage(storeObject, Constants.STORE, "");
            @SuppressWarnings("unchecked")
            final List<Object> storeObjects = (List<Object>) storeObject;
            for (final Object storeObjectsItem : storeObjects)
            {
                ParameterUtils.isLinkedHashMapMessage(storeObjectsItem, Constants.STORE, "");
                @SuppressWarnings("unchecked")
                final LinkedHashMap<String, Object> storeItem = (LinkedHashMap<String, Object>) storeObjectsItem;

                fillURLActionStoreBuilder(storeItem);

                final URLActionDataStore store = storeBuilder.build();
                actionBuilder.addStore(store);
            }
        }
    }

    private void fillURLActionStoreBuilder(final LinkedHashMap<String, Object> storeItem)
    {
        storeBuilder.reset();

        final String storeName = getNameOfFirstElementFromLinkedHashMap(storeItem);

        storeBuilder.setName(storeName);
        storeBuilder.setInterpreter(interpreter);

        final Object rawStoreSubObject = storeItem.get(storeName);
        if (rawStoreSubObject != null)
        {
            ParameterUtils.isLinkedHashMapMessage(rawStoreSubObject, storeName, "");
            @SuppressWarnings("unchecked")
            final LinkedHashMap<Object, Object> rawStoreSubItem = (LinkedHashMap<Object, Object>) rawStoreSubObject;
            fillStoreBuilderWithDataFromLinkedHashMap(rawStoreSubItem);
        }
        else
        {
            ParameterUtils.doThrow(Constants.STORE, storeName, Reason.UNCOMPLETE);
        }

    }

    private void fillStoreBuilderWithDataFromLinkedHashMap(final LinkedHashMap<Object, Object> rawStoreSubItem)
    {
        final Set<?> entrySet = rawStoreSubItem.entrySet();
        final Iterator<?> it = entrySet.iterator();
        Map.Entry<?, ?> entry = (Map.Entry<?, ?>) it.next();
        final String selectionMode = (String) entry.getKey();
        final String selectionContent = (String) entry.getValue();
        String subSelectionMode = null;
        String subSelectionContent = null;

        if (it.hasNext())
        {
            entry = (Map.Entry<?, ?>) it.next();
            subSelectionMode = entry.getKey().toString();
            subSelectionContent = entry.getValue().toString();
        }
        storeBuilder.setSelectionMode(selectionMode);
        storeBuilder.setSelectionContent(selectionContent);

        if (subSelectionMode != null)
        {
            storeBuilder.setSubSelectionMode(subSelectionMode);
            storeBuilder.setSubSelectionContent(subSelectionContent);
        }
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
        final String value = entry.getValue() != null ? entry.getValue().toString() : null;
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
