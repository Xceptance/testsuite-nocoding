package com.xceptance.xlt.common.util;

import java.io.StringReader;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.json.JSONObject;
import org.xml.sax.InputSource;

import bsh.EvalError;

import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.common.util.bsh.ParameterInterpreter;

public class URLActionStore
{
    public final static Set<String> PERMITTEDSELECTIONMODE = new HashSet<String>();

    public static final String XPATH = "XPath";

    public static final String REGEXP = "RegExp";

    public static final String HEADER = "Header";

    static
    {
        PERMITTEDSELECTIONMODE.add(XPATH);
        PERMITTEDSELECTIONMODE.add(REGEXP);
        PERMITTEDSELECTIONMODE.add(HEADER);
    }

    static final HashMap<String, String> HEADERCONTENTTYPES = new HashMap<String, String>();

    static final String JSON = "json";

    static final String HTML = "html";

    static final String XML = "xml";

    static
    {
        HEADERCONTENTTYPES.put("application/json", JSON);
        HEADERCONTENTTYPES.put("text/json", JSON);
        HEADERCONTENTTYPES.put("text/x-json", JSON);

        HEADERCONTENTTYPES.put("text/xml", XML);
        HEADERCONTENTTYPES.put("application/xml", XML);

        HEADERCONTENTTYPES.put("text/html", HTML);
        HEADERCONTENTTYPES.put("text/application", HTML);

    }

    private String name;

    private String selectionMode;

    private String selectionContent;

    private ParameterInterpreter interpreter;

    public void outlineRaw()
    {
        System.err.println("\t\t" + this.name);
        System.err.println("\t\t\t" + this.selectionMode + " : " + this.selectionContent);
    }

    public void outline()
    {
        System.err.println("\t\t" + this.getName());
        System.err.println("\t\t\t" + this.getSelectionMode() + " : " + this.getSelectionContent());
    }

    public URLActionStore(String name, String selectionMode, String selectionContent, ParameterInterpreter interpreter)
    {
        XltLogger.runTimeLogger.info("Creating new Store Item");
        setName(name);
        setSelectionMode(selectionMode);
        setSelectionContent(selectionContent);
        setParameterInterpreter(interpreter);
    }

    private void setParameterInterpreter(ParameterInterpreter interpreter)
    {
        this.interpreter = (interpreter != null) ? interpreter
                                                : (ParameterInterpreter) throwIllegalArgumentException(getTagCannotBeNullMessage("Parameter Interpreter"));
    }

    public void setSelectionContent(String selectionContent)
    {
        this.selectionContent = (selectionContent != null) ? selectionContent
                                                          : (String) throwIllegalArgumentException(getTagCannotBeNullMessage("Selection Content"));
    }

    public void setSelectionMode(String selectionMode)
    {
        this.selectionMode = (selectionMode != null) ? selectionMode
                                                    : (String) throwIllegalArgumentException(getTagCannotBeNullMessage("Selection Mode"));
    }

    public void setName(String name)
    {
        this.name = (name != null) ? name
                                  : (String) throwIllegalArgumentException("[URLActionStore] \"Name\" cannot be null");
        XltLogger.runTimeLogger.info(MessageFormat.format("[URLActionStore] Set Validation 'Name' to \"{0}\"", name));
    }

    public String getName()
    {
        return interpreter.processDynamicData(this.name);
    }

    public String getSelectionMode()
    {
        String dynamicSelectionMode = interpreter.processDynamicData(this.selectionMode);
        if (!isPermittedSelectionMode(dynamicSelectionMode))
        {
            throw new IllegalArgumentException(getIllegalValueForTagMessage(dynamicSelectionMode, "Selection Mode"));
        }
        return dynamicSelectionMode;
    }

    public String getSelectionContent()
    {
        return interpreter.processDynamicData(this.selectionContent);
    }

    public boolean isPermittedSelectionMode(String s)
    {
        return PERMITTEDSELECTIONMODE.contains(s);
    }

