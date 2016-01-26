package com.xceptance.xlt.common.util.action.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang.StringUtils;

import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.common.util.bsh.ParameterInterpreter;

/**
 * Implementation of the {@link URLActionDataListBuilder} for files of type 'CSV'.
 * <ul>
 * <li>Takes a file of type csv and build a List<{@link #URLActionData}> from it.
 * <li>The structure of the data is determined within this class and described in syntax.csv
 * <li>The names of the tags, whose values should be parsed into a URLActionData are also determined here.
 * <li>If you want to change the names of the tags, you can do it easily.
 * </ul>
 * 
 * @author matthias mitterreiter
 */

public class CSVBasedURLActionDataListBuilder extends URLActionDataListBuilder
{

    // dynamic parameters
    public static final String XPATH_GETTER_PREFIX = "xpath";

    public static final String REGEXP_GETTER_PREFIX = "regexp";

    public static final int DYNAMIC_GETTER_COUNT = 10;

    /**
     * Permitted header fields, checked to avoid accidental incorrect spelling
     */
    private final static Set<String> PERMITTEDHEADERFIELDS = new HashSet<String>();

    public static final String TYPE = "Type";

    public static final String TYPE_ACTION = "A";

    public static final String TYPE_STATIC = "S";

    public static final String TYPE_DEFAULT = TYPE_ACTION;

    public static final String TYPE_XHR_ACTION = "XA";

    public static final String NAME = "Name";

    public static final String URL = "URL";

    public static final String METHOD = "Method";

    public static final String METHOD_DEFAULT = URLActionData.METHOD_GET;

    public static final String PARAMETERS = "Parameters";

    public static final String RESPONSECODE = "ResponseCode";

    public static final String RESPONSECODE_DEFAULT = "200";

    public static final String XPATH = "XPath";

    public static final String REGEXP = "RegExp";

    public static final String TEXT = "Text";

    public static final String ENCODED = "Encoded";

    public static final String ENCODED_DEFAULT = "false";

    private static final CSVFormat CSV_FORMAT;

    static
    {
        PERMITTEDHEADERFIELDS.add(TYPE);
        PERMITTEDHEADERFIELDS.add(NAME);
        PERMITTEDHEADERFIELDS.add(URL);
        PERMITTEDHEADERFIELDS.add(METHOD);
        PERMITTEDHEADERFIELDS.add(PARAMETERS);
        PERMITTEDHEADERFIELDS.add(RESPONSECODE);
        PERMITTEDHEADERFIELDS.add(XPATH);
        PERMITTEDHEADERFIELDS.add(REGEXP);
        PERMITTEDHEADERFIELDS.add(TEXT);
        PERMITTEDHEADERFIELDS.add(ENCODED);

        for (int i = 1; i <= DYNAMIC_GETTER_COUNT; i++)
        {
            PERMITTEDHEADERFIELDS.add(XPATH_GETTER_PREFIX + i);
            PERMITTEDHEADERFIELDS.add(REGEXP_GETTER_PREFIX + i);
        }

        CSV_FORMAT = CSVFormat.RFC4180.toBuilder().withIgnoreEmptyLines(true).withCommentStart('#').withHeader()
                                      .withIgnoreSurroundingSpaces(true).build();
    }

    public CSVBasedURLActionDataListBuilder(final String filePath, final ParameterInterpreter interpreter,
                                            final URLActionDataBuilder actionBuilder)
    {
        super(filePath, interpreter, actionBuilder);
    }

    private CSVParser createCSVParserFromFilepath(final String filePath)
    {
        final BufferedReader br = createBufferedReaderFromFile(new File(filePath));
        final CSVParser parser = createCSVParser(br, CSV_FORMAT);
        return parser;
    }

    private void checkHeaderSpelling(final Map<String, Integer> headerMap)
    {
        for (final String headerField : headerMap.keySet())
        {
            if (!isPermittedHeaderField(headerField))
            {
                throw new IllegalArgumentException(MessageFormat.format("Unsupported or misspelled header field: {0}", headerField));
            }
        }
    }

