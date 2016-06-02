package experiments;

import core.MiprConfigurationParser;
import opencv.MatImageInputFormat;
import opencv.MatImageOutputFormat;
import opencv.MatImageWritable;
import opencv.OpenCVMapper;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.objdetect.CascadeClassifier;

import java.io.IOException;

public class FaceCounter_opencv {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        String input = args[0];
        String output = args[1];

        Job job = new MiprConfigurationParser().getOpenCVJobTemplate();
        job.setJarByClass(FaceCounter_opencv.class);
        job.setMapperClass(FaceCounterMapper.class);
        job.setNumReduceTasks(0);
        job.setInputFormatClass(MatImageInputFormat.class);
        job.setOutputFormatClass(MatImageOutputFormat.class);
        Path outputPath = new Path(output);
        FileInputFormat.setInputPaths(job, input);
        FileOutputFormat.setOutputPath(job, outputPath);
        job.setOutputKeyClass(IntWritable.class);
        job.setOutputValueClass(IntWritable.class);
        job.setReducerClass(FaceCounterReducer.class);
        job.waitForCompletion(true);
    }

    public static class FaceCounterMapper extends OpenCVMapper<NullWritable, MatImageWritable, IntWritable, IntWritable> {

        @Override
        protected void map(NullWritable key, MatImageWritable value, Context context) throws IOException, InterruptedException {
            Mat image = value.getImage();

            //TODO: load image from distributed cache
            CascadeClassifier faceDetector = new CascadeClassifier("..\\images\\lbpcascade_frontalface.xml");
            MatOfRect faceDetections = new MatOfRect();
            faceDetector.detectMultiScale(image, faceDetections);

            int result = faceDetections.toArray().length;

            context.write(new IntWritable(result),new IntWritable(1));
        }
    }

    public static class FaceCounterReducer extends Reducer<IntWritable, IntWritable, IntWritable, IntWritable> {

        @Override
        protected void reduce(IntWritable key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
            int sum = 0;
            for (IntWritable value : values) {
                sum += value.get();
            }
            context.write(key, new IntWritable(sum));
        }
    }
}

