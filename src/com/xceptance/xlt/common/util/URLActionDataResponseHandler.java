package com.xceptance.xlt.common.util;

import com.xceptance.xlt.api.util.XltLogger;

public class URLActionDataResponseHandler
{
    public URLActionDataResponseHandler(){
        XltLogger.runTimeLogger.debug("Creating new Instance");
    }
    public void handleURLActionResponse(final URLActionData action,
                                        final URLActionExecutableResult result)
    {
        ParameterUtils.isNotNull(action, "URLActionData");
        ParameterUtils.isNotNull(result, "URLActionDataResult");
        XltLogger.runTimeLogger.debug("Handling URLActionData: " + action.getName());
    }
}
