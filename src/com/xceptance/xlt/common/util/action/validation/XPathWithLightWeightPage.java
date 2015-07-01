package com.xceptance.xlt.common.util.action.validation;

import java.util.List;

import com.xceptance.xlt.api.htmlunit.LightWeightPage;
import com.xceptance.xlt.api.util.XltLogger;

/**
 * Implementation of {@link XPathGetable}. <br>
 * Offers a simple way to throw a IllegalArgumentException.<br>
 * Objects of the class {@link LightWeightPage} are not parsed into the DOM. <br>
 * Therefore its is impossible to select anything by XPath.
 * {@link #getByXPath(String)}.
 * 
 * @author matthias mitterreiter
 */
public class XPathWithLightWeightPage implements XPathGetable
{
    public XPathWithLightWeightPage()
    {
        XltLogger.runTimeLogger.debug("Creating new Instance");
    }

    /**
     * Alway throws.
     * 
     * @throws IllegalArgumentException
     */
    @Override
    public List<String> getByXPath(final String xPath)
    {
        XltLogger.runTimeLogger.error("It is not possible to get elements by xpath from a LightWeightPage!");
        throw new IllegalArgumentException("It is not possible to get elements by xpath from a LightWeightPage!");
    }

}
