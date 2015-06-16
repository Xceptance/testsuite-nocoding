package test.com.xceptance.xlt.common.util.action.validation;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.xceptance.xlt.api.data.GeneralDataProvider;
import com.xceptance.xlt.api.util.XltProperties;
import com.xceptance.xlt.common.util.MockObjects;
import com.xceptance.xlt.common.util.action.data.URLActionDataStore;
import com.xceptance.xlt.common.util.action.validation.URLActionDataExecutableResult;
import com.xceptance.xlt.common.util.action.validation.URLActionDataStoreResponseHandler;
import com.xceptance.xlt.common.util.action.validation.XPathWithHtmlPage;
import com.xceptance.xlt.common.util.bsh.ParameterInterpreter;

public class URLActionDataStoreResponseHandlerTest
{

    MockObjects mockObjects;

    private ParameterInterpreter interpreter;

    private XltProperties properties;

    private GeneralDataProvider dataProvider;

    private URLActionDataExecutableResult result;

    private URLActionDataStoreResponseHandler storeHandler;

    private URLActionDataStore storeItemRegex;

    private final String regexString = "href=\"[\\s\\S]*?\"";
    private final String regexStringExpected = "href=\"/en/\"";

    private URLActionDataStore storeItemXPath;

    private final String xpathString = "//*[@id='service-areas']/div[1]/div/div/h1";
    private final String xpathStringExpected = "Committed to Software Quality";

    private URLActionDataStore storeItemHeader;

    private final String headerString = "Server";
    private final String headerStringExpected = "Apache";

    @Before
    public void setup()
    {
        properties = XltProperties.getInstance();
        dataProvider = GeneralDataProvider.getInstance();
        interpreter = new ParameterInterpreter(properties, dataProvider);
        storeHandler = new URLActionDataStoreResponseHandler();
        mockObjects = new MockObjects();
        mockObjects.load();
        final XPathWithHtmlPage xpwh = new XPathWithHtmlPage(mockObjects.getHtmlPage());
        result = new URLActionDataExecutableResult(mockObjects.getResponse(),
                                                   xpwh);
        storeItemRegex = new URLActionDataStore("regex",
                                                URLActionDataStore.REGEXP,
                                                regexString,
                                                interpreter);
        storeItemXPath = new URLActionDataStore("xpath",
                                                URLActionDataStore.XPATH,
                                                xpathString,
                                                interpreter);
        storeItemHeader = new URLActionDataStore("header",
                                                 URLActionDataStore.HEADER,
                                                 headerString,
                                                 interpreter);
    }

    @Test
    public void testRegex()
    {
        storeHandler.handleStore(storeItemRegex, result);
        Assert.assertEquals(regexStringExpected, interpreter.processDynamicData("${regex}"));
    }

    @Test
    public void testXPath()
    {
        storeHandler.handleStore(storeItemXPath, result);
        Assert.assertEquals(xpathStringExpected, interpreter.processDynamicData("${xpath}"));
    }

    @Test
    public void testHeader()
    {
        storeHandler.handleStore(storeItemHeader, result);
        Assert.assertEquals(headerStringExpected, interpreter.processDynamicData("${header}"));
    }
}
