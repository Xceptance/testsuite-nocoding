package com.xceptance.xlt.common.util.action.data;

import java.util.List;

import org.apache.commons.io.FilenameUtils;

import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.common.util.ParameterUtils;
import com.xceptance.xlt.common.util.bsh.ParameterInterpreter;

/**<p>
 * Give this class a file path and it tries to construct a List<{@link #URLActionData}> 
 * from the data in the file via {@link #buildUrlActions()}. <br>
 * For this purpose it determines the file type and decides which implementation of the {@link URLActionDataListBuilder}
 * to use.
 * </p>
 * 
 * @author matthias mitterreiter
 *
 */
public class URLActionDataListFacade
{
    private String filePath;

    private ParameterInterpreter interpreter;

    /**
     * 
     * @param filePath {@link #filePath}
     * @param interpreter {@link #interpreter}
     */
    public URLActionDataListFacade(final String filePath,
                                   final ParameterInterpreter interpreter)
    {
        setFilePath(filePath);
        setParameterInterpreter(interpreter);
        XltLogger.runTimeLogger.debug("Creating new Instance");
    }

    private void setFilePath(final String filePath)
    {
        ParameterUtils.isNotNull(filePath, "filePath");
        this.filePath = filePath;
    }

    private void setParameterInterpreter(final ParameterInterpreter interpreter)
    {
        ParameterUtils.isNotNull(interpreter, "ParameterInterpreter");
        this.interpreter = interpreter;
    }

    /**
     * Builds a List<{@link #URLActionData}> from the data in {@link #filePath file}, 
     * by using an {@link URLActionDataListBuilder}.
     *  @return List<{@link URLActionData}>
     */
    public List<URLActionData> buildUrlActions()
    {
        final URLActionDataListBuilder builder = createBuilder();
        return builder.buildURLActionDataList();
    }

    /**
     * Created an implementation of the {@link URLActionDataListBuilder}, 
     * depending on the file type.
     * @return {@link URLActionDataListBuilder}
     */
    private URLActionDataListBuilder createBuilder()
    {
        final String fileNameExtension = getFileNameExtension(this.filePath);
        final URLActionDataListBuilder resultBuilder;

        if (fileNameExtension.equals("yml") || fileNameExtension.equals("yaml"))
        {
            resultBuilder = createYAMLBuilder();
        }
        else if (fileNameExtension.equals("csv"))
        {
            resultBuilder = createCSVBuilder();
        }
        else
        {
            throw new IllegalArgumentException("Illegal file type: "
                                               + "\""
                                               + fileNameExtension
                                               + "\""
                                               + "\n"
                                               + "Supported types: '.yaml' | '.yml' or '.csv'"
                                               + "\n");
        }
        return resultBuilder;
    }

    private String getFileNameExtension(final String filePath)
    {
        final String fileNameExtension = FilenameUtils.getExtension(filePath);
        return fileNameExtension;
    }

    /**
     *  Creates the YAML implementation of the {@link URLActionDataListBuilder}
     */
    private YAMLBasedURLActionDataListBuilder createYAMLBuilder()
    {
        final URLActionDataStoreBuilder storeBuilder = new URLActionDataStoreBuilder();
        final URLActionDataBuilder actionBuilder = new URLActionDataBuilder();
        final URLActionDataValidationBuilder validationBuilder = new URLActionDataValidationBuilder();

        final YAMLBasedURLActionDataListBuilder yamlBuilder = new YAMLBasedURLActionDataListBuilder(this.filePath,
                                                                                                    this.interpreter,
                                                                                                    actionBuilder,
                                                                                                    validationBuilder,
                                                                                                    storeBuilder);
        return yamlBuilder;
    }

    /**
     *  Creates the CSV implementation of the {@link URLActionDataListBuilder}
     */
    private CSVBasedURLActionDataListBuilder createCSVBuilder()
    {

        final URLActionDataBuilder actionBuilder = new URLActionDataBuilder();

        final CSVBasedURLActionDataListBuilder csvBuilder = new CSVBasedURLActionDataListBuilder(this.filePath,
                                                                                                 this.interpreter,
                                                                                                 actionBuilder);
        return csvBuilder;
    }
}
