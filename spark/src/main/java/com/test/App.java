package com.test;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

public class App {

    public static void main(String[] args) {
        SparkConf conf = new SparkConf()
                .setAppName("my-test");

        JavaSparkContext sc = new JavaSparkContext(conf);
        JavaRDD<String> lines = sc.textFile("hdfs://hadoop01:9000/a.txt");
    }

}
