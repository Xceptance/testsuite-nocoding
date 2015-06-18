package com.xceptance.xlt.common.util.action.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Assert;

import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.common.util.action.data.URLActionDataValidation;

public class URLActionDataValidationResponseHandler
{
    public URLActionDataValidationResponseHandler()
    {
        XltLogger.runTimeLogger.debug("Creating new Instance");
    }

    public void validate(final URLActionDataValidation validation,
                         final URLActionDataExecutableResult result)
    {
        XltLogger.runTimeLogger.debug("Validating: " + validation.getName());
        try
        {
            handleValidation(validation, result);
        }
        catch (final Exception e)
        {
            throw new IllegalArgumentException("Failed to validate Response : "
                                               + validation.getName() + ": "
                                               + e.getMessage(), e);
        }
    }

    private void handleValidation(final URLActionDataValidation validation,
                                  final URLActionDataExecutableResult result)
    {
        final List<String> resultSelection = selectFromResult(validation,
                                                              result);
        validateContent(resultSelection, validation);

    }

    private List<String> selectFromResult(final URLActionDataValidation validation,
                                          final URLActionDataExecutableResult result)
    {
        final String selectionMode = validation.getSelectionMode();

        List<String> resultSelection = new ArrayList<String>();

        switch (selectionMode)
        {
            case URLActionDataValidation.XPATH:
                resultSelection = handleXPathValidationItem(validation, result);
                break;
            case URLActionDataValidation.REGEXP:
                resultSelection = handleRegExValidationItem(validation, result);
                break;
            case URLActionDataValidation.HEADER:
                resultSelection = handleHeaderValidationItem(validation, result);
                break;
            default:
                break;
        }
        return resultSelection;
    }

    private void validateContent(final List<String> resultSelection,
                                 final URLActionDataValidation validation)
    {
        final String validationMode = validation.getValidationMode();
        switch (validationMode)
        {
            case URLActionDataValidation.EXISTS:
                validateExists(resultSelection, validation);
                break;
            case URLActionDataValidation.COUNT:
                validateCount(resultSelection, validation);
                break;
            case URLActionDataValidation.MATCHES:
                validateMatches(resultSelection, validation);
                break;
            case URLActionDataValidation.TEXT:
                validateText(resultSelection, validation);
                break;
            default:
                break;
        }
    }

    private void validateExists(final List<String> resultSelection,
                                final URLActionDataValidation validation)
    {
        XltLogger.runTimeLogger.debug("Validating EXISTANCE");
        Assert.assertFalse(resultSelection.isEmpty());
    }

    private void validateCount(final List<String> resultSelection,
                               final URLActionDataValidation validation)
    {
        final int expectedLength = Integer.valueOf(validation.getValidationContent());
        final int actualLength = resultSelection.size();
        XltLogger.runTimeLogger.debug("Validating COUNT: " + expectedLength
                                      + " = " + actualLength);
        Assert.assertEquals(expectedLength, actualLength);

    }

    private void validateMatches(final List<String> resultSelection,
                                 final URLActionDataValidation validation)
    {
        validateExists(resultSelection, validation);
        final String matcherString = resultSelection.get(0);
        final String patternString = validation.getValidationContent();
        XltLogger.runTimeLogger.debug("Validating MATECHES: " + matcherString
                                      + " = " + patternString);
        final Pattern pattern = Pattern.compile(patternString);
        final Matcher matcher = pattern.matcher(matcherString);
        Assert.assertTrue(matcher.find());

    }

    private void validateText(final List<String> resultSelection,
                              final URLActionDataValidation validation)
    {
        validateExists(resultSelection, validation);
        final String expectedText = validation.getValidationContent();
        final String actualText = resultSelection.get(0);
        XltLogger.runTimeLogger.debug("Validating TEXT: " + "'" + expectedText
                                      + "'" + " = " + "'" + actualText + "'");
        Assert.assertEquals(expectedText, actualText);
    }

    private List<String> handleXPathValidationItem(final URLActionDataValidation validation,
                                                   final URLActionDataExecutableResult result)
    {
        final List<String> someThing = result.getByXPath(validation.getSelectionContent());

        return result.getByXPath(validation.getSelectionContent());

    }

    private List<String> handleRegExValidationItem(final URLActionDataValidation validation,
                                                   final URLActionDataExecutableResult result)
    {
        return result.getByRegEx(validation.getSelectionContent());

    }

    private List<String> handleHeaderValidationItem(final URLActionDataValidation validation,
                                                    final URLActionDataExecutableResult result)
    {
        return result.getHeaderByName(validation.getSelectionContent());

    }
}
