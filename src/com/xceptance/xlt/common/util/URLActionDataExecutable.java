package com.xceptance.xlt.common.util;

import java.net.URL;

public interface URLActionDataExecutable
{
    public URLActionDataResult getResult();
    public void executeURLAction();
    public void addStaticRequest(final URL url);
    public URL getUrl();
}
