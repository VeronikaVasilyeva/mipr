import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import core.MiprConfigurationParser;
import experiments.Img2Gray_opencv;
import opencv.MatImageInputFormat;
import opencv.MatImageOutputFormat;
import opencv.MatImageWritable;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hdfs.HdfsConfiguration;
import org.apache.hadoop.hdfs.MiniDFSCluster;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.test.PathUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.opencv.core.Core;
//todo: dont works :(
public class MiniDfsExpiriment {

    private static final String CLUSTER_1 = "cluster1";

    private File testDataPath;
    private Configuration conf;
    private MiniDFSCluster cluster;
    private FileSystem fs;
    MiprConfigurationParser confParser;

    @Before
    public void setUp() throws Exception {
        testDataPath = new File(PathUtils.getTestDir(getClass()),
                "miniclusters");
        confParser = new MiprConfigurationParser(new HdfsConfiguration());
        System.clearProperty(MiniDFSCluster.PROP_TEST_BUILD_DATA);
        conf = confParser.conf;
        //conf.set("mapred.job.tracker", "local");

        File testDataCluster1 = new File(testDataPath, CLUSTER_1);
        String c1Path = testDataCluster1.getAbsolutePath();
        conf.set(MiniDFSCluster.HDFS_MINIDFS_BASEDIR, c1Path);
        cluster = new MiniDFSCluster.Builder(conf).build();

        fs = FileSystem.get(conf);
    }

    @After
    public void tearDown() throws Exception {
        return;

//        Path dataDir = new Path(
//                testDataPath.getParentFile().getParentFile().getParent());
//        fs.delete(dataDir, true);
//        File rootTestFile = new File(testDataPath.getParentFile().getParentFile().getParent());
//        String rootTestDir = rootTestFile.getAbsolutePath();
//        Path rootTestPath = new Path(rootTestDir);
//        LocalFileSystem localFileSystem = FileSystem.getLocal(conf);
//        localFileSystem.delete(rootTestPath, true);
//        cluster.shutdown();
    }

    @Test
    public void testClusterWithData() throws Throwable {

        String IN_DIR = "testing/wordcount/input";
        String OUT_DIR = "testing/wordcount/output";


        String DATA_FILE = "10.jpg";

        Path inDir = new Path(IN_DIR);
        Path outDir = new Path(OUT_DIR);

        fs.delete(inDir, true);
        fs.delete(outDir,true);

        InputStream stream = getClass().getResourceAsStream("/10.jpg");
        writeHDFSContent(fs, inDir, DATA_FILE, stream);

        //InputStream in = new FileInputStream("../lib/x64/opencv_java249.dll");
        //writeHDFSContent(fs,new Path("data/lib"),"opencv_java249.dll",in);
        Path hdfsPath = new Path("/data/lib/libopencv_java2411.so");
        fs.copyFromLocalFile(false, true, new Path("../lib/x64/opencv_java249.dll"),hdfsPath); // Load file into HDFS


        Job job = confParser.getOpenCVJobTemplate();
        job.setJarByClass(Img2Gray_opencv.class);
        job.setMapperClass(Img2Gray_opencv.Img2Gray_opencvMapper.class);
        job.setInputFormatClass(MatImageInputFormat.class);
        job.setOutputFormatClass(MatImageOutputFormat.class);

        FileInputFormat.setMaxInputSplitSize(job, confParser.getMaxSplitSize());


        FileInputFormat.addInputPath(job, inDir);
        FileOutputFormat.setOutputPath(job, outDir);

        job.setOutputKeyClass(NullWritable.class);
        job.setOutputValueClass(MatImageWritable.class);

        job.waitForCompletion(true);
        assertTrue(job.isSuccessful());

        // now check that the output is as expected
        List<String> results = getJobResults(fs, outDir, 11);
        assertTrue(results.contains("She\t1"));
        assertTrue(results.contains("sells\t2"));

        // clean up after test case
        fs.delete(inDir, true);
        fs.delete(outDir,true);
    }


    private void writeHDFSContent(FileSystem fs, Path dir, String fileName, InputStream stream) throws IOException {
        Path newFilePath = new Path(dir, fileName);
        FSDataOutputStream out = fs.create(newFilePath);
        byte[] bytes = readStream(stream);
        out.write(bytes);
        out.close();
    }

    protected List<String> getJobResults(FileSystem fs, Path outDir, int numLines) throws Exception {
        List<String> results = new ArrayList<String>();
        FileStatus[] fileStatus = fs.listStatus(outDir);
        for (FileStatus file : fileStatus) {
            String name = file.getPath().getName();
            if (name.contains("part-r-00000")){
                Path filePath = new Path(outDir + "/" + name);
                BufferedReader reader = new BufferedReader(new InputStreamReader(fs.open(filePath)));
                for (int i=0; i < numLines; i++){
                    String line = reader.readLine();
                    if (line == null){
                        fail("Results are not what was expected");
                    }
                    results.add(line);
                }
                assertNull(reader.readLine());
                reader.close();
            }
        }
        return results;
    }

    private static byte[] readStream(InputStream stream) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[16384];

        while ((nRead = stream.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }

        buffer.flush();
        byte[] temporaryImageInMemory = buffer.toByteArray();
        buffer.close();
        stream.close();
        return temporaryImageInMemory;
    }

}
