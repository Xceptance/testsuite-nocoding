/**  
 *  Copyright 2014 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License. 
 *
 */
package com.xceptance.xlt.common.util;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang.StringUtils;

import bsh.EvalError;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.xceptance.common.util.RegExUtils;
import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.api.validators.HttpResponseCodeValidator;
import com.xceptance.xlt.common.tests.AbstractURLTestCase;
import com.xceptance.xlt.common.util.bsh.ParamInterpreter;

public class CSVBasedURLAction
{
    // dynamic parameters
    public static final String XPATH_GETTER_PREFIX = "xpath";
    public static final String REGEXP_GETTER_PREFIX = "regexp";
    
    public static final int DYNAMIC_GETTER_COUNT = 10;

    /**
     * Permitted header fields, checked to avoid accidental incorrect spelling
     */
    private final static Set<String> PERMITTEDHEADERFIELDS = new HashSet<String>();

    public static final String GET = "GET";
    public static final String POST = "POST";
    
    public static final String TYPE = "Type";
    public static final String TYPE_ACTION = "A";
    public static final String TYPE_STATIC = "S";
    
    public static final String NAME = "Name";

    public static final String URL = "URL";

    public static final String METHOD = "Method";

    public static final String PARAMETERS = "Parameters";

    public static final String RESPONSECODE = "ResponseCode";

    public static final String XPATH = "XPath";

    public static final String REGEXP = "RegExp";

    public static final String TEXT = "Text";

    public static final String ENCODED = "Encoded";

    static
    {
        PERMITTEDHEADERFIELDS.add(TYPE);
        PERMITTEDHEADERFIELDS.add(NAME);
        PERMITTEDHEADERFIELDS.add(URL);
        PERMITTEDHEADERFIELDS.add(METHOD);
        PERMITTEDHEADERFIELDS.add(PARAMETERS);
        PERMITTEDHEADERFIELDS.add(RESPONSECODE);
        PERMITTEDHEADERFIELDS.add(XPATH);
        PERMITTEDHEADERFIELDS.add(REGEXP);
        PERMITTEDHEADERFIELDS.add(TEXT);
        PERMITTEDHEADERFIELDS.add(ENCODED);
        
        for (int i = 1; i <= DYNAMIC_GETTER_COUNT; i++)
        {
            PERMITTEDHEADERFIELDS.add(XPATH_GETTER_PREFIX + i);
            PERMITTEDHEADERFIELDS.add(REGEXP_GETTER_PREFIX + i);
        }
    }       
    
    private final String type;
    private final String name;

    private final URL url;
    private final String urlString;

    private final String method;

    private final List<NameValuePair> parameters;

    private final HttpResponseCodeValidator httpResponseCodeValidator;

    private final String xPath;

    private final String regexpString;
    private final Pattern regexp;

    private final String text;

    private final boolean encoded;
    
    private final List<String> xpathGetterList = new ArrayList<String>(DYNAMIC_GETTER_COUNT);
    private final List<String> regexpGetterList = new ArrayList<String>(DYNAMIC_GETTER_COUNT);

    /**
     * Our bean shell 
     */
    private final ParamInterpreter interpreter;

    /**
     * Constructor based upon read CSV data
     * 
     * @param record the record to process
     * @param interpreter the bean shell interpreter to use
     * 
     * @throws UnsupportedEncodingException
     * @throws MalformedURLException
     */
    public CSVBasedURLAction(final CSVRecord record, final ParamInterpreter interpreter) throws UnsupportedEncodingException, MalformedURLException
    {
        // no bean shell, so we do not do anything, satisfy final here
        this.interpreter = interpreter;

        // the header is record 1, so we have to subtract one, for autonaming
        this.name = StringUtils.defaultIfBlank(record.get(NAME), "Action-" + (record.getRecordNumber() - 1)); 

        // take care of type
        String _type = StringUtils.defaultIfBlank(record.get(TYPE), TYPE_ACTION); 
        if (!_type.equals(TYPE_ACTION) && !_type.equals(TYPE_STATIC))
        {
            XltLogger.runTimeLogger.warn(MessageFormat.format("Unknown type '{0}' in line {1}, defaulting to 'A'", _type, record.getRecordNumber()));
            _type = TYPE_ACTION;
        }
        this.type = _type;
        
        // we need at least an url, stop here of not given
        this.urlString = record.get(URL);
        if (this.urlString == null)
        {
            throw new IllegalArgumentException(MessageFormat.format("No url given in record in line {0}. Need at least that.", record.getRecordNumber()));
        }
        this.url = interpreter == null ? new URL(this.urlString) : null;
        
        // take care of method
        String _method = StringUtils.defaultIfBlank(record.get(METHOD), GET);
        if (!_method.equals(GET) && !_method.equals(POST))
        {
            XltLogger.runTimeLogger.warn(MessageFormat.format("Unknown method '{0}' in line {1}, defaulting to 'GET'", _method, record.getRecordNumber()));
            _method = GET;
        }
        this.method = _method;
        
        // get the response code validator
        this.httpResponseCodeValidator = StringUtils.isNotBlank(record.get(RESPONSECODE)) ?  new HttpResponseCodeValidator(Integer.parseInt(record.get(RESPONSECODE))) : HttpResponseCodeValidator.getInstance();

        // compile pattern only, if no interpreter shall be used
        this.regexpString = StringUtils.isNotEmpty(record.get(REGEXP)) ? record.get(REGEXP) : null;
        if (interpreter == null)
        {
            this.regexp = StringUtils.isNotEmpty(regexpString) ? RegExUtils.getPattern(regexpString) : null;
        }
        else
        {
            this.regexp = null;
        }
        
        this.xPath  = StringUtils.isNotBlank(record.get(XPATH)) ? record.get(XPATH) : null;
        this.text  = StringUtils.isNotEmpty(record.get(TEXT)) ? record.get(TEXT) : null;
        this.encoded  = StringUtils.isNotBlank(record.get(ENCODED)) ? Boolean.parseBoolean(record.get(ENCODED)) : false;
        
        // ok, get all the parameters
        for (int i = 1; i <= DYNAMIC_GETTER_COUNT; i++)
        {
            xpathGetterList.add(StringUtils.isNotBlank(record.get(XPATH_GETTER_PREFIX + i)) ? record.get(XPATH_GETTER_PREFIX + i) : null);
            regexpGetterList.add(StringUtils.isNotBlank(record.get(REGEXP_GETTER_PREFIX + i)) ? record.get(REGEXP_GETTER_PREFIX + i) : null);
        }
        
        // ok, this is the tricky part
        this.parameters = StringUtils.isNotEmpty(record.get(PARAMETERS)) ? setupParameters(record.get(PARAMETERS)) : null;
    }
    
