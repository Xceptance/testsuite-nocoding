package com.xceptance.xlt.common.util;

import com.gargoylesoftware.htmlunit.WebRequest;
import com.xceptance.xlt.api.util.XltLogger;


public class LightWeightPageActionFactory extends URLActionDataExecutableFactory
{
    public LightWeightPageActionFactory()
    {
        super();
        XltLogger.runTimeLogger.debug("Creating new Instance");
    }

    @Override
    public URLActionDataExecutable createPageAction(final String name, final WebRequest request)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public URLActionDataExecutable createXhrPageAction(final String name, final WebRequest request)
    {
        // TODO Auto-generated method stub
        return null;
    }
}
