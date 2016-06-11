package old;

import experiments.Img2Gray_opencv;
import opencv.MatImageWritable;
import opencv.OpenCVMapper;
import org.apache.hadoop.io.NullWritable;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;

public class Img2Gray_opencvTest extends OpenCv_MapperTestBase<NullWritable, MatImageWritable,NullWritable,MatImageWritable>{

    @Override
    protected OpenCVMapper<NullWritable, MatImageWritable, NullWritable, MatImageWritable> createMapper() {
        return new Img2Gray_opencv.Img2Gray_opencvMapper();
    }

    @Override
    public void testMapper() throws IOException {

        MatImageWritable image = MatImageWritable.FromResource("/color2.jpg");
        mapDriver.withInput(NullWritable.get(), image);

        Mat expectedOutput = image.getImage().clone();
        Imgproc.cvtColor(expectedOutput, expectedOutput, Imgproc.COLOR_RGB2GRAY);
        mapDriver.withOutput(NullWritable.get(),  new MatImageWritable(expectedOutput));

        mapDriver.runTest();
    }
}
