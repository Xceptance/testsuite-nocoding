package com.xceptance.xlt.common.util;

import java.util.List;

import com.xceptance.xlt.common.util.bsh.ParameterInterpreter;

public class CSVBasedURLActionListBuilder extends URLActionListBuilder
{
    public CSVBasedURLActionListBuilder(final String filePath,
                                        final ParameterInterpreter interpreter,
                                        final URLActionBuilder actionBuilder)
   {
       super(filePath, interpreter, actionBuilder);
   }

    public List<URLAction> buildURLActions()
    {
        return actions;
    }

}
