package com.xceptance.xlt.common.util;

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.common.util.bsh.ParameterInterpreter;

public class URLActionDataValidation
{
    private String name;

    private String selectionMode;

    private String selectionContent;

    private String validationMode;

    private String validationContent;

    private ParameterInterpreter interpreter;

    public final static Set<String> PERMITTEDSELECTIONMODE = new HashSet<String>();

    public final static Set<String> PERMITTEDVALIDATIONMODE = new HashSet<String>();

    public static final String XPATH = "XPath";

    public static final String REGEXP = "RegExp";

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

    public URLActionDataValidation(@NonNull final String name,
                               @NonNull final String selectionMode,
                               final String selectionContent,
                               @NonNull final String validationMode,
                               final String validationContent,
                               @NonNull final ParameterInterpreter interpreter)
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

    private void setParameterInterpreter(final ParameterInterpreter interpreter)
    {
        this.interpreter = (interpreter != null) ? interpreter
                                                : (ParameterInterpreter) throwIllegalArgumentException(getTagCannotBeNullMessage("Parameter Interpreter"));
        XltLogger.runTimeLogger.info(getSetNewTagMessage("Interpreter"));
    }

    private void setSelectionMode(final String selectionMode)
    {
        this.selectionMode = (selectionMode != null) ? selectionMode
                                                    : (String) throwIllegalArgumentException(getTagCannotBeNullMessage("Selection Mode"));
        XltLogger.runTimeLogger.info(getSetTagToValueMessage("Selection Mode", selectionMode));
    }

    private void setValidationContent(final String validationContent)
    {
        this.validationContent = validationContent;
        XltLogger.runTimeLogger.info(getSetTagToValueMessage("Validation Content", validationContent));
    }

    private void setValidationMode(final String validationMode)
    {
        this.validationMode = (validationMode != null) ? validationMode
                                                      : (String) throwIllegalArgumentException(getTagCannotBeNullMessage("Validation Mode"));
        XltLogger.runTimeLogger.info(getSetTagToValueMessage("Validation Mode", validationMode));
    }

    private void setSelectionContent(final String selectionContent)
    {
        this.selectionContent = selectionContent;
        XltLogger.runTimeLogger.info(getSetTagToValueMessage("Selection Content", selectionContent));
    }

    private void setName(final String name)
    {
        this.name = name != null ? name : (String) throwIllegalArgumentException("Validation name cannot be null");
        XltLogger.runTimeLogger.info(MessageFormat.format("Set Validation 'Name' to \"{0}\"", name));
    }

    public String getName()
    {
        return interpreter.processDynamicData(this.name);
    }

    public String getSelectionMode()
    {
        final String dynamicSelectionMode = interpreter.processDynamicData(selectionMode);
        if (!isPermittedSelectionMode(dynamicSelectionMode))
        {
            throw new IllegalArgumentException(getIllegalValueForTagMessage(dynamicSelectionMode, "Selection Mode"));
        }
        return dynamicSelectionMode;
    }

    @Nullable
    public String getSelectionContent()
    {
        return interpreter.processDynamicData(selectionContent);
    }

    public String getValidationMode()
    {
        final String dynamicValidationMode = interpreter.processDynamicData(validationMode);
        if (!isPermittedValidationMode(dynamicValidationMode))
        {
            throw new IllegalArgumentException(getIllegalValueForTagMessage(dynamicValidationMode, "Validation Mode"));
        }
        return dynamicValidationMode;
    }

    @Nullable
    public String getValidationContent()
    {
        return interpreter.processDynamicData(validationContent);
    }

    public boolean isPermittedSelectionMode(final String s)
    {
        return PERMITTEDSELECTIONMODE.contains(s);
    }

    public boolean isPermittedValidationMode(final String s)
    {
        return PERMITTEDVALIDATIONMODE.contains(s);
    }

    private Object throwIllegalArgumentException(final String message)
    {
        throw new IllegalArgumentException(message);
    }

    private String getSetTagToValueMessage(final String tag, final String value)
    {
        final String message = MessageFormat.format("Validation: \"{0}\", Set \"{1}\" to value: \"{2}\"",
                                                    this.name, tag, value);
        return message;
    }

    private String getSetNewTagMessage(final String tag)
    {
        final String message = MessageFormat.format("Validation: \"{0}\", Set new \"{1}\"",
                                                    this.name, tag);
        return message;
    }

    private String getIllegalValueForTagMessage(final String value, final String tag)
    {
        final String message = MessageFormat.format("Validation: \"{0}\", Illegal value: \"{1}\" for tag \"{2}\"",
                                                    this.name, value, tag);
        return message;
    }

    private String getTagCannotBeNullMessage(final String tag)
    {
        final String message = MessageFormat.format("Validation: \"{0}\", tag \"{1}\"  cannot be NULL",
                                                    this.name, tag);
        return message;
    }
}
