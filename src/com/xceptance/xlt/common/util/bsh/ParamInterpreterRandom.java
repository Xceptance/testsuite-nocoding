/**  
 *  Copyright 2014 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License. 
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
