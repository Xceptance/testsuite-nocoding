package com.xceptance.xlt.common.util;

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.xceptance.xlt.common.util.bsh.ParameterInterpreter;

public class WebActionFactoryBuilder
{
    private ParameterInterpreter interpreter;

    private String mode;
    
    private List<URLAction> actions;

    public static final String MODE_DOM = "dom";

    public static final String MODE_LIGHT = "light";

    public static final Set<String> PERMITTEDMODES = new HashSet<String>();

    static
    {
        PERMITTEDMODES.add(MODE_DOM);
        PERMITTEDMODES.add(MODE_LIGHT);
    }

    public WebActionFactoryBuilder(final ParameterInterpreter interpreter,
                                   final String mode,
                                   final List<URLAction> actions)
    {
        setMode(mode);
        setInterpreter(interpreter);
        setActions(actions);
    }
    public WebActionFactory buildFactory()
    {
        final WebActionFactory factory = produceFactory();
        return factory;
    }
    private void setActions(final List<URLAction> actions){
        ParameterUtils.isNotNull(actions, "List<URLAction>");
        this.actions = actions;
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
        ParameterUtils.isNotNull(interpreter, "ParameterInterpreter");
        this.interpreter = interpreter;
    }

    private WebActionFactory produceFactory()
    {

        final WebActionFactory resultFactory;

        if (this.mode.equals(MODE_DOM))
        {
            resultFactory = createHtmlPageActionFactory();
        }
        else if (this.mode.equals(MODE_LIGHT))
        {
            resultFactory = createLightWeightPageActionFactory();
        }
        else
        {
            throw new IllegalArgumentException("THIS WILL NEVER HAPPEN :D");
        }
        return resultFactory;
    }

    private HtmlPageActionFactory createHtmlPageActionFactory()
    {
        return new HtmlPageActionFactory(this.interpreter);
    }

    private LightWeightPageActionFactory createLightWeightPageActionFactory()
    {
        return new LightWeightPageActionFactory(this.interpreter);
    }

    private boolean isPermittedMode(final String item)
    {
        return PERMITTEDMODES.contains(item);
    }
}
