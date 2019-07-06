package hive

/**
  * @author LaZY(李志一) 
  * @create 2019-07-06 18:37 
  */
object Hivetest {
  import java.io.File

  import org.apache.spark.sql.{Row, SaveMode, SparkSession}

  case class Record(key: Int, value: String)

  // warehouseLocation points to the default location for managed databases and tables
  val warehouseLocation = new File("spark-warehouse").getAbsolutePath

  val spark = SparkSession
    .builder()
    .appName("Spark Hive Example")
    .config("spark.sql.warehouse.dir", warehouseLocation)
    .enableHiveSupport()
    .getOrCreate()

  import spark.implicits._
  import spark.sql

  sql("CREATE TABLE IF NOT EXISTS src (key INT, value STRING) USING hive")
  sql("LOAD DATA LOCAL INPATH 'examples/src/main/resources/kv1.txt' INTO TABLE src")

  // Queries are expressed in HiveQL
  sql("SELECT * FROM src").show()

}
