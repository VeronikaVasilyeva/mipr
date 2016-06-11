import experiments.FaceCounter_opencv;
import opencv.MatImageWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mrunit.mapreduce.MapDriver;
import org.apache.hadoop.mrunit.mapreduce.MapReduceDriver;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.opencv.core.Core;

import java.io.IOException;

@RunWith(JUnit4.class)
public class FaceCounter_opencvTest {
    MapReduceDriver<NullWritable, MatImageWritable, IntWritable, IntWritable, IntWritable, IntWritable> mapReduceDriver;

    @Before
    public void setUp() throws IOException {
        FaceCounter_opencv.FaceCounterMapper mapper = new FaceCounter_opencv.FaceCounterMapper();
        mapReduceDriver = MapReduceDriver.newMapReduceDriver();
        mapReduceDriver.setMapper(mapper);
        mapReduceDriver.setReducer(new FaceCounter_opencv.FaceCounterReducer());

        //load opencv and let mapper know this
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        FaceCounter_opencv.FaceCounterMapper.set_openCvLoaded();
    }

    @Test
    public void testMapperReducer() throws IOException {
        //TODO: rewrite using munit
        mapReduceDriver.withInput(NullWritable.get(), MatImageWritable.FromResource("/color.jpg"));
        mapReduceDriver.withInput(NullWritable.get(), MatImageWritable.FromResource("/face2.jpg"));
        mapReduceDriver.withInput(NullWritable.get(), MatImageWritable.FromResource("/face2.jpg"));
        mapReduceDriver.withInput(NullWritable.get(), MatImageWritable.FromResource("/10.jpg"));
        mapReduceDriver.withOutput(new IntWritable(0),  new IntWritable(1));
        mapReduceDriver.withOutput(new IntWritable(1),  new IntWritable(2));
        mapReduceDriver.withOutput(new IntWritable(10),  new IntWritable(1));
        mapReduceDriver.runTest();
    }
}
