package com.xceptance.xlt.common.util.action.data;

import java.text.MessageFormat;

import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.common.util.bsh.ParameterInterpreter;

/**
 * Helper class to build an {@link URLActionDataStore}. <br>
 * Construct a {@link URLActionDataStore} object step by step. For this fill the
 * URLActionStoreBuilder with values via setters until you want to create a
 * URLActionDataStore. For this call {@link #build()}.
 * 
 * @author matthias mitterreiter
 */

public class URLActionDataStoreBuilder
{
	private String name;

	private String selectionMode;

	private String selectionContent;

	private String subSelectionMode;

	private String subSelectionContent;

	private ParameterInterpreter interpreter;

	/**
	 * Builds an {@link URLActionDataStore} object from the values of the local
	 * attributes. If an important attribute is not set, or invalid, it throws.
	 * After execution, the values of the local attributes are reset to 'null'.
	 * 
	 * @return {@link URLActionDataStore}
	 * @throws IllegalArgumentException
	 */
	public URLActionDataStore build()
	{
		URLActionDataStore store = null;
		try
		{
			if (this.subSelectionMode == null)
			{
				store = new URLActionDataStore(getName(),
						getSelectionMode(),
						getSelectionContent(),
						getInterpreter());
			}
			else
			{

				store = new URLActionDataStore(getName(),
						getSelectionMode(),
						getSelectionContent(),
						getSubSelectionMode(),
						getSubSelectionContent(),
						getInterpreter());
			}
		}
		catch (final IllegalArgumentException e)
		{
			throw new IllegalArgumentException("Failed to create URLActionStore: " + e.getMessage(),
					e);
		}
		reset();

		return store;
	}

	/**
	 * Resets all local attribute values to 'null'.
	 */
	public void reset()
	{
		this.name = null;
		this.selectionMode = null;
		this.selectionContent = null;
		this.interpreter = null;
		this.subSelectionMode = null;
		this.subSelectionContent = null;
		XltLogger.runTimeLogger.debug("Resetting values");
	}

	public String getName()
	{
		return name;
	}

	public void setName(final String name)
	{
		this.name = name;
		XltLogger.runTimeLogger.debug(infoSetTagToValue("name", name));
	}

	public String getSelectionMode()
	{
		return selectionMode;
	}

	public void setSelectionMode(final String selectionMode)
	{
		this.selectionMode = selectionMode;
		XltLogger.runTimeLogger.debug(infoSetTagToValue("selectionMode",
				selectionMode));
	}

	public String getSelectionContent()
	{
		return selectionContent;
	}

	public void setSelectionContent(final String selectionContent)
	{
		this.selectionContent = selectionContent;
		XltLogger.runTimeLogger.debug(infoSetTagToValue("selectionContent",
				selectionContent));
	}

	public ParameterInterpreter getInterpreter()
	{
		return interpreter;
	}

	public void setInterpreter(final ParameterInterpreter interpreter)
	{
		this.interpreter = interpreter;
		XltLogger.runTimeLogger.debug(infoSetTag("interpreter"));
	}

	private String infoSetTagToValue(final String tag, final String value)
	{
		final String message = MessageFormat.format("Set tag \"{0}\" = \"{1}\" ",
				tag,
				value);
		return message;
	}

	private String infoSetTag(final String tag)
	{
		final String message = MessageFormat.format("Set tag \"{0}\" ", tag);
		return message;
	}

	public String getSubSelectionMode()
	{
		return subSelectionMode;
	}

	public void setSubSelectionMode(String subSelectionMode)
	{
		this.subSelectionMode = subSelectionMode;
		XltLogger.runTimeLogger.debug(infoSetTagToValue("subSelectionMode",
				subSelectionMode));
	}

	public String getSubSelectionContent()
	{
		return subSelectionContent;
	}

	public void setSubSelectionContent(String subSelectionContent)
	{
		this.subSelectionContent = subSelectionContent;
		XltLogger.runTimeLogger.debug(infoSetTagToValue("subSelectionContent",
				subSelectionContent));
	}

}
