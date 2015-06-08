package com.xceptance.xlt.common.util;

import java.util.ArrayList;
import java.util.List;

import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.common.util.bsh.ParameterInterpreter;

public abstract class URLActionDataListBuilder
{
    protected List<URLActionData> actions = new ArrayList<URLActionData>();
    
    protected String filePath;
    
    protected ParameterInterpreter interpreter;
    
    protected URLActionDataBuilder actionBuilder;
    
    protected URLActionDataListBuilder(final String filePath,
                                   final ParameterInterpreter interpreter,
                                   final URLActionDataBuilder actionBuilder)
    {
        setFilePath(filePath);
        setInterpreter(interpreter);
        setActionBuilder(actionBuilder);
        XltLogger.runTimeLogger.debug("Creating new Instance");
    }
    
    abstract protected List<URLActionData> buildURLActions(); 

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

    private void setActionBuilder(final URLActionDataBuilder actionBuilder)
    {
        ParameterUtils.isNotNull(actionBuilder, "URLActionBuilder");
        this.actionBuilder = actionBuilder;
    }

}
