package com.xceptance.xlt.common.util.action.data;

import java.text.MessageFormat;

import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.common.util.bsh.ParameterInterpreter;

/**
 * Helper class to build an {@link URLActionDataValidation}. <br>
 * Construct a {@link URLActionDataValidation} object step by step. For this fill the URLActionDataValidationBuilder
 * with values via setters until you want to create a URLActionDataValidation. For this call {@link #build()}.
 * 
 * @author matthias mitterreiter
 */

public class URLActionDataValidationBuilder
{
    private String name;

    private String selectionMode;

    private String selectionContent;

    private String validationMode;

    private String validationContent;

    private ParameterInterpreter interpreter;

    /**
     * Builds an {@link URLActionDataValidation} object from the values of the local attributes. If an important
     * attribute is not set or invalid, it throws. After execution, the values of the local attributes are reset to
     * 'null'.
     * 
     * @return {@link URLActionDataValidation}
     * @throws IllegalArgumentException
     */

    public URLActionDataValidation build()
    {

        URLActionDataValidation validation;

        try
        {
            validation = new URLActionDataValidation(getName(),
                                                     getSelectionMode(),
                                                     getSelectionContent(),
                                                     getValidationMode(),
                                                     getValidationContent(),
                                                     getInterpreter());
        }
        catch (final IllegalArgumentException e)
        {
            throw new IllegalArgumentException("Failed to create URLActionValidation: "
                                                   + e.getMessage(),
                                               e);
        }
        reset();

        return validation;
    }

    /**
     * Resets all local attribute values to 'null'.
     */
    public void reset()
    {
        this.name = null;
        this.selectionMode = null;
        this.selectionContent = null;
        this.validationMode = null;
        this.validationContent = null;
        this.interpreter = null;
    }

    public String getName()
    {
        return name;
    }

    public void setName(final String name)
    {
        this.name = name;
        XltLogger.runTimeLogger.debug(infoSetTagToValue("name", name));
    }

    public String getSelectionMode()
    {
        return selectionMode;
    }

    public void setSelectionMode(final String selectionMode)
    {
        this.selectionMode = selectionMode;
        XltLogger.runTimeLogger.debug(infoSetTagToValue("selectionMode",
                                                        selectionMode));
    }

    public String getSelectionContent()
    {
        return selectionContent;
    }

    public void setSelectionContent(final String selectionContent)
    {
        this.selectionContent = selectionContent;
        XltLogger.runTimeLogger.debug(infoSetTagToValue("selectionContent",
                                                        selectionContent));
    }

    public String getValidationMode()
    {
        return validationMode;
    }

    public void setValidationMode(final String validationMode)
    {
        this.validationMode = validationMode;
        XltLogger.runTimeLogger.debug(infoSetTagToValue("validationMode",
                                                        validationMode));
    }

    public String getValidationContent()
    {
        return validationContent;
    }

    public void setValidationContent(final String validationContent)
    {
        this.validationContent = validationContent;
        XltLogger.runTimeLogger.debug(infoSetTagToValue("validationContent", validationContent));
    }

    public ParameterInterpreter getInterpreter()
    {
        return interpreter;
    }

    public void setInterpreter(final ParameterInterpreter interpreter)
    {
        this.interpreter = interpreter;
        XltLogger.runTimeLogger.debug(infoSetTag("interpreter"));
    }

    private String infoSetTagToValue(final String tag, final String value)
    {
        final String message = MessageFormat.format("Set tag \"{0}\" = \"{1}\" ",
                                                    tag,
                                                    value);
        return message;
    }

    private String infoSetTag(final String tag)
    {
        final String message = MessageFormat.format("Set tag \"{0}\" ", tag);
        return message;
    }
}
