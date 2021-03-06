package framework;

import framework.MiprMapDriver;
import opencv.MatImageWritable;
import opencv.OpenCVMapper;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Mapper;
import org.opencv.core.Core;
import org.opencv.core.Mat;

/**
 * Created by VeronikaV on 05.06.2016.
 */

public abstract class MiprOpenCVMapDriver<KEYOUT, VALUEOUT> extends MiprMapDriver<Mat, MatImageWritable, KEYOUT, VALUEOUT> {
    public MiprOpenCVMapDriver(Mapper<NullWritable, MatImageWritable, KEYOUT, VALUEOUT> map) {
        super(map);
    }

    @Override
    protected void setup() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        OpenCVMapper.set_openCvLoaded();
    }

    protected MatImageWritable getWritable(String fileName) throws Exception {
        return MatImageWritable.FromResource(fileName);
    }
}
