package test.com.xceptance.xlt.common.util;

import java.net.MalformedURLException;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.xceptance.xlt.common.util.URLAction;
import com.xceptance.xlt.common.util.URLActionBuilder;
import com.xceptance.xlt.common.util.URLActionStore;
import com.xceptance.xlt.common.util.URLActionStoreBuilder;
import com.xceptance.xlt.common.util.URLActionValidation;
import com.xceptance.xlt.common.util.URLActionValidationBuilder;
import com.xceptance.xlt.common.util.YAMLBasedURLActionListBuilder;
import com.xceptance.xlt.common.util.bsh.ParameterInterpreter;

public class YAMLBasedURLActionListBuilderTest
{
    private final ParameterInterpreter interpreter = new ParameterInterpreter(null);

    private final String path = "./config/data/test/";
    private final String fileTmp = path + "tmp.yml";
    private final String fileXhrSubrequests = path + "xhrSubrequests.yml";
    private final String fileTestData =  path + "testData.yml";
    private final String fileEmptyFile =  path + "emptyFile.yml";
    private final String fileNotExistingFile =  path + "notExistingFile.yml";
    private final String fileSingleActionNoDefaultsData =  path + "SAND.yml";
    private final String fileStaticSubrequests = path + "staticSubrequests.yml";
    private final String fileComplexTestCase = path + "complexTestCase.yml";
    private final URLActionBuilder actionBuilder = new URLActionBuilder();
    private final URLActionStoreBuilder storeBuilder = new URLActionStoreBuilder();
    private final URLActionValidationBuilder validationBuilder = new URLActionValidationBuilder();

    @Before
    public void setup()
    {

    }

    @Test
    public void testCorrectConstructor()
    {
        final YAMLBasedURLActionListBuilder listBuilder = new YAMLBasedURLActionListBuilder(
                                                                                            this.fileTestData,
                                                                                            this.interpreter,
                                                                                            this.actionBuilder,
                                                                                            this.validationBuilder,
                                                                                            this.storeBuilder);
    }

    @Test
    public void testOutputForUnExistingFile()
    {
        final YAMLBasedURLActionListBuilder listBuilder = new YAMLBasedURLActionListBuilder(
                                                                                            this.fileNotExistingFile,
                                                                                            this.interpreter,
                                                                                            this.actionBuilder,
                                                                                            this.validationBuilder,
                                                                                            this.storeBuilder);
        final List<URLAction> actions  = listBuilder.buildURLActions();
        Assert.assertTrue(actions.isEmpty());
    }
    
    @Test
    public void testOutputForEmptyFile()
    {
        final YAMLBasedURLActionListBuilder listBuilder = new YAMLBasedURLActionListBuilder(
                                                                                            this.fileEmptyFile,
                                                                                            this.interpreter,
                                                                                            this.actionBuilder,
                                                                                            this.validationBuilder,
                                                                                            this.storeBuilder);
        final List<URLAction> actions  = listBuilder.buildURLActions();
        Assert.assertTrue(actions.isEmpty());
    }
    
