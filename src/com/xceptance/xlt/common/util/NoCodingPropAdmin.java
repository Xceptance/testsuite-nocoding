package com.xceptance.xlt.common.util;

import java.text.MessageFormat;

import org.apache.commons.codec.binary.Base64;

import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.api.util.XltProperties;
import com.xceptance.xlt.engine.XltWebClient;

public class NoCodingPropAdmin
{
    private final String fullTestCaseName;

    private final String testName;

    private final XltProperties xltProperties;

    public static final String JAVASCRIPTENABLED = "com.xceptance.xlt.javaScriptEnabled";

    public static final String CSSENABLED = "com.xceptance.xlt.cssEnabled";

    public static final String LOADSTATICCONTENT = "com.xceptance.xlt.loadStaticContent";

    public static final String USERNAMEAUTH = "com.xceptance.xlt.auth.userName";

    public static final String PASSWORDAUTH = "com.xceptance.xlt.auth.password";

    public static final String REDIRECTENABLED = "com.xceptance.xlt.nocoding.redirect";

    public static final String FILENAME = "com.xceptance.xlt.nocoding.filename";

    public static final String DIRECTORY = "com.xceptance.xlt.nocoding.directory";

    public static final String MODE = "com.xceptance.xlt.nocoding.mode";

    public static final String USERAGENTUID = "userAgent.UID";

    public static final String DOWNLOADTHREADS = "com.xceptance.xlt.staticContent.downloadThreads";

    public static final String TLSVERSION = "com.xceptance.xlt.nocoding.TLSVersion";

    public NoCodingPropAdmin(final XltProperties xltProperties, final String fullTestCaseName, final String testName)
    {
        ParameterUtils.isNotNull(fullTestCaseName, "fullTestCaseName");
        ParameterUtils.isNotNull(xltProperties, "XltProperties");
        ParameterUtils.isNotNull(testName, "testName");

        this.fullTestCaseName = fullTestCaseName;
        this.xltProperties = xltProperties;
        this.testName = testName;
    }

    public void configWebClient(final XltWebClient webClient)
    {
        setJavaScriptEnabled(webClient);
        setCssEnabled(webClient);
        setLoadStaticContent(webClient);
        setCredentials(webClient);
        setRedirectEnabled(webClient);
        setTlsVersion(webClient);
    }

    private void setTlsVersion(final XltWebClient webClient)
    {
        final String tlsVersion = getPropertyByKey(TLSVERSION);
        if (tlsVersion != null)
        {
            webClient.getOptions().setSSLClientProtocols(new String[]
                {
                    tlsVersion
                });
            XltLogger.runTimeLogger.debug(getConfigWebClient("TLSVersion", tlsVersion));
        }
    }

    private void setJavaScriptEnabled(final XltWebClient webClient)
    {
        final String property = getPropertyByKey(JAVASCRIPTENABLED);
        if (property.equalsIgnoreCase("true") || property.equalsIgnoreCase("false"))
        {
            final boolean bool = Boolean.valueOf(property);
            webClient.getOptions().setJavaScriptEnabled(bool);
            XltLogger.runTimeLogger.debug(getConfigWebClient("JavaScriptEnabled", String.valueOf(bool)));
        }
        else
        {
            throw new IllegalArgumentException(getIllegalPropertyValue(property, JAVASCRIPTENABLED));
        }
    }

    private void setCssEnabled(final XltWebClient webClient)
    {
        final String property = getPropertyByKey(CSSENABLED);

        if (property.equalsIgnoreCase("true") || property.equalsIgnoreCase("false"))
        {
            final boolean bool = Boolean.valueOf(property);
            webClient.getOptions().setCssEnabled(bool);
            XltLogger.runTimeLogger.debug(getConfigWebClient("CssEnabled", String.valueOf(bool)));
        }
        else
        {
            throw new IllegalArgumentException(getIllegalPropertyValue(property, CSSENABLED));
        }
    }

    private void setLoadStaticContent(final XltWebClient webClient)
    {
        final String property = getPropertyByKey(LOADSTATICCONTENT);

        if (property.equalsIgnoreCase("true") || property.equalsIgnoreCase("false"))
        {
            final boolean bool = Boolean.valueOf(property);
            webClient.setLoadStaticContent(bool);
            XltLogger.runTimeLogger.debug(getConfigWebClient("LoadStaticContent", String.valueOf(bool)));
        }
        else
        {
            throw new IllegalArgumentException(getIllegalPropertyValue(property, LOADSTATICCONTENT));
        }
    }

