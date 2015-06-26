package com.xceptance.xlt.common.util.action.validation;

import java.util.List;

/**
 * Well, true friends stab you in the front. <br>
 * 
 * @author matthias mitterreiter
 *
 */

public interface XPathGetable
{
    public List<String> getByXPath(String xPath);
}
