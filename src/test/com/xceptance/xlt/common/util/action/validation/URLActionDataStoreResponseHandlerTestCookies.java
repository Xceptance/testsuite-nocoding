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

public class URLActionDataStoreResponseHandlerTestCookies
{
    private static MockObjects mockObjects;

    private static ParameterInterpreter interpreter;

    private static XltProperties properties;

    private static GeneralDataProvider dataProvider;

    private static URLActionDataExecutableResult result;

    private static URLActionDataStoreResponseHandler storeHandler;

    private static URLActionDataStore storeItemCookie1;
    
    private static URLActionDataStore storeItemCookie2;

    private static URLActionDataStore storeItemCookie3;

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

        storeItemCookie1 = new URLActionDataStore("cookie_session_id", URLActionDataStore.COOKIE, mockObjects.cookieName1, interpreter);
        storeItemCookie2 = new URLActionDataStore("cookie_session_id_time", URLActionDataStore.COOKIE, mockObjects.cookieName2, interpreter);
        storeItemCookie3 = new URLActionDataStore("cookie_ubid_acbde", URLActionDataStore.COOKIE, mockObjects.cookieName3, interpreter);
    }

    @Test
    public void testCookie1()
    {
        storeHandler.handleStore(storeItemCookie1, result);
        Assert.assertEquals(mockObjects.cookieValue1, interpreter.processDynamicData("${cookie_session_id}"));
    }

    @Test
    public void testCookie2()
    {
        storeHandler.handleStore(storeItemCookie2, result);
        Assert.assertEquals(mockObjects.cookieValue2, interpreter.processDynamicData("${cookie_session_id_time}"));
    }

    @Test
    public void testCookie3()
    {
        storeHandler.handleStore(storeItemCookie3, result);
        Assert.assertEquals(mockObjects.cookieValue3, interpreter.processDynamicData("${cookie_ubid_acbde}"));
    }
}
