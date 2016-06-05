import opencv.OpenCVMapper;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mrunit.ReduceDriver;
import org.apache.hadoop.mrunit.mapreduce.MapDriver;
import org.junit.Test;

import java.io.IOException;

public abstract class OpenCv_MapperTestBase<KEYIN, VALUEIN, KEYOUT, VALUEOUT> extends OpenCv_TestBase{
    MapDriver<KEYIN, VALUEIN, KEYOUT, VALUEOUT> mapDriver;

    @Override
    public void onSetUp() {
        OpenCVMapper<KEYIN, VALUEIN, KEYOUT, VALUEOUT> mapper = createMapper();
        mapDriver = MapDriver.newMapDriver(mapper);
    }

    protected abstract OpenCVMapper<KEYIN, VALUEIN, KEYOUT, VALUEOUT> createMapper();

    @Test
    public abstract void testMapper() throws IOException;
}

