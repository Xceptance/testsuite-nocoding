package tmp;

import java.net.URL;
import java.util.List;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class TestXPath
{
    public static void main(final String args[])
    {
        try
        {
            final WebClient client = new WebClient();

            client.getOptions().setThrowExceptionOnFailingStatusCode(false);
            client.getOptions().setThrowExceptionOnScriptError(false);

            final URL url = new URL("https://dev01.lab04b.dw.demandware.net/s/SiteGenesis/register");
            final HtmlPage page = client.getPage(url);
            final List<DomNode> nodeList = (List<DomNode>) page.getByXPath("//*[@id='RegistrationForm']/fieldset[2]/input/@value");
            if (!(nodeList.isEmpty()))
            {
                for (final DomNode n : nodeList)
                {
                    System.err.println("Elements: ");
                    System.err.println("asText() :" + n.asText());
                    System.err.println("asXml() :" + n.asXml());
                    System.err.println("getTextContent() :" + n.getTextContent());

                }
            }
            else
            {
                System.err.println("Elements by XPath not found!");
            }

        }
        catch (final Exception e)
        {
            e.printStackTrace();
        }
    }
}
