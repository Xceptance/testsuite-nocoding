package test.com.xceptance.xlt.common.util;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.xceptance.xlt.api.data.GeneralDataProvider;
import com.xceptance.xlt.api.util.XltProperties;
import com.xceptance.xlt.common.util.URLActionDataValidation;
import com.xceptance.xlt.common.util.bsh.ParameterInterpreter;

public class URLActionDataValidationTest
{
    ParameterInterpreter interpreter;
    private XltProperties properties;

    private GeneralDataProvider dataProvider;

    List<String> selectionModes;

    List<String> validationModes;

    List<URLActionDataValidation> validations;

    @Before
    public void setup()
    {
        properties = XltProperties.getInstance();
        dataProvider = GeneralDataProvider.getInstance();
        interpreter = new ParameterInterpreter(properties, dataProvider);
        
        selectionModes = new ArrayList<String>();
        validationModes = new ArrayList<String>();

        selectionModes.addAll(URLActionDataValidation.PERMITTEDSELECTIONMODE);
        validationModes.addAll(URLActionDataValidation.PERMITTEDVALIDATIONMODE);

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
                final URLActionDataValidation validation = new URLActionDataValidation("name",
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
        final URLActionDataValidation validation = new URLActionDataValidation(null, null, null, null,
                                                                       null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void wrongSetupSelectionMode()
    {
        @SuppressWarnings({
                "null", "unused"
            })
        final URLActionDataValidation validation = new URLActionDataValidation("name", null, "something",
                                                                       URLActionDataValidation.EXISTS,
                                                                       null, interpreter);
    }

    @Test(expected = IllegalArgumentException.class)
    public void wrongSetupValidationMode()
    {
        @SuppressWarnings({
                "null", "unused"
            })
        final URLActionDataValidation validation = new URLActionDataValidation("name",
                                                                       URLActionDataValidation.XPATH,
                                                                       "something", null, null,
                                                                       interpreter);
    }

    @Test(expected = IllegalArgumentException.class)
    public void wrongInterpreter()
    {
        @SuppressWarnings({
                "null", "unused"
            })
        final URLActionDataValidation validation = new URLActionDataValidation("name",
                                                                       URLActionDataValidation.XPATH,
                                                                       "something",
                                                                       URLActionDataValidation.MATCHES,
                                                                       null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void wrongSelectionMode()
    {
        @SuppressWarnings("null")
        final URLActionDataValidation validation = new URLActionDataValidation("name", "x", "something",
                                                                       URLActionDataValidation.MATCHES,
                                                                       null, interpreter);


    }

    @Test(expected = IllegalArgumentException.class)
    public void wrongValidationMode()
    {
        @SuppressWarnings("null")
        final URLActionDataValidation validation = new URLActionDataValidation("name",
                                                                       URLActionDataValidation.XPATH,
                                                                       "something", "x", null,
                                                                       interpreter);
    }
}
