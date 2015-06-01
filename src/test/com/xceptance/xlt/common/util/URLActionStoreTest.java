package test.com.xceptance.xlt.common.util;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.xceptance.xlt.common.util.URLActionStore;
import com.xceptance.xlt.common.util.bsh.ParameterInterpreter;

public class URLActionStoreTest
{

    ParameterInterpreter interpreter;

    List<String> selectionModes;

    List<URLActionStore> stores;

    @BeforeClass
    public void setup()
    {
        interpreter = new ParameterInterpreter(null);
        selectionModes = new ArrayList<String>();

        selectionModes.addAll(URLActionStore.PERMITTEDSELECTIONMODE);
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
            final URLActionStore store = new URLActionStore("name", selectionMode, "content",
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
        final URLActionStore store = new URLActionStore(null, URLActionStore.REGEXP, "content",
                                                        interpreter);
    }

    @Test(expected = IllegalArgumentException.class)
    public void wrongSetupSelectionMode()
    {
        @SuppressWarnings(
            {
                "null", "unused"
            })
        final URLActionStore store = new URLActionStore("name", null, "content", interpreter);
    }

    @Test(expected = IllegalArgumentException.class)
    public void wrongSetupInterpreter()
    {
        @SuppressWarnings(
            {
                "null", "unused"
            })
        final URLActionStore store = new URLActionStore("name", URLActionStore.REGEXP, "content",
                                                        null);
    }
    @Test(expected = IllegalArgumentException.class)
    public void wrongSelectionMode()
    {
        @SuppressWarnings(
            {
                "null", "unused"
            })
        final URLActionStore store = new URLActionStore("name", "bla", "content",
                                                        interpreter);
       final String name = store.getName();
       final String mode = store.getSelectionMode();
       final String content = store.getSelectionContent();
    }
}
