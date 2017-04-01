package test.com.xceptance.xlt.common.util.action.validation;

import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.xceptance.xlt.common.util.action.validation.URLActionDataExecutableResult;
import com.xceptance.xlt.common.util.action.validation.XPathGetable;
import com.xceptance.xlt.common.util.action.validation.XPathWithHtmlPage;

import test.com.xceptance.xlt.common.util.MockObjects;

public class URLActionDataExecutableResultTest
{
    static XPathGetable xPathGetable;

    static MockObjects mockObjects;

    static HtmlPage page;

    @BeforeClass
    public static void setupBeforeClass()
    {
        mockObjects = new MockObjects();
        mockObjects.load();
        page = mockObjects.getHtmlPage();
        xPathGetable = new XPathWithHtmlPage(page);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testNullConstructor()
    {
        @SuppressWarnings("unused")
		URLActionDataExecutableResult executableResult;
        executableResult = new URLActionDataExecutableResult(null, null);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testNullWebResponse()
    {
        @SuppressWarnings("unused")
		URLActionDataExecutableResult executableResult;
        executableResult = new URLActionDataExecutableResult(null, xPathGetable);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testNullXpath()
    {
        @SuppressWarnings("unused")
		URLActionDataExecutableResult executableResult;
        executableResult = new URLActionDataExecutableResult(page.getWebResponse(), null);
    }
    
    @Test
    public void testGetByXPath()
    {
        URLActionDataExecutableResult executableResult;
        executableResult = new URLActionDataExecutableResult(page.getWebResponse(), xPathGetable);
        final List<String> something = executableResult.getByXPath(mockObjects.xPathString);

        Assert.assertEquals(mockObjects.xpathStringExpected, something.get(0));
    }

    @Test
    public void testGetByRegEx()
    {
        URLActionDataExecutableResult executableResult;
        executableResult = new URLActionDataExecutableResult(page.getWebResponse(), xPathGetable);

        final List<String> something = executableResult.getByRegEx(mockObjects.regexString);
        Assert.assertFalse(something.isEmpty());
        Assert.assertEquals(mockObjects.regexStringExpected, something.get(0));
    }

    @Test
    public void testGetHeaders()
    {
        URLActionDataExecutableResult executableResult;
        executableResult = new URLActionDataExecutableResult(page.getWebResponse(), xPathGetable);
        final List<NameValuePair> headers = executableResult.getHeaders();
        Assert.assertFalse(headers.isEmpty());
    }

}
