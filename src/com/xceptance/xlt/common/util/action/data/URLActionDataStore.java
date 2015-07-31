package com.xceptance.xlt.common.util.action.data;

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.jdt.annotation.Nullable;

import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.common.util.bsh.ParameterInterpreter;

/**
 * <p>
 * Data container which holds all the necessary information to grab and store
 * information out of a Http response in order to provide access to them via the
 * {@link #interpreter}.
 * </p>
 * 
 * @author matthias
 *
 */
public class URLActionDataStore
{
	/**
	 * identifier for the variable
	 */
	private String name;

	/**
	 * the way you want to take information out of the http response. <br>
	 * See {@link #PERMITTEDSELECTIONMODE supported modes}.
	 */
	private String selectionMode;

	/**
	 * the individual selection specification.
	 */
	private String selectionContent;

	private String subSelectionMode;

	private String subSelectionValue;

	/**
	 * The interpreter for dynamic parameter interpretation.
	 */
	private ParameterInterpreter interpreter;

	/**
	 * Supported selection modes:
	 * <ul>
	 * <li> {@link #XPATH}
	 * <li> {@link #REGEXP}
	 * <li> {@link #HEADER}
	 * <li> {@link #COOKIE}
	 * </ul>
	 */
	public final static Set<String> PERMITTEDSELECTIONMODE = new HashSet<String>();

	public static final String XPATH = "XPath";

	public static final String REGEXP = "Regex";

	public static final String HEADER = "Header";

	public static final String COOKIE = "Cookie";

	static
	{
		PERMITTEDSELECTIONMODE.add(XPATH);
		PERMITTEDSELECTIONMODE.add(REGEXP);
		PERMITTEDSELECTIONMODE.add(HEADER);
		PERMITTEDSELECTIONMODE.add(COOKIE);
	}

	/**
	 * Supported sub selection modes:
	 * <ul>
	 * <li> {@link #REGEXGROUP}
	 * </ul>
	 */
	
	public final static Set<String> PERMITTEDSUBSELECTIONMODE = new HashSet<String>();
	
	/**
	 * Capturing group for regex
	 */
	public static final String REGEXGROUP = "Group";
	
	static
	{
		PERMITTEDSUBSELECTIONMODE.add(REGEXGROUP);
	}
	
	/**
	 * Takes the minimal set of parameters that are necessary to select and
	 * store information <br>
	 * from a http response.
	 * 
	 * @param name
	 *            {@link #name}
	 * @param selectionMode
	 *            {@link #selectionMode}
	 * @param selectionContent
	 *            {@link #selectionContent}
	 * @param interpreter
	 *            {@link #interpreter}
	 */
	public URLActionDataStore(final String name,
			final String selectionMode,
			final String selectionContent,
			final ParameterInterpreter interpreter)
	{
		XltLogger.runTimeLogger.debug("Creating new Instance");
		setName(name);
		setSelectionMode(selectionMode);
		setSelectionContent(selectionContent);
		setParameterInterpreter(interpreter);
	}

	public URLActionDataStore(final String name,
			final String selectionMode,
			final String selectionContent,
			final String subSelectionMode,
			final String subSelectionValue,
			final ParameterInterpreter interpreter)
	{
		XltLogger.runTimeLogger.debug("Creating new Instance");
		setName(name);
		setSelectionMode(selectionMode);
		setSelectionContent(selectionContent);
		setSubSelectionMode(subSelectionMode);
		setSubSelectionValue(subSelectionValue);
		setParameterInterpreter(interpreter);
	}

	/**
	 * For debugging purpose. <br>
	 * 'err-streams' the attributes of the object without dynamic interpretation
	 * of the return values. <br>
	 */
	public void outlineRaw()
	{
		System.err.println("\t\t" + this.name);
		System.err.println("\t\t\t" + this.selectionMode
				+ " : "
				+ this.selectionContent);
	}

	/**
	 * For debugging purpose. <br>
	 * 'err-streams' the attributes of the object. Interprets the values via
	 * {@link #interpreter} <br>
	 */
	public void outline()
	{
		System.err.println("\t\t" + getName());
		System.err.println("\t\t\t" + getSelectionMode()
				+ " : "
				+ getSelectionContent());
	}

	/**
	 * @param interpreter
	 *            : if NULL throws.
	 * @throws IllegalArgumentException
	 */
	private void
			setParameterInterpreter(final ParameterInterpreter interpreter)
	{
		this.interpreter = (interpreter != null) ? interpreter
				: (ParameterInterpreter) throwIllegalArgumentException(getTagCannotBeNullMessage("ParameterInterpreter"));
		XltLogger.runTimeLogger.debug("Set 'ParameterInterpreter'");

	}

	/**
	 * @param selectionContent
	 *            : if NULL throws.
	 * @throws IllegalArgumentException
	 */
	public void setSelectionContent(final String selectionContent)
	{
		this.selectionContent = (selectionContent != null) ? selectionContent
				: (String) throwIllegalArgumentException(getTagCannotBeNullMessage("Selection Content"));
		XltLogger.runTimeLogger.debug(MessageFormat.format("Set 'Selection Content': \"{0}\"",
				selectionContent));
	}

