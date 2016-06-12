package writable;

import core.writables.ImageWritable;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public abstract class ImageWritableTests<I, WRITABLE extends ImageWritable<I>> extends Assert {

    @Before
    public void setup(){}

    @Test
    public void serializationDeserialization() throws Exception {
        WRITABLE sut = getWritable();
        MapWritable map = new MapWritable();
        map.put(NullWritable.get(),sut);
        MapWritable clone = WritableUtils.clone(map,new Configuration());
        Writable result = clone.get(NullWritable.get());
        assertEquals(sut,result);
    }

    protected abstract WRITABLE getWritable() throws Exception;
}
