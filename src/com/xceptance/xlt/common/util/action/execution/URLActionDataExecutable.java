package com.xceptance.xlt.common.util.action.execution;

import java.net.URL;

import com.xceptance.xlt.common.util.action.validation.URLActionDataExecutableResult;

public interface URLActionDataExecutable
{
    public URLActionDataExecutableResult getResult();
    public void executeAction();
    public void addStaticRequest(final URL url);
    public URL getUrl();
}
