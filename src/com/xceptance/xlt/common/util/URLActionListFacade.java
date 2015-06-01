package com.xceptance.xlt.common.util;

import java.util.List;

import com.xceptance.xlt.common.util.bsh.ParameterInterpreter;

public class URLActionListFacade
{
    private final String filePath;

    private final ParameterInterpreter interpreter;

    public URLActionListFacade(final String filePath, final ParameterInterpreter interpreter)
    {
        this.filePath = filePath;
        this.interpreter = interpreter;

    }

    public List<URLAction> buildUrlActions()
    {
        return null;
    }
}
