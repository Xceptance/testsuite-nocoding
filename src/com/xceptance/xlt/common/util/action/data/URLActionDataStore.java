package com.xceptance.xlt.common.util.action.data;

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.jdt.annotation.Nullable;

import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.common.util.bsh.ParameterInterpreter;

public class URLActionDataStore
{
    private String name;

    private String selectionMode;

    private String selectionContent;

    private ParameterInterpreter interpreter;

    public final static Set<String> PERMITTEDSELECTIONMODE = new HashSet<String>();

    public static final String XPATH = "XPath";

    public static final String REGEXP = "RegExp";

    public static final String HEADER = "Header";

    public static final String COOKIE = "Cookie";

    static
    {
        PERMITTEDSELECTIONMODE.add(XPATH);
        PERMITTEDSELECTIONMODE.add(REGEXP);
        PERMITTEDSELECTIONMODE.add(HEADER);
        PERMITTEDSELECTIONMODE.add(COOKIE);
    }

    public URLActionDataStore(final String name,
                              final String selectionMode,
                              final String selectionContent,
                              final ParameterInterpreter interpreter)
    {
        XltLogger.runTimeLogger.debug("Creating new Instance");
        setName(name);
        setSelectionMode(selectionMode);
        setSelectionContent(selectionContent);
        setParameterInterpreter(interpreter);
    }

    public void outlineRaw()
    {
        System.err.println("\t\t" + this.name);
        System.err.println("\t\t\t" + this.selectionMode + " : "
                           + this.selectionContent);
    }

    public void outline()
    {
        System.err.println("\t\t" + getName());
        System.err.println("\t\t\t" + getSelectionMode() + " : "
                           + getSelectionContent());
    }

    private void setParameterInterpreter(final ParameterInterpreter interpreter)
    {
        this.interpreter = (interpreter != null) ? interpreter
                                                : (ParameterInterpreter) throwIllegalArgumentException(getTagCannotBeNullMessage("ParameterInterpreter"));
        XltLogger.runTimeLogger.debug("Set 'ParameterInterpreter'");

    }

    public void setSelectionContent(final String selectionContent)
    {
        this.selectionContent = (selectionContent != null) ? selectionContent
                                                          : (String) throwIllegalArgumentException(getTagCannotBeNullMessage("Selection Content"));
        XltLogger.runTimeLogger.debug(MessageFormat.format("Set 'Selection Content': \"{0}\"",
                                                           selectionContent));
    }

    public void setSelectionMode(final String selectionMode)
    {
        this.selectionMode = (selectionMode != null) ? selectionMode
                                                    : (String) throwIllegalArgumentException(getTagCannotBeNullMessage("Selection Mode"));
        XltLogger.runTimeLogger.debug(MessageFormat.format("Set 'Selection Mode': \"{0}\"",
                                                           selectionMode));
    }

    public void setName(final String name)
    {
        this.name = (name != null) ? name
                                  : (String) throwIllegalArgumentException("\"Name\" cannot be null");
        XltLogger.runTimeLogger.debug(MessageFormat.format("Set 'Name' to \"{0}\"",
                                                           name));
    }

    public String getName()
    {
        return interpreter.processDynamicData(this.name);
    }

    public String getSelectionMode()
    {
        final String dynamicSelectionMode = interpreter.processDynamicData(this.selectionMode);
        if (!isPermittedSelectionMode(dynamicSelectionMode))
        {
            throw new IllegalArgumentException(getIllegalValueForTagMessage(dynamicSelectionMode,
                                                                            "Selection Mode"));
        }
        return dynamicSelectionMode;
    }

    @Nullable
    public String getSelectionContent()
    {
        return interpreter.processDynamicData(this.selectionContent);
    }

    public ParameterInterpreter getInterpreter()
    {
        return this.interpreter;
    }

    public boolean isPermittedSelectionMode(final String s)
    {
        return PERMITTEDSELECTIONMODE.contains(s);
    }

    private Object throwIllegalArgumentException(final String message)
    {
        throw new IllegalArgumentException(message);
    }

    private String getTagCannotBeNullMessage(final String tag)
    {
        final String message = MessageFormat.format("Store: \"{0}\", tag \"{1}\"  cannot be NULL",
                                                    this.name,
                                                    tag);
        return message;
    }

    private String getIllegalValueForTagMessage(final String value,
                                                final String tag)
    {
        final String message = MessageFormat.format("Store: \"{0}\", Illegal value: \"{1}\" for tag \"{2}\"",
                                                    this.name,
                                                    value,
                                                    tag);
        return message;
    }
}
