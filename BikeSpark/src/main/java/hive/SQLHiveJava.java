package hive;


import org.apache.spark.sql.SparkSession;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/4/3.
 */
public class SQLHiveJava {
    public static class Record implements Serializable {
        private int key;
        private String value;

        public int getKey() {
            return key;
        }

        public void setKey(int key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }



    public static void main(String[] args) {

//        String warehouseLocation = new File("hdfs://mycluster/user/hive/warehouse").getAbsolutePath();
        SparkSession spark = SparkSession
                .builder()
                .appName("Java Spark Hive Example")
                .master("local[*]")
                .config("spark.sql.warehouse.dir","hdfs://mycluster/user/hive/warehouse")
                .enableHiveSupport()
                .getOrCreate();

////        spark.sql("CREATE TABLE IF NOT EXISTS src (key INT, value STRING) USING hive");
////        spark.sql("LOAD DATA LOCAL INPATH 'examples/src/main/resources/kv1.txt' INTO TABLE src");
//
//// Queries are expressed in HiveQL
//        spark.sql("SELECT * FROM mobike.logs").show();

//        spark.sql("CREATE TABLE IF NOT EXISTS src (key INT, value STRING) USING hive");
//        spark.sql("LOAD DATA LOCAL INPATH 'examples/src/main/resources/kv1.txt' INTO TABLE src");

// Queries are expressed in HiveQL
        spark.sql("show databases").show();


























//        String warehouseLocation = "/spark-warehouse";
//
//        SparkSession spark = SparkSession
//                .builder()
//                .appName("Java Spark Hive Example")
//                .master("local[*]")
//                .config("spark.sql.warehouse.mobike", warehouseLocation)
//                .enableHiveSupport()
//                .getOrCreate();
//
//        Dataset<Row> df = spark.sql("select * from mobike.logs");
//        df.show();


    }
}
