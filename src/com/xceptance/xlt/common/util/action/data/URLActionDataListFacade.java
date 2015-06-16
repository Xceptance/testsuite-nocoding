package com.xceptance.xlt.common.util.action.data;

import java.util.List;

import org.apache.commons.io.FilenameUtils;

import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.common.util.ParameterUtils;
import com.xceptance.xlt.common.util.bsh.ParameterInterpreter;

public class URLActionDataListFacade
{
    private String filePath;

    private ParameterInterpreter interpreter;

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

    public List<URLActionData> buildUrlActions()
    {
        final URLActionDataListBuilder builder = createBuilder();
        return builder.buildURLActionDataList();
    }

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
            throw new IllegalArgumentException(
                                               "Illegal file format: "
                                                   + fileNameExtension
                                                   + "\n"
                                                   + "Supported formats: '.yaml' | '.yml' or '.csv'"
                                                   + "\n");
        }
        return resultBuilder;
    }

    private String getFileNameExtension(final String filePath)
    {
        final String fileNameExtension = FilenameUtils.getExtension(filePath);
        return fileNameExtension;
    }

    private YAMLBasedURLActionDataListBuilder createYAMLBuilder()
    {
        final URLActionDataStoreBuilder storeBuilder = new URLActionDataStoreBuilder();
        final URLActionDataBuilder actionBuilder = new URLActionDataBuilder();
        final URLActionDataValidationBuilder validationBuilder = new URLActionDataValidationBuilder();

        final YAMLBasedURLActionDataListBuilder yamlBuilder = new YAMLBasedURLActionDataListBuilder(
                                                                                            this.filePath,
                                                                                            this.interpreter,
                                                                                            actionBuilder,
                                                                                            validationBuilder,
                                                                                            storeBuilder);
        return yamlBuilder;
    }
    private CSVBasedURLActionDataListBuilder createCSVBuilder(){
        
        final URLActionDataBuilder actionBuilder = new URLActionDataBuilder();
        
        final CSVBasedURLActionDataListBuilder csvBuilder = new CSVBasedURLActionDataListBuilder(this.filePath,
                                                       this.interpreter,
                                                       actionBuilder);
        return csvBuilder;
        
    }
}
