
import framework.MiprMapDriver;
import openimaj.MBFImageWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Mapper;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;

import java.io.File;

/**
 * Created by VeronikaV on 06.06.2016.
 */
public abstract class MiprOpenIMAJDriver<KEYOUT, VALUEOUT> extends MiprMapDriver<MBFImage, MBFImageWritable, KEYOUT, VALUEOUT> {

      //  static boolean isOpenCVLoaded = false;

        public MiprOpenIMAJDriver(Mapper<NullWritable, MBFImageWritable, KEYOUT, VALUEOUT> map) {
            super(map);
          /*   if (!isOpenCVLoaded) {
                System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
                isOpenCVLoaded = true;
                OpenCVMapper.set_openCvLoaded();
            } */
        }

        protected MBFImageWritable getWritable(String fileName) throws Exception {
            File f = new File(fileName);
            if (f.exists() && !f.isDirectory()) {
                MBFImage image = ImageUtilities.readMBF(f);
                return new MBFImageWritable(image, f.getAbsolutePath(), "jpg");
            }
            throw new Exception();
        }
}
