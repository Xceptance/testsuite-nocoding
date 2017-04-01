package com.xceptance.xlt.common.util;

import java.util.HashSet;
import java.util.Set;


public class Constants {
	
    public static final String ACTION = "Action";

    public static final String REQUEST = "Request";

    public static final String RESPONSE = "Response";

    public static final String BODY = "Body";

    public static final String STORE = "Store";

    public static final String SUBREQUESTS = "Subrequests";

    public static final String NAME = "Name";

    public static final String URL = "Url";

    public static final String METHOD = "Method";

    public static final String ENCODEPARAMETERS = "Encode-Parameters";

    public static final String ENCODEBODY = "Encode-Body";

    public static final String XHR = "Xhr";

    public static final String PARAMETERS = "Parameters";

    public static final String HTTPCODE = "Httpcode";

    public static final String VALIDATION = "Validate";

    public static final String STATIC = "Static";

    public static final String COOKIES = "Cookies";

    public static final String HEADERS = "Headers";

    public static final String DELETE = "Delete";
    
    public static final String VARIABLES = "Variables";

    public static final String ENCODED = "Encoded";

    public static final String METHOD_POST = "POST";

    public static final String METHOD_GET = "GET";

    public static final String METHOD_PUT = "PUT";

    public static final String METHOD_DELETE = "DELETE";

    public static final String METHOD_HEAD = "HEAD";

    public static final String METHOD_TRACE = "TRACE";

    public static final String METHOD_OPTIONS = "OPTIONS";
    
    public static final String XPATH = "XPath";

    public static final String REGEXP = "Regex";

    public static final String TEXT = "Text";

    public static final String HEADER = "Header";

    public static final String COOKIE = "Cookie";

    public static final String MATCHES = "Matches";

    public static final String COUNT = "Count";

    public static final String EXISTS = "Exists";
    
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

    /**
     * Supported list items:
     * <ul> {@link #ACTION}
     * <li> {@link #STORE}
     * <li> {@link #NAME}
     * <li> {@link #HTTPCODE}
     * <li> {@link #URL}
     * <li> {@link #METHOD}
     * <li> {@link #ENCODEPARAMETERS}
     * <li> {@link #ENCODEBODY}
     * <li> {@link #HEADERS}
     * <li> {@link #PARAMETERS}
     * <li> {@link #COOKIES}
     * <li> {@link #VARIABLES}
     * <li> {@link #HEADER}
     * <li> {@link #COOKIE}
     * <li> {@link #BODY}
     * <li> {@link #STATIC}
     * </ul>
     */
    public final static Set<String> PERMITTEDLISTITEMS = new HashSet<String>();
    
    /**
     * Supported action items:
     * <li> {@link #NAME}
     * <li> {@link #REQUEST}
     * <li> {@link #RESPONSE}
     * <li> {@link #SUBREQUESTS}
     * </ul>
     */
    public final static Set<String> PERMITTEDACTIONITEMS = new HashSet<String>();

    /**
     * Supported request items:
     * <li> {@link #URL}
     * <li> {@link #METHOD}
     * <li> {@link #XHR}
     * <li> {@link #ENCODEPARAMETERS}
     * <li> {@link #PARAMETERS}
     * <li> {@link #HEADERS}
     * <li> {@link #BODY}
     * <li> {@link #ENCODEBODYDY}
     * </ul>
     */
    public final static Set<String> PERMITTEDREQUESTITEMS = new HashSet<String>();
    
    /**
     * Supported subrequest items:
     * <li> {@link #XHR}
     * <li> {@link #STATIC}
     * </ul>
     */
    public final static Set<String> PERMITTEDSUBREQUESTITEMS = new HashSet<String>();

    /**
     * Supported response items:
     * <ul>
     * <li> {@link #HTTPCODE}
     * <li> {@link #VALIDATION}
     * <li> {@link #STORE}
     * </ul>
     */
    public final static Set<String> PERMITTEDRESPONSEITEMS = new HashSet<String>();

    /**
     * Supported selection modes:
     * <ul>
     * <li> {@link #XPATH}
     * <li> {@link #REGEXP}
     * <li> {@link #HEADER}
     * <li> {@link #COOKIE}
     * </ul>
     */
    public final static Set<String> PERMITTEDSELECTIONMODE = new HashSet<String>();

