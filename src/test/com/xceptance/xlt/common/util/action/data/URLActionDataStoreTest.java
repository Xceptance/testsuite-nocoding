package test.com.xceptance.xlt.common.util.action.data;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.xceptance.xlt.api.data.GeneralDataProvider;
import com.xceptance.xlt.api.util.XltProperties;
import com.xceptance.xlt.common.util.action.data.URLActionDataStore;
import com.xceptance.xlt.common.util.bsh.ParameterInterpreter;

public class URLActionDataStoreTest
{

    private static ParameterInterpreter interpreter;

    private static XltProperties properties;

    private static GeneralDataProvider dataProvider;

    private static List<String> selectionModes;

    private static List<URLActionDataStore> stores;

    @BeforeClass
    public static void setup()
    {
        properties = XltProperties.getInstance();
        dataProvider = GeneralDataProvider.getInstance();
        interpreter = new ParameterInterpreter(properties, dataProvider);
        
        selectionModes = new ArrayList<String>();

        selectionModes.addAll(URLActionDataStore.PERMITTEDSELECTIONMODE);
    }

    @Test
    public void constructorTest()
    {
        for (final String selectionMode : selectionModes)
        {
            @SuppressWarnings(
                {
                    "null", "unused"
                })
            final URLActionDataStore store = new URLActionDataStore("name", selectionMode, "content",
                                                            interpreter);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void wrongSetupName()
    {
        @SuppressWarnings(
            {
                "null", "unused"
            })
        final URLActionDataStore store = new URLActionDataStore(null, URLActionDataStore.REGEXP, "content",
                                                        interpreter);
    }

    @Test(expected = IllegalArgumentException.class)
    public void wrongSetupSelectionMode()
    {
        @SuppressWarnings(
            {
                "null", "unused"
            })
        final URLActionDataStore store = new URLActionDataStore("name", null, "content", interpreter);
    }

    @Test(expected = IllegalArgumentException.class)
    public void wrongSetupInterpreter()
    {
        @SuppressWarnings(
            {
                "null", "unused"
            })
        final URLActionDataStore store = new URLActionDataStore("name", URLActionDataStore.REGEXP, "content",
                                                        null);
    }
    @Test(expected = IllegalArgumentException.class)
    public void wrongSelectionMode()
    {
        @SuppressWarnings(
            {
                "null", "unused"
            })
        final URLActionDataStore store = new URLActionDataStore("name", "bla", "content",
                                                        interpreter);
       final String name = store.getName();
       final String mode = store.getSelectionMode();
       final String content = store.getSelectionContent();
    }
}
