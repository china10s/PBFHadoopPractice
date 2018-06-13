package com.hadoop.sort;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.compress.GzipCodec;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.IOException;

public class SortDataPreprocessor extends Configured implements Tool{
    static class CleanerMaopper extends Mapper<LongWritable,Text,IntWritable,Text>{
        private NcdcRecordParser parser = new NcdcRecordParser();

        @Override
        protected  void map(LongWritable key ,Text value,Context context) throws IOException,InterruptedException{
            parser.parse(value);
            if (parser.isValidTemperature()){
                context.write(new IntWritable(parser.getAirTemperature()),value);
            }
        }
    }

    @Override
    public int run(String[] args) throws Exception{
        Job job = new Job();
        job.setJarByClass(SortDataPreprocessor.class);
        job.setJobName("Ncd sort");
        FileInputFormat.addInputPath(job,new Path(args[0]));
        FileOutputFormat.setOutputPath(job,new Path(args[1]));

        job.setMapperClass(CleanerMaopper.class);
        job.setOutputKeyClass(IntWritable.class);
        job.setOutputValueClass(Text.class);
        job.setNumReduceTasks(0);
        job.setOutputFormatClass(SequenceFileOutputFormat.class);
//        SequenceFileOutputFormat.setCompressOutput(job,true);
//        SequenceFileOutputFormat.setOutputCompressorClass(job, GzipCodec.class);
//        SequenceFileOutputFormat.setOutputCompressionType(job, SequenceFile.CompressionType.BLOCK);

        return job.waitForCompletion(true)?0:1;
    }

    public static void main(String[] args) throws Exception{
        int exitCode= ToolRunner.run(new SortDataPreprocessor(),args);
        System.exit(exitCode);
    }
}
