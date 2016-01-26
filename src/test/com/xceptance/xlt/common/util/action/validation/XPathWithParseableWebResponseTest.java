package test.com.xceptance.xlt.common.util.action.validation;

import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import test.com.xceptance.xlt.common.util.MockWebResponse;

import com.gargoylesoftware.htmlunit.WebResponse;
import com.xceptance.xlt.common.util.MockObjects;
import com.xceptance.xlt.common.util.action.validation.XPathWithParseableWebResponse;

public class XPathWithParseableWebResponseTest
{
    private static MockObjects mockObjects;

    @BeforeClass
    public static void setup()
    {
        mockObjects = new MockObjects();
        mockObjects.load();
    }

    @Test
    public void testConstructor()
    {
        final WebResponse response = mockObjects.getResponse();
        @SuppressWarnings("unused")
        final XPathWithParseableWebResponse thing = new XPathWithParseableWebResponse(response);
    }

    @Test
    public void testIsParseable()
    {
        final MockWebResponse mockWeResponseUnsupported = new MockWebResponse("some content", mockObjects.getUrl(), "unsupported/type");
        Assert.assertFalse(XPathWithParseableWebResponse.isWebResponseParseable(mockWeResponseUnsupported));
        final MockWebResponse mockWeResponseHtml = new MockWebResponse("some content", mockObjects.getUrl(), "text/html");
        Assert.assertTrue(XPathWithParseableWebResponse.isWebResponseParseable(mockWeResponseHtml));
        final MockWebResponse mockWeResponseHtml2 = new MockWebResponse("some content", mockObjects.getUrl(), "text/application");
        Assert.assertTrue(XPathWithParseableWebResponse.isWebResponseParseable(mockWeResponseHtml2));

    }

    @Test
    public void testGetByXPath()
    {
        final WebResponse response = mockObjects.getResponse();
        final XPathWithParseableWebResponse thing = new XPathWithParseableWebResponse(response);
        final List<String> xPathResults = thing.getByXPath("//*[@id='service-areas']/div[1]/div/div/h1");
        Assert.assertEquals(xPathResults.get(0), "Committed to Software Quality");
    }

}