    /**
     * Constructor based upon read CSV data
     * @param record
     * @throws UnsupportedEncodingException
     * @throws MalformedURLException
     */
    public CSVBasedURLAction(final CSVRecord record) throws UnsupportedEncodingException, MalformedURLException
    {
        this(record, null);
    }  

    /**
     * Takes the list of parameters and turns it into name value pairs for later usage.
     * 
     * @param paramers the csv definition string of parameters
     * @return a list with parsed key value pairs
     * @throws UnsupportedEncodingException 
     */
    private List<NameValuePair> setupParameters(final String parameters) throws UnsupportedEncodingException
    {
        final List<NameValuePair> list = new ArrayList<NameValuePair>();
        
        // ok, turn them into & split strings
        final StringTokenizer tokenizer = new StringTokenizer(parameters, "&");
        while (tokenizer.hasMoreTokens())
        {
            final String token = tokenizer.nextToken();
            
            // the future pair
            String key = null;
            String value = null;
            
            // split it into key and value at =
            final int pos = token.indexOf("=");
            if (pos >= 0)
            {
                key = token.substring(0, pos);
                if (pos < token.length() - 1)
                {
                    value = token.substring(pos + 1);
                }
            }
            else
            {
                key = token;
            }
            
            // ok, if this is encoded, we have to decode it again, because the httpclient will encode it
            // on its own later on
            if (encoded)
            {
                key = key != null ? URLDecoder.decode(key, "UTF-8") : null;
                value = value != null ? URLDecoder.decode(value, "UTF-8") : "";
            }
            if (key != null)
            {
                list.add(new NameValuePair(key, value));
            }
        }
        
        return list;
    }

//    /**
//     * Is this static content or dynamic stuff, important for the downloader and concurrency
//     * 
//     * @return it is either A or S
//     */
//    public String getType()
//    {
//        return type;
//    }
    
    /**
     * Returns if this is static content to be downloaded
     *  
     * @return true if this is static content
     */
    public boolean isStaticContent()
    {
        return type.equals(TYPE_STATIC);
    }
    
    /**
     * Returns true if this is an action to be executed
     * 
     * @return true if this is an action
     */
    public boolean isAction()
    {
        return type.equals(TYPE_ACTION);
    }
    
    /**
     * Returns the name of this line.
     * 
     * @return the name of this line
     */
    public String getName(final AbstractURLTestCase testCase)
    {
        return interpreter != null ? interpreter.processDynamicData(testCase, name) : name;
    }

    public String getName()
    {
        return getName(null);
    }

    /**
     * Returns the url of that action. Is required.
     * 
     * @param testCase for the correct data resulution
     * @return the url with data resolution
     * @throws MalformedURLException
     */
    public URL getURL(final AbstractURLTestCase testCase) throws MalformedURLException
    {
        // process bean shell part
        return interpreter != null ? new URL(interpreter.processDynamicData(testCase, urlString)) : url;
    }

    public URL getURL() throws MalformedURLException
    {
        return getURL(null);
    }
    
    public HttpMethod getMethod()
    {
        if (this.method.equals(POST))
        {
            return HttpMethod.POST;
        }
        else
        {
            return HttpMethod.GET;
        }
    }

