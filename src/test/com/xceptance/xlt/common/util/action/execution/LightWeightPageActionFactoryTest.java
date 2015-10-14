package test.com.xceptance.xlt.common.util.action.execution;

import org.junit.Before;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.WebRequest;
import com.xceptance.xlt.api.util.XltProperties;
import com.xceptance.xlt.common.util.MockObjects;
import com.xceptance.xlt.common.util.NoCodingPropAdmin;
import com.xceptance.xlt.common.util.action.execution.LightWeightPageActionFactory;
import com.xceptance.xlt.common.util.action.execution.URLActionDataExecutionable;

public class LightWeightPageActionFactoryTest
{
    private NoCodingPropAdmin propAdmin;

    private MockObjects mockObjects;

    private WebRequest request;

    @Before
    public void setup()
    {
        propAdmin = new NoCodingPropAdmin(XltProperties.getInstance(), "", "");
        mockObjects = new MockObjects("http://xceptance.github.io/SiteGenesis-Community-TestSuite/");
        mockObjects.initURL();
        mockObjects.initWebRequest();
        request = mockObjects.getRequest();
    }

    @Test
    public void testConstructor()
    {
        @SuppressWarnings("unused")
		final LightWeightPageActionFactory factory = new LightWeightPageActionFactory(propAdmin);

    }

    @Test
    public void testCreatePageAction()
    {
        final LightWeightPageActionFactory factory = new LightWeightPageActionFactory(propAdmin);
        final URLActionDataExecutionable executionable = factory.createPageAction("Action",
                                                                                  request);
        executionable.executeAction();

    }

    @Test
    public void testCreateXhrPageAction()
    {
        final LightWeightPageActionFactory factory = new LightWeightPageActionFactory(propAdmin);
        URLActionDataExecutionable executionable = factory.createPageAction("Action",
                                                                            request);
        executionable.executeAction();
        executionable = factory.createXhrPageAction("Xhr", request);
        executionable.executeAction();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateXhrPageActionAsFirstAction()
    {
        final LightWeightPageActionFactory factory = new LightWeightPageActionFactory(propAdmin);
        @SuppressWarnings("unused")
		final URLActionDataExecutionable executionable = factory.createXhrPageAction("Xhr",
                                                                                     request);
    }
}
