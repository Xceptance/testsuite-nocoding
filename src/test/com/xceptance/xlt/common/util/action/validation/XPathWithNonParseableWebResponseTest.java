package test.com.xceptance.xlt.common.util.action.validation;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import test.com.xceptance.xlt.common.util.MockWebResponse;

import com.gargoylesoftware.htmlunit.WebResponse;
import com.xceptance.xlt.common.util.action.validation.XPathWithNonParseableWebResponse;

public class XPathWithNonParseableWebResponseTest
{
    String urlString = "http://www.xceptance.com";

    URL url;

    WebResponse jsonResponse;

    String jsonType = "text/json";

    String jsonContent = "{" 
        + "  \"geodata\": [" 
        + "    {" 
        + "      \"id\": \"1\"," 
        + "      \"name\": \"Julie Sherman\","                  
        + "      \"gender\" : \"female\"," 
        + "      \"latitude\" : \"37.33774833333334\"," 
        + "      \"longitude\" : \"-121.88670166666667\""
        + "    }," 
        + "    {" 
        + "      \"id\": \"2\"," 
        + "      \"name\": \"Johnny Depp\","          
        + "      \"gender\" : \"male\"," 
        + "      \"latitude\" : \"37.336453\"," 
        + "      \"longitude\" : \"-121.884985\""
        + "    }" 
        + "  ]" 
        + "}"; 

    WebResponse xmlResponse;

    String xmlType = "text/xml";

    String xmlContent = "<data>" + " <employee>" + "   <name>John</name>"
                        + "   <title>Manager</title>" + " </employee>" + " <employee>"
                        + "   <name>Sara</name>" + "   <title>Clerk</title>"
                        + " </employee>" + "</data>";

    WebResponse unknownTypeResponse;

    WebResponse maliciousJsonContentResponse;

    String maliciousJsonContent = "{" 
        + "  \"geodata\": [" 
        + "    {" 
        + "      \"id\": \"1\"," 
        + "      \"name\": \"Julie Sherman\","                  
        + "      \"gender\" : \"female\"," 
        + "      \"latitude\" : \"37.33774833333334\"," 
        + "      \"longitude\" : \"-121.88670166666667\""
        + "    }," 
        + "    {" 
        + "      \"id\": \"2\"," 
        + "      \"name\": \"Johnny Depp\","          
        + "      \"gender\" : \"male\"," 
        + "      \"latitude\" : \"37.336453\"," 
        + "      \"longitude\" : \"-121.884985\""
        + "    }" 
        + "  ]";

    WebResponse maliciousXmlContentResponse;

    String maliciousXmlContent = "<data>" +
        " <employee>" +
        "   <name>John</name>" +
        "   <title>Manager</title>" +
        " </employee>" +
        " <employee>" +
        "   <name>Sara</name>" +
        "   <title>Clerk</title>" +
        " </employee>";

    @Before
    public void setup() throws MalformedURLException
    {
        url = new URL(urlString);

        jsonResponse = new MockWebResponse(jsonContent, url, jsonType);

        xmlResponse = new MockWebResponse(xmlContent, url, xmlType);

        unknownTypeResponse = new MockWebResponse("", url, "unknown");

        maliciousJsonContentResponse = new MockWebResponse(maliciousJsonContent, url,
                                                           jsonType);

        maliciousXmlContentResponse = new MockWebResponse(maliciousXmlContent, url,
                                                           xmlType);
    }

    @Test
    public void testConstructorWithJsonResponse()
    {
        @SuppressWarnings("unused")
		final XPathWithNonParseableWebResponse xpathResponse = new XPathWithNonParseableWebResponse(
                                                                                                    jsonResponse);
    }

    @Test
    public void testConstructorWithXmlResponse()
    {
        @SuppressWarnings("unused")
		final XPathWithNonParseableWebResponse xpathResponse = new XPathWithNonParseableWebResponse(
                                                                                                    xmlResponse);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithUUnsupportedResponseType()
    {
        @SuppressWarnings("unused")
		final XPathWithNonParseableWebResponse xpathResponse = new XPathWithNonParseableWebResponse(
                                                                                                    unknownTypeResponse);
    }
    
    @Test
    public void testGetByXPathWithJson()
    {
        final XPathWithNonParseableWebResponse xpathResponse = new XPathWithNonParseableWebResponse(
                                                                                                    jsonResponse);
        final List<String> list = xpathResponse.getByXPath("//latitude");
        final String lat0 = list.get(0);
        final String lat1 = list.get(1);
        Assert.assertEquals("37.33774833333334", lat0);
        Assert.assertEquals("37.336453", lat1);
    }
    @Test
    public void testGetByXPathWithXML()
    {
        final XPathWithNonParseableWebResponse xpathResponse = new XPathWithNonParseableWebResponse(
                                                                                                    xmlResponse);
        final List<String> list = xpathResponse.getByXPath("//title");
        final String tit0 = list.get(0);
        final String tit1 = list.get(1);
        Assert.assertEquals("Manager", tit0);
        Assert.assertEquals("Clerk", tit1);
    }
    @Test(expected = IllegalArgumentException.class)
    public void testGetByXPathWithMaliciousJson()
    {
        final XPathWithNonParseableWebResponse xpathResponse = new XPathWithNonParseableWebResponse(
                                                                                                    maliciousJsonContentResponse);
        @SuppressWarnings("unused")
		final List<String> list = xpathResponse.getByXPath("//latitude");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetByXPathWithMaliciousXml()
    {
        final XPathWithNonParseableWebResponse xpathResponse = new XPathWithNonParseableWebResponse(
                                                                                                    maliciousXmlContentResponse);
        @SuppressWarnings("unused")
		final List<String> list = xpathResponse.getByXPath("//title");
    }

}
