package com.xceptance.xlt.common.util;

import java.util.List;

import org.apache.commons.io.FilenameUtils;

import com.xceptance.xlt.common.util.bsh.ParameterInterpreter;

public class URLActionListFacade
{
    private String filePath;

    private ParameterInterpreter interpreter;

    public URLActionListFacade(final String filePath,
                               final ParameterInterpreter interpreter)
    {
        setFilePath(filePath);
        setParameterInterpreter(interpreter);
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

    public List<URLAction> buildUrlActions()
    {
        final URLActionListBuilder builder = createBuilder();
        return builder.buildURLActions();
    }

    private URLActionListBuilder createBuilder()
    {
        final String fileNameExtension = getFileNameExtension(this.filePath);
        final URLActionListBuilder resultBuilder;

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

    private YAMLBasedURLActionListBuilder createYAMLBuilder()
    {
        final URLActionStoreBuilder storeBuilder = new URLActionStoreBuilder();
        final URLActionBuilder actionBuilder = new URLActionBuilder();
        final URLActionValidationBuilder validationBuilder = new URLActionValidationBuilder();

        final YAMLBasedURLActionListBuilder yamlBuilder = new YAMLBasedURLActionListBuilder(
                                                                                            this.filePath,
                                                                                            this.interpreter,
                                                                                            actionBuilder,
                                                                                            validationBuilder,
                                                                                            storeBuilder);
        return yamlBuilder;
    }
    private CSVBasedURLActionListBuilder createCSVBuilder(){
        
        final URLActionBuilder actionBuilder = new URLActionBuilder();
        
        final CSVBasedURLActionListBuilder csvBuilder = new CSVBasedURLActionListBuilder(this.filePath,
                                                       this.interpreter,
                                                       actionBuilder);
        return csvBuilder;
        
    }
}