    @Override
    protected Object parseData() throws IOException
    {
        final CSVParser parser = createCSVParserFromFilepath(this.filePath);
        checkHeaderSpelling(parser.getHeaderMap());

        return new ArrayList<>(parser.getRecords());
    }

    @Override
    public List<URLActionData> buildURLActionDataList()
    {
        final List<URLActionData> actions = new ArrayList<URLActionData>();

        @SuppressWarnings("unused")
        boolean incorrectLines = false;

        try
        {
            @SuppressWarnings("unchecked")
            final List<CSVRecord> records = (List<CSVRecord>) getOrParseData();
            for (final CSVRecord csvRecord : records)
            {

                if (csvRecord.isConsistent())
                {
                    final URLActionData action = buildURLActionData(csvRecord);
                    actions.add(action);
                }
                else
                {
                    XltLogger.runTimeLogger.error(new StringBuilder("Line at ").append(csvRecord.getRecordNumber())
                                                                               .append(" has not been correctly formatted. Line is ignored: ")
                                                                               .append(csvRecord).toString());
                    incorrectLines = true;
                }
            }

        }
        catch (final Exception e)
        {
            throw new IllegalArgumentException(new StringBuilder("Failed to parse '").append(filePath).append("' as CSV data").toString(),
                                               e);
        }
        return actions;
    }

    private URLActionData buildURLActionDataFromCSVRecord(final CSVRecord csvRecord)
    {
        final String urlString = csvRecord.get(URL);
        if (urlString == null)
        {
            throw new IllegalArgumentException("url null");
        }

        final String name = StringUtils.defaultIfBlank(csvRecord.get(NAME), "Action-" + (csvRecord.getRecordNumber() - 1));

        final String type = StringUtils.defaultIfBlank(csvRecord.get(TYPE), TYPE_DEFAULT);
        final String method = StringUtils.defaultIfBlank(csvRecord.get(METHOD), METHOD_DEFAULT);
        final String encoded = StringUtils.defaultIfBlank(csvRecord.get(ENCODED), ENCODED_DEFAULT);

        final String parametersString = csvRecord.get(PARAMETERS);

        final String responseCode = StringUtils.defaultIfBlank(csvRecord.get(RESPONSECODE), RESPONSECODE_DEFAULT);

        List<NameValuePair> parameters = new ArrayList<NameValuePair>();

        if (parametersString != null)
        {
            parameters = setupParameters(parametersString);
        }
        final URLActionData action = new URLActionData(name, urlString, this.interpreter);
        if (TYPE_ACTION.equals(type))
        {
            action.setType(URLActionData.TYPE_ACTION);
        }
        else if (TYPE_STATIC.equals(type))
        {
            action.setType(URLActionData.TYPE_STATIC);
        }
        else if (TYPE_XHR_ACTION.equals(type))
        {
            action.setType(URLActionData.TYPE_XHR);
        }
        action.setMethod(method);
        action.setHttpResponceCode(responseCode);
        action.setEncodeParameters("true".equals(encoded));
        action.setParameters(parameters);

        return action;
    }

    private List<URLActionDataValidation> buildURLActionDataValidations(final CSVRecord csvRecord)
    {
        final List<URLActionDataValidation> resultList = new ArrayList<URLActionDataValidation>();

        String selectionMode = null;
        String selectionValue = null;
        String validationMode = null;
        String validationValue = null;

        final String regexpString = csvRecord.isSet(REGEXP) ? csvRecord.get(REGEXP) : null;
        final String xPath = csvRecord.isSet(XPATH) ? csvRecord.get(XPATH) : null;

        final String text = csvRecord.get(TEXT);

        if (StringUtils.isNotBlank(regexpString))
        {
            selectionMode = URLActionDataValidation.REGEXP;
            selectionValue = regexpString;
        }
        else if (StringUtils.isNotBlank(xPath))
        {
            selectionMode = URLActionDataValidation.XPATH;
            selectionValue = xPath;
        }

        if (text != null)
        {
            validationMode = URLActionDataValidation.MATCHES;
            validationValue = text;
        }
        else
        {
            validationMode = URLActionDataValidation.EXISTS;
        }

        if (selectionMode != null && selectionValue != null)
        {
            final URLActionDataValidation validation = new URLActionDataValidation("valdiation", selectionMode, selectionValue,
                                                                                   validationMode, validationValue, this.interpreter);
            resultList.add(validation);

        }

        if (StringUtils.isNotBlank(regexpString) && StringUtils.isNotBlank(xPath))
        {
            throw new IllegalArgumentException("naaaaaaaaaaaaaa");
        }

        return resultList;
    }