    /**
     * Supported validation modes:
     * <ul>
     * <li> {@link #TEXT}
     * <li> {@link #COUNT}
     * <li> {@link #MATCHES}
     * <li> {@link #EXISTS}
     * </ul>
     */
    public final static Set<String> PERMITTEDVALIDATIONMODE = new HashSet<String>();
    
    static
    {
    	PERMITTEDLISTITEMS.add(ACTION);
    	PERMITTEDLISTITEMS.add(STORE);
    	PERMITTEDLISTITEMS.add(NAME);
    	PERMITTEDLISTITEMS.add(HTTPCODE);
    	PERMITTEDLISTITEMS.add(URL);
    	PERMITTEDLISTITEMS.add(METHOD);
    	PERMITTEDLISTITEMS.add(XHR);
    	PERMITTEDLISTITEMS.add(ENCODEPARAMETERS);
    	PERMITTEDLISTITEMS.add(ENCODEBODY);
    	PERMITTEDLISTITEMS.add(HEADERS);
    	PERMITTEDLISTITEMS.add(PARAMETERS);
    	PERMITTEDLISTITEMS.add(COOKIES);
    	PERMITTEDLISTITEMS.add(VARIABLES);
    	PERMITTEDLISTITEMS.add(HEADER);
    	PERMITTEDLISTITEMS.add(BODY);
    	PERMITTEDLISTITEMS.add(STATIC);

    	PERMITTEDACTIONITEMS.add(NAME);
    	PERMITTEDACTIONITEMS.add(REQUEST);
    	PERMITTEDACTIONITEMS.add(RESPONSE);
    	PERMITTEDACTIONITEMS.add(SUBREQUESTS);
		
		PERMITTEDREQUESTITEMS.add(URL);
		PERMITTEDREQUESTITEMS.add(METHOD);
		PERMITTEDREQUESTITEMS.add(XHR);
		PERMITTEDREQUESTITEMS.add(ENCODEPARAMETERS);
		PERMITTEDREQUESTITEMS.add(PARAMETERS);
		PERMITTEDREQUESTITEMS.add(HEADERS);
		PERMITTEDREQUESTITEMS.add(BODY);
		PERMITTEDREQUESTITEMS.add(ENCODEBODY);
		
		PERMITTEDRESPONSEITEMS.add(HTTPCODE);
		PERMITTEDRESPONSEITEMS.add(VALIDATION);
		PERMITTEDRESPONSEITEMS.add(STORE);

		PERMITTEDSUBREQUESTITEMS.add(XHR);
		PERMITTEDSUBREQUESTITEMS.add(STATIC);		
		
        PERMITTEDSELECTIONMODE.add(XPATH);
        PERMITTEDSELECTIONMODE.add(REGEXP);
        PERMITTEDSELECTIONMODE.add(HEADER);
        PERMITTEDSELECTIONMODE.add(COOKIE);

        PERMITTEDVALIDATIONMODE.add(TEXT);
        PERMITTEDVALIDATIONMODE.add(MATCHES);
        PERMITTEDVALIDATIONMODE.add(COUNT);
        PERMITTEDVALIDATIONMODE.add(EXISTS);
    }
    
    public static boolean isPermittedListItem(final String s)
    {
        return PERMITTEDLISTITEMS.contains(s);
    }
    
    public static boolean isPermittedActionItem(final String s)
    {
        return PERMITTEDACTIONITEMS.contains(s);
    }
    
    public static boolean isPermittedRequestItem(final String s)
    {
        return PERMITTEDREQUESTITEMS.contains(s);
    }
    
    public static boolean isPermittedResponseItem(final String s)
    {
        return PERMITTEDRESPONSEITEMS.contains(s);
    }
    
    public static boolean isPermittedSubRequestItem(final String s)
    {
        return PERMITTEDSUBREQUESTITEMS.contains(s);
    }
    
    public static boolean isPermittedSelectionMode(final String s)
    {
        return PERMITTEDSELECTIONMODE.contains(s);
    }
    
    public static boolean isPermittedValidationMode(final String s)
    {
        return PERMITTEDVALIDATIONMODE.contains(s);
    }

}
