package framework;

import opencv.MatImageWritable;
import opencv.OpenCVMapper;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Mapper;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;

import java.io.File;

/**
 * Created by VeronikaV on 05.06.2016.
 */

public abstract class MiprOpenCVMapDriver<KEYOUT, VALUEOUT> extends MiprMapDriver<Mat, MatImageWritable, KEYOUT, VALUEOUT> {

    static boolean isOpenCVLoaded = false;

    public MiprOpenCVMapDriver(Mapper<NullWritable, MatImageWritable, KEYOUT, VALUEOUT> map) {
        super(map);
        if (!isOpenCVLoaded) {
            System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
            isOpenCVLoaded = true;
            OpenCVMapper.set_openCvLoaded();
        }
    }

    protected MatImageWritable getWritable(String fileName) throws Exception {
        File f = new File(fileName);
        if(f.exists() && !f.isDirectory()) {
            return new MatImageWritable(Highgui.imread(fileName));
        } throw new Exception();
    }
}
