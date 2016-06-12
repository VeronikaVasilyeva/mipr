import experiments.FaceDetectionOpenCV;
import framework.MiprOpenCVAssert;
import framework.MiprOpenCVMapDriver;
import framework.MiprOpenCVSingleResultAssert;
import opencv.MatImageWritable;
import org.apache.hadoop.io.NullWritable;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

import java.io.IOException;

@RunWith(Theories.class)
public class FaceDetectionOpenCVTest {
    @DataPoints
    public static String[] testFileNames = new String[]{ "/color.jpg", "/10.jpg", "/face2.jpg"};

    @Theory
    public void TestMapper_ResultSameAsRawOpenCV(String fileName) throws Exception {
        System.out.println("Test for image: "+fileName);
        final FaceDetectionOpenCV.FaceDetectorMapper map = new FaceDetectionOpenCV.FaceDetectorMapper();
        MiprOpenCVMapDriver driver = new MiprOpenCVMapDriver<NullWritable, MatImageWritable>(map) {};
        driver.withInputFile(fileName);

        driver.test(new MiprOpenCVSingleResultAssert<MatImageWritable, MatImageWritable>() {
            @Override
            protected void assertResult(MatImageWritable source, MatImageWritable resultValue) throws IOException {
                Mat sourceImage = source.getImage();
                Mat expectedOutput = map.detectFaces(sourceImage);
                MiprOpenCVAssert.assertSamePerPixel(expectedOutput, resultValue.getImage());
            }
        });
    }
}
