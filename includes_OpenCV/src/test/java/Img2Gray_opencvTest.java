import experiments.Img2Gray_opencv;
import opencv.MatImageWritable;
import opencv.OpenCVMapper;
import org.apache.hadoop.io.NullWritable;
import org.junit.Before;
import org.junit.BeforeClass;
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
        Mat testImage = LoadMat(".\\src\\test\\java\\images\\color2.jpg");
        mapDriver.withInput(NullWritable.get(), new MatImageWritable(testImage));

        Mat expectedOutput = testImage.clone();
        Imgproc.cvtColor(expectedOutput, expectedOutput, Imgproc.COLOR_RGB2GRAY);
        mapDriver.withOutput(NullWritable.get(),  new MatImageWritable(expectedOutput));

        mapDriver.runTest();
    }
}
