package com.xceptance.xlt.common.util;

import java.util.ArrayList;
import java.util.List;

import com.xceptance.xlt.common.util.bsh.ParameterInterpreter;

public abstract class URLActionListBuilder
{
    protected List<URLAction> actions = new ArrayList<URLAction>();
    
    protected final String filePath;
    
    protected final ParameterInterpreter interpreter;
    
    protected final URLActionBuilder actionBuilder;
    
    protected URLActionListBuilder(final String filePath,
                                   final ParameterInterpreter interpreter,
                                   final URLActionBuilder actionBuilder)
    {
        this.filePath = filePath;
        this.interpreter = interpreter;
        this.actionBuilder = actionBuilder;
       
    }
    
    public abstract List<URLAction> buildURLActions();
}
