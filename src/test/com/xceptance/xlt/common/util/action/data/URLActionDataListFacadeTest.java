package test.com.xceptance.xlt.common.util.action.data;

import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.xceptance.xlt.api.data.GeneralDataProvider;
import com.xceptance.xlt.api.util.XltProperties;
import com.xceptance.xlt.common.util.action.data.URLActionData;
import com.xceptance.xlt.common.util.action.data.URLActionDataListFacade;
import com.xceptance.xlt.common.util.bsh.ParameterInterpreter;

public class URLActionDataListFacadeTest
{
    private static final String fileWithYamlExtension = "file.yaml";

    private static final String fileWithYmlExtension = "file.yml";

    private static final String fileWithCsvExtension = "./config/data/torder.csv";

    private static final String fileWithUnknownExtension = "file.unknown";

    private static final String fileWithNoExtension = "file";

    private static final String fileWithEmptyExtension = "file.";

    private static final String fileWithOnlyExtension = ".yml";

    private static final String emptyFileString = "";

    private static ParameterInterpreter interpreter;

    private static XltProperties properties;

    private static GeneralDataProvider dataProvider;

    private static URLActionDataListFacade facade;

    @BeforeClass
    public static void setup()
    {
        properties = XltProperties.getInstance();
        dataProvider = GeneralDataProvider.getInstance();
        interpreter = new ParameterInterpreter(properties, dataProvider);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFileWithYamlExtension()
    {
        facade = new URLActionDataListFacade(fileWithYamlExtension, interpreter);
        final List<URLActionData> actions = facade.buildUrlActions();
        Assert.assertTrue(!actions.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFileWithYmlExtension()
    {
        facade = new URLActionDataListFacade(fileWithYmlExtension, interpreter);
        final List<URLActionData> actions = facade.buildUrlActions();
        Assert.assertTrue(actions.isEmpty());
    }

    @Test
    public void testFileWithCsvExtension()
    {
        facade = new URLActionDataListFacade(fileWithCsvExtension, interpreter);
        final List<URLActionData> actions = facade.buildUrlActions();
        Assert.assertFalse(actions.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFileWithUnknownExtension()
    {
        facade = new URLActionDataListFacade(fileWithUnknownExtension, interpreter);
        final List<URLActionData> actions = facade.buildUrlActions();
        Assert.assertNotNull(actions);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFileWithNoExtension()
    {
        facade = new URLActionDataListFacade(fileWithNoExtension, interpreter);
        final List<URLActionData> actions = facade.buildUrlActions();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFileWithEmptyExtension()
    {
        facade = new URLActionDataListFacade(fileWithEmptyExtension, interpreter);
        final List<URLActionData> actions = facade.buildUrlActions();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFileWithOnlyExtension()
    {
        facade = new URLActionDataListFacade(fileWithOnlyExtension, interpreter);
        final List<URLActionData> actions = facade.buildUrlActions();
        Assert.assertTrue(actions.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyFileString()
    {
        facade = new URLActionDataListFacade(emptyFileString, interpreter);
        final List<URLActionData> actions = facade.buildUrlActions();
        Assert.assertTrue(actions.isEmpty());
    }

}
