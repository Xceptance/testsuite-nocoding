package test.com.xceptance.xlt.common.util.action.validation;

import java.net.MalformedURLException;
import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.xceptance.xlt.common.util.action.validation.XPathWithHtmlPage;

import test.com.xceptance.xlt.common.util.MockObjects;

public class XPathWithHtmlPageTest
{
    private static MockObjects mockObjects;

    @BeforeClass
    public static void setup() throws MalformedURLException
    {
        mockObjects = new MockObjects();
        mockObjects.load();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNull()
    {
        @SuppressWarnings("unused")
        final XPathWithHtmlPage xp = new XPathWithHtmlPage(null);
    }

    @Test
    public void testGetByXPath()
    {
        final XPathWithHtmlPage xp = new XPathWithHtmlPage(mockObjects.getHtmlPage());
        final List<String> something = xp.getByXPath(mockObjects.xPathString);
        Assert.assertEquals(mockObjects.xpathStringExpected, something.get(0));
    }
}
