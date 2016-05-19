package opencv;

import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * Created by Epanchee on 13.08.2015.
 */
public class OpenCVMapper<KEYIN, VALUEIN, KEYOUT, VALUEOUT> extends Mapper<KEYIN, VALUEIN, KEYOUT, VALUEOUT> {
    private static boolean openCvLoaded = false;
    public static void set_openCvLoaded(){
        openCvLoaded = true;
    }

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        if(!openCvLoaded)
        {
            Path[] myCacheFiles = DistributedCache.getLocalCacheFiles(context.getConfiguration());
            System.load(myCacheFiles[0].toUri().getPath());
            set_openCvLoaded();
        }
    }
}
