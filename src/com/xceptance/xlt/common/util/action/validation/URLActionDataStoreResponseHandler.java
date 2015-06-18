package com.xceptance.xlt.common.util.action.validation;

import java.util.List;

import bsh.EvalError;

import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.common.util.action.data.URLActionDataStore;
import com.xceptance.xlt.common.util.bsh.ParameterInterpreter;

public class URLActionDataStoreResponseHandler
{
    public URLActionDataStoreResponseHandler()
    {
        XltLogger.runTimeLogger.debug("Creating new Instance");
    }

    public void handleStore(final URLActionDataStore storeItem,
                            final URLActionDataExecutableResult result)
    {
        XltLogger.runTimeLogger.debug("Handling StoreItem");
        try
        {
            handleStoreItem(storeItem, result);
        }
        catch (final Exception e)
        {
            throw new IllegalArgumentException("Failed to handle URLActionDataStore Item Response : "
                                                   + e.getMessage(),
                                               e);
        }

    }

    private void handleStoreItem(final URLActionDataStore storeItem,
                                 final URLActionDataExecutableResult result)
        throws EvalError
    {
        final String selectionMode = storeItem.getSelectionMode();
        switch (selectionMode)
        {
            case URLActionDataStore.XPATH:
                handleXPathStoreItem(storeItem, result);
                break;
            case URLActionDataStore.REGEXP:
                handleRegExStoreItem(storeItem, result);
                break;
            case URLActionDataStore.HEADER:
                handleHeaderStoreItem(storeItem, result);
                break;
            default:
                break;
        }
    }

    private void handleXPathStoreItem(final URLActionDataStore storeItem,
                                      final URLActionDataExecutableResult result)
        throws EvalError
    {
        final List<String> xpathList = result.getByXPath(storeItem.getSelectionContent());
        if (!(xpathList.isEmpty()))
        {
            storeContentInterpreter(storeItem, xpathList.get(0));
        }

    }

    private void handleRegExStoreItem(final URLActionDataStore storeItem,
                                      final URLActionDataExecutableResult result)
        throws EvalError
    {
        final List<String> regexList = result.getByRegEx(storeItem.getSelectionContent());
        if (!(regexList.isEmpty()))
        {
            storeContentInterpreter(storeItem, regexList.get(0));
        }
    }

    private void handleHeaderStoreItem(final URLActionDataStore storeItem,
                                       final URLActionDataExecutableResult result)
        throws EvalError
    {
        final List<String> headers = result.getHeaderByName(storeItem.getSelectionContent());
        if (!(headers.isEmpty()))
        {
            storeContentInterpreter(storeItem, headers.get(0));
        }
    }

    private void storeContentInterpreter(final URLActionDataStore storeItem,
                                                final String variableValue)
        throws EvalError
    {
        final ParameterInterpreter interpreter = storeItem.getInterpreter();
        final String variableName = storeItem.getName();
        final NameValuePair nvp = new NameValuePair(variableName, variableValue);
        interpreter.set(nvp);
    }
}
