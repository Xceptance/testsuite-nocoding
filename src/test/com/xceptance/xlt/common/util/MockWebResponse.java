package test.com.xceptance.xlt.common.util;

import java.net.URL;

import com.gargoylesoftware.htmlunit.StringWebResponse;

public class MockWebResponse extends StringWebResponse
{

    private static final long serialVersionUID = -6805954103312051680L;

    private String contentType;

    private String content;

    public MockWebResponse(final String content, final URL url, final String contentType)
    {
        super(content, url);
        this.contentType = contentType;
        this.content = content;
    }

    @Override
    public String getContentType()
    {
        return contentType;
    }

    @Override
    public String getContentAsString()
    {
        return content;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(final String content)
    {
        this.content = content;
    }

    public void setContentType(final String contentType)
    {
        this.contentType = contentType;
    }
}
