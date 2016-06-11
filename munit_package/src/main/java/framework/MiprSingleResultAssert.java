package framework;

import org.apache.hadoop.mrunit.types.Pair;

import java.util.List;

public abstract class MiprSingleResultAssert<KEY, VALUE, WRITABLE> extends MiprAssert<KEY, VALUE, WRITABLE>{
    private WRITABLE source;

    @Override
    protected final void assertResults(List<Pair<KEY, VALUE>> runResults, List<WRITABLE> originalWritables){
        assertEquals(1, runResults.toArray().length);
        source = originalWritables.get(0);
    }

    @Override
    protected final void assertResult(Pair<KEY, VALUE> runResult) {
        assertResult(source, runResult.getSecond());
    }

    protected abstract void assertResult(WRITABLE source, VALUE result);
}
