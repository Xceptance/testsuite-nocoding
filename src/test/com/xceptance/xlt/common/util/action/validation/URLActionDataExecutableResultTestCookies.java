package test.com.xceptance.xlt.common.util.action.validation;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.xceptance.xlt.common.util.MockObjects;
import com.xceptance.xlt.common.util.action.validation.URLActionDataExecutableResult;
import com.xceptance.xlt.common.util.action.validation.XPathGetable;
import com.xceptance.xlt.common.util.action.validation.XPathWithHtmlPage;
public class URLActionDataExecutableResultTestCookies
{
    static XPathGetable xPathGetable;

    static MockObjects mockObjects;

    static HtmlPage page;

    @BeforeClass
    public static void setupBeforeClass()
    {
        mockObjects = new MockObjects("http://www.amazon.de");
        mockObjects.load();
        page = mockObjects.getHtmlPage();
        xPathGetable = new XPathWithHtmlPage(page);
    }

    @Before
    public void setup()
    {
    }

    @Test
    public void testGetCookies()
    {

        URLActionDataExecutableResult executableResult;
        executableResult = new URLActionDataExecutableResult(page.getWebResponse(),
                                                             xPathGetable);
        executableResult.getCookie();
        Assert.assertEquals(executableResult.getCookie().size(), 3);
    }

    @Test
    public void testGetHttpCookies()
    {
        URLActionDataExecutableResult executableResult;
        executableResult = new URLActionDataExecutableResult(page.getWebResponse(),
                                                             xPathGetable);
        Assert.assertEquals(executableResult.getCookie().size(), 3);
    }

    @Test
    public void testGetCookiesByName()
    {
        URLActionDataExecutableResult executableResult;
        executableResult = new URLActionDataExecutableResult(page.getWebResponse(),
                                                             xPathGetable);
        final List<NameValuePair> cookies = executableResult.getCookieByName("session-id");
        Assert.assertEquals(1, cookies.size());
        
    }
}
