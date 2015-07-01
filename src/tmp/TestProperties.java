package tmp;

import com.xceptance.xlt.api.util.XltProperties;

public class TestProperties
{
    public static void main(final String args)
    {
        final XltProperties properties = XltProperties.getInstance();
        final String filename = properties.getProperty("filename");
        System.err.println(filename);
        System.err.println("blaaaa");
    }
}
