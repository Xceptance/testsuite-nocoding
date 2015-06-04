package test.com.xceptance.xlt.common.util;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.xceptance.xlt.common.util.HtmlPageActionFactory;
import com.xceptance.xlt.common.util.LightWeightPageActionFactory;
import com.xceptance.xlt.common.util.WebActionFactory;
import com.xceptance.xlt.common.util.WebActionFactoryBuilder;
import com.xceptance.xlt.common.util.bsh.ParameterInterpreter;

public class WebActionListFacadeTest
{
    private ParameterInterpreter interpreter;

    @Before
    public void setup()
    {
        interpreter = new ParameterInterpreter(null);
    }

    @Test
    public void testCorrectConstructorCreation()
    {
        WebActionFactoryBuilder factory;
        factory = new WebActionFactoryBuilder(this.interpreter,
                                              WebActionFactoryBuilder.MODE_DOM);

        factory = new WebActionFactoryBuilder(this.interpreter,
                                              WebActionFactoryBuilder.MODE_LIGHT);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWrongConstructorParameterInterpreter()
    {
        final WebActionFactoryBuilder factory = new WebActionFactoryBuilder(
                                                                            null,
                                                                            WebActionFactoryBuilder.MODE_DOM);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWrongConstructorParameterMode()
    {
        final WebActionFactoryBuilder factory = new WebActionFactoryBuilder(
                                                                            this.interpreter,
                                                                            "someMode");
    }

    @Test
    public void testCorrectDomBuildingResult()
    {
        final WebActionFactoryBuilder factory = new WebActionFactoryBuilder(
                                                                            this.interpreter,
                                                                            WebActionFactoryBuilder.MODE_DOM);
        final WebActionFactory actionFactory = factory.buildFactory();
        Assert.assertTrue(actionFactory instanceof HtmlPageActionFactory);
    }

    @Test
    public void testCorrectLightBuildingResult()
    {
        final WebActionFactoryBuilder factory = new WebActionFactoryBuilder(
                                                                            this.interpreter,
                                                                            WebActionFactoryBuilder.MODE_LIGHT);
        final WebActionFactory actionFactory = factory.buildFactory();
        Assert.assertTrue(actionFactory instanceof LightWeightPageActionFactory);
    }
    
}
