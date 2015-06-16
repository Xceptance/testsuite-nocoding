package test.com.xceptance.xlt.common.util.action.validation;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.xceptance.xlt.common.util.MockObjects;
import com.xceptance.xlt.common.util.action.validation.URLActionDataExecutableResult;
import com.xceptance.xlt.common.util.action.validation.XPathGetable;
import com.xceptance.xlt.common.util.action.validation.XPathWithHtmlPage;

public class URLActionDataExecutableResultTest
{
    XPathGetable xPathGetable;

    MockObjects mockObjects;

    HtmlPage page;

    @Before
    public void setup()
    {
        mockObjects = new MockObjects();
        mockObjects.load();
        page = mockObjects.getHtmlPage();
        xPathGetable = new XPathWithHtmlPage(page);
    }

    @Test
    public void testConstructor()
    {
        URLActionDataExecutableResult executableResult;
        executableResult = new URLActionDataExecutableResult(page.getWebResponse(),
                                                             xPathGetable);
    }

    @Test
    public void testGetByXPath()
    {
        URLActionDataExecutableResult executableResult;
        executableResult = new URLActionDataExecutableResult(page.getWebResponse(),
                                                             xPathGetable);
        final List<String> something = executableResult.getByXPath("//*[@id='service-areas']/div[1]/div/div/h1");

        Assert.assertEquals(something.get(0), "Committed to Software Quality");
    }

    @Test
    public void testGetByRegEx()
    {
        URLActionDataExecutableResult executableResult;
        executableResult = new URLActionDataExecutableResult(page.getWebResponse(),
                                                             xPathGetable);

        final List<String> something = executableResult.getByRegEx("href=\"[\\s\\S]*?\"");
        Assert.assertFalse(something.isEmpty());
        Assert.assertEquals("href=\"/en/\"", something.get(0));
    }

    @Test
    public void testGetHeaders()
    {
        URLActionDataExecutableResult executableResult;
        executableResult = new URLActionDataExecutableResult(page.getWebResponse(),
                                                             xPathGetable);
        final List<NameValuePair> headers = executableResult.getHeaders();
        Assert.assertFalse(headers.isEmpty());
    }
}
