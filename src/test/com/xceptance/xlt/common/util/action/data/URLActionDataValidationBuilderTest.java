package test.com.xceptance.xlt.common.util.action.data;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.xceptance.xlt.api.data.GeneralDataProvider;
import com.xceptance.xlt.api.util.XltProperties;
import com.xceptance.xlt.common.util.action.data.URLActionDataValidation;
import com.xceptance.xlt.common.util.action.data.URLActionDataValidationBuilder;
import com.xceptance.xlt.common.util.bsh.ParameterInterpreter;

public class URLActionDataValidationBuilderTest
{
    ParameterInterpreter interpreter;

    private XltProperties properties;

    private GeneralDataProvider dataProvider;

    String name;

    String selectionMode;

    String selectionContent;

    String validationMode;

    String validationContent;

    URLActionDataValidationBuilder validationBuilder;

    @Before
    public void setup()
    {
        name = "name";
        selectionMode = "XPath";
        selectionContent = "someXPath";
        validationMode = "Matches";
        validationContent = "someText";
        validationBuilder = new URLActionDataValidationBuilder();

        properties = XltProperties.getInstance();
        dataProvider = GeneralDataProvider.getInstance();
        interpreter = new ParameterInterpreter(properties, dataProvider);
    }

    @Test
    public void testGettersAndSetters()
    {
        validationBuilder.setName(name);
        validationBuilder.setInterpreter(interpreter);
        validationBuilder.setSelectionContent(selectionContent);
        validationBuilder.setSelectionMode(selectionMode);
        validationBuilder.setValidationContent(validationContent);
        validationBuilder.setValidationMode(validationMode);

        Assert.assertEquals(name, validationBuilder.getName());
        Assert.assertEquals(selectionMode, validationBuilder.getSelectionMode());
        Assert.assertEquals(selectionContent, validationBuilder.getSelectionContent());
        Assert.assertEquals(validationContent, validationBuilder.getValidationContent());
        Assert.assertEquals(validationMode, validationBuilder.getValidationMode());
    }

    @Test
    public void testBuild()
    {
        validationBuilder.setName(name);
        validationBuilder.setInterpreter(interpreter);
        validationBuilder.setSelectionContent(selectionContent);
        validationBuilder.setSelectionMode(selectionMode);
        validationBuilder.setValidationContent(validationContent);
        validationBuilder.setValidationMode(validationMode);

        final URLActionDataValidation validation = validationBuilder.build();

        Assert.assertEquals(name, validation.getName());
        Assert.assertEquals(selectionMode, validation.getSelectionMode());
        Assert.assertEquals(selectionContent, validation.getSelectionContent());
        Assert.assertEquals(validationContent, validation.getValidationContent());
        Assert.assertEquals(validationMode, validation.getValidationMode());

    }
}
