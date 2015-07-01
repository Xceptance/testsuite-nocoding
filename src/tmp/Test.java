package tmp;

import java.io.File;

import com.xceptance.xlt.api.engine.Session;
import com.xceptance.xlt.api.util.XltProperties;
import com.xceptance.xlt.common.XltConstants;


public class Test
{
    
    public static void main(final String args[])
    {
        final XltProperties properties = XltProperties.getInstance();
        final String filename = properties.getProperty("filename", Session.getCurrent().getUserName() + ".csv");
        final String dataDirectory = properties.getProperty(XltConstants.XLT_PACKAGE_PATH
                                                 + ".data.directory",
                                             "config" + File.separatorChar + "data");
        System.err.println(filename);
        System.err.println(dataDirectory);
    }
}
