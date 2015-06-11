package com.xceptance.xlt.common.util;

import java.net.URL;

public interface URLActionDataExecutable
{
    public URLActionExecutableResult getResult();
    public void executeAction();
    public void addStaticRequest(final URL url);
    public URL getUrl();
}
