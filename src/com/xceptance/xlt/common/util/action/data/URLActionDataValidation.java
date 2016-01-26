package com.xceptance.xlt.common.util.action.data;

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.jdt.annotation.Nullable;

import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.common.util.bsh.ParameterInterpreter;

/**
 * <p>
 * Data container which holds all the necessary information to grab and validate information out of a http request.
 * </p>
 */
public class URLActionDataValidation
{
    /**
     * name of the validation.
     */
    private String name;

    /**
     * the way you want to take information out of the http response. <br>
     * See {@link #PERMITTEDSELECTIONMODE supported modes}.
     */
    private String selectionMode;

    /**
     * the individual selection specification.
     */
    private String selectionContent;

    /**
     * the way you want to validate the response information. <br>
     * See {@link #PERMITTEDVALIDATIONMODE supported modes}.
     */
    private String validationMode;

    /**
     * the individual validation specification.
     */
    private String validationContent;

    /**
     * The interpreter for dynamic parameter interpretation.
     */
    private ParameterInterpreter interpreter;

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

    public static final String XPATH = "XPath";

    public static final String REGEXP = "Regex";

    public static final String TEXT = "Text";

    public static final String HEADER = "Header";

    public static final String COOKIE = "Cookie";

    public static final String MATCHES = "Matches";

    public static final String COUNT = "Count";

    public static final String EXISTS = "Exists";

    static
    {
        PERMITTEDSELECTIONMODE.add(XPATH);
        PERMITTEDSELECTIONMODE.add(REGEXP);
        PERMITTEDSELECTIONMODE.add(HEADER);
        PERMITTEDSELECTIONMODE.add(COOKIE);

        PERMITTEDVALIDATIONMODE.add(TEXT);
        PERMITTEDVALIDATIONMODE.add(MATCHES);
        PERMITTEDVALIDATIONMODE.add(COUNT);
        PERMITTEDVALIDATIONMODE.add(EXISTS);
    }

    /**
     * Takes the minimal set of parameters that are necessary to select and v information <br>
     * from a http response.
     * 
     * @param name
     * @param selectionMode
     * @param selectionContent
     * @param validationMode
     * @param validationContent
     * @param interpreter
     */
    public URLActionDataValidation(final String name, final String selectionMode, final String selectionContent,
                                   final String validationMode, final String validationContent, final ParameterInterpreter interpreter)
    {
        XltLogger.runTimeLogger.debug("Creating new Validation Item");
        setName(name);
        setSelectionMode(selectionMode);
        setSelectionContent(selectionContent);
        setValidationMode(validationMode);
        setValidationContent(validationContent);
        setParameterInterpreter(interpreter);
    }

    /**
     * For debugging purpose. <br>
     * 'err-streams' the attributes of the object without dynamic interpretation of the return values. <br>
     */
    public void outlineRaw()
    {
        System.err.println("\t\t" + "Name : " + name);
        System.err.println("\t\t\t" + "Selection Mode : " + selectionMode);
        System.err.println("\t\t\t" + "Selection Value : " + selectionContent);
        System.err.println("\t\t\t" + "Validation Mode : " + validationMode);
        System.err.println("\t\t\t" + "Validation Value : " + validationContent);
    }

    /**
     * For debugging purpose. <br>
     * 'err-streams' the attributes of the object. Interprets the values via {@link #interpreter} <br>
     */
    public void outline()
    {
        System.err.println("\t\t" + "Name : " + getName());
        System.err.println("\t\t\t" + "Selection Mode : " + getSelectionMode());
        System.err.println("\t\t\t" + "Selection Content : " + getSelectionContent());
        System.err.println("\t\t\t" + "Validation Mode : " + getValidationMode());
        System.err.println("\t\t\t" + "Validation Content : " + getValidationContent());
    }

    /**
     * @param interpreter
     *            : if NULL throws.
     * @throws IllegalArgumentException
     */
    private void setParameterInterpreter(final ParameterInterpreter interpreter)
    {
        this.interpreter = (interpreter != null)
                                                ? interpreter
                                                : (ParameterInterpreter) throwIllegalArgumentException(getTagCannotBeNullMessage("Parameter Interpreter"));
        XltLogger.runTimeLogger.debug(getSetNewTagMessage("Interpreter"));
    }

    /**
     * @param selectionMode
     *            :if NULL throws.
     * @throws IllegalArgumentException
     */
    private void setSelectionMode(final String selectionMode)
    {
        this.selectionMode = (selectionMode != null) ? selectionMode
                                                    : (String) throwIllegalArgumentException(getTagCannotBeNullMessage("Selection Mode"));
        XltLogger.runTimeLogger.debug(getSetTagToValueMessage("Selection Mode", selectionMode));
    }

