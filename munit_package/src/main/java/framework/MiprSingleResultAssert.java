package framework;

import core.writables.ImageWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mrunit.types.Pair;

import java.util.List;

public abstract class MiprSingleResultAssert<KEY, VALUE, SOURCE_WRITABLE extends ImageWritable>
        extends MiprAssert<KEY, VALUE, SOURCE_WRITABLE>{
    private SOURCE_WRITABLE source;

    @Override
    protected final void assertResults(List<Pair<KEY, VALUE>> runResults, List<SOURCE_WRITABLE> originalWritables){
        assertEquals(1, runResults.toArray().length);
        source = originalWritables.get(0);
    }

    @Override
    protected final void assertResult(Pair<KEY, VALUE> runResult) {
        assertResult(source, runResult.getFirst(), runResult.getSecond());
    }

    protected void assertResult(SOURCE_WRITABLE source, KEY resultKey, VALUE resultValue){
        assertResult(source,resultValue);
    }
    protected void assertResult(SOURCE_WRITABLE source, VALUE resultValue){};
}
