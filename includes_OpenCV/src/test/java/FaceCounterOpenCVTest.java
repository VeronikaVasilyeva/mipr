import experiments.FaceCounter_opencv;
import framework.MiprOpenCVMapDriver;
import framework.MiprOpenCVSingleResultAssert;
import opencv.MatImageWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Mapper;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

@RunWith(Theories.class)
public class FaceCounterOpenCVTest {
    @DataPoints
    public static TestData[] testFileNames = new TestData[]{
            new TestData("/color.jpg", 0),
            new TestData("/10.jpg", 10),
            new TestData("/face2.jpg", 1),
            new TestData("/true-detective-2.jpg",2),
            new TestData("/game-of-thrones-4.jpg",4),
            new TestData("/game-of-thrones-5.jpg",5)
    };

    @Theory
    public void TestMapper(final TestData data) throws Exception {
        System.out.println("Test for data: "+data);
        Mapper<NullWritable, MatImageWritable, IntWritable, IntWritable> map = new FaceCounter_opencv.FaceCounterMapper();
        MiprOpenCVMapDriver driver = new MiprOpenCVMapDriver<IntWritable, IntWritable>(map){};
        driver.withInputFile(data.fileName);
        driver.test(new MiprOpenCVSingleResultAssert<IntWritable,IntWritable>() {
            @Override
            protected void assertResult(MatImageWritable source, IntWritable resultKey, IntWritable resultValue) {
                assertEquals(1, resultValue.get());
                assertEquals(data.facesCount, resultKey.get());
            }
        });
    }
}

class TestData{
    public String fileName;
    public int facesCount;
    TestData(String fileName, int facesCount){
        this.facesCount = facesCount;
        this.fileName = fileName;
    }

    @Override
    public String toString() {
        return "FileName = "+fileName+"; FacesCount="+facesCount;
    }
}
