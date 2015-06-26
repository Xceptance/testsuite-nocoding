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
import org.junit.Assert;
import org.xml.sax.InputSource;

import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.xceptance.common.util.RegExUtils;
import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.common.util.bsh.ParameterInterpreter;

public class URLActionValidation
{
    private String name;

    private String selectionMode;

    private String selectionContent;

    private String validationMode;

    private String validationContent;

    private ParameterInterpreter interpreter;

    private final static Set<String> PERMITTEDSELECTIONMODE = new HashSet<String>();

    private final static Set<String> PERMITTEDVALIDATIONMODE = new HashSet<String>();

    public static final String XPATH = "XPath";

    public static final String REGEXP = "RegExp";

    public static final String TEXT = "Text";

    public static final String HEADER = "Header";

    public static final String MATCHES = "Matches";

    public static final String COUNT = "Count";

    public static final String EXISTS = "Exists";

    static
    {
        PERMITTEDSELECTIONMODE.add(XPATH);
        PERMITTEDSELECTIONMODE.add(REGEXP);
        PERMITTEDSELECTIONMODE.add(HEADER);

        PERMITTEDVALIDATIONMODE.add(TEXT);
        PERMITTEDVALIDATIONMODE.add(MATCHES);
        PERMITTEDVALIDATIONMODE.add(COUNT);
        PERMITTEDVALIDATIONMODE.add(EXISTS);

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

    // fields cannot be empty!

    public URLActionValidation(String name, String selectionMode, String selectionContent, String validationMode,
        String validationContent, ParameterInterpreter interpreter)
    {
        XltLogger.runTimeLogger.info("Creating new Validation Item");
        setName(name);
        setSelectionMode(selectionMode);
        setSelectionContent(selectionContent);
        setValidationMode(validationMode);
        setValidationContent(validationContent);
        setParameterInterpreter(interpreter);
    }

    public void outlineRaw()
    {
        System.err.println("\t\t" + "Name : " + name);
        System.err.println("\t\t\t" + "Selection Mode : " + selectionMode);
        System.err.println("\t\t\t" + "Selection Value : " + selectionContent);
        System.err.println("\t\t\t" + "Validation Mode : " + validationMode);
        System.err.println("\t\t\t" + "Validation Value : " + validationContent);
    }

    public void outline()
    {
        System.err.println("\t\t" + "Name : " + getName());
        System.err.println("\t\t\t" + "Selection Mode : " + getSelectionMode());
        System.err.println("\t\t\t" + "Selection Content : " + getSelectionContent());
        System.err.println("\t\t\t" + "Validation Mode : " + getValidationMode());
        System.err.println("\t\t\t" + "Validation Content : " + getValidationContent());
    }

    private void setParameterInterpreter(ParameterInterpreter interpreter)
    {
        this.interpreter = (interpreter != null) ? interpreter
                                                : (ParameterInterpreter) throwIllegalArgumentException(getTagCannotBeNullMessage("Parameter Interpreter"));
        XltLogger.runTimeLogger.info(getSetNewTagMessage("Interpreter"));
    }

    private void setSelectionMode(String selectionMode)
    {
        this.selectionMode = (selectionMode != null) ? selectionMode
                                                    : (String) throwIllegalArgumentException(getTagCannotBeNullMessage("Selection Mode"));
        XltLogger.runTimeLogger.info(getSetTagToValueMessage("Selection Mode", selectionMode));
    }

    private void setValidationContent(String validationContent)
    {
        this.validationContent = validationContent;
        XltLogger.runTimeLogger.info(getSetTagToValueMessage("Validation Content", validationContent));
    }

    private void setValidationMode(String validationMode)
    {
        this.validationMode = (validationMode != null) ? validationMode
                                                      : (String) throwIllegalArgumentException(getTagCannotBeNullMessage("Validation Mode"));
        XltLogger.runTimeLogger.info(getSetTagToValueMessage("Validation Mode", validationMode));
    }

    private void setSelectionContent(String selectionContent)
    {
        this.selectionContent = selectionContent;
        XltLogger.runTimeLogger.info(getSetTagToValueMessage("Selection Content", selectionContent));
    }

    private void setName(String name)
    {
        this.name = name != null ? name
                                : (String) throwIllegalArgumentException("[URLActionValidation] Validation name cannot be null");
        XltLogger.runTimeLogger.info(MessageFormat.format("[URLActionValidation] Set Validation 'Name' to \"{0}\"",
                                                          name));
    }

    public String getName()
    {
        return interpreter.processDynamicData(this.name);
    }

    public String getSelectionMode()
    {
        String dynamicSelectionMode = interpreter.processDynamicData(selectionMode);
        if (!isPermittedSelectionMode(dynamicSelectionMode))
        {
            throw new IllegalArgumentException(getIllegalValueForTagMessage(dynamicSelectionMode, "Selection Mode"));
        }
        return dynamicSelectionMode;
    }

    public String getSelectionContent()
    {
        return interpreter.processDynamicData(selectionContent);
    }

    public String getValidationMode()
    {
        String dynamicValidationMode = interpreter.processDynamicData(validationMode);
        if (!isPermittedValidationMode(dynamicValidationMode))
        {
            throw new IllegalArgumentException(getIllegalValueForTagMessage(dynamicValidationMode, "Validation Mode"));
        }
        return dynamicValidationMode;
    }

    public String getValidationContent()
    {
        return interpreter.processDynamicData(validationContent);
    }

    public boolean isPermittedSelectionMode(String s)
    {
        return PERMITTEDSELECTIONMODE.contains(s);
    }

    public boolean isPermittedValidationMode(String s)
    {
        return PERMITTEDVALIDATIONMODE.contains(s);
    }

    public void validate(WebResponse response, HtmlPage page) throws XPathExpressionException
    {
        XltLogger.runTimeLogger.info(getStartValidationMessage());

        String contentType = getContentTypeOfResponse(response);

        if (HEADERCONTENTTYPES.get(contentType).equals(JSON))
        {
            validateJsonResponse(response);
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

    private void validateJsonResponse(WebResponse response) throws XPathExpressionException
    {
        XltLogger.runTimeLogger.info(getValidateContentTypeMessage(JSON));
        if (getSelectionMode().equalsIgnoreCase(HEADER))
        {
            validateHeaders(response);
        }
        else if (getSelectionMode().equalsIgnoreCase(XPATH))
        {
            validateXPath(createJsonFromResponse(response));
        }
        else if (getSelectionMode().equalsIgnoreCase(REGEXP))
        {
            // TODO
        }
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

    public void validateXPath(WebResponse response) throws XPathExpressionException
    {
        String contentType = response.getContentType();
        if (HEADERCONTENTTYPES.get(contentType).equals(JSON))
        {
            String jsonString = response.getContentAsString();
            JSONObject jsonObject = new JSONObject(jsonString);
            this.validate(jsonObject);
        }
        else if (HEADERCONTENTTYPES.get(contentType).equals(XML))
        {

        }
        else if (HEADERCONTENTTYPES.get(contentType).equals(HTML))
        {

        }
        else
        {
            XltLogger.runTimeLogger.warn("");
        }
    }

    public void validate(JSONObject jsonObject) throws XPathExpressionException
    {
        if (getSelectionMode().equals(XPATH))
        {
            validateXPath(jsonObject);
        }

    }

    private void validateXPath(JSONObject jsonObject) throws XPathExpressionException
    {
        XltLogger.runTimeLogger.info(getValidateXPathMessage());

        String xmlString = org.json.XML.toString(jsonObject);
        String xml = "<json>" + xmlString + "</json>";

        XPathFactory xpathFactory = XPathFactory.newInstance();
        XPath xpath = xpathFactory.newXPath();

        InputSource source = new InputSource(new StringReader(xml));

        String status = xpath.evaluate(getSelectionContent(), source);

        if (getValidationMode().equals(EXISTS))
        {
            validateExistance(status);
        }
        else if (getValidationMode().equals(TEXT) || getValidationMode().equals(MATCHES))
        {
            validateText(status);

        }
        else if (getValidationMode().equals(COUNT))
        {
            validateCount(status);

        }

    }

    private void validateExistance(String status)
    {
        XltLogger.runTimeLogger.info(getValidateExistanceMessage());
        Assert.assertNotNull("XPath not found: <" + getSelectionContent() + ">", status);
    }

    private void validateText(String status)
    {
        validateExistance(status);
        XltLogger.runTimeLogger.info(getValidateTextMessage(status));
        Assert.assertEquals(MessageFormat.format("Text does not match. Expected:<{0}> but was:<{1}>",
                                                 getValidationContent(), status), status, getValidationContent());
    }

    private void validateCount(String status)
    {
        validateExistance(status);
        XltLogger.runTimeLogger.info(getValidateCountMessage());
        // TODO
    }

    public void validate(HtmlPage page)
    {
        XltLogger.runTimeLogger.info(getStartValidationMessage());
        if (getSelectionMode().equals(XPATH))
        {
            validateXPath(page);
        }
        else if (getSelectionMode().equals(REGEXP))
        {
            // XltLogger.runTimeLogger.info(getValidateXPathMessage());
        }
        else if (getSelectionMode().equalsIgnoreCase(HEADER))
        {
            WebResponse response = page.getWebResponse();
            validateHeaders(response);
        }
    }

    private void validateHeaders(WebResponse response)
    {
        XltLogger.runTimeLogger.info(getValidateHeaderMessage());
        if (getValidationMode().equals(EXISTS))
        {
            validateExistanceHeader(response);
        }
        else if (getValidationMode().equals(TEXT) || getValidationMode().equals(MATCHES))
        {
            validateTextHeader(response);

        }
        else if (getValidationMode().equals(COUNT))
        {
            validateCountHeader(response);

        }

    }

    private void validateExistanceHeader(WebResponse response)
    {
        List<NameValuePair> headers = response.getResponseHeaders();
        boolean exists = false;
        for (NameValuePair header : headers)
        {
            if (header.getName().equals(getSelectionContent()))
            {
                exists = true;
            }
        }
        Assert.assertTrue(exists);
    }

    private void validateTextHeader(WebResponse response)
    {
        validateExistanceHeader(response);
        List<NameValuePair> headers = response.getResponseHeaders();
        for (NameValuePair header : headers)
        {
            if (header.getName().equals(getSelectionContent()))
            {
                Assert.assertEquals(header.getValue(), getValidationContent());
            }
        }
    }

    private void validateCountHeader(WebResponse response)
    {
        validateExistanceHeader(response);
        List<NameValuePair> headers = response.getResponseHeaders();
        int count = 0;
        for (NameValuePair header : headers)
        {
            if (header.getName().equals(getSelectionContent()))
            {
                count++;
            }
        }
        Assert.assertEquals(Integer.parseInt(getValidationContent()), count);
    }

    @SuppressWarnings("unchecked")
    private void validateXPath(HtmlPage page)
    {
        XltLogger.runTimeLogger.info(getValidateXPathMessage());
        final List<HtmlElement> elements = (List<HtmlElement>) page.getByXPath(getSelectionContent());

        if (getValidationMode().equals(EXISTS))
        {
            validateExistance(elements);
        }
        else if (getValidationMode().equals(TEXT) || getValidationMode().equals(MATCHES))
        {
            validateText(elements);

        }
        else if (getValidationMode().equals(COUNT))
        {
            validateCount(elements);

        }
    }

    private void validateExistance(List<HtmlElement> o)
    {
        XltLogger.runTimeLogger.info(getValidateExistanceMessage());
        Assert.assertFalse("[URLActionValidation] Elements not found: <" + getSelectionContent() + ">", o.isEmpty());
    }

    private void validateText(List<HtmlElement> elements)
    {
        validateExistance(elements);
        final String actual = elements.get(0).asText().trim();
        XltLogger.runTimeLogger.info(getValidateTextMessage(actual));
        System.err.println(getValidationContent());
        System.err.println(actual);
        Assert.assertNotNull(MessageFormat.format("[URLActionValidation] Text does not match. Expected:<{0}> but was:<{1}>",
                                                  getValidationContent(), actual),
                             RegExUtils.getFirstMatch(actual, getValidationContent()));

    }

    private void validateCount(List<HtmlElement> elements)
    {
        XltLogger.runTimeLogger.info(getValidateCountMessage());
        validateExistance(elements);
        Assert.assertEquals(MessageFormat.format("[URLActionValidation] Expected elements \"{0}\", but was \"{1}\" ",
                                                 getValidationContent(), elements.size()), getValidationContent(),
                            elements.size());
    }

    private Object throwIllegalArgumentException(String message)
    {
        throw new IllegalArgumentException(message);
    }

    private String getSetTagToValueMessage(String tag, String value)
    {
        String message = MessageFormat.format("[URLActionValidation] Validation: \"{0}\", Set \"{1}\" to value: \"{2}\"",
                                              this.name, tag, value);
        return message;
    }

    private String getSetNewTagMessage(String tag)
    {
        String message = MessageFormat.format("[URLActionValidation] Validation: \"{0}\", Set new \"{1}\"", this.name,
                                              tag);
        return message;
    }

    private String getIllegalValueForTagMessage(String value, String tag)
    {
        String message = MessageFormat.format("[URLActionValidation] Validation: \"{0}\", Illegal value: \"{1}\" for tag \"{2}\"",
                                              this.name, value, tag);
        return message;
    }

    private String getTagCannotBeNullMessage(String tag)
    {
        String message = MessageFormat.format("[URLActionValidation] Validation: \"{0}\", tag \"{1}\"  cannot be NULL",
                                              this.name, tag);
        return message;
    }

    private String getStartValidationMessage()
    {
        String message = MessageFormat.format("[URLActionValidation] -- Starting Validation -- with RAWDATA: Name : \"{0}\" Validate {1} : {2} for {3} : \"{4}\"",
                                              this.name, this.validationMode, this.validationContent,
                                              this.selectionMode, selectionContent);
        return message;
    }

    private String getValidateExistanceMessage()
    {
        String message = MessageFormat.format("[URLActionValidation] Validate EXISTANCE for Element : {0} : {1}",
                                              getSelectionMode(), getSelectionContent());
        return message;
    }

    private String getValidateTextMessage(String text)
    {
        String message = MessageFormat.format("[URLActionValidation] Validate TEXT, Check \"{0}\" for Element : {1} : {2} matches \"{3}\"",
                                              getValidationContent(), getSelectionMode(), getSelectionContent(), text);
        return message;
    }

    private String getValidateCountMessage()
    {
        String message = MessageFormat.format("[URLActionValidation] Validate COUNT : \"{0}\" for Element : {0} : {1}",
                                              getValidationContent(), getSelectionMode(), getSelectionContent());
        return message;
    }

    private String getValidateXPathMessage()
    {
        String message = MessageFormat.format("[URLActionValidation] Getting Element by XPath:  {0} ",
                                              getSelectionContent());
        return message;
    }

    private String getValidateRegExpMessage()
    {
        String message = MessageFormat.format("[URLActionValidation] Getting Element by RegExp: {0} ",
                                              getSelectionContent());
        return message;
    }

    private String getValidateHeaderMessage()
    {
        String message = MessageFormat.format("[URLActionValidation] Getting Header Element:  {0} ",
                                              getSelectionContent());
        return message;
    }

    private String getDetermineContentTypeMessage()
    {
        String message = MessageFormat.format("[URLActionValidation] Determining Content-Type of Response", "");

        return message;
    }

    private String getValidateContentTypeMessage(String type)
    {
        String message = MessageFormat.format("[URLActionValidation] Validation Response with Content-Type : \"{0}\"",
                                              type);

        return message;
    }

    private String getUnsupportedContentTypeMessage(String type)
    {
        String message = MessageFormat.format("[URLActionValidation] Unsupported Content Type: \"{0}\",  Stop Validation",
                                              type);

        return message;
    }

}
