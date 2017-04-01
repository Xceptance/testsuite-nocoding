package test.com.xceptance.xlt.common.util.action.validation;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import test.com.xceptance.xlt.common.util.MockObjects;

import com.xceptance.xlt.api.data.GeneralDataProvider;
import com.xceptance.xlt.api.util.XltProperties;
import com.xceptance.xlt.common.util.action.data.URLActionDataStore;
import com.xceptance.xlt.common.util.action.validation.URLActionDataExecutableResult;
import com.xceptance.xlt.common.util.action.validation.URLActionDataStoreResponseHandler;
import com.xceptance.xlt.common.util.action.validation.XPathWithHtmlPage;
import com.xceptance.xlt.common.util.bsh.ParameterInterpreter;

public class URLActionDataStoreResponseHandlerTest
{

    private static MockObjects mockObjects;

    private static ParameterInterpreter interpreter;

    private static XltProperties properties;

    private static GeneralDataProvider dataProvider;

    private static URLActionDataExecutableResult result;

    private static URLActionDataStoreResponseHandler storeHandler;

    private static URLActionDataStore storeItemRegex;

    private static URLActionDataStore storeItemRegexCaptureGroup;

    private static URLActionDataStore storeItemXPath;

    private static URLActionDataStore storeItemHeader;

    private static final String headerString = "Content-Type";

    private static final String headerStringExpected = "text/html";

    private static final String regexCaptureGroupStringTitle = "<title>([\\s\\S]*)</title>";

    private static final String regexCaptureGroupStringTitleExpected = "A demo html site for this test suite";

    @BeforeClass
    public static void setup()
    {
        properties = XltProperties.getInstance();
        dataProvider = GeneralDataProvider.getInstance();
        interpreter = new ParameterInterpreter(properties, dataProvider);
        storeHandler = new URLActionDataStoreResponseHandler();
        mockObjects = new MockObjects();
        mockObjects.load();
        final XPathWithHtmlPage xpwh = new XPathWithHtmlPage(mockObjects.getHtmlPage());
        result = new URLActionDataExecutableResult(mockObjects.getResponse(), xpwh);
        storeItemRegex = new URLActionDataStore("regex", URLActionDataStore.REGEXP, mockObjects.regexString, interpreter);
        storeItemXPath = new URLActionDataStore("xpath", URLActionDataStore.XPATH, mockObjects.xPathString, interpreter);
        storeItemHeader = new URLActionDataStore("header", URLActionDataStore.HEADER, headerString, interpreter);

        storeItemRegexCaptureGroup = new URLActionDataStore("regexcapture", URLActionDataStore.REGEXP, regexCaptureGroupStringTitle,
                                                            URLActionDataStore.REGEXGROUP, "1", interpreter);
    }

    @Test
    public void testRegex()
    {
        storeHandler.handleStore(storeItemRegex, result);
        Assert.assertEquals(mockObjects.regexStringExpected, interpreter.processDynamicData("${regex}"));
    }

    @Test
    public void testRegexCaptureGroup()
    {
        storeHandler.handleStore(storeItemRegexCaptureGroup, result);
        Assert.assertEquals(regexCaptureGroupStringTitleExpected, interpreter.processDynamicData("${regexcapture}"));
    }

    @Test
    public void testXPath()
    {
        storeHandler.handleStore(storeItemXPath, result);
        Assert.assertEquals(mockObjects.xpathStringExpected, interpreter.processDynamicData("${xpath}"));
    }

    @Test
    public void testHeader()
    {
        storeHandler.handleStore(storeItemHeader, result);
        Assert.assertEquals(headerStringExpected, interpreter.processDynamicData("${header}"));
    }

}
