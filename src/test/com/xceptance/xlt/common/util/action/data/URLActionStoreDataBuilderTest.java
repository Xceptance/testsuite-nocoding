package test.com.xceptance.xlt.common.util.action.data;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.xceptance.xlt.api.data.GeneralDataProvider;
import com.xceptance.xlt.api.util.XltProperties;
import com.xceptance.xlt.common.util.action.data.URLActionDataStore;
import com.xceptance.xlt.common.util.action.data.URLActionDataStoreBuilder;
import com.xceptance.xlt.common.util.bsh.ParameterInterpreter;

public class URLActionStoreDataBuilderTest
{
    ParameterInterpreter interpreter;

    String name;

    String selectionMode;

    String selectionContent;

    String subSelectionContent;

    String subSelectionMode;

    URLActionDataStoreBuilder storeBuilder;

    private XltProperties properties;

    private GeneralDataProvider dataProvider;

    @Before
    public void setup()
    {
        properties = XltProperties.getInstance();
        dataProvider = GeneralDataProvider.getInstance();
        interpreter = new ParameterInterpreter(properties, dataProvider);

        name = "name";
        selectionMode = "XPath";
        selectionContent = "someXPath";
        subSelectionContent = "blabla";
        subSelectionMode = URLActionDataStore.REGEXGROUP;
        storeBuilder = new URLActionDataStoreBuilder();

    }

    @Test
    public void testSettersAndGetters()
    {
        storeBuilder.setName(name);
        storeBuilder.setSelectionMode(selectionMode);
        storeBuilder.setSelectionContent(selectionContent);
        storeBuilder.setInterpreter(interpreter);
        storeBuilder.setSubSelectionContent(subSelectionContent);
        storeBuilder.setSubSelectionMode(subSelectionMode);

        Assert.assertEquals(name, storeBuilder.getName());
        Assert.assertEquals(selectionMode, storeBuilder.getSelectionMode());
        Assert.assertEquals(selectionContent, storeBuilder.getSelectionContent());
        Assert.assertEquals(subSelectionContent, storeBuilder.getSubSelectionContent());
        Assert.assertEquals(subSelectionMode, storeBuilder.getSubSelectionMode());
    }

    @Test
    public void testReset()
    {
        storeBuilder.setName(name);
        storeBuilder.setSelectionMode(selectionMode);
        storeBuilder.setSelectionContent(selectionContent);
        storeBuilder.setInterpreter(interpreter);

        Assert.assertEquals(name, storeBuilder.getName());
        Assert.assertEquals(selectionMode, storeBuilder.getSelectionMode());
        Assert.assertEquals(selectionContent, storeBuilder.getSelectionContent());

        storeBuilder.reset();

        Assert.assertNull(storeBuilder.getName());
        Assert.assertNull(storeBuilder.getSelectionContent());
        Assert.assertNull(storeBuilder.getSelectionMode());
        Assert.assertNull(storeBuilder.getInterpreter());

    }

    @Test
    public void testBuild()
    {
        storeBuilder.setName(name);
        storeBuilder.setSelectionMode(selectionMode);
        storeBuilder.setSelectionContent(selectionContent);
        storeBuilder.setInterpreter(interpreter);

        final URLActionDataStore store = storeBuilder.build();

        Assert.assertNotNull(store);

        Assert.assertNull(storeBuilder.getName());
        Assert.assertNull(storeBuilder.getSelectionContent());
        Assert.assertNull(storeBuilder.getSelectionMode());
        Assert.assertNull(storeBuilder.getInterpreter());

        Assert.assertEquals(name, store.getName());
        Assert.assertEquals(selectionMode, store.getSelectionMode());
        Assert.assertEquals(selectionContent, store.getSelectionContent());

    }
}
