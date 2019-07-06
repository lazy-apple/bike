package kafka

import java.util.{Calendar, Date}

import com.google.gson.Gson
import org.apache.spark._
import org.apache.spark.streaming._
import org.apache.spark.streaming.StreamingContext._
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.kafka010._
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe

/**
  * @author LaZY(李志一) 
  * @create 2019-07-05 12:40 
  */
object KafkaSteaming {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
      .setAppName("KafkaSteaming")
      .setMaster("local[4]")

    val streamingContext = new StreamingContext(conf, Seconds(5))

    val kafkaParams = Map[String, Object](
      "bootstrap.servers" -> "s202:9092,s203:9092,s204:9092",
      "key.deserializer" -> classOf[StringDeserializer],
      "value.deserializer" -> classOf[StringDeserializer],
      "group.id" -> "group_15_10",
      "auto.offset.reset" -> "earliest",
      "enable.auto.commit" -> (false: java.lang.Boolean)
    )


    val topics = Array("track")
    val stream = KafkaUtils.createDirectStream[String, String](
      streamingContext,
      PreferConsistent,
      Subscribe[String, String](topics, kafkaParams)
    )



//   ==================下面处理json=============================
    stream.map(record => handleMessage2CaseClass(record.value())).foreachRDD(rdd => {
      val date = new Date();

      var y = MyDate.getYear(date);
      var m = MyDate.getMonth(date);
      var d = MyDate.getDay(date);
      var h = MyDate.getHour(date)
      var mi = MyDate.getMinute(date);
      var s = MyDate.getSecond(date);
      var da = "year="+y+"/month="+m+"/day="+d+"/hour="+h+"/minute="+mi+"/second="+s
      print(da)
      val spark = SparkSession.builder().config(rdd.sparkContext.getConf).getOrCreate()
      val df = spark.createDataFrame(rdd)
      df.write.format("csv").mode("overwrite").save("hdfs://s201/user/hive/warehouse/mobike.db/logs/"+da)
      df.show()

    })


    def handleMessage2CaseClass(jsonStr: String): KafkaMessage = {
      val gson = new Gson()
      gson.fromJson(jsonStr, classOf[KafkaMessage])
    }


    streamingContext.start()
    streamingContext.awaitTermination()
  }


}
case class KafkaMessage(phoneNum: String, amount: String, date: String, lat: String, log: String, province: String, city: String, district: String, model: String,language:String)
