package framework;

import org.apache.hadoop.mrunit.types.Pair;
import org.junit.Assert;

import java.util.List;

/**
 * Created by VeronikaV on 05.06.2016.
 */
public abstract class MiprAssert<KEY, VALUE, WRITABLE> extends Assert{

    protected void assertResults(List<Pair<KEY, VALUE>> runResults, List<WRITABLE> originalWritables){}
    protected void assertResult(Pair<KEY, VALUE> runResult){}
}

