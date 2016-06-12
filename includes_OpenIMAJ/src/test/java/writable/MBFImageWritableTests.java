package writable;

import openimaj.MBFImageWritable;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;

import java.io.InputStream;

public class MBFImageWritableTests extends ImageWritableTests<MBFImage, MBFImageWritable> {

    @Override
    protected MBFImageWritable getWritable() throws Exception {
        String filename = "/10.jpg";
        InputStream input = getClass().getResourceAsStream(filename);
        MBFImage image = ImageUtilities.readMBF(input);
        return new MBFImageWritable(image,filename,"jpg");
    }
}