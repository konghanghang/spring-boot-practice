package com.test;

import org.apache.commons.lang3.StringUtils;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.sql.*;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructType;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class App {

    /**
     * master-url可以是以下任一种形式：
     * local 使用一个Worker线程本地化运行Spark(完全不并行)
     * local[*] 使用逻辑CPU数量的线程来本地化运行Spark
     * local[K] 使用K个Worker线程本地化运行Spark（理想情况下，K应该根据运行机器的CPU核数设定）
     * spark://HOST:PORT 连接到指定的Spark standalone master。默认端口是7077。
     * yarn-client 以客户端模式连接YARN集群。集群的位置可以在HADOOP_CONF_DIR 环境变量中找到。
     * yarn-cluster 以集群模式连接YARN集群。集群的位置可以在HADOOP_CONF_DIR 环境变量中找到。
     * mesos://HOST:PORT 连接到指定的Mesos集群。默认接口是5050.
     * @param args
     */
    public static void main(String[] args) {
        SparkConf conf = new SparkConf()
                .setAppName("my-test").setMaster("local[*]");
        hdfs(conf);
        sql(conf);
    }

    private static void hdfs(SparkConf conf) {
        JavaSparkContext sc = new JavaSparkContext(conf);
        JavaRDD<String> lines = sc.textFile("hdfs://DESKTOP-67PPMIS.localdomain:9000/hello/word/README.txt");
        System.out.println("---------------------");
        JavaRDD<String> stringJavaRDD = lines.filter(line -> StringUtils.isNotBlank(line)).flatMap(line -> {
            List<String> strings = Arrays.asList(line.split(" "));
            return strings.iterator();
        });
        JavaPairRDD<String, Integer> javaPairRDD = stringJavaRDD.filter(line -> StringUtils.isNotBlank(line)).mapToPair(w -> new Tuple2(w, 1));

        JavaPairRDD<String, Integer> reduceByKeyJavaPairRDD = javaPairRDD.reduceByKey((Function2<Integer, Integer, Integer>) (integer, integer2) -> integer + integer2);
        reduceByKeyJavaPairRDD.foreach((VoidFunction<Tuple2<String, Integer>>) stringIntegerTuple2 -> System.out.println(stringIntegerTuple2._1 + ":" + stringIntegerTuple2._2));
        //.foreach((VoidFunction) o -> System.out.println(o));
        // .foreach((VoidFunction<Tuple2>) tuple2 -> System.out.println(tuple2._1 + " " + tuple2._2));
        // .foreach((VoidFunction<String>) s -> System.out.println(s));
        // lines.foreach((VoidFunction<String>) s -> System.out.println(s));
        System.out.println("---------------------");
        sc.close();
    }

    private static void sql(SparkConf conf) {
        SparkSession session = SparkSession.builder().config(conf).getOrCreate();
        SQLContext sqlContext = session.sqlContext();

        String url = "jdbc:mysql://localhost:3306/yslao";
        //查找的表名
        String table = "m_menses";
        //增加数据库的用户名(user)密码(password),指定test数据库的驱动(driver)
        Properties connectionProperties = new Properties();
        connectionProperties.put("user","root");
        connectionProperties.put("password","2222");
        connectionProperties.put("driver","com.mysql.jdbc.Driver");

        System.out.println("读取test数据库中的user_test表内容");
        // 读取表中所有数据
        Dataset<Row> select = sqlContext.read().jdbc(url, table, connectionProperties).select("*");
        //显示数据
        select.show();

        //写入的数据内容
        List<String> strings = Arrays.asList("1 tom 5", "2 jack 6", "3 alex 7");
        JavaSparkContext sparkContext = new JavaSparkContext(session.sparkContext());
        JavaRDD<String> personData = sparkContext.parallelize(strings);

        /**
         * 第一步：在RDD的基础上创建类型为Row的RDD
         */
        //将RDD变成以Row为类型的RDD。Row可以简单理解为Table的一行数据
        JavaRDD<Row> rowJavaRDD = personData.map(person -> {
            String[] split = person.split(" ");
            return RowFactory.create(Integer.valueOf(split[0]), split[1], Integer.valueOf(split[2]));
        });

        /**
         * 第二步：动态构造DataFrame的元数据。
         */
        List structFields = new ArrayList();
        structFields.add(DataTypes.createStructField("id",DataTypes.IntegerType,true));
        structFields.add(DataTypes.createStructField("name",DataTypes.StringType,true));
        structFields.add(DataTypes.createStructField("age",DataTypes.IntegerType,true));

        //构建StructType，用于最后DataFrame元数据的描述
        StructType structType = DataTypes.createStructType(structFields);

        /**
         * 第三步：基于已有的元数据以及RDD<Row>来构造DataFrame
         */
        Dataset<Row> dataset = sqlContext.createDataFrame(rowJavaRDD, structType);

        /**
         * 第四步：将数据写入到person表中
         */
        dataset.write().mode("append").jdbc(url,"person",connectionProperties);

        sparkContext.close();
        session.stop();
    }

}
