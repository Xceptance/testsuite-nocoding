package com.xceptance.xlt.common.tests;

import java.io.File;
import java.util.List;

import org.junit.Before;

import com.xceptance.xlt.api.engine.Session;
import com.xceptance.xlt.api.tests.AbstractTestCase;
import com.xceptance.xlt.api.util.XltProperties;
import com.xceptance.xlt.common.XltConstants;
import com.xceptance.xlt.common.util.URLAction;
import com.xceptance.xlt.common.util.URLActionListFacade;
import com.xceptance.xlt.common.util.WebActionFactory;
import com.xceptance.xlt.common.util.WebActionFactoryBuilder;
import com.xceptance.xlt.common.util.bsh.ParameterInterpreter;

public class AbstractURLTestCase extends AbstractTestCase
{

    protected String login;

    protected String password;

    protected String dataDirectory;

    protected String filePath;

    protected ParameterInterpreter interpreter;

    protected WebActionFactory webActionFactory;

    protected URLActionListFacade urlActionListFacade;

    protected String mode;

    protected List<URLAction> actions;

    @Before
    public void initializeVariables()
    {
        loadCredentials();
        loadDataDirectory();
        loadFilePath();
        loadMode();
        setupParameterInterpreter();
        setupURLActionList();
        setupWebActionFactory();
    }

    private void loadCredentials()
    {
        this.login = getProperty("login", getProperty("com.xceptance.xlt.auth.userName"));
        this.password = getProperty("password",
                                    getProperty("com.xceptance.xlt.auth.password"));

    }

    private void loadDataDirectory()
    {
        this.dataDirectory = getProperty(XltConstants.XLT_PACKAGE_PATH
                                         + ".data.directory", "config"
                                                              + File.separatorChar
                                                              + "data");
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
        }
        else
        {
            throw new IllegalArgumentException("Missing property 'mode'!");
        }
    }

    private void setupParameterInterpreter()
    {
        this.interpreter = new ParameterInterpreter(this);
    }

    private void setupURLActionList()
    {
        urlActionListFacade = new URLActionListFacade();
        this.actions = urlActionListFacade.buildUrlActions(filePath, interpreter);
    }

    private void setupWebActionFactory()
    {
        final WebActionFactoryBuilder factoryBuilder = new WebActionFactoryBuilder();
        this.webActionFactory = factoryBuilder.buildFactory(this.mode,this.interpreter);
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
