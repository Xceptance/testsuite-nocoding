package test.com.xceptance.xlt.common.util.action.data;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.xceptance.xlt.api.data.GeneralDataProvider;
import com.xceptance.xlt.api.util.XltProperties;
import com.xceptance.xlt.common.util.action.data.URLActionData;
import com.xceptance.xlt.common.util.action.data.URLActionDataBuilder;
import com.xceptance.xlt.common.util.action.data.URLActionDataListBuilder;
import com.xceptance.xlt.common.util.bsh.ParameterInterpreter;

/**
 * Some trivial tests for the caching feature of {@link URLActionDataListBuilder}.
 */
public class URLActionDataListBuilderTest
{
    @Test
    public void testParseOnlyOnce_SameBuilder() throws Throwable
    {
        final DummyBuilder builder = new DummyBuilder("doesnotexist.txt", new ParameterInterpreter(XltProperties.getInstance(),
                                                                                                   GeneralDataProvider.getInstance()),
                                                      new URLActionDataBuilder());

        final List<URLActionData> l1 = builder.buildURLActionDataList();
        final List<URLActionData> l2 = builder.buildURLActionDataList();

        // Validate number of invocations (parseData() should have been called only once)
        Assert.assertEquals(2, builder.nbInvocations_buildActions);
        Assert.assertEquals(1, builder.nbInvocations_parseData);

        // Validate action lists
        Assert.assertNotNull(l1);
        Assert.assertNotNull(l2);

        Assert.assertEquals(1, l1.size());
        Assert.assertEquals(1, l2.size());

        Assert.assertNotSame(l1, l2);

        final URLActionData action1 = l1.get(0);
        final URLActionData action2 = l2.get(0);

        // Make sure that actions are not the same, but the URL strings are
        Assert.assertNotSame(action1, action2);
        Assert.assertEquals(action1.getUrlString(), action2.getUrlString());
    }

    @Test
    public void testParseOnlyOnce_DifferentBuilder() throws Throwable
    {
        final DummyBuilder builder1 = new DummyBuilder("doesnotexist1.txt", new ParameterInterpreter(XltProperties.getInstance(),
                                                                                                    GeneralDataProvider.getInstance()),
                                                       new URLActionDataBuilder());

        final DummyBuilder builder2 = new DummyBuilder("doesnotexist1.txt", new ParameterInterpreter(XltProperties.getInstance(),
                                                                                                    GeneralDataProvider.getInstance()),
                                                       new URLActionDataBuilder());

        final List<URLActionData> l1 = builder1.buildURLActionDataList();
        final List<URLActionData> l2 = builder2.buildURLActionDataList();

        // Validate number of invocations (one invocation of 'buildURLActionDataList()' for each builder)
        Assert.assertEquals(1, builder1.nbInvocations_buildActions);
        Assert.assertEquals(1, builder2.nbInvocations_buildActions);
        // ... one invocation of 'parseData()' for the first builder as it was called first
        Assert.assertEquals(1, builder1.nbInvocations_parseData);
        // ... but no invocation of 'parseData()' for the second builder as data should be served from cache
        Assert.assertEquals(0, builder2.nbInvocations_parseData);

        // Validate action lists
        Assert.assertNotNull(l1);
        Assert.assertNotNull(l2);

        Assert.assertEquals(1, l1.size());
        Assert.assertEquals(1, l2.size());

        Assert.assertNotSame(l1, l2);

        final URLActionData action1 = l1.get(0);
        final URLActionData action2 = l2.get(0);

        // Make sure that actions are not the same, but the URL strings are
        Assert.assertNotSame(action1, action2);
        Assert.assertEquals(action1.getUrlString(), action2.getUrlString());
    }

    private static class DummyBuilder extends URLActionDataListBuilder
    {
        private int nbInvocations_buildActions = 0;

        private int nbInvocations_parseData = 0;

        public DummyBuilder(String filePath, ParameterInterpreter interpreter, URLActionDataBuilder actionBuilder)
        {
            super(filePath, interpreter, actionBuilder);
        }

        @Override
        public List<URLActionData> buildURLActionDataList()
        {
            ++nbInvocations_buildActions;
            try
            {
                final Object o = getOrParseData();
                this.actionBuilder.setUrl((String) o);
                this.actionBuilder.setName("TEST");
                this.actionBuilder.setInterpreter(this.interpreter);
                return Arrays.asList(this.actionBuilder.build());
            }
            catch (final Throwable t)
            {
                throw new IllegalArgumentException(t);
            }
        }

        @Override
        protected Object parseData() throws IOException
        {
            ++nbInvocations_parseData;
            return "http://localhost:1234/foobar";
        }

    }
}
