package test.com.xceptance.xlt.common.util.action.validation;

import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.xceptance.xlt.common.util.MockObjects;
import com.xceptance.xlt.common.util.action.validation.URLActionDataExecutableResult;
import com.xceptance.xlt.common.util.action.validation.URLActionDataExecutableResultFactory;

public class URLActionDataExecutableResultFactoryTest
{
    private static URLActionDataExecutableResultFactory resultFactory;

    private static MockObjects mockObjects;

    @BeforeClass
    public static void setup()
    {
        resultFactory = new URLActionDataExecutableResultFactory();
        mockObjects = new MockObjects();
        mockObjects.load();
    }

    @Test
    public void testGetResultWithHtmlPage()
    {
        final URLActionDataExecutableResult result = resultFactory.getResult(mockObjects.getHtmlPage());
        final List<String> something = result.getByXPath("//*[@id='service-areas']/div[1]/div/div/h1");
        Assert.assertEquals(something.get(0), "Committed to Software Quality");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetResultWithLightWeightPage()
    {
        final URLActionDataExecutableResult result = resultFactory.getResult(mockObjects.getLightWeightPage());
        final List<String> something = result.getByXPath("//*[@id='service-areas']/div[1]/div/div/h1");
    }

    @Test
    public void testGetResultWithWebResponse()
    {
        final URLActionDataExecutableResult result = resultFactory.getResult(mockObjects.getResponse());
        final List<String> something = result.getByXPath("//*[@id='service-areas']/div[1]/div/div/h1");
        Assert.assertEquals(something.get(0), "Committed to Software Quality");
    }

}
