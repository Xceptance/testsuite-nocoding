package posters.tests.nocoding;

import org.junit.runner.JUnitCore;

public class ExecuteAllTests
{
    public static void main(String[] args) throws Exception {
        JUnitCore.main("posters.tests.nocoding.TDOrder", "posters.tests.nocoding.TDHomepageAndStaticCSV",
                "posters.tests.nocoding.TDOrderCSV", "posters.tests.nocoding.TLExampleSubSelection",
                "posters.tests.nocoding.TLHomepageAndStaticCSV", "posters.tests.nocoding.TLLogin",
                "posters.tests.nocoding.TLOrderCSV", "posters.tests.nocoding.TLRegister", "posters.tests.nocoding.TLSearchCSV");
 }
}
