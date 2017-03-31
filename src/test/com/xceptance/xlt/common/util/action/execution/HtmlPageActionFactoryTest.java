package test.com.xceptance.xlt.common.util.action.execution;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.WebRequest;
import com.xceptance.xlt.api.util.XltProperties;
import com.xceptance.xlt.common.util.NoCodingPropAdmin;
import com.xceptance.xlt.common.util.action.execution.HtmlPageActionFactory;
import com.xceptance.xlt.common.util.action.execution.URLActionDataExecutionable;

import test.com.xceptance.xlt.common.util.MockObjects;

public class HtmlPageActionFactoryTest
{
    private static NoCodingPropAdmin propAdmin;

    private static MockObjects mockObjects;

    private static WebRequest request;

    @BeforeClass
    public static void setup()
    {
        propAdmin = new NoCodingPropAdmin(XltProperties.getInstance(), "", "");
        mockObjects = new MockObjects();
        mockObjects.setUrlString(mockObjects.urlStringDemoHtml);
        mockObjects.initURL();
        mockObjects.initWebRequest();
        request = mockObjects.getRequest();
    }

	@Test
    public void testConstructor()
    {
        final HtmlPageActionFactory factory = new HtmlPageActionFactory(propAdmin);
        Assert.assertEquals(propAdmin, factory.getPropAdmin());
    }

    @Test
    public void testCreatePageAction()
    {
        final HtmlPageActionFactory factory = new HtmlPageActionFactory(propAdmin);
        final URLActionDataExecutionable executionable = factory.createPageAction("Action", request);
        executionable.executeAction();
        Assert.assertNotNull(executionable.getResult());
    }

    @Test
    public void testCreateXhrPageAction()
    {
        final HtmlPageActionFactory factory = new HtmlPageActionFactory(propAdmin);
        URLActionDataExecutionable executionable = factory.createPageAction("Action", request);
        executionable.executeAction();
        executionable = factory.createXhrPageAction("Xhr", request);
        executionable.executeAction();
        Assert.assertNotNull(executionable.getResult());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateXhrPageActionAsFirstAction()
    {
        final HtmlPageActionFactory factory = new HtmlPageActionFactory(propAdmin);
        @SuppressWarnings("unused")
        final URLActionDataExecutionable executionable = factory.createXhrPageAction("Xhr", request);
    }
}
