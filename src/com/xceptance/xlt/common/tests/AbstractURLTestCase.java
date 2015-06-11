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
import com.xceptance.xlt.common.util.URLActionData;
import com.xceptance.xlt.common.util.URLActionDataExecutableFactory;
import com.xceptance.xlt.common.util.URLActionDataExecutableFactoryBuilder;
import com.xceptance.xlt.common.util.URLActionDataListFacade;
import com.xceptance.xlt.common.util.URLActionDataRequestBuilder;
import com.xceptance.xlt.common.util.URLActionDataResponseHandler;
import com.xceptance.xlt.common.util.bsh.ParameterInterpreter;

public class AbstractURLTestCase extends AbstractTestCase
{

    protected String login;

    protected String password;

    protected String dataDirectory;

    protected String filePath;

    protected ParameterInterpreter interpreter;

    protected URLActionDataExecutableFactory executableFactory;

    protected URLActionDataListFacade urlActionListFacade;

    protected URLActionDataResponseHandler responseHandler;

    protected URLActionDataRequestBuilder requestBuilder;

    protected GeneralDataProvider dataProvider;

    protected XltProperties properties;

    protected String mode;

    protected List<URLActionData> actions;

    @Before
    public void initializeVariables()
    {
        loadXltProperties();
        loadGeneralDataProvider();
        loadCredentials();
        loadDataDirectory();
        loadFilePath();
        loadMode();
        setupParameterInterpreter();
        setupURLActionList();
        setupURLActionExecutableFactory();
        setupURLActionRequestBuilder();
        setupURLActionResponseHandler();
    }

    private void loadXltProperties()
    {
        this.properties = XltProperties.getInstance();
    }

    private void loadGeneralDataProvider()
    {
        this.dataProvider = GeneralDataProvider.getInstance();
    }

    private void loadCredentials()
    {
        this.login = getProperty("login", getProperty("com.xceptance.xlt.auth.userName"));
        this.password = getProperty("password",
                                    getProperty("com.xceptance.xlt.auth.password"));

    }

    private void loadDataDirectory()
    {
        final String dataDirectory = getProperty(XltConstants.XLT_PACKAGE_PATH
                                                     + ".data.directory",
                                                 "config" + File.separatorChar + "data");
        if (dataDirectory != null)
        {
            this.dataDirectory = dataDirectory;
        }
        else
        {
            throw new IllegalArgumentException("Missing property 'data directory'!");
        }
    }

    private void loadFilePath()
    {
        final String filePath = getProperty("com.xceptance.xlt.common.tests.TURL.filename");
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
        this.interpreter = new ParameterInterpreter(this.properties, this.dataProvider);
    }

    private void setupURLActionList()
    {
        urlActionListFacade = new URLActionDataListFacade(this.filePath, this.interpreter);
        this.actions = urlActionListFacade.buildUrlActions();
    }

    private void setupURLActionExecutableFactory()
    {
        final URLActionDataExecutableFactoryBuilder factoryBuilder = new URLActionDataExecutableFactoryBuilder(
                                                                                                               this.properties,
                                                                                                               this.mode);
        this.executableFactory = factoryBuilder.buildFactory();
    }

    private void setupURLActionRequestBuilder()
    {
        this.requestBuilder = new URLActionDataRequestBuilder();
    }

    private void setupURLActionResponseHandler()
    {
        this.responseHandler = new URLActionDataResponseHandler();
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
        final String userNameQualifiedKey = Session.getCurrent().getUserName() + "."
                                            + bareKey;
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
