package com.xceptance.xlt.common.util.action.data;

import java.text.MessageFormat;

import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.common.util.bsh.ParameterInterpreter;

public class URLActionDataStoreBuilder
{
    private String name;

    private String selectionMode;

    private String selectionContent;

    private ParameterInterpreter interpreter;

    public URLActionDataStore build()
    {
        URLActionDataStore store = null;
        try
        {
            store = new URLActionDataStore(getName(), getSelectionMode(),
                                       getSelectionContent(), getInterpreter());
        }
        catch (final IllegalArgumentException e)
        {
            throw new IllegalArgumentException("Failed to create URLActionStore: " + e.getMessage(), e);
        }
        reset();

        return store;
    }

    public void reset()
    {
        this.name = null;
        this.selectionMode = null;
        this.selectionContent = null;
        this.interpreter = null;
        XltLogger.runTimeLogger.debug("Resetting values");
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
        XltLogger.runTimeLogger.debug(infoSetTagToValue("selectionMode", selectionMode));
    }

    public String getSelectionContent()
    {
        return selectionContent;
    }

    public void setSelectionContent(final String selectionContent)
    {
        this.selectionContent = selectionContent;
        XltLogger.runTimeLogger.debug(infoSetTagToValue("selectionContent", selectionContent));
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
        final String message = MessageFormat.format("Set tag \"{0}\" = \"{1}\" ", tag,
                                                    value);
        return message;
    }

    private String infoSetTag(final String tag)
    {
        final String message = MessageFormat.format("Set tag \"{0}\" ", tag);
        return message;
    }

}
