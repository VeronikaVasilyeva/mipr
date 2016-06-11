package framework;

import java.io.IOException;
import java.util.ArrayList;
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
    List<WRITABLE> originalWritables;

    public MiprMapDriver(Mapper<NullWritable, WRITABLE, KEYOUT, VALUEOUT> map) {
        mapper = map;
        mapDriver = MapDriver.newMapDriver(map);
        originalWritables = new ArrayList<WRITABLE>();
        setup();
    }

    public void withInputFile(String fileName) throws Exception {
        WRITABLE writable = getWritable(fileName);
        originalWritables.add(writable);
        mapDriver.withInput(NullWritable.get(), writable);
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

    public final void test(MiprAssert<KEYOUT, VALUEOUT, WRITABLE> miprAssert) throws IOException {
        //предоставляет возможность преднастройки
        perTestSetup();
        try{
            //получаем выходные занные после запуска
            final List<Pair<KEYOUT, VALUEOUT>> runResults = mapDriver.run();

            //проверка на всю коллекцию - опциональна
            miprAssert.assertResults(runResults, originalWritables);

            for (Pair<KEYOUT, VALUEOUT> pair: runResults) {
                miprAssert.assertResult(pair);
            }
        }
       finally {
            perTestTeardown();
        }
    }

    protected void setup() {};
    protected void perTestSetup(){};
    protected void perTestTeardown(){};
}
