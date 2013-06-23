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
