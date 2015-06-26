package com.xceptance.xlt.common.util.action.data;

import java.util.ArrayList;
import java.util.List;

import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.common.util.ParameterUtils;
import com.xceptance.xlt.common.util.bsh.ParameterInterpreter;
/**
 * Abstract class that offers an interface for the implementation of various 
 * file type specific List<{@link #URLActionData}> Builders.
 * 
 * @author matthias mitterreiter
 *
 */
public abstract class URLActionDataListBuilder
{
    /**
     * Result list
     */
    protected List<URLActionData> actions = new ArrayList<URLActionData>();
    
    /**
     * Path to the raw data, which must be parsed.
     */
    protected String filePath;
    
    protected ParameterInterpreter interpreter;
    
    /**
     * The actual Builder that produces a single URLActionData object.
     */
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
    
    /**
     * @return the result List<{@link #URLActionData}>
     */
    abstract protected List<URLActionData> buildURLActionDataList(); 

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
