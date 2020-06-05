package com.test;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.VoidFunction;

public class App {

    public static void main(String[] args) {
        SparkConf conf = new SparkConf()
                .setAppName("my-test").setMaster("local[*]");

        JavaSparkContext sc = new JavaSparkContext(conf);
        JavaRDD<String> lines = sc.textFile("hdfs://DESKTOP-67PPMIS.localdomain:9000/hello/word/README.txt");
        System.out.println("---------------------");
        lines.foreach((VoidFunction<String>) s -> System.out.println(s));
        System.out.println("---------------------");
    }

}
