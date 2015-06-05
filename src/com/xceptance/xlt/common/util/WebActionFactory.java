package com.xceptance.xlt.common.util;


import com.xceptance.xlt.api.actions.AbstractWebAction;
import com.xceptance.xlt.common.util.bsh.ParameterInterpreter;

public abstract class WebActionFactory
{
    protected ParameterInterpreter interpreter;

    protected AbstractWebAction previousAction;
    
    protected URLActionResponseHandler responseHandler;
    
    protected URLActionRequestBuilder requestCreator;

    public WebActionFactory(final ParameterInterpreter interpreter,
                            final URLActionResponseHandler responseHandler,
                            final URLActionRequestBuilder requestBuilder)
    {
        setInterpreter(interpreter);

    }

    abstract public AbstractWebAction createPageAction();

    abstract public AbstractWebAction createXhrPageAction();

    private void setInterpreter(final ParameterInterpreter interpreter)
    {
        ParameterUtils.isNotNull(interpreter, "ParameterInterpreter");
        this.interpreter = interpreter;
    }

    public AbstractWebAction getPreviousAction()
    {
        return this.previousAction;
    }

}
