package test.com.xceptance.xlt.common.util;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.xceptance.xlt.common.util.URLActionValidation;
import com.xceptance.xlt.common.util.bsh.ParameterInterpreter;

public class URLActionValidationTest
{
    ParameterInterpreter interpreter;

    List<String> selectionModes;

    List<String> validationModes;

    List<URLActionValidation> validations;

    @Before
    public void setup()
    {
        interpreter = new ParameterInterpreter(null);
        selectionModes = new ArrayList<String>();
        validationModes = new ArrayList<String>();

        selectionModes.addAll(URLActionValidation.PERMITTEDSELECTIONMODE);
        validationModes.addAll(URLActionValidation.PERMITTEDVALIDATIONMODE);

    }
    @Test 
    public void constructorTest(){
        for (final String selectionMode : selectionModes)
        {
            for (final String validationMode : validationModes)
            {
                @SuppressWarnings({
                        "null", "unused"
                    })
                final URLActionValidation validation = new URLActionValidation("name",
                                                                               selectionMode,
                                                                               "content",
                                                                               validationMode,
                                                                               "content",
                                                                               interpreter);

            }
        }
    }
    @Test(expected = IllegalArgumentException.class)
    public void wrongSetupName()
    {
        @SuppressWarnings({
                "unused", "null"
            })
        final URLActionValidation validation = new URLActionValidation(null, null, null, null,
                                                                       null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void wrongSetupSelectionMode()
    {
        @SuppressWarnings({
                "null", "unused"
            })
        final URLActionValidation validation = new URLActionValidation("name", null, "something",
                                                                       URLActionValidation.EXISTS,
                                                                       null, interpreter);
    }

    @Test(expected = IllegalArgumentException.class)
    public void wrongSetupValidationMode()
    {
        @SuppressWarnings({
                "null", "unused"
            })
        final URLActionValidation validation = new URLActionValidation("name",
                                                                       URLActionValidation.XPATH,
                                                                       "something", null, null,
                                                                       interpreter);
    }

    @Test(expected = IllegalArgumentException.class)
    public void wrongInterpreter()
    {
        @SuppressWarnings({
                "null", "unused"
            })
        final URLActionValidation validation = new URLActionValidation("name",
                                                                       URLActionValidation.XPATH,
                                                                       "something",
                                                                       URLActionValidation.MATCHES,
                                                                       null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void wrongSelectionMode()
    {
        @SuppressWarnings("null")
        final URLActionValidation validation = new URLActionValidation("name", "x", "something",
                                                                       URLActionValidation.MATCHES,
                                                                       null, interpreter);

        validation.outline();

    }

    @Test(expected = IllegalArgumentException.class)
    public void wrongValidationMode()
    {
        @SuppressWarnings("null")
        final URLActionValidation validation = new URLActionValidation("name",
                                                                       URLActionValidation.XPATH,
                                                                       "something", "x", null,
                                                                       interpreter);
        validation.outline();
    }
}
