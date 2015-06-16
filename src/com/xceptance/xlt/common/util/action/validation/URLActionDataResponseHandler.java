package com.xceptance.xlt.common.util.action.validation;

import java.util.List;

import org.junit.Assert;

import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.xceptance.xlt.api.htmlunit.LightWeightPage;
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

        if (result.isHtmlPage())
        {
            handleHtmlPage(action, result.getHtmlPage());
        }
        else if (result.isLightWeightPage())
        {
            handleLightWeightPage(action, result.getLightHtmlPage());
        }
        else if (result.isWebResponse())
        {
            handleWebResponse(action, result.getResponse());
        }
        else
        {
            throw new IllegalArgumentException("No valid Respone Type available");
        }

    }

    private void handleHtmlPage(final URLActionData action, final HtmlPage htmlPage)
    {
        final WebResponse response = htmlPage.getWebResponse();
        validateResponseCode(response, action.getHttpResponseCode());
        final List<URLActionDataValidation> validations = action.getValidations();
        for (final URLActionDataValidation validation : validations)
        {
            final String selectionMode = validation.getSelectionMode();
            switch (selectionMode)
            {
                case URLActionDataValidation.XPATH:
                    break;
                case URLActionDataValidation.REGEXP:
                    break;
                case URLActionDataValidation.HEADER:
                    break;
                case URLActionDataValidation.COOKIE:
                    break;
                default:
                    throw new IllegalArgumentException("SelectionMode: " + selectionMode
                                                       + " is not supported");
            }

        }
        final List<URLActionDataStore> store = action.getStore();
        for (final URLActionDataStore storeItem : store)
        {

        }
    }

    private void validateHtmlPageByXPath(final HtmlPage page,
                                         final URLActionDataValidation validation)
    {
        
    }

    private void handleLightWeightPage(final URLActionData action,
                                       final LightWeightPage lightPage)
    {
        final WebResponse response = lightPage.getWebResponse();
        validateResponseCode(response, action.getHttpResponseCode());
    }

    private void handleWebResponse(final URLActionData action, final WebResponse response)
    {
        validateResponseCode(response, action.getHttpResponseCode());
    }

    private void validateResponseCode(final WebResponse response,
                                      final int expectedResponseCode)
    {
        final int responseCode = response.getStatusCode();
        Assert.assertEquals(expectedResponseCode, responseCode);
    }
}
