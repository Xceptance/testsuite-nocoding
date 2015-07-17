package test.com.xceptance.xlt.common.util;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.xceptance.xlt.common.util.MockObjects;

public class MockObjectsTest
{
    String urlStringXceptance = "https://www.xceptance.com/en/";

    String urlStringGoogle = "https://www.google.de";

    private MockObjects mockObjects;

    @Before
    public void setup()
    {

    }

    @Test
    public void testConstructor()
    {
        mockObjects = new MockObjects();
        Assert.assertEquals(urlStringXceptance, mockObjects.getUrlString());

        mockObjects = new MockObjects(urlStringGoogle);
        Assert.assertEquals(urlStringGoogle, mockObjects.getUrlString());
    }

    @Test
    public void testLoadResponse()
    {
        mockObjects = new MockObjects();
        mockObjects.load();
        System.err.println("-------------LIGHTWEIGHT-----------------");
        System.err.println(mockObjects.getLightWeightPage().getContent().replaceAll("\\s+",""));
        System.err.println("-------------HTMLPAGE-----------------");
        System.err.println(mockObjects.getHtmlPage().getWebResponse().getContentAsString().replaceAll("\\s+",""));
    }

}
