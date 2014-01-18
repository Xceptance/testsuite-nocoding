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
package test.com.xceptance.xlt.common.util.bsh;

import java.util.Random;

import org.junit.Assert;
import org.junit.Test;

import com.xceptance.xlt.common.util.bsh.ParamInterpreterNow;

public class ParamInterpreterNowTest
{
    @Test
    public final void testToString()
    {
        final ParamInterpreterNow NOW = new ParamInterpreterNow();
        final long now = System.currentTimeMillis();
        
        Assert.assertTrue(Long.parseLong(NOW.toString()) >= now);
    }

    @Test
    public final void testChange() throws InterruptedException
    {
        final ParamInterpreterNow NOW = new ParamInterpreterNow();
        long result = 0;
        final Random r = new Random();
        
        for (int i = 0; i < 10; i++)
        {
            result = result + Long.parseLong(NOW.toString());
            Thread.sleep(r.nextInt(50));
            result = result - Long.parseLong(NOW.toString());
        }
  
        Assert.assertTrue(result != 0);
    }    
}
