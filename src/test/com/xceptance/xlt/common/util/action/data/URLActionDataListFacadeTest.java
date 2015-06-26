package test.com.xceptance.xlt.common.util.action.data;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.xceptance.xlt.api.data.GeneralDataProvider;
import com.xceptance.xlt.api.util.XltProperties;
import com.xceptance.xlt.common.util.action.data.URLActionData;
import com.xceptance.xlt.common.util.action.data.URLActionDataListFacade;
import com.xceptance.xlt.common.util.bsh.ParameterInterpreter;

public class URLActionDataListFacadeTest
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

    private XltProperties properties;

    private GeneralDataProvider dataProvider;

    private URLActionDataListFacade facade;

    @Before
    public void setup()
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
        Assert.assertTrue(actions.isEmpty());
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
        Assert.assertTrue(actions.isEmpty());
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