    private void setCredentials(final XltWebClient webClient)
    {
        final String name = getAuthName();
        final String password = getAuthPassword();
        if (name != null)
        {
            final String userPass = name + ":" + password;
            final String userPassBase64 = Base64.encodeBase64String(userPass.getBytes());
            webClient.addRequestHeader("Authorization", "Basic " + userPassBase64);
            XltLogger.runTimeLogger.debug(getConfigWebClient("Credentials", userPass));
        }
    }

    private String getAuthName()
    {
        final String property = getPropertyByKey(USERNAMEAUTH);
        return property;
    }

    private String getAuthPassword()
    {
        final String property = getPropertyByKey(PASSWORDAUTH);
        return property;
    }

    private void setRedirectEnabled(final XltWebClient webClient)
    {
        final String property = getPropertyByKey(REDIRECTENABLED);

        if (property.equalsIgnoreCase("true") || property.equalsIgnoreCase("false"))
        {
            final boolean bool = Boolean.valueOf(property);
            webClient.getOptions().setRedirectEnabled(bool);
            XltLogger.runTimeLogger.debug(getConfigWebClient("Redirect", String.valueOf(bool)));
        }
        else
        {
            throw new IllegalArgumentException(getIllegalPropertyValue(property, REDIRECTENABLED));
        }
    }

    public String getPropertyByKey(final String key)
    {
        final String effectiveKey = getEffectiveKey(key);
        final String property = this.xltProperties.getProperty(effectiveKey);
        return property;
    }

    public String getPropertyByKey(final String key, final String defaultValue)
    {
        final String effectiveKey = getEffectiveKey(key);
        final String property = this.xltProperties.getProperty(effectiveKey, defaultValue);
        return property;
    }

    public int getPropertyByKey(final String key, final int defaultValue)
    {
        final String effectiveKey = getEffectiveKey(key);
        final int property = this.xltProperties.getProperty(effectiveKey, defaultValue);
        return property;
    }

    public boolean getPropertyByKey(final String key, final boolean defaultValue)
    {
        final String effectiveKey = getEffectiveKey(key);
        final boolean property = this.xltProperties.getProperty(effectiveKey, defaultValue);
        return property;
    }

    public long getPropertyByKey(final String key, final long defaultValue)
    {
        final String effectiveKey = getEffectiveKey(key);
        final long property = this.xltProperties.getProperty(effectiveKey, defaultValue);
        return property;
    }

    /**
     * Returns the effective key to be used for property lookup via one of the getProperty(...) methods.
     * <p>
     * When looking up a key, "password" for example, the following effective keys are tried, in this order:
     * <ol>
     * <li>the test user name plus simple key, e.g. "TAuthor.password"</li>
     * <li>the test class name plus simple key, e.g. "com.xceptance.xlt.samples.tests.TAuthor.password"</li>
     * <li>the simple key, e.g. "password"</li>
     * </ol>
     *
     * @param bareKey
     *            the bare property key, i.e. without any prefixes
     * @return the first key that produces a result
     */
    protected String getEffectiveKey(final String bareKey)
    {
        final String effectiveKey;

        // 1. use the current user name as prefix
        final String userNameQualifiedKey = this.testName + "." + bareKey;
        if (this.xltProperties.containsKey(userNameQualifiedKey))
        {
            effectiveKey = userNameQualifiedKey;
        }
        else
        {
            // 2. use the current class name as prefix
            final String classNameQualifiedKey = this.fullTestCaseName + "." + bareKey;
            if (this.xltProperties.containsKey(classNameQualifiedKey))
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

    public XltProperties getProperties()
    {
        return this.xltProperties;
    }

    private String getIllegalPropertyValue(final String value, final String property)
    {
        final String message = MessageFormat.format("Illegal value: \"{0}\" for Property: \"{1}\"", value, property);
        return message;
    }

    private String getConfigWebClient(final String option, final String value)
    {
        final String message = MessageFormat.format("Config WebClient: \"{0}\" = \"{1}\"", option, value);
        return message;
    }
}
