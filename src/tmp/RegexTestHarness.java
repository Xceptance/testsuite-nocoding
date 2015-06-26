package tmp;

import java.io.Console;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexTestHarness
{

    public static void main(final String[] args)
    {
        final Console console = System.console();
        if (console == null)
        {
            System.err.println("No console.");
            System.exit(1);
        }
        while (true)
        {

            final Pattern pattern = Pattern.compile(console.readLine("%nEnter your regex: "));

            final Matcher matcher = pattern.matcher(console.readLine("Enter input string to search: "));

            boolean found = false;
            while (matcher.find())
            {
                console.format("I found the text" + " \"%s\" starting at "
                                   + "index %d and ending at index %d.%n",
                               matcher.group(),
                               matcher.start(), matcher.end());
                found = true;
            }
            if (!found)
            {
                console.format("No match found.%n");
            }
        }
    }
}
