/**
 * 
 */
package com.xceptance.xlt.common.util.bsh;

/**
 * NOW in params. Just returns the current time stamp.
 * 
 * @author rschwietzke
 */
public class ParamInterpreterNow
{
    /**
     * Returns the current time in millis 
     */
    public String toString()
    {
        return String.valueOf(System.currentTimeMillis());
    }
}
