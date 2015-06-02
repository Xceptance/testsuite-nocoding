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
    private final String filePathYaml = "file.yaml";
    private final String filePathYml = "file.yml";
    private final String filePathCsv = "file.csv";
    private final String filePathX = "file.x";
    private ParameterInterpreter interpreter;
    private URLActionListFacade facade;
    
    @Before
    public void setup(){
        interpreter = new ParameterInterpreter(null);
        facade = new URLActionListFacade();
    }
    @Test
    public void testYaml(){
        final List<URLAction> actions = facade.buildUrlActions(filePathYaml, interpreter);
        Assert.assertNotNull(actions);
    }
    @Test
    public void testYml(){
        final List<URLAction> actions = facade.buildUrlActions(filePathYml, interpreter);
        Assert.assertNotNull(actions);
    }
    @Test
    public void testCsv(){
        final List<URLAction> actions = facade.buildUrlActions(filePathCsv, interpreter);
        Assert.assertNotNull(actions);
    }
    @Test(expected = IllegalArgumentException.class)
    public void testOther(){
        final List<URLAction> actions = facade.buildUrlActions(filePathX, interpreter);
        Assert.assertNotNull(actions);
    }
    
}
