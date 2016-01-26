package test.com.xceptance.xlt.common.util.action.validation;

import java.net.MalformedURLException;
import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.xceptance.xlt.common.util.MockObjects;
import com.xceptance.xlt.common.util.action.validation.XPathWithHtmlPage;

public class XPathWithHtmlPageTest
{

    private static final String urlString = "https://www.xceptance.com/en/";

    private static MockObjects mockObjects;

    @BeforeClass
    public static void setup() throws MalformedURLException
    {
        mockObjects = new MockObjects(urlString);
        mockObjects.load();
    }

    @Test
    public void testConstructor()
    {
        @SuppressWarnings("unused")
        final XPathWithHtmlPage xp = new XPathWithHtmlPage(mockObjects.getHtmlPage());
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
        final List<String> something = xp.getByXPath("//*[@id='service-areas']/div[1]/div/div/h1");
        Assert.assertEquals(something.get(0), "Committed to Software Quality");
    }
}
