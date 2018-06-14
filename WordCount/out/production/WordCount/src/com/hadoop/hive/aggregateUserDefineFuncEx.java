package com.hadoop.hive;

import org.apache.hadoop.hive.ql.exec.UDAF;
import org.apache.hadoop.hive.ql.exec.UDAFEvaluator;
import org.apache.hadoop.io.IntWritable;

public class aggregateUserDefineFuncEx extends UDAF{
    public static class MeanValue implements UDAFEvaluator{
        public static class ExteraValue{
            public int mean;
            public int sum;
        }

        private ExteraValue exteraValue;

        public void init(){
            exteraValue = new ExteraValue();
            exteraValue.mean = 0;
            exteraValue.sum = 0;
        }

        public boolean iterate(IntWritable value){
            if (value == null){
                return false;
            }
            exteraValue.mean = (exteraValue.mean*exteraValue.sum+value.get())/(++exteraValue.sum);
            return true;
        }

        public ExteraValue terminatePartial(){
            return exteraValue;
        }

        public boolean merge(ExteraValue exValue){
            ExteraValue newExteraValue = new ExteraValue();
            newExteraValue.sum = exteraValue.sum+exValue.sum;
            newExteraValue.mean = (exteraValue.mean*exteraValue.sum+
                exValue.mean*exValue.sum)/newExteraValue.sum;
            exteraValue = newExteraValue;
            return true;
        }

        public IntWritable terminate(){
            return new IntWritable(exteraValue.mean);
        }
    }
}
