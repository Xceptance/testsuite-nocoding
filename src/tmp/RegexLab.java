package tmp;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.xml.XmlPage;

public class RegexLab
{
    private static final String BASE_PATTERN = "<base\\b.*?(></base>|/>|>)";

    public static List<String> getAllMatches(final String patternString, final String text)
    {
        if (text == null || text.length() == 0 || patternString == null
            || patternString.length() == 0)
        {
            return Collections.emptyList();
        }

        final List<String> resultList = new ArrayList<String>();
        final Pattern pattern = Pattern.compile(patternString);
        final Matcher matcher = pattern.matcher(text);
        while (matcher.find())
        {
            final String match = matcher.group();
            resultList.add(match);
        }

        return resultList;
    }

    public static void main(final String args[]) throws IOException
    {
        String htmlPage = "";
        try
        {
            final WebClient webClient = new WebClient();
            final WebRequest request = new WebRequest(
                                                      new URL(
                                                              "https://www.xceptance.com/en/"));
            final WebResponse response = webClient.loadWebResponse(request);
            htmlPage = response.getContentAsString();
            final XmlPage xmlPage = webClient.getPage("http://www.w3schools.com/xml/note.xml");
            System.err.println(xmlPage.getContent());
            
        }
        catch (final Exception e)
        {
            e.printStackTrace();
        }
    }

}
