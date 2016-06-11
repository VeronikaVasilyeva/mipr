package old;

import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mrunit.ReduceDriver;
import org.junit.Test;

public abstract class OpenCv_ReducerTestBase<KEYIN, VALUEIN, KEYOUT, VALUEOUT> extends OpenCv_TestBase{
    ReduceDriver<KEYIN, VALUEIN, KEYOUT, VALUEOUT> reduceDriver;

    @Override
    public void onSetUp() {
        Reducer<KEYIN, VALUEIN, KEYOUT, VALUEOUT> reducer = createReducer();
        reduceDriver = ReduceDriver.newReduceDriver(reducer);
    }

    protected abstract Reducer<KEYIN, VALUEIN, KEYOUT, VALUEOUT> createReducer();

    @Test
    public abstract void testReducer();
}
