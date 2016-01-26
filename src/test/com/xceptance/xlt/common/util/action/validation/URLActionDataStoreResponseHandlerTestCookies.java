package test.com.xceptance.xlt.common.util.action.validation;

import org.junit.BeforeClass;
import org.junit.Test;

import com.xceptance.xlt.api.data.GeneralDataProvider;
import com.xceptance.xlt.api.util.XltProperties;
import com.xceptance.xlt.common.util.MockObjects;
import com.xceptance.xlt.common.util.action.data.URLActionDataStore;
import com.xceptance.xlt.common.util.action.validation.URLActionDataExecutableResult;
import com.xceptance.xlt.common.util.action.validation.URLActionDataStoreResponseHandler;
import com.xceptance.xlt.common.util.action.validation.XPathWithHtmlPage;
import com.xceptance.xlt.common.util.bsh.ParameterInterpreter;

public class URLActionDataStoreResponseHandlerTestCookies
{
    private final static String urlString = "http://www.amazon.de";

    private static MockObjects mockObjects;

    private static ParameterInterpreter interpreter;

    private static XltProperties properties;

    private static GeneralDataProvider dataProvider;

    private static URLActionDataExecutableResult result;

    private static URLActionDataStoreResponseHandler storeHandler;

    private static URLActionDataStore storeItemCookie1;

    private static final String cookieName1 = "session-id";

    private static URLActionDataStore storeItemCookie2;

    private static final String cookieName2 = "session-id-time";

    private static URLActionDataStore storeItemCookie3;

    private static final String cookieName3 = "x-wl-uid";

    @BeforeClass
    public static void setup()
    {
        properties = XltProperties.getInstance();
        dataProvider = GeneralDataProvider.getInstance();
        interpreter = new ParameterInterpreter(properties, dataProvider);
        storeHandler = new URLActionDataStoreResponseHandler();
        mockObjects = new MockObjects(urlString);
        mockObjects.load();
        final XPathWithHtmlPage xpwh = new XPathWithHtmlPage(mockObjects.getHtmlPage());
        result = new URLActionDataExecutableResult(mockObjects.getResponse(), xpwh);

        storeItemCookie1 = new URLActionDataStore("cookie_session_id", URLActionDataStore.COOKIE, cookieName1, interpreter);
        storeItemCookie2 = new URLActionDataStore("cookie_session-id-time", URLActionDataStore.COOKIE, cookieName2, interpreter);
        storeItemCookie3 = new URLActionDataStore("cookie_ubid-acbde", URLActionDataStore.COOKIE, cookieName3, interpreter);
    }

    @Test
    public void testCooki1()
    {
        storeHandler.handleStore(storeItemCookie1, result);
    }

    @Test
    public void testCooki2()
    {
        storeHandler.handleStore(storeItemCookie2, result);
    }

    @Test
    public void testCooki3()
    {
        storeHandler.handleStore(storeItemCookie3, result);
    }
}