    @Test
    public void testOutputForDefaultValues()
    {
        final YAMLBasedURLActionListBuilder listBuilder = new YAMLBasedURLActionListBuilder(
                                                                                            this.fileTestData,
                                                                                            this.interpreter,
                                                                                            this.actionBuilder,
                                                                                            this.validationBuilder,
                                                                                            this.storeBuilder);
        final List<URLAction> actions  = listBuilder.buildURLActions();
    }
    @Test
    public void testBuildSingleActionWithoutDefaults() throws MalformedURLException
    {
        final YAMLBasedURLActionListBuilder listBuilder = new YAMLBasedURLActionListBuilder(
                                                                                            this.fileSingleActionNoDefaultsData,
                                                                                            this.interpreter,
                                                                                            this.actionBuilder,
                                                                                            this.validationBuilder,
                                                                                            this.storeBuilder);
        final List<URLAction> actions  = listBuilder.buildURLActions();
        Assert.assertFalse(actions.isEmpty());
        final URLAction action = actions.get(0);
        
        Assert.assertEquals("name", action.getName());
        Assert.assertEquals("http://www.xceptance.com", action.getUrl().toString());
        Assert.assertEquals(URLAction.TYPE_XHR, action.getType());
        Assert.assertTrue(action.isXHRAction());
        Assert.assertEquals(URLAction.METHOD_GET, action.getMethod().toString());
        Assert.assertTrue(action.isEncoded());
        Assert.assertEquals("body", action.getBody());
        Assert.assertEquals(400, action.getResponseCodeValidator().getHttpResponseCode());
        
        final List<NameValuePair> headers = action.getHeaders();
        Assert.assertFalse(headers.isEmpty());
        
        for(int i=0; i < headers.size(); i++){
            final NameValuePair nvp = headers.get(i);
            Assert.assertEquals("header_"+ (i+1), nvp.getName());
            Assert.assertEquals("header_value_"+ (i+1), nvp.getValue());
        }
        
        
        final List<NameValuePair> parameters = action.getParameters();
        Assert.assertFalse(parameters.isEmpty());
        
        for(int i=0; i < parameters.size(); i++){
            final NameValuePair nvp = parameters.get(i);
            Assert.assertEquals("parameter_"+ (i+1), nvp.getName());
            Assert.assertEquals("parameter_value_"+ (i+1), nvp.getValue());
        }
        
        final List<URLActionStore> store = action.getStore();
        Assert.assertFalse(store.isEmpty());
        
        final URLActionStore store1 = store.get(0);
        
        Assert.assertEquals("variable_1", store1.getName());
        Assert.assertEquals("XPath", store1.getSelectionMode());
        Assert.assertEquals("xpath_1", store1.getSelectionContent());
        
        final URLActionStore store2 = store.get(1);
        
        Assert.assertEquals("variable_2", store2.getName());
        Assert.assertEquals("RegExp", store2.getSelectionMode());
        Assert.assertEquals("xpath_2", store2.getSelectionContent());
        
        final List<URLActionValidation> validations = action.getValidations();
        Assert.assertFalse(validations.isEmpty());
        
        final URLActionValidation validation1 = validations.get(0);
        
        Assert.assertEquals("validation_name_1", validation1.getName());
        Assert.assertEquals(URLActionValidation.XPATH, validation1.getSelectionMode());
        Assert.assertEquals("xpath_value_1", validation1.getSelectionContent());
        Assert.assertEquals(URLActionValidation.EXISTS, validation1.getValidationMode());
        Assert.assertNull(validation1.getValidationContent());
        
        final URLActionValidation validation2 = validations.get(1);
        
        Assert.assertEquals("validation_name_2", validation2.getName());
        Assert.assertEquals(URLActionValidation.REGEXP, validation2.getSelectionMode());
        Assert.assertEquals("regexp_value_2", validation2.getSelectionContent());
        Assert.assertEquals(URLActionValidation.MATCHES, validation2.getValidationMode());
        Assert.assertEquals("matches_value_2", validation2.getValidationContent());
        
        
    }
    @Test
    public void testStaticSubrequestCreation() throws MalformedURLException{
        final YAMLBasedURLActionListBuilder listBuilder = new YAMLBasedURLActionListBuilder(
                                                                                            this.fileStaticSubrequests,
                                                                                            this.interpreter,
                                                                                            this.actionBuilder,
                                                                                            this.validationBuilder,
                                                                                            this.storeBuilder);
        final List<URLAction> actions  = listBuilder.buildURLActions();
        Assert.assertFalse(actions.isEmpty());
        
        final URLAction static_action_1 = actions.get(1);
        
        Assert.assertEquals("https://www.xceptance.com/images/xceptance-logo-transparent-202px.png", static_action_1.getUrl().toString());
        Assert.assertEquals(URLAction.TYPE_STATIC, static_action_1.getType());
        Assert.assertTrue(static_action_1.isStaticContent());
        Assert.assertEquals(URLAction.METHOD_GET, static_action_1.getMethod().toString());

        final URLAction static_action_2 = actions.get(2);
        
        Assert.assertEquals("https://www.xceptance.com/images/xlt-logo-small.png", static_action_2.getUrl().toString());
        Assert.assertEquals(URLAction.TYPE_STATIC, static_action_2.getType());
        Assert.assertTrue(static_action_2.isStaticContent());
        Assert.assertEquals(URLAction.METHOD_GET, static_action_2.getMethod().toString());
    }
    
    @Test
    public void testXhrSubrequestCreation() throws MalformedURLException{
        final YAMLBasedURLActionListBuilder listBuilder = new YAMLBasedURLActionListBuilder(
                                                                                            this.fileXhrSubrequests,
                                                                                            this.interpreter,
                                                                                            this.actionBuilder,
                                                                                            this.validationBuilder,
                                                                                            this.storeBuilder);
        final List<URLAction> actions  = listBuilder.buildURLActions();
        Assert.assertFalse(actions.isEmpty());
        
    }
    @Test
    public void testXomplexTestCase() throws MalformedURLException{
        final YAMLBasedURLActionListBuilder listBuilder = new YAMLBasedURLActionListBuilder(
                                                                                            this.fileComplexTestCase,
                                                                                            this.interpreter,
                                                                                            this.actionBuilder,
                                                                                            this.validationBuilder,
                                                                                            this.storeBuilder);
        final List<URLAction> actions  = listBuilder.buildURLActions();
        Assert.assertFalse(actions.isEmpty());
        
    }
    @Test
    public void testTmp() throws MalformedURLException{
        final YAMLBasedURLActionListBuilder listBuilder = new YAMLBasedURLActionListBuilder(
                                                                                            this.fileTmp,
                                                                                            this.interpreter,
                                                                                            this.actionBuilder,
                                                                                            this.validationBuilder,
                                                                                            this.storeBuilder);
        final List<URLAction> actions  = listBuilder.buildURLActions();    
    }
    
}
