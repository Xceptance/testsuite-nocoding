package com.xceptance.xlt.common.util;

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Set;

import com.xceptance.xlt.common.util.bsh.ParameterInterpreter;

public class WebActionFactoryBuilder
{
    private ParameterInterpreter interpreter;
    
    private WebActionFactory factory;

    private String mode;

    public static final String MODE_DOM = "dom";

    public static final String MODE_LIGHT = "light";

    public static final Set<String> PERMITTEDMODES = new HashSet<String>();

    static
    {
        PERMITTEDMODES.add(MODE_DOM);
        PERMITTEDMODES.add(MODE_LIGHT);
    }

    public WebActionFactoryBuilder()
    {
    }

    public WebActionFactory buildFactory(final String mode,
                                         final ParameterInterpreter interpreter)
    {

        setMode(mode);
        setInterpreter(interpreter);
        produce();
        
        return this.factory;
    }
    private void setMode(final String mode)
    {
        if (isPermittedMode(mode))
        {
            this.mode = mode;
        }
        else
        {
            throw new IllegalArgumentException(
                                               MessageFormat.format("Running mode : \"{0}\" is not supported!",
                                                                    mode));
        }
    }

    private void setInterpreter(final ParameterInterpreter interpreter)
    {
        if (interpreter != null)
        {
            this.interpreter = interpreter;
        }
        else
        {
            throw new IllegalArgumentException("ParameterInterpreter cannot be null!");
        }
    }

    private void produce(){
        
        final WebActionFactory resultFactory;
        if (this.mode.equals(MODE_DOM))
        {
            resultFactory = new HtmlPageActionFactory();
        }
        else if (this.mode.equals(MODE_LIGHT))
        {
            resultFactory = new LightWeightPageActionFactory();
        }
        else
        {
            throw new IllegalArgumentException("THIS WILL NEVER HAPPEN :D");
        }
        this.factory = resultFactory;
    }
    private boolean isPermittedMode(final String item)
    {
        return PERMITTEDMODES.contains(item);
    }
}