    private List<URLActionDataStore> buildURLActionDataStoreVariables(final CSVRecord csvRecord)
    {
        final List<URLActionDataStore> resultList = new ArrayList<URLActionDataStore>();

        for (int i = 1; i <= DYNAMIC_GETTER_COUNT; i++)
        {
            String key = XPATH_GETTER_PREFIX + i;
            final String xpath = csvRecord.isSet(key) ? csvRecord.get(key) : null;
            if (StringUtils.isNotBlank(xpath))
            {
                final URLActionDataStore storeItem = new URLActionDataStore(key, URLActionDataStore.XPATH, xpath, this.interpreter);
                resultList.add(storeItem);
            }

            key = REGEXP_GETTER_PREFIX + i;
            final String regexp = csvRecord.isSet(key) ? csvRecord.get(key) : null;
            if (StringUtils.isNotBlank(regexp))
            {
                final URLActionDataStore storeItem = new URLActionDataStore(REGEXP_GETTER_PREFIX + i, URLActionDataStore.REGEXP, regexp,
                                                                            this.interpreter);
                resultList.add(storeItem);
            }
        }
        return resultList;
    }

    private URLActionData buildURLActionData(final CSVRecord csvRecord)
    {
        final URLActionData action = buildURLActionDataFromCSVRecord(csvRecord);
        final List<URLActionDataValidation> validations = buildURLActionDataValidations(csvRecord);
        final List<URLActionDataStore> store = buildURLActionDataStoreVariables(csvRecord);

        action.setValidations(validations);
        action.setStore(store);

        return action;
    }

    /**
     * Takes the list of parameters and turns it into name value pairs for later usage.
     * 
     * @param paramers
     *            the csv definition string of parameters
     * @return a list with parsed key value pairs
     * @throws UnsupportedEncodingException
     */
    private List<NameValuePair> setupParameters(final String parameters)
    {
        final List<NameValuePair> list = new ArrayList<NameValuePair>();

        // ok, turn them into & split strings
        final StringTokenizer tokenizer = new StringTokenizer(parameters, "&");
        while (tokenizer.hasMoreTokens())
        {
            final String token = tokenizer.nextToken();

            // the future pair
            String key = null;
            String value = null;

            // split it into key and value at =
            final int pos = token.indexOf("=");
            if (pos >= 0)
            {
                key = token.substring(0, pos);
                if (pos < token.length() - 1)
                {
                    value = token.substring(pos + 1);
                }
            }
            else
            {
                key = token;
            }
            if (key != null)
            {
                list.add(new NameValuePair(key, value));
            }
        }
        return list;
    }

    private boolean isPermittedHeaderField(final String fieldName)
    {
        return PERMITTEDHEADERFIELDS.contains(fieldName);
    }

    private BufferedReader createBufferedReaderFromFile(final File file)
    {
        try
        {
            return new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
        }
        catch (UnsupportedEncodingException | FileNotFoundException e)
        {
            throw new IllegalArgumentException("Failed to create a BufferedReader, because:" + e.getMessage());
        }
    }

    private CSVParser createCSVParser(final BufferedReader br, final CSVFormat csvFormat)
    {

        CSVParser parser = null;
        try
        {
            parser = new CSVParser(br, csvFormat);
        }
        catch (final IOException e)
        {
            throw new IllegalArgumentException("Failed to create a CSVParser, because:" + e.getMessage());
        }
        return parser;
    }
}
