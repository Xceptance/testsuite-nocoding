package com.xceptance.xlt.common.util.action.execution;

import com.gargoylesoftware.htmlunit.WebRequest;

public abstract class URLActionDataExecutableFactory
{

    abstract public URLActionDataExecutable createPageAction(final String name, WebRequest request);

    abstract public URLActionDataExecutable createXhrPageAction(final String name, WebRequest request);

}