    /**
     * @param validationContent
     */
    private void setValidationContent(final String validationContent)
    {
        this.validationContent = validationContent;
        XltLogger.runTimeLogger.debug(getSetTagToValueMessage("Validation Content", validationContent));
    }

    /**
     * @param validateionMode
     *            :if NULL throws.
     * @throws IllegalArgumentException
     */
    private void setValidationMode(final String validationMode)
    {
        this.validationMode = (validationMode != null)
                                                      ? validationMode
                                                      : (String) throwIllegalArgumentException(getTagCannotBeNullMessage("Validation Mode"));
        XltLogger.runTimeLogger.debug(getSetTagToValueMessage("Validation Mode", validationMode));
    }

    /**
     * @param selectionContent
     */
    private void setSelectionContent(final String selectionContent)
    {
        this.selectionContent = selectionContent;
        XltLogger.runTimeLogger.debug(getSetTagToValueMessage("Selection Content", selectionContent));
    }

    /**
     * @param name
     *            :if NULL throws.
     * @throws IllegalArgumentException
     */
    private void setName(final String name)
    {
        this.name = name != null ? name : (String) throwIllegalArgumentException("Validation name cannot be null");
        XltLogger.runTimeLogger.debug(MessageFormat.format("Set Validation 'Name' to \"{0}\"", name));
    }

    /**
     * @return {@link #name}, after its dynamic interpretation via the {@link #interpreter}.
     */
    public String getName()
    {
        return interpreter.processDynamicData(this.name);
    }

    /**
     * @return {@link #selectionMode }, after its dynamic interpretation via the {@link #interpreter}.
     */
    public String getSelectionMode()
    {
        final String dynamicSelectionMode = interpreter.processDynamicData(selectionMode);
        if (!isPermittedSelectionMode(dynamicSelectionMode))
        {
            throw new IllegalArgumentException(getIllegalValueForTagMessage(dynamicSelectionMode, "Selection Mode"));
        }
        return dynamicSelectionMode;
    }

    /**
     * @return {@link #selectionContent}, after its dynamic interpretation via the {@link #interpreter}.
     */
    @Nullable
    public String getSelectionContent()
    {
        return interpreter.processDynamicData(selectionContent);
    }

    /**
     * @return {@link #validationMode}, after its dynamic interpretation via the {@link #interpreter}.
     */
    public String getValidationMode()
    {
        final String dynamicValidationMode = interpreter.processDynamicData(validationMode);
        if (!isPermittedValidationMode(dynamicValidationMode))
        {
            throw new IllegalArgumentException(getIllegalValueForTagMessage(dynamicValidationMode, "Validation Mode"));
        }
        return dynamicValidationMode;
    }

    /**
     * @return {@link #validationContent}, after its dynamic interpretation via the {@link #interpreter}.
     */
    @Nullable
    public String getValidationContent()
    {
        return interpreter.processDynamicData(validationContent);
    }

    /**
     * @param selectionMode
     * @return if ({@link #selectionMode} is permitted) ? true : false.
     */
    public boolean isPermittedSelectionMode(final String s)
    {
        return PERMITTEDSELECTIONMODE.contains(s);
    }

    /**
     * @param selectionMode
     * @return if ({@link #validationMode} is permitted) ? true : false.
     */
    public boolean isPermittedValidationMode(final String s)
    {
        return PERMITTEDVALIDATIONMODE.contains(s);
    }

    /**
     * Dirty way of throwing a IllegalArgumentException with the passed message.
     * 
     * @param message
     * @return nothing.
     */
    private Object throwIllegalArgumentException(final String message)
    {
        throw new IllegalArgumentException(message);
    }

    /**
     * @param tag
     * @param value
     * @return Formated message.
     */
    private String getSetTagToValueMessage(final String tag, final String value)
    {
        final String message = MessageFormat.format("Validation: \"{0}\", Set \"{1}\" to value: \"{2}\"", this.name, tag, value);
        return message;
    }

    /**
     * @param tag
     * @return Formated message.
     */
    private String getSetNewTagMessage(final String tag)
    {
        final String message = MessageFormat.format("Validation: \"{0}\", Set new \"{1}\"", this.name, tag);
        return message;
    }

    /**
     * @param value
     * @param tag
     * @return Formated message.
     */
    private String getIllegalValueForTagMessage(final String value, final String tag)
    {
        final String message = MessageFormat.format("Validation: \"{0}\", Illegal value: \"{1}\" for tag \"{2}\"", this.name, value, tag);
        return message;
    }

    /**
     * @param tag
     * @return Formated message.
     */
    private String getTagCannotBeNullMessage(final String tag)
    {
        final String message = MessageFormat.format("Validation: \"{0}\", tag \"{1}\"  cannot be NULL", this.name, tag);
        return message;
    }
}
