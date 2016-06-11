package opencv;

import core.writables.ImageWritable;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.highgui.Highgui;

import java.io.*;
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
        // Write Mat array size
        out.writeInt((int) (im.total() * im.channels()));
        // Write Mat image width
        out.writeInt(im.width());
        // Write Mat image height
        out.writeInt(im.height());
        // Write image type
        out.writeInt(im.type());

        // Write image
        byte[] byteArray = getImageAsBytes();
        out.write(byteArray);
    }

    @Override
    protected byte[] getImageAsBytes() {
        return matToBytes(this.im);
    }

    public static byte[] matToBytes(Mat image) {
        byte[] byteArray = new byte[(int) (image.total() * image.channels())];
        image.get(0, 0, byteArray);
        return byteArray;
    }

    public static boolean areSamePerPixel(Mat image1,Mat image2){
        return Arrays.equals(matToBytes(image1),matToBytes(image2));
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

    public static MatImageWritable FromResource(String resourceName) throws IOException {
        InputStream stream = MatImageWritable.class.getResourceAsStream(resourceName);
        return FromResourceStream(stream);
    }

    public static MatImageWritable FromResourceStream(InputStream stream) throws IOException {
        byte[] temporaryImageInMemory = readStream(stream);
        Mat outputImage = Highgui.imdecode(new MatOfByte(temporaryImageInMemory), Highgui.IMREAD_UNCHANGED);
        return new MatImageWritable(outputImage);
    }

    private static byte[] readStream(InputStream stream) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[16384];

        while ((nRead = stream.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }

        buffer.flush();
        byte[] temporaryImageInMemory = buffer.toByteArray();
        buffer.close();
        stream.close();
        return temporaryImageInMemory;
    }
}
