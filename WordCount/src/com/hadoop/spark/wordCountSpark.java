package com.hadoop.spark;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.io.LZ4CompressionCodec;
import scala.Tuple2;

import java.util.Arrays;
import java.util.Iterator;

public class wordCountSpark {
    public static void main(String[] args){
        SparkConf conf = new SparkConf().setAppName("Spark WordCount written by JAVA").setMaster("local");
        JavaSparkContext sc = new JavaSparkContext(conf);
        //获取文件内容
        JavaRDD<String> lines = sc.textFile("file:///Users/guest/spark-2.3.0/README.md");
        //分割成词
        JavaRDD<String> words = lines.flatMap(new FlatMapFunction<String, String>() {
            @Override
            public Iterator<String> call(String s) throws Exception {
                return Arrays.asList(s.split(" ")).iterator();
            }
        });
        //Map
        JavaPairRDD<String,Integer> pairs = words.mapToPair(new PairFunction<String, String, Integer>() {
            @Override
            public Tuple2<String, Integer> call(String s) throws Exception {
                return new Tuple2<String,Integer>(s,1);
            }
        });
        //Reduce
        JavaPairRDD<String,Integer> wordsCount = pairs.reduceByKey(new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer call(Integer integer, Integer integer2) throws Exception {
                return integer+integer2;
            }
        });
        //展示
        wordsCount.foreach(new VoidFunction<Tuple2<String, Integer>>() {
            @Override
            public void call(Tuple2<String, Integer> stringIntegerTuple2) throws Exception {
                System.out.println(stringIntegerTuple2._1+" -- "+stringIntegerTuple2._2);
            }
        });
        //关闭
        sc.close();
    }
}
