package writable;

import framework.OpenCVWritableTests;
import opencv.MatImageWritable;
import org.opencv.core.Mat;

import java.io.IOException;

public class MatImageWritableTests extends OpenCVWritableTests<Mat, MatImageWritable> {
    @Override
    protected MatImageWritable getWritable() throws IOException {
        return MatImageWritable.FromResource("/10.jpg");
    }
}

