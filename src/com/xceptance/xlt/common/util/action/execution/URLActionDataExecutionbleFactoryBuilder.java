package com.xceptance.xlt.common.util.action.execution;

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Set;

import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.api.util.XltProperties;
import com.xceptance.xlt.common.util.ParameterUtils;

/**
 * Builder <br>
 * Creates a {@link URLActionDataExecutionableFactory}, depending on the {@link #mode}.
 * 
 * @author matthias mitterreiter
 */
public class URLActionDataExecutionbleFactoryBuilder
{

    /**
     * mode of execution
     */
    private String mode;

    private XltProperties properties;

    public static final String MODE_DOM = "dom";

    public static final String MODE_LIGHT = "light";

    public static final Set<String> PERMITTEDMODES = new HashSet<String>();

    static
    {
        PERMITTEDMODES.add(MODE_DOM);
        PERMITTEDMODES.add(MODE_LIGHT);
    }

    /**
     * @param properties
     *            {@link XltProperties}
     * @param mode
     *            {@link mode}
     */
    public URLActionDataExecutionbleFactoryBuilder(final XltProperties properties,
                                                 final String mode)
    {
        setMode(mode);
        setProperties(properties);
        XltLogger.runTimeLogger.debug("Creating new Instance");
    }

    /**
     * builds a {@link URLActionDataExecutionableFactory} depending on the {@link #mode}, set in the Constructor.
     * @return {@link URLActionDataExecutionableFactory}
     */
    public URLActionDataExecutionableFactory buildFactory()
    {
        final URLActionDataExecutionableFactory factory = produceFactory();
        return factory;
    }

    private void setProperties(final XltProperties properties)
    {
        ParameterUtils.isNotNull(properties, "XltProperties");
        this.properties = properties;
    }

    private void setMode(final String mode)
    {
        if (isPermittedMode(mode))
        {
            this.mode = mode;
        }
        else
        {
            throw new IllegalArgumentException(MessageFormat.format("Running mode : \"{0}\" is not supported!",
                                                                    mode));
        }
    }

    private URLActionDataExecutionableFactory produceFactory()
    {

        final URLActionDataExecutionableFactory resultFactory;

        if (this.mode.equals(MODE_DOM))
        {
            resultFactory = createHtmlPageActionFactory();
        }
        else if (this.mode.equals(MODE_LIGHT))
        {
            resultFactory = createLightWeightPageActionFactory();
        }
        else
        {
            throw new IllegalArgumentException("THIS WILL NEVER HAPPEN :D");
        }
        return resultFactory;
    }

    private HtmlPageActionFactory createHtmlPageActionFactory()
    {
        return new HtmlPageActionFactory(properties);
    }

    private LightWeightPageActionFactory createLightWeightPageActionFactory()
    {
        return new LightWeightPageActionFactory(properties);
    }

    private boolean isPermittedMode(final String item)
    {
        return PERMITTEDMODES.contains(item);
    }
}
