package com.xceptance.xlt.common.util.action.execution;

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Set;

import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.api.util.XltProperties;
import com.xceptance.xlt.common.util.NoCodingPropAdmin;
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

    private NoCodingPropAdmin propAdmin;

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
    public URLActionDataExecutionbleFactoryBuilder(final NoCodingPropAdmin propAdmin, final String mode)
    {
        setMode(mode);
        setPropertiesAdmin(propAdmin);
        XltLogger.runTimeLogger.debug("Creating new Instance");
    }

    /**
     * builds a {@link URLActionDataExecutionableFactory} depending on the {@link #mode}, set in the Constructor.
     * 
     * @return {@link URLActionDataExecutionableFactory}
     */
    public URLActionDataExecutionableFactory buildFactory()
    {
        final URLActionDataExecutionableFactory factory = produceFactory();
        return factory;
    }

    private void setPropertiesAdmin(final NoCodingPropAdmin propAdmin)
    {
        ParameterUtils.isNotNull(propAdmin, "NoCodingPropAdmin");
        this.propAdmin = propAdmin;
    }

    private void setMode(final String mode)
    {
        if (isPermittedMode(mode))
        {
            this.mode = mode;
        }
        else
        {
            throw new IllegalArgumentException(MessageFormat.format("Running mode : \"{0}\" is not supported!", mode));
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
        return new HtmlPageActionFactory(this.propAdmin);
    }

    private LightWeightPageActionFactory createLightWeightPageActionFactory()
    {
        return new LightWeightPageActionFactory(this.propAdmin);
    }

    private boolean isPermittedMode(final String item)
    {
        return PERMITTEDMODES.contains(item);
    }

	public String getMode() {
		return mode;
	}

	public NoCodingPropAdmin getPropAdmin() {
		return propAdmin;
	}
}
