package tmp;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.gargoylesoftware.htmlunit.util.UrlUtils;
import com.xceptance.common.lang.StringUtils;

public class TestUrl
{
    public String urlString = "http://en.wikipedia.org/w/index.php?title=Percent-encoding&action=edit";

    public static void main(final String args[])
        throws MalformedURLException, UnsupportedEncodingException
    {
        final NameValuePair parameter_1 = new NameValuePair("parameter_Name_1",
                                                            "parameter_Value_1");
        final NameValuePair parameter_2 = new NameValuePair("parameter_Name_2",
                                                            "parameter_Value_2");
        final NameValuePair parameter_1_encoded = new NameValuePair(
                                                                    URLEncoder.encode("parameter&_Name_1",
                                                                                      "UTF-8"),
                                                                    URLEncoder.encode("parameter&_Value_1",
                                                                                      "UTF-8"));
        final NameValuePair parameter_2_encoded = new NameValuePair(
                                                                    URLEncoder.encode("parameter_Name_2",
                                                                                      "UTF-8"),
                                                                    URLEncoder.encode("parameter_Value_2",
                                                                                      "UTF-8"));

        final List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        final List<NameValuePair> parametersEncoded = new ArrayList<NameValuePair>();

        parameters.add(parameter_1);
        parameters.add(parameter_2);

        parametersEncoded.add(parameter_1_encoded);
        parametersEncoded.add(parameter_2_encoded);

        final String urlString = "http://en.wikipedia.org/w/index.php?title=Percent-encoding&action=edit";
        final URL url = new URL(urlString);

        final WebRequest requestGet = new WebRequest(url);
        requestGet.setHttpMethod(HttpMethod.GET);

        final WebRequest requestPost = new WebRequest(url);
        requestPost.setHttpMethod(HttpMethod.POST);

        TestUrl.addParametersToUrl(requestGet, parameters);

        final String urlStrin2 = "http://www.google.de";

        final WebRequest request = new WebRequest(new URL(urlStrin2));

        final String name = URLEncoder.encode("Target:", "UTF-8");
        final String value = URLEncoder.encode("http://google.com/resource?key=value1 & value2",
                                               "UTF-8");
        final NameValuePair parameter = new NameValuePair(name, value);
        final List<NameValuePair> ps = new ArrayList<NameValuePair>();
        ps.add(parameter);

        addParametersToUrl(request, ps);

        final URL url2 = request.getUrl();
        System.err.println(url2.toString());
        
        System.err.println(request.getEncodingType());
        System.err.println(request.toString());
    }

    public static void addParametersToUrl(final WebRequest request,
                                          final List<NameValuePair> parameters)
        throws MalformedURLException
    {
        String urlString = request.getUrl().toString();
        urlString = StringUtils.replace(urlString, "&amp;", "&");
        final URL newUrl = new URL(urlString);
        final String oldQueryString = newUrl.getQuery();
        final StringBuilder newQueryString = new StringBuilder();

        final boolean insertLeadingAmp = oldQueryString != null
                                         && !oldQueryString.isEmpty();

        if (insertLeadingAmp)
        {
            newQueryString.append(oldQueryString);
        }
        for (int index = 0; index < parameters.size(); index++)
        {
            final NameValuePair nameValuePair = parameters.get(index);
            /*
             * For each pair except the first, an & has to be appended. For the first pair it depends on whether there
             * is already a query (in which case insertLeadingAmp is true).
             */
            if (index > 0 || insertLeadingAmp)
            {
                newQueryString.append('&');
            }
            newQueryString.append(nameValuePair.getName()).append('=')
                          .append(nameValuePair.getValue());
        }

        final URL newNewUrl = UrlUtils.getUrlWithNewQuery(newUrl,
                                                          newQueryString.toString());
        request.setUrl(newNewUrl);
    }
}
