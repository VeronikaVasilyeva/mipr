package framework;

import java.io.IOException;
import java.util.List;
import java.io.File;
import core.writables.ImageWritable;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mrunit.mapreduce.MapDriver;
import org.apache.hadoop.mrunit.types.Pair;
import org.junit.*;

/**
 * Created by VeronikaV on 05.06.2016.
 */

public abstract class MiprMapDriver<IMAGE, WRITABLE extends ImageWritable<IMAGE>, KEYOUT, VALUEOUT> extends Assert {

    MapDriver<NullWritable, WRITABLE, KEYOUT, VALUEOUT> mapDriver;
    Mapper<NullWritable, WRITABLE, KEYOUT, VALUEOUT> mapper;

    public MiprMapDriver(Mapper<NullWritable, WRITABLE, KEYOUT, VALUEOUT> map) {
        mapper = map;
        mapDriver = MapDriver.newMapDriver(map);
    }

    public void withInputFile(String fileName) throws Exception {
        mapDriver.withInput(NullWritable.get(), getWritable(fileName));
    }

    public void withInputFolder(String folderName) throws Exception {
        File folder = new File(folderName);
        File[] files = folder.listFiles();

        for (File file: files) {
            withInputFile(file.getAbsolutePath());
        }
    }

    //вернет объект типа Writable - возвращает конкретную реализацию из наследника - фабричный метод
    protected abstract WRITABLE getWritable(String fileName) throws Exception;

    public final void test(MiprAssert<KEYOUT, VALUEOUT> miprAssert) throws IOException {
        setUp(); // предоставляет возможность преднастройки

        final List<Pair<KEYOUT, VALUEOUT>> runResults = mapDriver.run(); //получаем выходные занные после запуска

        miprAssert.assertResults(runResults);  //проверка на всю коллекцию - опциональна

        for (Pair<KEYOUT, VALUEOUT> pair: runResults) {
            miprAssert.assertResult(pair);
        }
    }

    protected void setUp() {};
}
