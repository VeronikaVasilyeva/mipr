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
import org.opencv.highgui.Highgui;

import java.io.File;
import java.io.IOException;

@RunWith(JUnit4.class)
public class FaceCounter_opencvTest{
    MapDriver<NullWritable, MatImageWritable, IntWritable, IntWritable> mapDriver;
    MapReduceDriver<NullWritable, MatImageWritable, IntWritable, IntWritable, IntWritable, IntWritable> mapReduceDriver;

    @Before
    public void setUp() throws IOException {
        FaceCounter_opencv.FaceCounterMapper mapper = new FaceCounter_opencv.FaceCounterMapper();
        mapDriver = MapDriver.newMapDriver(mapper);
        mapReduceDriver = MapReduceDriver.newMapReduceDriver();
        mapReduceDriver.setMapper(mapper);
        mapReduceDriver.setReducer(new FaceCounter_opencv.FaceCounterReducer());
        
        //load opencv and let mapper know this
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        FaceCounter_opencv.FaceCounterMapper.set_openCvLoaded();
    }

    @Test
    public void testMapper() throws IOException {
        //mapDriver.withInput(NullWritable.get(), new MatImageWritable(Highgui.imread(".\\src\\test\\java\\images\\color2.jpg")));
        //mapDriver.withInput(NullWritable.get(), new MatImageWritable(Highgui.imread(".\\src\\test\\java\\images\\face2.jpg")));
        //mapDriver.withInput(NullWritable.get(), new MatImageWritable(Highgui.imread(".\\src\\test\\java\\images\\3.jpg")));
        //mapDriver.withInput(NullWritable.get(), new MatImageWritable(Highgui.imread(".\\src\\test\\java\\images\\12.jpg")));
        //mapDriver.withInput(NullWritable.get(), new MatImageWritable(Highgui.imread(".\\src\\test\\java\\images\\5.jpg")));
        String path = getClass().getResource("10.jpg").getPath();
        mapDriver.withInput(NullWritable.get(), new MatImageWritable(Highgui.imread(path)));
        //mapDriver.withOutput(new IntWritable(0),  new IntWritable(1));
        //mapDriver.withOutput(new IntWritable(1),  new IntWritable(1));
        //mapDriver.withOutput(new IntWritable(3),  new IntWritable(1));
        //mapDriver.withOutput(new IntWritable(12),  new IntWritable(1));
        //mapDriver.withOutput(new IntWritable(5),  new IntWritable(1));
        mapDriver.withOutput(new IntWritable(10),  new IntWritable(1));
        mapDriver.runTest();
    }

    @Test
    public void testMapperReducer() throws IOException {
        mapReduceDriver.withInput(NullWritable.get(), new MatImageWritable(Highgui.imread("color2.jpg")));
        mapReduceDriver.withInput(NullWritable.get(), new MatImageWritable(Highgui.imread("face2.jpg")));
        mapReduceDriver.withInput(NullWritable.get(), new MatImageWritable(Highgui.imread("face2.jpg")));
        mapReduceDriver.withInput(NullWritable.get(), new MatImageWritable(Highgui.imread("10.jpg")));
        mapReduceDriver.withOutput(new IntWritable(0),  new IntWritable(1));
        mapReduceDriver.withOutput(new IntWritable(1),  new IntWritable(2));
        mapReduceDriver.withOutput(new IntWritable(10),  new IntWritable(1));
        mapReduceDriver.runTest();
    }
}
