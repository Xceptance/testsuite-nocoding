package tmp;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import org.apache.http.auth.Credentials;

import com.gargoylesoftware.htmlunit.DefaultCredentialsProvider;
import com.gargoylesoftware.htmlunit.FormEncodingType;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.util.NameValuePair;

public class TestWebClient
{
    public static void main(final String args[]) throws MalformedURLException
    {
        final String urlString = "http://www.google.de";
        
        final URL url = new URL(urlString);
        final WebClient client = new WebClient();

        final DefaultCredentialsProvider provider = new DefaultCredentialsProvider();
        provider.addCredentials("matthias", "matthiaspassword");
        client.setCredentialsProvider(provider);
        
        
        final WebRequest request = new WebRequest(url);
        request.setAdditionalHeader("Cookie", "cookie_Header = v1; cookie_Header = v2");
        
        loadAndPrint(request, client);

    }

    public static void loadAndPrint(final WebRequest request, final WebClient client)
    {
        System.err.println("Before");
        printRequest(request);
        Page page;
        try
        {
            page = client.getPage(request);
            final WebResponse response = page.getWebResponse();
            System.err.println("After");
            final WebRequest responseRequest = response.getWebRequest();
            printRequest(responseRequest);
        }
        catch (final Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void printRequest(final WebRequest request)
    {
        System.err.println("------------Webrequest------------");
        final String url = request.getUrl().toString();
        if (url != null)
        {
            System.err.println("URL: " + url);
        }
        final HttpMethod method = request.getHttpMethod();
        if (method != null)
        {
            System.err.println("Method : " + method);
        }
        final String charSet = request.getCharset();
        if (charSet != null)
        {
            System.err.println("Chraset: " + charSet);
        }
        final String proxy = request.getProxyHost();
        if (proxy != null)
        {
            System.err.println("Proxy: " + proxy);
        }
        final String body = request.getRequestBody();
        if (body != null)
        {
            System.err.println("Body: " + body);
        }
        final FormEncodingType encoding = request.getEncodingType();
        if (encoding != null)
        {
            final String encodingString = encoding.toString();
            System.err.println("Encoding: " + encodingString);
        }
        final URL originalUrl = request.getOriginalURL();
        if (originalUrl != null)
        {
            final String originalUrlString = originalUrl.toString();
            System.err.println("OriginalUrl: " + originalUrlString);
        }
        final List<NameValuePair> parameters = request.getRequestParameters();
        if (!(parameters == null))
        {
            System.err.println("Parameters: ");
            for (final NameValuePair parameter : parameters)
            {
                System.err.println("\t" + parameter.getName() + " = "
                                   + parameter.getValue());
            }
        }
        final Map<String, String> headers = request.getAdditionalHeaders();
        if (!(headers == null))
        {
            System.err.println("Headers: ");
            for (final Map.Entry<String, String> entry : headers.entrySet())
            {
                System.err.println("\t" + entry.getKey() + " = " + entry.getValue());
            }
        }

        final Credentials credentials = request.getCredentials();
        if (credentials != null)
        {
            System.err.println("Credentials: " + credentials.toString());
        }

    }
}
