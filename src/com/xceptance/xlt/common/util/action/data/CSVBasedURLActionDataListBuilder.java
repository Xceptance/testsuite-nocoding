package com.xceptance.xlt.common.util.action.data;

import java.util.List;

import com.xceptance.xlt.common.util.bsh.ParameterInterpreter;

/**
 * 
 * Implement Me O.O
 *
 */
public class CSVBasedURLActionDataListBuilder extends URLActionDataListBuilder
{
    public CSVBasedURLActionDataListBuilder(final String filePath,
                                        final ParameterInterpreter interpreter,
                                        final URLActionDataBuilder actionBuilder)
   {
       super(filePath, interpreter, actionBuilder);
   }

    public List<URLActionData> buildURLActionDataList()
    {
        return actions;
    }

}
