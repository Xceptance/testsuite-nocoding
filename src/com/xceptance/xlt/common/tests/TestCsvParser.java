package com.xceptance.xlt.common.tests;

import com.xceptance.xlt.common.util.action.data.URLActionData;

public class TestCsvParser extends URLTestCase
{
    @Override
    public void testURLs()
    {
        for (final URLActionData action : actions)
        {
            action.outline();
        }
    }
}
