package opencv;

import core.writables.ImageWritable;
import org.opencv.core.Mat;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

/**
 * Created by Epanchee on 24.02.15.
 */
public class MatImageWritable extends ImageWritable<Mat> {
    public MatImageWritable() {
        this.im = new Mat();
        this.format = "undef";
        this.fileName = "unnamed";
    }

    public MatImageWritable(Mat mat) {
        this();
        this.im = mat;
    }

    public MatImageWritable(Mat mat, String fileName, String format) {
        this(mat);
        setFileName(fileName);
        setFormat(format.toLowerCase());
    }

    public void write(DataOutput out) throws IOException {
        super.write(out);
        // Write image
        byte[] byteArray = new byte[(int) (im.total() * im.channels())];
        im.get(0, 0, byteArray);
        // Write Mat array size
        out.writeInt((int) (im.total() * im.channels()));
        // Write Mat image width
        out.writeInt(im.width());
        // Write Mat image height
        out.writeInt(im.height());
        // Write image type
        out.writeInt(im.type());
        // Write image bytes
        out.write(byteArray);
    }

    public void readFields(DataInput in) throws IOException {
        super.readFields(in);
        // Read Mat array size
        int arraySize = in.readInt();
        // Read Mat image width
        int mWidth = in.readInt();
        // Read Mat image height
        int mHeight = in.readInt();
        // Read Mat image type
        int type = in.readInt();
        // Read image byte array
        byte[] bArray = new byte[arraySize];
        in.readFully(bArray);
        this.im = new Mat(mHeight, mWidth, type);
        // Read image from byte array
        this.im.put(0, 0, bArray);
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof MatImageWritable)) {
            return false;
        } else {
            MatImageWritable other = (MatImageWritable)obj;
            return areSame(this, other);
        }
    }

    @Override
    public int hashCode() {
        //TODO
        return 0;
    }

    public static boolean areSame(MatImageWritable writable1, MatImageWritable writable2) {
        return  writable1.fileName.equals(writable2.fileName) &&
                writable1.format.equals(writable2.format) &&
                AreSamePerPixel(writable1.im,writable2.im);
    }

    public static boolean AreSamePerPixel(Mat img1,Mat img2) {
        byte[] array1 = MatToBytesArray(img1);
        byte[] array2 = MatToBytesArray(img2);
        boolean areSameAsBytes = Arrays.equals(array1, array2);
        return areSameAsBytes;
    }

    public static byte[] MatToBytesArray(Mat image){
        byte[] result = new byte[(int) (image.total() * image.channels())];
        image.get(0, 0, result);
        return result;
    }
}
