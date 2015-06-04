package test.com.xceptance.xlt.common.util;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.xceptance.xlt.common.util.URLAction;
import com.xceptance.xlt.common.util.URLActionListFacade;
import com.xceptance.xlt.common.util.bsh.ParameterInterpreter;

public class URLActionListFacadeTest
{
    private final String fileWithYamlExtension = "file.yaml";

    private final String fileWithYmlExtension = "file.yml";

    private final String fileWithCsvExtension = "file.csv";

    private final String fileWithUnknownExtension = "file.unknown";

    private final String fileWithNoExtension = "file";
    
    private final String fileWithEmptyExtension = "file.";
    
    private final String fileWithOnlyExtension = ".yml";
    
    private final String emptyFileString = "";

    private ParameterInterpreter interpreter;

    private URLActionListFacade facade;

    @Before
    public void setup()
    {
        interpreter = new ParameterInterpreter(null);
    }

    @Test
    public void testFileWithYamlExtension()
    {
        facade = new URLActionListFacade(fileWithYamlExtension, interpreter);
        final List<URLAction> actions = facade.buildUrlActions();
        Assert.assertTrue(actions.isEmpty());
    }

    @Test
    public void testFileWithYmlExtension()
    {
        facade = new URLActionListFacade(fileWithYmlExtension, interpreter);
        final List<URLAction> actions = facade.buildUrlActions();
        Assert.assertTrue(actions.isEmpty());
    }

    @Test
    public void testFileWithCsvExtension()
    {
        facade = new URLActionListFacade(fileWithCsvExtension, interpreter);
        final List<URLAction> actions = facade.buildUrlActions();
        Assert.assertTrue(actions.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFileWithUnknownExtension()
    {
        facade = new URLActionListFacade(fileWithUnknownExtension, interpreter);
        final List<URLAction> actions = facade.buildUrlActions();
        Assert.assertNotNull(actions);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFileWithNoExtension()
    {
        facade = new URLActionListFacade(fileWithNoExtension, interpreter);
        final List<URLAction> actions = facade.buildUrlActions();
    }
    @Test(expected = IllegalArgumentException.class)
    public void testFileWithEmptyExtension()
    {
        facade = new URLActionListFacade(fileWithEmptyExtension, interpreter);
        final List<URLAction> actions = facade.buildUrlActions();
    }
    
    @Test
    public void testFileWithOnlyExtension()
    {
        facade = new URLActionListFacade(fileWithOnlyExtension, interpreter);
        final List<URLAction> actions = facade.buildUrlActions();
        Assert.assertTrue(actions.isEmpty());
    }
    @Test(expected = IllegalArgumentException.class)
    public void testEmptyFileString()
    {
        facade = new URLActionListFacade(emptyFileString, interpreter);
        final List<URLAction> actions = facade.buildUrlActions();
        Assert.assertTrue(actions.isEmpty());
    }
    

}
