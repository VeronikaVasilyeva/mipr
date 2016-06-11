import experiments.Img2Gray_opencv;
import framework.MiprAssert;
import opencv.MatImageWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mrunit.types.Pair;
import org.junit.Test;
import org.opencv.core.CvType;

import java.util.List;

/**
 * Created by VeronikaV on 05.06.2016.
 */
public class Img2GrayOpenCVTest {

    @Test
    public void TestMapper () throws Exception {
        Mapper<NullWritable, MatImageWritable, NullWritable, MatImageWritable> map = new Img2Gray_opencv.Img2Gray_opencvMapper();
        MiprOpenCVMapDriver driver = new MiprOpenCVMapDriver<NullWritable, MatImageWritable>(map){};
        driver.withInputFile("/color.jpg");
        driver.test(new MiprAssert<NullWritable, MatImageWritable>() {
            @Override
            protected void assertResults(List<Pair<NullWritable, MatImageWritable>> runResults){
                assertEquals(1,runResults.toArray().length);
            }

            @Override
            protected void assertResult(Pair<NullWritable, MatImageWritable> runResult) {
                //проверяем, что у изображения один канал (чб)
                assertEquals(runResult.getSecond().getImage().type(), CvType.CV_8UC1);
            }
        });
    }



}