    public void feedParameterInterpreter(HtmlPage page, WebResponse response) throws XPathExpressionException, EvalError
    {
        XltLogger.runTimeLogger.info(getStartStoringMessage());
        String contentType = getContentTypeOfResponse(response);

        if(getSelectionMode().equals(HEADER)){
            storeHeader(response);
        }
        else if (HEADERCONTENTTYPES.get(contentType).equals(JSON))
        {
            storeJson(response);
        }
        else if (HEADERCONTENTTYPES.get(contentType).equals(XML))
        {
            // DO
        }
        else if (HEADERCONTENTTYPES.get(contentType).equals(HTML))
        {
            // Do
        }
        else
        {
            XltLogger.runTimeLogger.warn(getUnsupportedContentTypeMessage(contentType));
        }
    }
    private void storeJson(WebResponse response) throws XPathExpressionException, EvalError{
        
        XltLogger.runTimeLogger.info(getStoreContentTypeMessage(JSON));
        JSONObject jsonObject = createJsonFromResponse(response);
        if(getSelectionMode().equals(XPATH)){
            storeXPath(jsonObject);
        }
        else if(getSelectionMode().equals(REGEXP)){
            //toDO
        }
        
    }
    private void storeXPath(JSONObject jsonObject) throws XPathExpressionException, EvalError{
        
        XltLogger.runTimeLogger.info(getStoreXPathMessage());
       
        String xmlString = org.json.XML.toString(jsonObject);
        String xml = "<json>" + xmlString + "</json>";
        XPathFactory xpathFactory = XPathFactory.newInstance();
        XPath xpath = xpathFactory.newXPath();
        InputSource source = new InputSource(new StringReader(xml));
        String status = xpath.evaluate(getSelectionContent(), source);
        
        NameValuePair nvp = new NameValuePair(getName(), status);
        interpreter.addVariables(nvp);
        
    }
    private JSONObject createJsonFromResponse(WebResponse response)
    {
        String jsonString = response.getContentAsString();
        JSONObject jsonObject = new JSONObject(jsonString);
        return jsonObject;
    }
    private String getContentTypeOfResponse(WebResponse response)
    {
        XltLogger.runTimeLogger.info(getDetermineContentTypeMessage());
        String contentType = response.getContentType();
        return contentType;
    }

    public void feedParameterInterpreter(HtmlPage page) throws EvalError
    {
        XltLogger.runTimeLogger.info(getStartStoringMessage());
        if (getSelectionMode().equalsIgnoreCase(XPATH))
        {
            storeXPath(page);
        }
        else if (getSelectionMode().equalsIgnoreCase(REGEXP))
        {
            storeRegExp(page);
        }
        else if (getSelectionMode().equalsIgnoreCase(HEADER))
        {
            WebResponse response = page.getWebResponse();
            storeHeader(response);
        }
    }

    private void storeHeader(WebResponse response) throws EvalError
    {
        XltLogger.runTimeLogger.info(getStoreHeaderMessage());
        List<NameValuePair> headers = response.getResponseHeaders();
        for (NameValuePair header : headers)
        {
            if (header.getName().equals(this.selectionContent))
            {  
                NameValuePair nvp = new NameValuePair(getName(), header.getValue());
                interpreter.addVariables(nvp);
            }
        }
    }

    private void storeXPath(HtmlPage page) throws EvalError
    {
        XltLogger.runTimeLogger.info(getStoreXPathMessage());
        final List<HtmlElement> elements = (List<HtmlElement>) page.getByXPath(selectionContent);
        if (elements == null)
        {
            // blabla, validateExistance
        }
        else
        {
            final String actual = elements.get(0).asText().trim();
            NameValuePair nvp = new NameValuePair(this.getName(), actual);
            interpreter.addVariables(nvp);
        }

    }

    private void storeRegExp(HtmlPage page)
    {

    }

    private Object throwIllegalArgumentException(String message)
    {
        throw new IllegalArgumentException(message);
    }

    private String getTagCannotBeNullMessage(String tag)
    {
        String message = MessageFormat.format("[URLActionStore] Store: \"{0}\", tag \"{1}\"  cannot be NULL",
                                              this.name, tag);
        return message;
    }

    private String getIllegalValueForTagMessage(String value, String tag)
    {
        String message = MessageFormat.format("[URLActionStore] Store: \"{0}\", Illegal value: \"{1}\" for tag \"{2}\"",
                                              this.name, value, tag);
        return message;
    }
    private String getDetermineContentTypeMessage()
    {
        String message = MessageFormat.format("[URLActionStore] Determining Content-Type of Response", "");

        return message;
    }
    private String getUnsupportedContentTypeMessage(String type)
    {
        String message = MessageFormat.format("[URLActionStore] Unsupported Content Type: \"{0}\",  Stop Storing Items",
                                              type);

        return message;
    }
    private String getStartStoringMessage()
    {
        String message = MessageFormat.format("[URLActionStore] RAWDATA - Name : \"{0}\", Selection Mode : \"{1}\", Selection Content : \"{2}\"", this.name, this.selectionMode, this.selectionContent);
        return message;
    }
    private String getStoreXPathMessage()
    {
        String message = MessageFormat.format("[URLActionStore] Getting Element by XPath \"{0}\"", getSelectionContent());
        return message;
    }
    private String getStoreHeaderMessage()
    {
        String message = MessageFormat.format("[URLActionStore] Getting Header Element \"{0}\"", getSelectionContent());
        return message;
    }
    private String getStoreContentTypeMessage(String type){
        String message = MessageFormat.format("[URLActionStore] Response of Content-Type : \"{0}\"", type);
        return message;
    }
}
