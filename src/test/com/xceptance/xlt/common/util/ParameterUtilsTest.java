package test.com.xceptance.xlt.common.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.junit.Test;

import com.xceptance.xlt.common.util.ParameterUtils;

public class ParameterUtilsTest
{

    @Test(expected = IllegalArgumentException.class)
    public void testIsNotNullNull()
    {
        ParameterUtils.isNotNull(null, "null");
    }

    @Test
    public void testIsNotNullNotNull()
    {
        ParameterUtils.isNotNull("hi", "null");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsNotNullMessageNull()
    {
        ParameterUtils.isNotNullMessages(null, "null", "a", "b");
    }

    @Test
    public void testIsNotNullMessageNotNull()
    {
        ParameterUtils.isNotNullMessages("hi", "null", "a", "b");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIstStringInt()
    {
        ParameterUtils.isString(1, "one");
    }

    @Test
    public void testIstStringString()
    {
        ParameterUtils.isString("1", "one");
    }

    @Test
    public void testIstStringOrNullNull()
    {
        ParameterUtils.isStringOrNull(null, "null");
    }

    @Test
    public void testIstStringOrNullString()
    {
        ParameterUtils.isStringOrNull("1", "one");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIstStringOrNullInt()
    {
        ParameterUtils.isStringOrNull(1, "one");
    }

    @Test
    public void testIsStringMessageString()
    {
        ParameterUtils.isStringMessage("1", "one", "zwo");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsStringMessageInt()
    {
        ParameterUtils.isStringMessage(1, "one", "zwo");
    }

    @Test
    public void testIsArrayListArrayList()
    {
        ParameterUtils.isArrayList(new ArrayList(), "list", "a");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsArrayListNotArrayList()
    {
        ParameterUtils.isArrayList(new Integer(1), "one", "a");
    }

    @Test
    public void testIsArrayListMessageArrayList()
    {
        ParameterUtils.isArrayListMessage(new ArrayList(), "list", "a");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsArrayListMessageNotArrayList()
    {
        ParameterUtils.isArrayListMessage(new Integer(1), "one", "a");
    }

    @Test
    public void testIsLinkedHashMapHashMap()
    {
        ParameterUtils.isLinkedHashMap(new LinkedHashMap<String, String>(),
                                       "hm",
                                       "a");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsLinkedHashMapNoHashMap()
    {
        ParameterUtils.isLinkedHashMap(new ArrayList(), "hm", "a");
    }

    @Test
    public void testIsLinkedHashMapMessageHashMap()
    {
        ParameterUtils.isLinkedHashMapMessage(new LinkedHashMap<String, String>(),
                                              "hm",
                                              "a");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsLinkedHashMapMessageNoHashMap()
    {
        ParameterUtils.isLinkedHashMapMessage(1, "hm", "a");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDoThrowMessages()
    {
        ParameterUtils.doThrowMessages("a", ParameterUtils.Reason.EMPTY, "");
    }

}
