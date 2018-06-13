package com.hadoop.unitTest;

import com.hadoop.maxtemperature.MaxTemperaturesMapper;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mrunit.mapreduce.MapDriver;
import org.junit.*;

import java.io.IOException;

public class MaxTemperatureMapperTest {
    @Test
    public void processValidRecord() throws IOException,InterruptedException{
        Text value = new Text("");
        new MapDriver<LongWritable,Text ,Text,IntWritable>()
                .withMapper(new MaxTemperaturesMapper())
                .withInput(new LongWritable(0),value)
                .withOutput(new Text("1950"),new IntWritable(-11))
                .runTest();
    }
}