    public List<NameValuePair> getParameters(final AbstractURLTestCase testCase)
    {
        // process bean shell part
        if (interpreter != null && parameters != null)
        {
            // create new list
            final List<NameValuePair> result = new ArrayList<NameValuePair>(parameters.size());
            
            // process all
            for (final NameValuePair pair : parameters)
            {
                final String name =  interpreter.processDynamicData(testCase, pair.getName());
                String value = pair.getValue();
                value = value != null ? interpreter.processDynamicData(testCase, value) : value;
                
                result.add(new NameValuePair(name, value));
            }
            
            return result;
        }
        else
        {
            return parameters;
        }
    }
    
    public List<NameValuePair> getParameters()
    {
        return getParameters(null);
    }

    public HttpResponseCodeValidator getHttpResponseCodeValidator()
    {
        return httpResponseCodeValidator;
    }

    public String getXPath(final AbstractURLTestCase testCase)
    {
        // process bean shell part
        return interpreter != null ? interpreter.processDynamicData(testCase, xPath) : xPath;    
    }

    public String getXPath()
    {
        return getXPath(null);
    }

    public Pattern getRegexp(final AbstractURLTestCase testCase)
    {
        // process bean shell part
        return interpreter != null && regexpString != null? RegExUtils.getPattern(interpreter.processDynamicData(testCase, regexpString)) : regexp;    
    }

    public Pattern getRegexp()
    {
        return getRegexp(null);    
    }
 
    public String getText(final AbstractURLTestCase testCase)
    {
        // process bean shell part
        return interpreter != null ? interpreter.processDynamicData(testCase, text) : text;    
    }

    public String getText()
    {
        return getText(null);
    }
    
    /**
     * Returns true of this data is already html encoded for transfer
     * 
     * @return true if parameters and url is encoded
     */
    public boolean isEncoded()
    {
        return encoded;
    }

    /**
     * Returns the active interpreter. Important for testing.
     * 
     * @return The active bean interpreter.
     */
    public ParamInterpreter getInterpreter()
    {
        return interpreter;
    }

    /**
     * Returns the list of optional getters
     * 
     * @return list of xpath getters
     * TODO test it
     */
    public List<String> getXPathGetterList(final AbstractURLTestCase testCase)
    {
        // do not do anything when there is not interpreter
        if (interpreter == null)
        {
            return xpathGetterList;
        }
        
        final List<String> result = new ArrayList<String>(xpathGetterList.size());
        for (int i = 0; i < xpathGetterList.size(); i++)
        {
            final String s = xpathGetterList.get(i);
            result.add(interpreter.processDynamicData(testCase, s));    
        }
        
        return result;
    }

    /**
     * Returns the list of regexp patterns
     * 
     * @return the list of regexp getter patterns
     * TODO test it
     */
    public List<Pattern> getRegExpGetterList(final AbstractURLTestCase testCase)
    {
        final List<Pattern> result = new ArrayList<Pattern>(regexpGetterList.size());
        for (int i = 0; i < regexpGetterList.size(); i++)
        {
            final String s = regexpGetterList.get(i);
            if (interpreter != null)
            {
                result.add(s != null ? RegExUtils.getPattern(interpreter.processDynamicData(testCase, s)) : null);
            }
            else
            {
                result.add(s != null ? RegExUtils.getPattern(s) : null);
            }
        }
        
        return result;
    }

    /**
     * Take back the evaluation results to spice up the interpreter
     * 
     * @param xpathGettersResults a list of results
     * 
     * TODO test it
     */
    public void setXPathGetterResult(final List<Object> results)
    {
        // of course, we do that only with an interpreter running
        if (interpreter != null)
        {
            for (int i = 1; i <= results.size(); i++)
            {
                try
                {
                    interpreter.set(XPATH_GETTER_PREFIX + i, results.get(i - 1));
                }
                catch (final EvalError e)
                {
                    XltLogger.runTimeLogger.warn("Unable to take in the result of an xpath evaluation.", e);    
                }
            }
        }
    }

    /**
     * Take back the evaluation results to spice up the interpreter
     * 
     * @param xpathGettersResults a list of results
     * TODO test it
     */
    public void setRegExpGetterResult(final List<Object> results)
    {
        // of course, we do that only with an interpreter running
        if (interpreter != null)
        {
            for (int i = 1; i <= results.size(); i++)
            {
                try
                {
                    interpreter.set(REGEXP_GETTER_PREFIX + i, results.get(i - 1));
                }
                catch (final EvalError e)
                {
                    XltLogger.runTimeLogger.warn("Unable to take in the result of a regexp evaluation.", e);    
                }
            }
        }
    }
    
    /**
     * Returns true of header field is value, false otherwise.
     * @param fieldName header field to check
     * @return true if valid field, false otherwise
     * TODO test it
     */
    public static boolean isPermittedHeaderField(final String fieldName)
    {
        return PERMITTEDHEADERFIELDS.contains(fieldName);
    }
}
