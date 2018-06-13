package com.hadoop.maxtemperature;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.http.conn.ConnectTimeoutException;

public class MaxTemperaturesMapper
        extends Mapper<LongWritable,Text,Text,IntWritable>{
    private static final int MISSING = 999;

    enum Temperature{
        POS_NUMBER,
        NAGTIVE_NUMBER
    }

    @Override
    public void map(LongWritable key,Text value,Context context) throws IOException,InterruptedException{
        String line = value.toString();
        String year = line.substring(15,19);
        int airTemperature;
        if (line.charAt(87) == '+'){
            airTemperature = Integer.parseInt(line.substring(88,92));
            context.getCounter(Temperature.POS_NUMBER).increment(1);
        }
        else{
            airTemperature = Integer.parseInt(line.substring(87,92));
            context.getCounter(Temperature.NAGTIVE_NUMBER).increment(1);
        }
        String quality = line.substring(92,93);
        if (airTemperature != MISSING && quality.matches("[01459]")){
            context.write(new Text(year),new IntWritable(airTemperature));
        }
    }
}
