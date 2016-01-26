package test.com.xceptance.xlt.common.util.action.data;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.xceptance.xlt.api.data.GeneralDataProvider;
import com.xceptance.xlt.api.util.XltProperties;
import com.xceptance.xlt.common.util.action.data.URLActionDataValidation;
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
    public void constructorTest()
    {
        for (final String selectionMode : selectionModes)
        {
            for (final String validationMode : validationModes)
            {
                @SuppressWarnings(
                    {
                        "unused"
                    })
                final URLActionDataValidation validation = new URLActionDataValidation("name", selectionMode, "content", validationMode,
                                                                                       "content", interpreter);

            }
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void illegalSetupName()
    {
        @SuppressWarnings(
            {
                "unused"
            })
        final URLActionDataValidation validation = new URLActionDataValidation(null, null, null, null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void illegalSetupSelectionMode()
    {
        @SuppressWarnings(
            {
                "unused"
            })
        final URLActionDataValidation validation = new URLActionDataValidation("name", null, "something", URLActionDataValidation.EXISTS,
                                                                               null, interpreter);
    }

    @Test(expected = IllegalArgumentException.class)
    public void illegalSetupValidationMode()
    {
        @SuppressWarnings(
            {
                "unused"
            })
        final URLActionDataValidation validation = new URLActionDataValidation("name", URLActionDataValidation.XPATH, "something", null,
                                                                               null, interpreter);
    }

    @Test(expected = IllegalArgumentException.class)
    public void illegalInterpreter()
    {
        @SuppressWarnings(
            {
                "unused"
            })
        final URLActionDataValidation validation = new URLActionDataValidation("name", URLActionDataValidation.XPATH, "something",
                                                                               URLActionDataValidation.MATCHES, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void illegalSelectionMode()
    {
        final URLActionDataValidation validation = new URLActionDataValidation("name", "x", "something", URLActionDataValidation.MATCHES,
                                                                               null, interpreter);
        @SuppressWarnings("unused")
        final String selectionMode = validation.getSelectionMode();

    }

    @Test(expected = IllegalArgumentException.class)
    public void illegalValidationMode()
    {
        final URLActionDataValidation validation = new URLActionDataValidation("name", URLActionDataValidation.XPATH, "something", "x",
                                                                               null, interpreter);
        @SuppressWarnings("unused")
        final String validationMode = validation.getValidationMode();
    }
}
