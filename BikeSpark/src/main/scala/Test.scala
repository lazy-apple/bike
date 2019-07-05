import org.apache.spark.{SparkConf, SparkContext}

import scala.util.parsing.json.JSON

/**
  * @author LaZY(李志一) 
  * @create 2019-07-05 0:21 
  */
object Test {

  def main(args: Array[String]): Unit = {
    //创建Spark配置对象
    val conf = new SparkConf();
    conf.setAppName("WordCountScala")
    //设置master属性
    conf.setMaster("local") ;

    //通过conf创建sc
    val sc = new SparkContext(conf);

    val sqlContext = new org.apache.spark.sql.SQLContext(sc)
    //加载文本文件
    val rdd1 = sc.textFile("E:\\IDEA_workspace\\bike\\BikeSpark\\src\\main\\log-.1562255728861");

    var res = rdd1.map(s => JSON.parseRaw("province"))
    val r = res.collect()
    r.foreach(println)

//    val df = sc.read.json("hdfs://localhost:9000/testdata/person.json")
//    df.show() //打印数据
//    df.printSchema() // 打印元数据
//    df.select($"name", $"age" + 1).show() // 使用表达式，scala的语法，要用$符号作为前缀
//    df.select("name").show() // select操作，典型的弱类型，untyped操作
//    df.createOrReplaceTempView("person") // 基于dataframe创建临时视图
//    spark.sql("SELECT * FROM person").show() // 用SparkSession的sql()函数就可以执行sql语句，默认是针对创建的临时视图
  }

}
