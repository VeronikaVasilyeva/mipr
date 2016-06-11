package old;

import experiments.Img2Gray_opencv;
import org.junit.Before;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;

import java.io.IOException;

public abstract class OpenCv_TestBase{
    @Before
    public void setUp() throws IOException {
        loadOpenCv();
        onSetUp();
    }

    protected abstract void onSetUp();

    protected final void loadOpenCv() {
        //Load opencv and let mapper know this.
        //Java open cv library shold by accessible.
        //Add it to java library path or use use -Djava.library.path=D:\Java\MIPR\opencv\build\java\x86\.
        //Use System.out.println(System.getProperty("java.library.path")); to check it.
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        Img2Gray_opencv.Img2Gray_opencvMapper.set_openCvLoaded();
    }
}
