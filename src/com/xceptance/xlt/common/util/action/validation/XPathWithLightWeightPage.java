package com.xceptance.xlt.common.util.action.validation;

import java.util.List;

public class XPathWithLightWeightPage implements XPathGetable
{
    public XPathWithLightWeightPage(){
        
    }
    @Override
    public List<String> getByXPath(final String xPath)
    {
        throw new IllegalArgumentException("This Mode is not supported!");
    }

}