	/**
	 * @param subSelectionMode
	 *            :if NULL throws.
	 * @throws IllegalArgumentException
	 */
	public void setSubSelectionMode(final String subSelectionMode)
	{
		this.subSelectionMode = (subSelectionMode != null) ? subSelectionMode
				: (String) throwIllegalArgumentException(getTagCannotBeNullMessage("Sub-Selection Mode"));
		XltLogger.runTimeLogger.debug(MessageFormat.format("Set 'Sub-Selection Mode': \"{0}\"",
				subSelectionMode));
	}
	/**
	 * @param subSelectionValue
	 *            :if NULL throws.
	 * @throws IllegalArgumentException
	 */
	public void setSubSelectionValue(final String subSelectionValue)
	{
		this.subSelectionValue = (subSelectionValue != null) ? subSelectionValue
				: (String) throwIllegalArgumentException(getTagCannotBeNullMessage("Sub-Selection Value"));
		XltLogger.runTimeLogger.debug(MessageFormat.format("Set 'Sub-Selection Value': \"{0}\"",
				subSelectionValue));
	}
	/**
	 * @param selectionMode
	 *            :if NULL throws.
	 * @throws IllegalArgumentException
	 */
	public void setSelectionMode(final String selectionMode)
	{
		this.selectionMode = (selectionMode != null) ? selectionMode
				: (String) throwIllegalArgumentException(getTagCannotBeNullMessage("Selection Mode"));
		XltLogger.runTimeLogger.debug(MessageFormat.format("Set 'Selection Mode': \"{0}\"",
				selectionMode));
	}

	/**
	 * @param name
	 *            :if NULL throws.
	 * @throws IllegalArgumentException
	 */
	public void setName(final String name)
	{
		this.name = (name != null) ? name
				: (String) throwIllegalArgumentException("\"Name\" cannot be null");
		XltLogger.runTimeLogger.debug(MessageFormat.format("Set 'Name' to \"{0}\"",
				name));
	}

	/**
	 * @return {@link #name}, after its dynamic interpretation via the
	 *         {@link #interpreter}.
	 */
	public String getName()
	{
		return interpreter.processDynamicData(this.name);
	}

	/**
	 * @return {@link #selectionMode }, after its dynamic interpretation via the
	 *         {@link #interpreter}.
	 */
	public String getSelectionMode()
	{
		final String dynamicSelectionMode = interpreter.processDynamicData(this.selectionMode);
		if (!isPermittedSelectionMode(dynamicSelectionMode))
		{
			throw new IllegalArgumentException(getIllegalValueForTagMessage(dynamicSelectionMode,
					"Selection Mode"));
		}
		return dynamicSelectionMode;
	}
	
	/**
	 * @return {@link #subSelectionMode }, after its dynamic interpretation via the
	 *         {@link #interpreter}.
	 */
	@Nullable
	public String getSubSelectionMode()
	{
		String dynamicSubSelectionMode = null;
		if(this.subSelectionMode != null)
		{
			dynamicSubSelectionMode = interpreter.processDynamicData(this.subSelectionMode);
			if (!isPermittedSubSelectionMode(dynamicSubSelectionMode))
			{
				throw new IllegalArgumentException(getIllegalValueForTagMessage(dynamicSubSelectionMode,
						"Sub-Selection Mode"));
			}
		}
		return dynamicSubSelectionMode;
	}

	/**
	 * @return {@link #selectionContent }, after its dynamic interpretation via
	 *         the {@link #interpreter}.
	 */
	@Nullable
	public String getSelectionContent()
	{
		return interpreter.processDynamicData(this.selectionContent);
	}
	
	/**
	 * @return {@link #selectionContent }, after its dynamic interpretation via
	 *         the {@link #interpreter}.
	 */
	@Nullable
	public String getSubSelectionContent()
	{
		return interpreter.processDynamicData(this.subSelectionValue);
	}

	/**
	 * @return {@link #interpreter}
	 */
	public ParameterInterpreter getInterpreter()
	{
		return this.interpreter;
	}

	/**
	 * @param selectionMode
	 * @return if ({@link #selectionMode} is permitted) ? true : false.
	 */
	public boolean isPermittedSelectionMode(final String selectionMode)
	{
		return PERMITTEDSELECTIONMODE.contains(selectionMode);
	}
	/**
	 * @param subSelectionMode
	 * @return if ({@link #selectionMode} is permitted) ? true : false.
	 */
	public boolean isPermittedSubSelectionMode(final String subSelectionMode)
	{
		return PERMITTEDSUBSELECTIONMODE.contains(subSelectionMode);
	}
	
	/**
	 * 
	 * @return whether there is a subSelectionMode
	 */
	public boolean hasSubSelection()
	{
		return this.subSelectionMode != null;
	}

	/**
	 * Dirty way of throwing a IllegalArgumentException with the passed message.
	 * 
	 * @param message
	 * @return nothing.
	 */
	private Object throwIllegalArgumentException(final String message)
	{
		throw new IllegalArgumentException(message);
	}

	/**
	 * 
	 * @param tag
	 * @return Formated message,
	 */
	private String getTagCannotBeNullMessage(final String tag)
	{
		final String message = MessageFormat.format("Store: \"{0}\", tag \"{1}\"  cannot be NULL",
				this.name,
				tag);
		return message;
	}

	/**
	 * 
	 * @param value
	 * @param tag
	 * @return Formated message.
	 */
	private String getIllegalValueForTagMessage(final String value,
			final String tag)
	{
		final String message = MessageFormat.format("Store: \"{0}\", Illegal value: \"{1}\" for tag \"{2}\"",
				this.name,
				value,
				tag);
		return message;
	}
}
