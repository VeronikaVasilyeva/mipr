package framework;

import opencv.MatImageWritable;
import org.junit.Assert;
import org.opencv.core.Mat;

/**
 * Created by VeronikaV on 05.06.2016.
 */

public class MiprOpenCVAssert extends Assert {
    public static void assertSameSize(MatImageWritable expected, MatImageWritable actual){
        Mat expectedImage = expected.getImage();
        Mat actualImage = actual.getImage();
        assertSameSize(expectedImage, actualImage);
    }

    public static void assertSameSize(Mat expectedImage, Mat actualImage) {
        assertEquals(expectedImage.width(),actualImage.width());
        assertEquals(expectedImage.height(),actualImage.height());
        assertEquals(expectedImage.total(),actualImage.total());
    }

    public static void assertSameNameAndFormat(MatImageWritable expected, MatImageWritable actual){
        assertEquals(expected.getFormat(),actual.getFormat());
        assertEquals(expected.getFileName(),actual.getFileName());
    }

    public static void assertType(int type, MatImageWritable actual){
        assertType(type,actual.getImage());
    }

    public static void assertType(int type, Mat actual){
        assertEquals(type,actual.type());
    }

    public static void assertChannelsCount(int expected, MatImageWritable actual){
        assertChannelsCount(expected,actual.getImage());
    }

    public static void assertChannelsCount(int expected, Mat actual){
        assertEquals(expected,actual.channels());
    }
}
