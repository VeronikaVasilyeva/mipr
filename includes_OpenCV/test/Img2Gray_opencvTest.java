import experiments.Img2Gray_opencv;
import opencv.MatImageWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mrunit.mapreduce.MapDriver;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;

/**
 * Created by Epanchee on 09.03.15.
 */

@RunWith(JUnit4.class)
public class Img2Gray_opencvTest{
    MapDriver<NullWritable, MatImageWritable, NullWritable, MatImageWritable> mapDriver;

    @Before
    public void setUp() throws IOException {
        Img2Gray_opencv.Img2Gray_opencvMapper mapper = new Img2Gray_opencv.Img2Gray_opencvMapper();
        mapDriver = MapDriver.newMapDriver(mapper);
        //Load opencv and let mapper know this.
        //Java open cv library shold by accessible.
        //Add it to java library path or use use -Djava.library.path=D:\Java\MIPR\opencv\build\java\x86\.
        //Use System.out.println(System.getProperty("java.library.path")); to check it.
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        Img2Gray_opencv.Img2Gray_opencvMapper.set_openCvLoaded();
    }

    @Test
    public void testMapper() {
        mapDriver.withInput(NullWritable.get(), new MatImageWritable(Highgui.imread("..\\images\\color2.jpg")));
        Mat image = Highgui.imread("..\\images\\color2.jpg");
        Imgproc.cvtColor(image, image, Imgproc.COLOR_RGB2GRAY);
        mapDriver.withOutput(NullWritable.get(),  new MatImageWritable(image));
        mapDriver.runTest();
    }
}

