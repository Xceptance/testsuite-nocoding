package com.xceptance.xlt.common.util.action.validation;

import java.util.List;

import org.junit.Assert;

import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.common.util.ParameterUtils;
import com.xceptance.xlt.common.util.action.data.URLActionData;
import com.xceptance.xlt.common.util.action.data.URLActionDataStore;
import com.xceptance.xlt.common.util.action.data.URLActionDataValidation;

/**
 * Handling the Response in form of a {@link URLActionDataResult} 
 * for a request described by a {@link URLActionData}. <br>
 * Automatically handles the response when 
 * {@link #handleURLActionResponse(URLActionData, URLActionDataExecutableResult) handleUrlActionResponse()} 
 * is called.
 * 
 * @author matthias mitterreiter
 *
 */
public class URLActionDataResponseHandler
{
    private URLActionDataStoreResponseHandler storeHandler;

    private URLActionDataValidationResponseHandler validationHandler;

    /**
     * 
     * @param storeHandler : for selecting elements of the response for dynamic 
     * parameter interpretation.
     * @param validationHandler : for validating the response content.
     */
    public URLActionDataResponseHandler(final URLActionDataStoreResponseHandler storeHandler,
                                        final URLActionDataValidationResponseHandler validationHandler)
    {
        XltLogger.runTimeLogger.debug("Creating new Instance");
        setStoreHandler(storeHandler);
        setValidationHandler(validationHandler);
    }

    private void setStoreHandler(final URLActionDataStoreResponseHandler storeHandler)
    {
        ParameterUtils.isNotNull(storeHandler, "URLActionDataStoreHandler");
        this.storeHandler = storeHandler;
    }

    private void setValidationHandler(final URLActionDataValidationResponseHandler validationHandler)
    {
        ParameterUtils.isNotNull(validationHandler,
                                 "URLActionDataValidationResponseHandler");
        this.validationHandler = validationHandler;
    }

    /**
     * Does the following:
     * <ul>
     * <li> Validates the http response code.
     * <li> validates the response content via the {@link URLActionDataStoreResponseHandler store handler}.
     * <li> Validates the response content via the {@link URLActionDataValidationResponseHandler validation handle}.
     * </ul>
     * @param action : the {@link URLActionData}, that describes the request.
     * @param result : the {@link URLActionDataExecutableResult}, that holds the response data.
     */
    public void handleURLActionResponse(final URLActionData action,
                                        final URLActionDataExecutableResult result)
    {
        ParameterUtils.isNotNull(action, "URLActionData");
        ParameterUtils.isNotNull(result, "URLActionDataResult");
        XltLogger.runTimeLogger.debug("Handling Response for URLActionData: "
                                      + action.getName());
        handleResponse(action, result);

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
            storeHandler.handleStore(storeItem, result);
        }
    }

    private void handleValidations(final URLActionData action,
                                   final URLActionDataExecutableResult result)
    {
        final List<URLActionDataValidation> validations = action.getValidations();
        for (final URLActionDataValidation validation : validations)
        {
            validationHandler.validate(validation, result);
        }
    }

    private void validateResponseCode(final URLActionData action,
                                      final URLActionDataExecutableResult result)
    {
        XltLogger.runTimeLogger.debug("Validating HttpResponseCode for: " + action.getName() );
        final int expextedResponseCode = action.getHttpResponseCode();
        final int actualResponseCode = result.getHttpResponseCode();
        Assert.assertEquals("Action: \""+ action.getName() + "\" HttpResponseCode", expextedResponseCode, actualResponseCode);
    }
}
