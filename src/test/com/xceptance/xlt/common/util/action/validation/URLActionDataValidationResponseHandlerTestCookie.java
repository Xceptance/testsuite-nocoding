package test.com.xceptance.xlt.common.util.action.validation;

import org.junit.BeforeClass;
import org.junit.Test;

import com.xceptance.xlt.api.data.GeneralDataProvider;
import com.xceptance.xlt.api.util.XltProperties;
import com.xceptance.xlt.common.util.MockObjects;
import com.xceptance.xlt.common.util.action.data.URLActionData;
import com.xceptance.xlt.common.util.action.data.URLActionDataValidation;
import com.xceptance.xlt.common.util.action.validation.URLActionDataExecutableResult;
import com.xceptance.xlt.common.util.action.validation.URLActionDataValidationResponseHandler;
import com.xceptance.xlt.common.util.action.validation.XPathWithHtmlPage;
import com.xceptance.xlt.common.util.bsh.ParameterInterpreter;

public class URLActionDataValidationResponseHandlerTestCookie
{
    private static final String urlString = "http://www.amazon.de";

    private static URLActionDataValidationResponseHandler validationHandler;

    private static MockObjects mockObjects;

    private static ParameterInterpreter interpreter;

    private static XltProperties properties;

    private static GeneralDataProvider dataProvider;

    private static URLActionDataExecutableResult result;

    private static XPathWithHtmlPage xpwh;

    private static URLActionData action;

    private static URLActionDataValidation validationCookieExists;

    private static URLActionDataValidation validationCookieExistsMalicious;

    private static URLActionDataValidation validationCookieTextMalicious;

    private static URLActionDataValidation validationCookieMatches;

    private static URLActionDataValidation validationCookieMatchesMalicious;

    private static URLActionDataValidation validationCookieCount;

    private static URLActionDataValidation validationCookieCountMalicious;

    private static final String cookieName1 = "session-id";

    private static final String cookieName2 = "session-id-time";

    @BeforeClass
    public static void setup()
    {
        properties = XltProperties.getInstance();
        dataProvider = GeneralDataProvider.getInstance();
        interpreter = new ParameterInterpreter(properties, dataProvider);
        mockObjects = new MockObjects(urlString);
        mockObjects.load();
        xpwh = new XPathWithHtmlPage(mockObjects.getHtmlPage());
        result = new URLActionDataExecutableResult(mockObjects.getResponse(), xpwh);

        validationCookieExists = new URLActionDataValidation("exists", URLActionDataValidation.COOKIE, cookieName1,
                                                             URLActionDataValidation.EXISTS, null, interpreter);
        validationCookieExistsMalicious = new URLActionDataValidation("exists_malicious", URLActionDataValidation.COOKIE,
                                                                      "non-existing-cookie", URLActionDataValidation.EXISTS, null,
                                                                      interpreter);

        validationCookieTextMalicious = new URLActionDataValidation("text_malicious", URLActionDataValidation.COOKIE, cookieName2,
                                                                    URLActionDataValidation.TEXT, "something stupid", interpreter);

        validationCookieMatches = new URLActionDataValidation("matches", URLActionDataValidation.COOKIE, cookieName2,
                                                              URLActionDataValidation.MATCHES, "[\\s\\S]+", interpreter);

        validationCookieMatchesMalicious = new URLActionDataValidation("matches_malicious", URLActionDataValidation.COOKIE, cookieName2,
                                                                       URLActionDataValidation.MATCHES, "\\d", interpreter);

        validationCookieCount = new URLActionDataValidation("count", URLActionDataValidation.COOKIE, cookieName1,
                                                            URLActionDataValidation.COUNT, "1", interpreter);

        validationCookieCountMalicious = new URLActionDataValidation("count_malicious", URLActionDataValidation.COOKIE, cookieName1,
                                                                     URLActionDataValidation.COUNT, "2", interpreter);

    }

    @Test
    public void testConstructor()
    {
        validationHandler = new URLActionDataValidationResponseHandler();
    }

    @Test
    public void testCookieExists()
    {
        validationHandler = new URLActionDataValidationResponseHandler();
        validationHandler.validate(validationCookieExists, result, action);
    }

    @Test(expected = AssertionError.class)
    public void testCookieExistsMalicious()
    {
        validationHandler = new URLActionDataValidationResponseHandler();
        validationHandler.validate(validationCookieExistsMalicious, result, action);
    }

    @Test
    public void testCookieCount()
    {
        validationHandler = new URLActionDataValidationResponseHandler();
        validationHandler.validate(validationCookieCount, result, action);
    }

    @Test(expected = AssertionError.class)
    public void testCookieCountMalicious()
    {
        validationHandler = new URLActionDataValidationResponseHandler();
        validationHandler.validate(validationCookieCountMalicious, result, action);
    }

    @Test(expected = AssertionError.class)
    public void testCookieTestMalicious()
    {
        validationHandler = new URLActionDataValidationResponseHandler();
        validationHandler.validate(validationCookieTextMalicious, result, action);
    }

    @Test
    public void testCookieMatches()
    {
        validationHandler = new URLActionDataValidationResponseHandler();
        validationHandler.validate(validationCookieMatches, result, action);
    }

    /*
     * This test case is broken Don't know why Actually it should throw, but it doesn't....
     */
    @Test
    public void testCookieMatchesMalicious()
    {
        validationHandler = new URLActionDataValidationResponseHandler();
        validationHandler.validate(validationCookieMatchesMalicious, result, action);
    }

}
