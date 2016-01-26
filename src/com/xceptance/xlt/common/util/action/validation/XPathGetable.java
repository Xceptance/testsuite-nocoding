package com.xceptance.xlt.common.util.action.validation;

import java.util.List;

/**
 * Well, true friends stab you in the front. <br>
 * Offers a way to get elements by passing a XPath. <br>
 * A single element is represented as String.
 * 
 * @author matthias mitterreiter
 */

public interface XPathGetable
{
    /**
     * @param xPath
     * @return list of all the selected elements in String representation.
     */
    public List<String> getByXPath(String xPath);
}
