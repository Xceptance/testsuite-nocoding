package com.xceptance.xlt.common.util;

import com.xceptance.xlt.common.util.bsh.ParameterInterpreter;

public class URLActionDataValidationBuilder
{
    private String name;

    private String selectionMode;

    private String selectionContent;

    private String validationMode;

    private String validationContent;

    private ParameterInterpreter interpreter;

    public URLActionDataValidation build(){
        
        URLActionDataValidation validation;
        
        try{
            validation = new URLActionDataValidation(getName(), getSelectionMode(), getSelectionContent(), getValidationMode(), getValidationContent(), getInterpreter());
        }
        catch (final IllegalArgumentException e)
        {
            throw new IllegalArgumentException("Failed to create URLActionValidation: " + e.getMessage(), e);
        }
        reset();

        return validation;
    }
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
    }

    public String getSelectionMode()
    {
        return selectionMode;
    }

    public void setSelectionMode(final String selectionMode)
    {
        this.selectionMode = selectionMode;
    }

    public String getSelectionContent()
    {
        return selectionContent;
    }

    public void setSelectionContent(final String selectionContent)
    {
        this.selectionContent = selectionContent;
    }

    public String getValidationMode()
    {
        return validationMode;
    }

    public void setValidationMode(final String validationMode)
    {
        this.validationMode = validationMode;
    }

    public String getValidationContent()
    {
        return validationContent;
    }

    public void setValidationContent(final String validationContent)
    {
        this.validationContent = validationContent;
    }

    public ParameterInterpreter getInterpreter()
    {
        return interpreter;
    }

    public void setInterpreter(final ParameterInterpreter interpreter)
    {
        this.interpreter = interpreter;
    }
}
