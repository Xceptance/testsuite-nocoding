package com.xceptance.xlt.common.util.action.validation;

import java.util.List;

import com.xceptance.xlt.api.util.XltLogger;

public class XPathWithLightWeightPage implements XPathGetable
{
    public XPathWithLightWeightPage()
    {
        XltLogger.runTimeLogger.debug("Creating new Instance");
    }

    @Override
    public List<String> getByXPath(final String xPath)
    {
        XltLogger.runTimeLogger.error("This Mode is not supported!");
        throw new IllegalArgumentException("This Mode is not supported!");
    }

}
