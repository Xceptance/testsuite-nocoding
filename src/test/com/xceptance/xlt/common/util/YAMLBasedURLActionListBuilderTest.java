package test.com.xceptance.xlt.common.util;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.xceptance.xlt.common.util.URLAction;
import com.xceptance.xlt.common.util.URLActionBuilder;
import com.xceptance.xlt.common.util.YAMLBasedURLActionListBuilder;
import com.xceptance.xlt.common.util.bsh.ParameterInterpreter;

public class YAMLBasedURLActionListBuilderTest
{
    private final ParameterInterpreter interpreter = new ParameterInterpreter(null);

    private final String path = "./config/data/test/";
    private final String testData =  path + "testData.yml";
    private final String emptyFile =  path + "emptyFile.yml";
    private final String notExistingFile =  path + "notExistingFile.yml";
    private final URLActionBuilder builder = new URLActionBuilder();

    @Before
    public void setup()
    {

    }

    @Test
    public void testConstructor()
    {
        final YAMLBasedURLActionListBuilder listBuilder = new YAMLBasedURLActionListBuilder(
                                                                                            this.testData,
                                                                                            this.interpreter,
                                                                                            this.builder);
    }

    @Test
    public void fileNotFound()
    {
        final YAMLBasedURLActionListBuilder listBuilder = new YAMLBasedURLActionListBuilder(
                                                                                            this.notExistingFile,
                                                                                            this.interpreter,
                                                                                            this.builder);
        final List<URLAction> actions  = listBuilder.buildURLActions();
        Assert.assertTrue(actions.isEmpty());
    }
    
    @Test
    public void emptyFile()
    {
        final YAMLBasedURLActionListBuilder listBuilder = new YAMLBasedURLActionListBuilder(
                                                                                            this.emptyFile,
                                                                                            this.interpreter,
                                                                                            this.builder);
        final List<URLAction> actions  = listBuilder.buildURLActions();
        Assert.assertTrue(actions.isEmpty());
    }
    
    @Test
    public void testDefaultValues()
    {
        final YAMLBasedURLActionListBuilder listBuilder = new YAMLBasedURLActionListBuilder(
                                                                                            this.testData,
                                                                                            this.interpreter,
                                                                                            this.builder);
        final List<URLAction> actions  = listBuilder.buildURLActions();
    }
    
}
