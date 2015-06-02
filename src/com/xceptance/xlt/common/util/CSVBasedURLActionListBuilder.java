package com.xceptance.xlt.common.util;

import java.util.List;

import com.xceptance.xlt.common.util.bsh.ParameterInterpreter;

public class CSVBasedURLActionListBuilder extends URLActionListBuilder
{
    public CSVBasedURLActionListBuilder(final String filePath,
                                        final ParameterInterpreter interpreter,
                                        final URLActionBuilder builder)
    {
        super(filePath, interpreter,builder);
    }

    public List<URLAction> buildURLActions()
    {
        return actions;
    }

}
