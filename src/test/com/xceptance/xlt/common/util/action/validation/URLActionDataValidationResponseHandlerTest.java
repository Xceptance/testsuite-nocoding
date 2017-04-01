package test.com.xceptance.xlt.common.util.action.validation;

import org.junit.BeforeClass;
import org.junit.Test;

import test.com.xceptance.xlt.common.util.MockObjects;

import com.xceptance.xlt.api.data.GeneralDataProvider;
import com.xceptance.xlt.api.util.XltProperties;
import com.xceptance.xlt.common.util.action.data.URLActionData;
import com.xceptance.xlt.common.util.action.data.URLActionDataValidation;
import com.xceptance.xlt.common.util.action.validation.URLActionDataExecutableResult;
import com.xceptance.xlt.common.util.action.validation.URLActionDataValidationResponseHandler;
import com.xceptance.xlt.common.util.action.validation.XPathWithHtmlPage;
import com.xceptance.xlt.common.util.bsh.ParameterInterpreter;

public class URLActionDataValidationResponseHandlerTest
{
    private static URLActionDataValidationResponseHandler validationHandler;

    private static MockObjects mockObjects;

    private static ParameterInterpreter interpreter;

    private static XltProperties properties;

    private static GeneralDataProvider dataProvider;

    private static URLActionDataExecutableResult result;

    private static XPathWithHtmlPage xpwh;
    
    private static URLActionData action;

    private static URLActionDataValidation validationExists;

    private static URLActionDataValidation validationCount;

    private static URLActionDataValidation validationText;

    private static URLActionDataValidation validationMatches;

    private static URLActionDataValidation validationExistsMalicious;

    private static URLActionDataValidation validationCountMalicious;

    private static URLActionDataValidation validationTextMalicious;

    private static URLActionDataValidation validationMatchesMalicious;

    @BeforeClass
    public static void setup()
    {
        properties = XltProperties.getInstance();
        dataProvider = GeneralDataProvider.getInstance();
        interpreter = new ParameterInterpreter(properties, dataProvider);
        mockObjects = new MockObjects();
        mockObjects.load();
        xpwh = new XPathWithHtmlPage(mockObjects.getHtmlPage());
        result = new URLActionDataExecutableResult(mockObjects.getResponse(), xpwh);

        validationExists = new URLActionDataValidation("exists", URLActionDataValidation.REGEXP, mockObjects.regexString,
                                                       URLActionDataValidation.EXISTS, null, interpreter);
        validationCount = new URLActionDataValidation("count", URLActionDataValidation.REGEXP, mockObjects.regexString, URLActionDataValidation.COUNT,
                                                      "1", interpreter);
        validationText = new URLActionDataValidation("text", URLActionDataValidation.REGEXP, mockObjects.regexString, URLActionDataValidation.TEXT,
                                                     mockObjects.regexStringExpected, interpreter);

        validationMatches = new URLActionDataValidation("matches", URLActionDataValidation.REGEXP, mockObjects.regexString,
                                                        URLActionDataValidation.MATCHES, mockObjects.regexString, interpreter);

        validationExistsMalicious = new URLActionDataValidation("exists", URLActionDataValidation.REGEXP, "malicious",
                                                                URLActionDataValidation.EXISTS, null, interpreter);
        validationCountMalicious = new URLActionDataValidation("count", URLActionDataValidation.REGEXP, mockObjects.regexString,
                                                               URLActionDataValidation.COUNT, "5", interpreter);
        validationTextMalicious = new URLActionDataValidation("text", URLActionDataValidation.REGEXP, mockObjects.regexString,
                                                              URLActionDataValidation.TEXT, mockObjects.regexString, interpreter);

        validationMatchesMalicious = new URLActionDataValidation("matches", URLActionDataValidation.REGEXP, mockObjects.regexString,
                                                                 URLActionDataValidation.MATCHES, "not a title", interpreter);
    }

    @Test
    public void testExists()
    {
        validationHandler = new URLActionDataValidationResponseHandler();
        validationHandler.validate(validationExists, result, action);    
    }

    @Test
    public void testCount()
    {
        validationHandler = new URLActionDataValidationResponseHandler();
        validationHandler.validate(validationCount, result, action);
    }

    @Test
    public void testText()
    {
        validationHandler = new URLActionDataValidationResponseHandler();
        validationHandler.validate(validationText, result, action);
    }

    @Test
    public void testMatches()
    {
        validationHandler = new URLActionDataValidationResponseHandler();
        validationHandler.validate(validationMatches, result, action);
    }

    @Test(expected = AssertionError.class)
    public void testExistsMalicious()
    {
        validationHandler = new URLActionDataValidationResponseHandler();
        validationHandler.validate(validationExistsMalicious, result, action);
    }

    @Test(expected = AssertionError.class)
    public void testCountMalicious()
    {
        validationHandler = new URLActionDataValidationResponseHandler();
        validationHandler.validate(validationCountMalicious, result, action);
    }

    @Test(expected = AssertionError.class)
    public void testTextMalicious()
    {
        validationHandler = new URLActionDataValidationResponseHandler();
        validationHandler.validate(validationTextMalicious, result, action);
    }

    @Test(expected = AssertionError.class)
    public void testMatchesMalicious()
    {
        validationHandler = new URLActionDataValidationResponseHandler();
        validationHandler.validate(validationMatchesMalicious, result, action);
    }

}
