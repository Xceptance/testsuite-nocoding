package com.xceptance.xlt.common.tests;

import java.io.File;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;

import com.xceptance.xlt.api.data.GeneralDataProvider;
import com.xceptance.xlt.api.engine.Session;
import com.xceptance.xlt.api.tests.AbstractTestCase;
import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.api.util.XltProperties;
import com.xceptance.xlt.common.util.NoCodingPropAdmin;
import com.xceptance.xlt.common.util.action.data.URLActionData;
import com.xceptance.xlt.common.util.action.data.URLActionDataListFacade;
import com.xceptance.xlt.common.util.action.data.URLActionDataStore;
import com.xceptance.xlt.common.util.action.data.URLActionDataValidation;
import com.xceptance.xlt.common.util.action.execution.URLActionDataExecutionableFactory;
import com.xceptance.xlt.common.util.action.execution.URLActionDataExecutionbleFactoryBuilder;
import com.xceptance.xlt.common.util.action.execution.URLActionDataRequestBuilder;
import com.xceptance.xlt.common.util.action.validation.URLActionDataResponseHandler;
import com.xceptance.xlt.common.util.action.validation.URLActionDataStoreResponseHandler;
import com.xceptance.xlt.common.util.action.validation.URLActionDataValidationResponseHandler;
import com.xceptance.xlt.common.util.bsh.ParameterInterpreter;

/**
 * Prepares the actual test run. <br>
 * For this, most of the important objects are created in this class to allow some kind of DI.
 * 
 * @extends {@link AbstractTestCase}
 * @author matthias mitterreiter
 */
public class AbstractURLTestCase extends AbstractTestCase
{
    /**
     * directory for the datafiles,read from the properties
     */
    protected String dataDirectory;

    /**
     * the complete filepath, read from the properties
     */
    protected String filePath;

    /**
     * the ParamterInterpreter for dynamic parameter interpretation. <br>
     * Created once and passed to every object that shouts for it.
     */
    protected ParameterInterpreter interpreter;

    protected URLActionDataExecutionableFactory executionableFactory;

    /**
     * Creates the List<{@link URLActionData}> that holds the data to create and validate responses.
     */
    protected URLActionDataListFacade urlActionListFacade;

    /**
     * Handles the responses.
     */
    protected URLActionDataResponseHandler responseHandler;

    /**
     * Handles the responses for all {@link URLActionDataStore} objects, that are linked in a single
     * {@link URLActionData}.
     */
    protected URLActionDataStoreResponseHandler storeHandler;

    /**
     * Handles the responses particularly for all {@link URLActionDataValidation} objects, that are linked in a single
     * {@link URLActionData}. So this does the validation stuff
     */
    protected URLActionDataValidationResponseHandler validationHandler;

    /**
     * Builds WebRequests from URLActionData objects.
     */
    protected URLActionDataRequestBuilder requestBuilder;

    /**
     * Provides some predefined data.
     */
    protected GeneralDataProvider dataProvider;

    /**
     * Needed for properties access.
     */
    protected XltProperties properties;

    /**
     * This class manages the NoCoding properties and is responsible for the TestCase Mapping
     */
    protected NoCodingPropAdmin propertiesAdmin;

    /**
     * This is running mode, defined in the properties.
     */
    protected String mode;

    /**
     * Mapped informations from the files, where the test case is specified. e.g. the .yaml file.
     */
    protected List<URLActionData> actions;

    /**
     * Preparation stuff for the actual test case
     */
    @Before
    public void initializeVariables()
    {
        loadXltProperties();
        loadNoCodingPropAdmin();
        loadGeneralDataProvider();
        loadDataDirectory();
        loadFileName();
        loadFilePath();
        loadMode();
        setupParameterInterpreter();
        setupURLActionListFacade();
        setupURLActionExecutableFactory();
        setupURLActionRequestBuilder();
        setupURLActionResponseHandler();
        setupURLActionList();
    }

    private void loadXltProperties()
    {
        this.properties = XltProperties.getInstance();
    }

    private void loadNoCodingPropAdmin()
    {
        this.propertiesAdmin = new NoCodingPropAdmin(properties, getTestName(), Session.getCurrent().getUserName());
    }

    private void loadGeneralDataProvider()
    {
        this.dataProvider = GeneralDataProvider.getInstance();
    }

    private void loadDataDirectory()
    {
        final String dataDirectory = propertiesAdmin.getPropertyByKey(NoCodingPropAdmin.DIRECTORY);
        if (dataDirectory != null)
        {
            this.dataDirectory = dataDirectory;
        }
        else
        {
            throw new IllegalArgumentException("Missing property 'data directory'!");
        }
    }

    protected void loadFileName()
    {
        final String filePath = propertiesAdmin.getPropertyByKey(NoCodingPropAdmin.FILENAME);
        if (StringUtils.isNotBlank(filePath))
        {
            this.filePath = filePath;
        }
        else
        {
            this.filePath = getClass().getSimpleName();
        }
    }

    protected void loadFilePath()
    {
        this.filePath = dataDirectory + File.separatorChar + filePath;
    }

    protected void loadMode()
    {
        final String mode = propertiesAdmin.getPropertyByKey(NoCodingPropAdmin.MODE);
        if (mode != null)
        {
            this.mode = mode;
            XltLogger.runTimeLogger.info("Test Mode : " + mode);
        }
        else
        {
            throw new IllegalArgumentException("Missing property 'mode'!");
        }
    }

    private void setupParameterInterpreter()
    {
        this.interpreter = new ParameterInterpreter(this.properties, this.dataProvider);
    }

    private void setupURLActionListFacade()
    {
        urlActionListFacade = new URLActionDataListFacade(this.filePath, this.interpreter);
    }

    private void setupURLActionExecutableFactory()
    {
        final URLActionDataExecutionbleFactoryBuilder factoryBuilder = new URLActionDataExecutionbleFactoryBuilder(this.propertiesAdmin,
                                                                                                                   this.mode);
        this.executionableFactory = factoryBuilder.buildFactory();
    }

    private void setupURLActionRequestBuilder()
    {
        this.requestBuilder = new URLActionDataRequestBuilder();
    }

    private void setupURLActionResponseHandler()
    {
        this.storeHandler = new URLActionDataStoreResponseHandler();
        this.validationHandler = new URLActionDataValidationResponseHandler();
        this.responseHandler = new URLActionDataResponseHandler(this.storeHandler, this.validationHandler);
    }

    private void setupURLActionList()
    {
        try
        {
            this.actions = urlActionListFacade.buildUrlActions();
        }
        catch (final Exception e)
        {
            throw new IllegalArgumentException(e.getMessage() + ". Because: " + e.getCause().getMessage(), e);
        }
    }
}
