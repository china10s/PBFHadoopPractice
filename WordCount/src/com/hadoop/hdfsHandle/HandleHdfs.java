package com.hadoop.hdfsHandle;

import java.io.*;
import java.net.URI;

import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

public class HandleHdfs {
    public static boolean CreateDir(String dst,Configuration conf){
        Path dstPath = new Path(dst);
        try{
            FileSystem hdfs = FileSystem.get(conf);
            boolean isSuccess = hdfs.mkdirs(dstPath);
            hdfs.close();
        }
        catch(IOException ie){
            return false;
        }
        return true;
    }

    public static void CopyFile(String scrFile,String dstFile,Configuration conf) throws Exception{
        FileInputStream fis = new FileInputStream(new File(scrFile));
        FileSystem fs = FileSystem.get(conf);
        OutputStream os = fs.create(new Path(dstFile));
        IOUtils.copyBytes(fis,os,4096,true);
        fs.close();
    }

    public static void DownloadFile(String srcFile,String destPath,Configuration conf) throws Exception{
        FileSystem fs = FileSystem.get(conf);
        InputStream is = fs.open(new Path(srcFile));
        IOUtils.copyBytes(is,new FileOutputStream(new File(destPath)),2048,true);
        fs.close();
    }

    public static void DeleteFile(String strPath,Configuration conf)throws Exception{
        try{
            FileSystem fs = FileSystem.get(conf);
            fs.delete(new Path(strPath),true);
            fs.close();
        }
        catch (Exception e){

        }
    }

    public static void main(String[] args) throws Exception{
        String dst = "hdfsHandle/";
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS","hdfs://localhost:9000");
        CreateDir(dst,conf);
        CopyFile("/Users/guest/Projects/hadoop/WordCount/input/1901","hdfsHandle/1901.txt",conf);
        DownloadFile("hdfsHandle/1901.txt","/Users/guest/Downloads/1901.txt",conf);
        DeleteFile("hdfsHandle1",conf);
    }
}

