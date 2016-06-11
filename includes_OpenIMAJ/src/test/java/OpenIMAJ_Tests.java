import experiments.Img2Gray_OpenIMAJ;
import framework.MiprAssert;
import framework.MiprMapDriver;
import framework.MiprSingleResultAssert;
import openimaj.MBFImageWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Mapper;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;

import java.io.InputStream;

@RunWith(Theories.class)
public class OpenIMAJ_Tests {
    @DataPoints
    public static String[] testFileNames = new String[]{ "/color.jpg", "/10.jpg", "/face2.jpg"};

    @Theory
    public void TestMapper(String fileName) throws Exception {
        System.out.println("Test for image: "+fileName);
        Mapper<NullWritable, MBFImageWritable, NullWritable, MBFImageWritable> map = new Img2Gray_OpenIMAJ.Img2GrayOimgMapper();
        MiprMapDriver driver = new MiprMapDriver<MBFImage, MBFImageWritable, NullWritable, MBFImageWritable>(map) {
            @Override
            protected MBFImageWritable getWritable(String fileName) throws Exception {
                InputStream input = getClass().getResourceAsStream(fileName);
                MBFImage image = ImageUtilities.readMBF(input);
                return new MBFImageWritable(image,fileName,"jpg");
            }
        };
        driver.withInputFile(fileName);
        driver.test(new MiprSingleResultAssert<NullWritable,MBFImageWritable,MBFImageWritable>() {
            @Override
            protected void assertResult(MBFImageWritable source, MBFImageWritable resultValue) {
                MiprAssert.assertSame(source,resultValue);
            }
        });
    }
}
