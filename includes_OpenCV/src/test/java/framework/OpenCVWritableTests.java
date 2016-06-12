package framework;

import core.writables.ImageWritable;
import org.opencv.core.Core;
import writable.ImageWritableTests;

public abstract class OpenCVWritableTests<I,WRITABLE extends ImageWritable<I>> extends ImageWritableTests<I, WRITABLE> {
    @Override
    public void setup(){
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }
}
