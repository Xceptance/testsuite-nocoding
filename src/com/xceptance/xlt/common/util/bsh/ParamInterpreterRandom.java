/**
 * 
 */
package com.xceptance.xlt.common.util.bsh;

import org.apache.commons.lang.RandomStringUtils;

import com.xceptance.xlt.api.util.XltRandom;

/**
 * Provide a simple interface to some often used functions to get random data. 
 * All just for convenience.
 * 
 * ${RANDOM.String(x)} : a random string with length x. Contains [A-Za-z].   
 * ${RANDOM.String(s, x)} : a random string with length x. Contains letters from s.   
 * ${RANDOM.Number(max)} : returns an integer between 0 (inclusive) and max (inclusive)   
 * ${RANDOM.Number(min, max)} : returns an integer between min (inclusive) and max (inclusive)
 *
 * @author rschwietzke
 */
public class ParamInterpreterRandom
{
    public int Number(final int max)
    {
        return Number(0, max);   
    }

    public int Number(final int minimum, final int maximum)
    {
        return XltRandom.nextInt(minimum, maximum);
    }
    
    public String String(final int length)
    {
        return RandomStringUtils.randomAlphabetic(length);
    }
    
    public String String(final String characters, final int length)
    {
        return RandomStringUtils.random(length, "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz");
    }    
}
