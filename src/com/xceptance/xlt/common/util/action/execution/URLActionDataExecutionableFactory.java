package com.xceptance.xlt.common.util.action.execution;

import com.gargoylesoftware.htmlunit.WebRequest;

public abstract class URLActionDataExecutionableFactory
{

    abstract public URLActionDataExecutionable createPageAction(final String name, WebRequest request);

    abstract public URLActionDataExecutionable createXhrPageAction(final String name, WebRequest request);

}
