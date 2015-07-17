package test.com.xceptance.xlt.common.tests;

import org.junit.Test;

import com.xceptance.xlt.api.engine.Session;
import com.xceptance.xlt.common.tests.AbstractURLTestCase;
import com.xceptance.xlt.common.util.NoCodingPropAdmin;
import com.xceptance.xlt.engine.XltWebClient;

public class TAbstractURLTestCase extends AbstractURLTestCase
{
    @Test
    public void test()
    {
        final XltWebClient client = new XltWebClient();

        final NoCodingPropAdmin admin = new NoCodingPropAdmin(properties,
                                                              getTestName(),
                                                              Session.getCurrent()
                                                                     .getUserName());

        System.err.println(NoCodingPropAdmin.FILENAME
                           + " : "
                           + admin.getPropertyByKey(NoCodingPropAdmin.FILENAME));
        System.err.println(NoCodingPropAdmin.DIRECTORY
                           + " : "
                           + admin.getPropertyByKey(NoCodingPropAdmin.DIRECTORY));
        System.err.println(NoCodingPropAdmin.MODE
                           + " : "
                           + admin.getPropertyByKey(NoCodingPropAdmin.MODE));
        System.err.println(NoCodingPropAdmin.USERNAMEAUTH
                           + " : "
                           + admin.getPropertyByKey(NoCodingPropAdmin.USERNAMEAUTH));
        System.err.println(NoCodingPropAdmin.PASSWORDAUTH
                           + " : "
                           + admin.getPropertyByKey(NoCodingPropAdmin.PASSWORDAUTH));
        System.err.println(NoCodingPropAdmin.JAVASCRIPTENABLED
                           + " : "
                           + admin.getPropertyByKey(NoCodingPropAdmin.JAVASCRIPTENABLED));
        
        System.err.println(NoCodingPropAdmin.LOADSTATICCONTENT
                           + " : "
                           + admin.getPropertyByKey(NoCodingPropAdmin.LOADSTATICCONTENT));
        
        System.err.println(NoCodingPropAdmin.REDIRECTENABLED + " : "
                           + admin.getPropertyByKey(NoCodingPropAdmin.REDIRECTENABLED));

        System.err.println("getTestName() : " + getTestName());
        System.err.println("----------------------------------------------------");
        
        admin.configWebClient(client);

    }
}
