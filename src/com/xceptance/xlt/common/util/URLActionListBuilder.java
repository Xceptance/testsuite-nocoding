package com.xceptance.xlt.common.util;

import java.util.ArrayList;
import java.util.List;

import com.xceptance.xlt.common.util.bsh.ParameterInterpreter;

public abstract class URLActionListBuilder
{
    protected List<URLAction> actions = new ArrayList<URLAction>();
    
    protected String filePath;
    
    protected ParameterInterpreter interpreter;
    
    protected URLActionBuilder actionBuilder;
    
    protected URLActionListBuilder(final String filePath,
                                   final ParameterInterpreter interpreter,
                                   final URLActionBuilder actionBuilder)
    {
        setFilePath(filePath);
        setInterpreter(interpreter);
        setActionBuilder(actionBuilder);
    }
    
    abstract protected List<URLAction> buildURLActions(); 

    private void setFilePath(final String filePath)
    {
        ParameterUtils.isNotNull(filePath, "filePath");
        this.filePath = filePath;
    }

    private void setInterpreter(final ParameterInterpreter interpreter)
    {
        ParameterUtils.isNotNull(interpreter, "ParameterInterpreter");
        this.interpreter = interpreter;
    }

    private void setActionBuilder(final URLActionBuilder actionBuilder)
    {
        ParameterUtils.isNotNull(actionBuilder, "URLActionBuilder");
        this.actionBuilder = actionBuilder;
    }

}
