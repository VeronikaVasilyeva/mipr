import experiments.Img2Gray_opencv;
import framework.MiprOpenCVAssert;
import framework.MiprOpenCVMapDriver;
import framework.MiprOpenCVSingleResultAssert;
import opencv.MatImageWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Mapper;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

@RunWith(Theories.class)
public class Img2GrayOpenCVTest {

    private MiprOpenCVSingleResultAssert _asserts = new MiprOpenCVSingleResultAssert<NullWritable, MatImageWritable>() {

        @Override
        protected void assertResult(MatImageWritable source, MatImageWritable result) {
            MiprOpenCVAssert.assertSameSize(source, result);
            MiprOpenCVAssert.assertSameNameAndFormat(source, result);
            MiprOpenCVAssert.assertType(CvType.CV_8UC1, result);
            MiprOpenCVAssert.assertChannelsCount(1, result);
        }
    };

    @DataPoints
    public static String[] testFileNames = new String[]{ "/color.jpg", "/10.jpg", "/face2.jpg"};

    @Theory
    public void TestMapper(String fileName) throws Exception {
        System.out.println("Test for image: "+fileName);
        Mapper<NullWritable, MatImageWritable, NullWritable, MatImageWritable> map = new Img2Gray_opencv.Img2Gray_opencvMapper();
        MiprOpenCVMapDriver driver = new MiprOpenCVMapDriver<NullWritable, MatImageWritable>(map) {};
        driver.withInputFile(fileName);
        driver.test(_asserts);
    }

    @Theory
    public void TestMapper_ResultSameAsRawOpenCV(String fileName) throws Exception {
        System.out.println("Test for image: "+fileName);
        Mapper<NullWritable, MatImageWritable, NullWritable, MatImageWritable> map = new Img2Gray_opencv.Img2Gray_opencvMapper();
        MiprOpenCVMapDriver driver = new MiprOpenCVMapDriver<NullWritable, MatImageWritable>(map) {};
        driver.withInputFile(fileName);

        driver.test(new MiprOpenCVSingleResultAssert<MatImageWritable, MatImageWritable>() {
            @Override
            protected void assertResult(MatImageWritable source, MatImageWritable resultValue) {
                Mat sourceImage = source.getImage();
                Mat expectedOutput = sourceImage.clone();
                Imgproc.cvtColor(expectedOutput, expectedOutput, Imgproc.COLOR_RGB2GRAY);
                MiprOpenCVAssert.assertSamePerPixel(expectedOutput, resultValue.getImage());
            }
        });
    }
}
