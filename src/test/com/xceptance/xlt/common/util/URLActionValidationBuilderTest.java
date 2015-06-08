package test.com.xceptance.xlt.common.util;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.xceptance.xlt.common.util.URLActionDataValidation;
import com.xceptance.xlt.common.util.URLActionDataValidationBuilder;
import com.xceptance.xlt.common.util.bsh.ParameterInterpreter;

public class URLActionValidationBuilderTest
{
    ParameterInterpreter interpreter;
    String name;
    String selectionMode;
    String selectionContent;
    String validationMode;
    String validationContent;
    URLActionDataValidationBuilder validationBuilder;
    
    @Before
    public void setup(){
        name = "name";
        selectionMode = "XPath";
        selectionContent = "someXPath";
        validationMode = "Matches";
        validationContent = "someText";
        validationBuilder = new URLActionDataValidationBuilder();
        interpreter = new ParameterInterpreter(null);
    }
    @Test
    public void testGettersAndSetters(){
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
    public void testBuild(){
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
