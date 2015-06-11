package test.com.xceptance.xlt.common.util;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.xceptance.xlt.api.util.XltProperties;
import com.xceptance.xlt.common.util.HtmlPageActionFactory;
import com.xceptance.xlt.common.util.LightWeightPageActionFactory;
import com.xceptance.xlt.common.util.URLActionDataExecutableFactory;
import com.xceptance.xlt.common.util.URLActionDataExecutableFactoryBuilder;

public class URLActionDataExecutableFactoryBuilderTest
{
    private XltProperties properties;

    @Before
    public void setup()
    {
        properties = XltProperties.getInstance();
    }

    @Test
    public void testCorrectConstructorCreation()
    {
        URLActionDataExecutableFactoryBuilder factoryBuilder;
        factoryBuilder = new URLActionDataExecutableFactoryBuilder(
                                                                   this.properties,
                                                                   URLActionDataExecutableFactoryBuilder.MODE_DOM);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWrongConstructorParameterInterpreter()
    {
        final URLActionDataExecutableFactoryBuilder factory = new URLActionDataExecutableFactoryBuilder(
                                                                                                        null,
                                                                                                        URLActionDataExecutableFactoryBuilder.MODE_DOM);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWrongConstructorParameterMode()
    {
        final URLActionDataExecutableFactoryBuilder factory = new URLActionDataExecutableFactoryBuilder(
                                                                                                        this.properties,
                                                                                                        "someMode");
    }

    @Test
    public void testCorrectDomBuildingResult()
    {
        final URLActionDataExecutableFactoryBuilder factory = new URLActionDataExecutableFactoryBuilder(
                                                                                                        this.properties,
                                                                                                        URLActionDataExecutableFactoryBuilder.MODE_DOM);
        final URLActionDataExecutableFactory actionFactory = factory.buildFactory();
        Assert.assertTrue(actionFactory instanceof HtmlPageActionFactory);
    }

    @Test
    public void testCorrectLightBuildingResult()
    {
        final URLActionDataExecutableFactoryBuilder factory = new URLActionDataExecutableFactoryBuilder(
                                                                                                        this.properties,
                                                                                                        URLActionDataExecutableFactoryBuilder.MODE_LIGHT);
        final URLActionDataExecutableFactory actionFactory = factory.buildFactory();
        Assert.assertTrue(actionFactory instanceof LightWeightPageActionFactory);
    }

}
