package com.xceptance.xlt.common.util.action.validation;

import java.util.List;

import org.junit.Assert;

import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.common.util.ParameterUtils;
import com.xceptance.xlt.common.util.action.data.URLActionData;
import com.xceptance.xlt.common.util.action.data.URLActionDataStore;
import com.xceptance.xlt.common.util.action.data.URLActionDataValidation;

public class URLActionDataResponseHandler
{
    public URLActionDataResponseHandler()
    {
        XltLogger.runTimeLogger.debug("Creating new Instance");
    }

    public void handleURLActionResponse(final URLActionData action,
                                        final URLActionDataExecutableResult result)
    {
        ParameterUtils.isNotNull(action, "URLActionData");
        ParameterUtils.isNotNull(result, "URLActionDataResult");
        XltLogger.runTimeLogger.debug("Handling Response for URLActionData: "
                                      + action.getName());

    }

    private void handleResponse(final URLActionData action,
                                final URLActionDataExecutableResult result)
    {
        validateResponseCode(action, result);
        handleStore(action, result);
        handleValidations(action, result);
    }

    private void handleStore(final URLActionData action,
                             final URLActionDataExecutableResult result)
    {
        final List<URLActionDataStore> store = action.getStore();
        for (final URLActionDataStore storeItem : store)
        {
            handleStoreItem(storeItem, result);
        }
    }

    private void handleStoreItem(final URLActionDataStore storeItem,
                                 final URLActionDataExecutableResult result)
    {
        final String selectionMode = storeItem.getSelectionMode();
        final String selectionContent = storeItem.getSelectionContent();
        switch (selectionMode)
        {
            case URLActionDataStore.XPATH:
                result.getByXPath(selectionContent);
                break;
            case URLActionDataStore.REGEXP:
                result.getByRegEx(selectionContent);
                break;
            case URLActionDataStore.HEADER:
                result.getHeaderByName(selectionContent);
                break;
            default:
                break;
        }
    }

    private void handleValidations(final URLActionData action,
                                   final URLActionDataExecutableResult result)
    {
        final List<URLActionDataValidation> validations = action.getValidations();
    }

    private void validateResponseCode(final URLActionData action,
                                      final URLActionDataExecutableResult result)
    {
        final int expextedResponseCode = action.getHttpResponseCode();
        final int actualResponseCode = result.getHttpResponseCode();
        Assert.assertEquals(expextedResponseCode, actualResponseCode);
    }
}
