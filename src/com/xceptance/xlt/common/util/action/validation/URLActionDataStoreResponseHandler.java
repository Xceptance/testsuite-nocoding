package com.xceptance.xlt.common.util.action.validation;

import java.util.List;

import bsh.EvalError;

import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.common.util.action.data.URLActionDataStore;
import com.xceptance.xlt.common.util.bsh.ParameterInterpreter;

/**
 * Handles a {@link URLActionDataStore} item. <br>
 * <ul>
 * <li>Reads the selectionMode & selectionContent of {@link URLActionDataStore}.
 * <li>Selects the described elements in {@link URLActionDataExecutableResult}.
 * <li>Feeds the {@link ParameterInterpreter}.
 * <li>For this use {@link #handleStore(URLActionDataStore, URLActionDataExecutableResult) handleStore()}.
 * </ul>
 * 
 * @author matthias mitterreiter
 */
public class URLActionDataStoreResponseHandler
{
    public URLActionDataStoreResponseHandler()
    {
        XltLogger.runTimeLogger.debug("Creating new Instance");
    }

    /**
     * @param storeItem
     *            : the description of the elements that should be taken out of the response.
     * @param result
     *            : the response in form of a {@link URLActionDataExecutableResult}.
     * @throws IllegalArgumentException
     *             if it is not possible to select the described elements.
     */
    public void handleStore(final URLActionDataStore storeItem, final URLActionDataExecutableResult result)
    {
        XltLogger.runTimeLogger.debug("Handling StoreItem: \"" + storeItem.getName() + "\"");
        try
        {
            handleStoreItem(storeItem, result);
        }
        catch (final Exception e)
        {
            throw new IllegalArgumentException("Failed to handle URLActionDataStore Item: \"" + storeItem.getName() + "\", Because : " +
                                               e.getMessage(), e);
        }

    }

    private void handleStoreItem(final URLActionDataStore storeItem, final URLActionDataExecutableResult result) throws EvalError
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
            case URLActionDataStore.COOKIE:
                handleCookieStoreItem(storeItem, result);
                break;
            default:
                throw new IllegalArgumentException("SelectionMode: \"" + storeItem.getSelectionMode() + "\" is not supported!");
        }
    }

    private void handleCookieStoreItem(final URLActionDataStore storeItem, final URLActionDataExecutableResult result) throws EvalError
    {
        final List<String> cookies = result.getCookieAsStringByName(storeItem.getSelectionContent());
        if (!(cookies.isEmpty()))
        {
            storeContentInterpreter(storeItem, cookies.get(0));
        }
        else
        {
            throwExceptionBecauseNothingWasFound(storeItem);
        }
    }

    private void throwExceptionBecauseNothingWasFound(final URLActionDataStore storeItem)
    {
        throw new IllegalArgumentException(storeItem.getSelectionMode() + " = " + "\"" + storeItem.getSelectionContent() + "\"" +
                                           "was not found!");
    }

    private void handleXPathStoreItem(final URLActionDataStore storeItem, final URLActionDataExecutableResult result) throws EvalError
    {
        final List<String> xpathList = result.getByXPath(storeItem.getSelectionContent());
        if (!(xpathList.isEmpty()))
        {
            storeContentInterpreter(storeItem, xpathList.get(0));
        }
        else
        {
            throwExceptionBecauseNothingWasFound(storeItem);
        }

    }

    private void handleRegExStoreItem(final URLActionDataStore storeItem, final URLActionDataExecutableResult result) throws EvalError
    {
        if (storeItem.hasSubSelection())
        {
            handleRegExStoreItemWithSubSelection(storeItem, result);
        }
        else
        {
            handleRegExStoreItemWithoutSubSelection(storeItem, result);
        }
    }

    private void handleRegExStoreItemWithoutSubSelection(final URLActionDataStore storeItem, final URLActionDataExecutableResult result)
        throws EvalError
    {
        final List<String> regexList = result.getByRegEx(storeItem.getSelectionContent());
        if (!(regexList.isEmpty()))
        {
            storeContentInterpreter(storeItem, regexList.get(0));
        }
        else
        {
            throwExceptionBecauseNothingWasFound(storeItem);
        }
    }

    private void handleRegExStoreItemWithSubSelection(final URLActionDataStore storeItem, final URLActionDataExecutableResult result)
        throws EvalError
    {
        if (URLActionDataStore.REGEXGROUP.equals(storeItem.getSubSelectionMode()))
        {
            final List<String> regexList = result.getByRegExGroup(storeItem.getSelectionContent(),
                                                                  Integer.valueOf(storeItem.getSubSelectionContent()));
            if (!(regexList.isEmpty()))
            {
                storeContentInterpreter(storeItem, regexList.get(0));
            }
            else
            {
                throwExceptionBecauseNothingWasFound(storeItem);
            }
        }
        else
        {
            XltLogger.runTimeLogger.warn("SUB-SELECTIONMODE: " + storeItem.getSubSelectionMode() + " is not implemented!");
        }
    }

    private void handleHeaderStoreItem(final URLActionDataStore storeItem, final URLActionDataExecutableResult result) throws EvalError
    {
        final List<String> headers = result.getHeaderByName(storeItem.getSelectionContent());
        if (!(headers.isEmpty()))
        {
            storeContentInterpreter(storeItem, headers.get(0));
        }
        else
        {
            throwExceptionBecauseNothingWasFound(storeItem);
        }
    }

    private void storeContentInterpreter(final URLActionDataStore storeItem, final String variableValue) throws EvalError
    {
        final ParameterInterpreter interpreter = storeItem.getInterpreter();
        final String variableName = storeItem.getName();
        final NameValuePair nvp = new NameValuePair(variableName, variableValue);
        interpreter.set(nvp);
    }
}
