package com.xceptance.xlt.common.tests;

import java.io.File;
import java.util.List;

import org.junit.Before;

import com.xceptance.xlt.api.data.GeneralDataProvider;
import com.xceptance.xlt.api.engine.Session;
import com.xceptance.xlt.api.tests.AbstractTestCase;
import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.api.util.XltProperties;
import com.xceptance.xlt.common.XltConstants;
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

    private void loadGeneralDataProvider()
    {
        this.dataProvider = GeneralDataProvider.getInstance();
    }

    private void loadDataDirectory()
    {
        final String dataDirectory = getProperty(XltConstants.XLT_PACKAGE_PATH
                                                     + ".data.directory",
                                                 "config" + File.separatorChar
                                                     + "data");
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
        this.filePath = getProperty("com.xceptance.xlt.common.tests.filename");
    }

    protected void loadFilePath()
    {
        if (filePath != null)
        {
            this.filePath = dataDirectory + File.separatorChar + filePath;
        }
        else
        {
            throw new IllegalArgumentException("Missing property: 'filename'!");
        }
    }

    private void loadMode()
    {
        final String mode = getProperty("mode");
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
        this.interpreter = new ParameterInterpreter(this.properties,
                                                    this.dataProvider);
    }

    private void setupURLActionListFacade()
    {
        urlActionListFacade = new URLActionDataListFacade(this.filePath,
                                                          this.interpreter);
    }

    private void setupURLActionExecutableFactory()
    {
        final URLActionDataExecutionbleFactoryBuilder factoryBuilder = new URLActionDataExecutionbleFactoryBuilder(this.properties,
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
        this.responseHandler = new URLActionDataResponseHandler(this.storeHandler,
                                                                this.validationHandler);
    }

    private void setupURLActionList()
    {
        try
        {
            this.actions = urlActionListFacade.buildUrlActions();
        }
        catch (final Exception e)
        {
            throw new IllegalArgumentException("Failed to read DATA from File: "
                                                   + e.getMessage(),
                                               e);
        }
    }

    /**
     * Returns the effective key to be used for property lookup via one of the getProperty(...) methods.
     * <p>
     * This method implements the fall-back logic:
     * <ol>
     * <li>user name plus simple key, e.g. TMyRunningTest.password
     * <li>test class name plus simple key, e.g. "com.xceptance.xlt.samples.testsuite.tests.TAuthor.password"</li>
     * <li>simple key, e.g. "password"</li>
     * </ol>
     * 
     * @param bareKey
     *            the bare property key, i.e. without any prefixes
     * @return the first key that produces a result
     */
    @Override
    protected String getEffectiveKey(final String bareKey)
    {
        String effectiveKey = null;
        final XltProperties xltProperties = XltProperties.getInstance();

        // 1. use the current user name as prefix
        final String userNameQualifiedKey = Session.getCurrent().getUserName()
                                            + "." + bareKey;
        if (xltProperties.containsKey(userNameQualifiedKey))
        {
            effectiveKey = userNameQualifiedKey;
        }
        else
        {
            // 2. use the current class name as prefix
            final String classNameQualifiedKey = getTestName() + "." + bareKey;
            if (xltProperties.containsKey(classNameQualifiedKey))
            {
                effectiveKey = classNameQualifiedKey;
            }
            else
            {
                // 3. use the bare key
                effectiveKey = bareKey;
            }
        }

        return effectiveKey;
    }
}
