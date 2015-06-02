package com.xceptance.xlt.common.util;

import java.util.Collections;
import java.util.List;

import com.xceptance.xlt.common.util.bsh.ParameterInterpreter;

public abstract class URLActionListBuilder
{
    protected List<URLAction> actions = Collections.emptyList();
    
    protected final String filePath;
    
    protected final ParameterInterpreter interpreter;
    
    protected final URLActionBuilder actionBuilder;
    
    protected URLActionListBuilder(final String filePath,
                                   final ParameterInterpreter interpreter,
                                   final URLActionBuilder builder)
    {
        this.filePath = filePath;
        this.interpreter = interpreter;
        this.actionBuilder = builder;
       
    }
    
    public abstract List<URLAction> buildURLActions();
}
