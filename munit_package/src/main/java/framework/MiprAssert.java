package framework;

import core.writables.ImageWritable;
import org.apache.hadoop.mrunit.types.Pair;
import org.junit.Assert;

import java.util.List;

/**
 * Created by VeronikaV on 05.06.2016.
 */
public abstract class MiprAssert<KEY, VALUE, SOURCE_WRITABLE extends ImageWritable> extends Assert{

    protected void assertResults(List<Pair<KEY, VALUE>> runResults, List<SOURCE_WRITABLE> originalWritables){}
    protected void assertResult(Pair<KEY, VALUE> runResult){}

    public static <WRITABLE extends ImageWritable> void assertSameNameAndFormat(WRITABLE expected, WRITABLE actual){
        assertEquals(expected.getFormat(),actual.getFormat());
        assertEquals(expected.getFileName(),actual.getFileName());
    }

    public static <WRITABLE extends ImageWritable> void assertSamePerPixel(WRITABLE expected, WRITABLE actual){
        assertTrue("Images not same", expected.equals(actual));
    }

    public static <WRITABLE extends ImageWritable> void assertSame(WRITABLE expected, WRITABLE actual){
        assertSameNameAndFormat(expected,actual);
        assertSamePerPixel(expected,actual);
    }
}

