package com.xceptance.xlt.common.util.action.data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xceptance.xlt.common.util.ParameterUtils;
import com.xceptance.xlt.common.util.bsh.ParameterInterpreter;

/**
 * Abstract class that offers an interface for the implementation of various file type specific List<
 * {@link #URLActionData}> Builders.
 * 
 * @author matthias mitterreiter
 */
public abstract class URLActionDataListBuilder
{
    /**
     * Cache of all parsed data.
     */
    private static final Map<String, Object> DATA_CACHE = new HashMap<String, Object>();

    /**
     * Result list
     */
    protected final List<URLActionData> actions = new ArrayList<URLActionData>();

    /**
     * Path to the raw data, which must be parsed.
     */
    protected final String filePath;

    /**
     * Parameter interpreter.
     */
    protected final ParameterInterpreter interpreter;

    /**
     * The actual Builder that produces a single URLActionData object.
     */
    protected final URLActionDataBuilder actionBuilder;

    public URLActionDataListBuilder(final String filePath, final ParameterInterpreter interpreter, final URLActionDataBuilder actionBuilder)
    {
        ParameterUtils.isNotNull(filePath, "filePath");
        ParameterUtils.isNotNull(interpreter, "ParameterInterpreter");
        ParameterUtils.isNotNull(actionBuilder, "URLActionBuilder");

        this.filePath = filePath;
        this.interpreter = interpreter;
        this.actionBuilder = actionBuilder;
    }

    /**
     * @return the result List<{@link #URLActionData}>
     */
    public abstract List<URLActionData> buildURLActionDataList();

    /**
     * Parses the data from the file denoted by this instance's {@link #filePath}
     * 
     * @return the parsed data
     * @throws IOException
     */
    protected abstract Object parseData() throws IOException;

    /**
     * Gets the parsed data either from cache or in case of a cache-miss from the file denoted by this instance's
     * {@link #filePath} using {@link #parseData()}.
     * 
     * @return the parsed data (either from cache or from file)
     * @throws IOException
     */
    protected Object getOrParseData() throws IOException
    {
        synchronized (DATA_CACHE)
        {
            Object result = DATA_CACHE.get(filePath);
            if (result == null)
            {
                result = parseData();
                DATA_CACHE.put(filePath, result);
            }

            return result;
        }
    }
}
