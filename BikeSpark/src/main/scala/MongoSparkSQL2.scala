import com.mongodb.spark.MongoSpark
import org.apache.spark.sql.{DataFrame, SparkSession}

/**
  * Created by zx on 2017/10/8.
  * https://docs.mongodb.com/spark-connector/current/scala/datasets-and-sql/
  */
object MongoSparkSQL {

  def main(args: Array[String]): Unit = {

    val session = SparkSession.builder()
      .master("local")
      .appName("MongoSparkConnectorIntro")
      .config("spark.mongodb.input.uri", "mongodb://192.168.255.202:27200,192.168.255.203:27200,192.168.255.204:27200/mobike.bikes?readPreference=secondaryPreferred")
//      .config("spark.mongodb.output.uri", "mongodb://lazy:root@192.168.255.201:27017/lazy_bike.resluts")
      .getOrCreate()

    val df: DataFrame = MongoSpark.load(session)

    df.createTempView("v_bikes")

    //val result:DataFrame = session.sql("SELECT age, name FROM v_student WHERE age >= 30 ORDER BY age DESC")

    //val result = session.sql("SELECT age, name FROM v_student WHERE age is null")

    val pv = session.sql("select * from v_bikes")


    pv.show()
//    val uv = session.sql("select count(*) pv, count(distinct openid) uv from v_logs")

    //pv.show()

    //uv.show()

//    MongoSpark.save(uv)
    //MongoSpark

    session.stop()

  }
}
