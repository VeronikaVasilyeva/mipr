import experiments.Img2Gray_opencv;
import framework.MiprOpenCVAssert;
import framework.MiprOpenCVMapDriver;
import framework.MiprOpenCVSingleResultAssert;
import opencv.MatImageWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Mapper;
import org.junit.Test;
import org.opencv.core.CvType;

public class Img2GrayOpenCVTest {

    private MiprOpenCVSingleResultAssert _asserts = new MiprOpenCVSingleResultAssert() {

        @Override
        protected void assertResult(MatImageWritable source, MatImageWritable result) {
            MiprOpenCVAssert.assertSameSize(source,result);
            MiprOpenCVAssert.assertSameNameAndFormat(source,result);
            MiprOpenCVAssert.assertType(CvType.CV_8UC1,result);
            MiprOpenCVAssert.assertChannelsCount(1,result);
        }
    };

    @Test
    public void TestImage1() throws Exception {
        TestMapper("/color.jpg");
    }

    @Test
    public void TestImage2() throws Exception {
        TestMapper("/10.jpg");
    }

    @Test
    public void TestImage3() throws Exception {
        TestMapper("/face2.jpg");
    }

    private void TestMapper(String fileName) throws Exception {
        Mapper<NullWritable, MatImageWritable, NullWritable, MatImageWritable> map = new Img2Gray_opencv.Img2Gray_opencvMapper();
        MiprOpenCVMapDriver driver = new MiprOpenCVMapDriver<NullWritable, MatImageWritable>(map){};
        driver.withInputFile(fileName);
        driver.test(_asserts);
    }
}

