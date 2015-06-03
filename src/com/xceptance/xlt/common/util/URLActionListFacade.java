package com.xceptance.xlt.common.util;

import java.util.List;

import org.apache.commons.io.FilenameUtils;

import com.xceptance.xlt.common.util.bsh.ParameterInterpreter;

public class URLActionListFacade
{
    public URLActionListFacade()
    {
    }

    public List<URLAction> buildUrlActions(final String filePath,
                                           final ParameterInterpreter interpreter)
    {
        final URLActionListBuilder builder = createBuilder(filePath, interpreter);
        return builder.buildURLActions();
    }

    private String getFileNameExtension(final String filePath)
    {
        final String fileNameExtension = FilenameUtils.getExtension(filePath);
        return fileNameExtension;
    }

    private URLActionListBuilder createBuilder(final String filePath,
                                               final ParameterInterpreter interpreter)
    {
        final String fileNameExtension = getFileNameExtension(filePath);
        final URLActionListBuilder listBuilder;
        final URLActionBuilder actionBuilder = new URLActionBuilder();

        if (fileNameExtension.equals("yml") || fileNameExtension.equals("yaml"))
        {
            final URLActionStoreBuilder storeBuilder = new URLActionStoreBuilder();
            final URLActionValidationBuilder validationBuilder = new URLActionValidationBuilder();
            listBuilder = new YAMLBasedURLActionListBuilder(filePath, interpreter,
                                                            actionBuilder,
                                                            validationBuilder,
                                                            storeBuilder);
        }
        else if (fileNameExtension.equals("csv"))
        {
            listBuilder = new CSVBasedURLActionListBuilder(filePath, interpreter,
                                                           actionBuilder);
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
        return listBuilder;
    }
}
