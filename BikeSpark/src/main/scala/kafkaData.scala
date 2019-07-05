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

/**显示集成kafka产生的数据
  * @author LaZY(李志一) 
  * @create 2019-07-05 12:40 
  */
object KafkaSteaming {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
      .setAppName("KafkaSteaming")
      .setMaster("local[4]")

    val streamingContext = new StreamingContext(conf, Seconds(10))

    val kafkaParams = Map[String, Object](
      "bootstrap.servers" -> "s202:9092,s203:9092,s204:9092",
      "key.deserializer" -> classOf[StringDeserializer],
      "value.deserializer" -> classOf[StringDeserializer],
      "group.id" -> "group_16_59",
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
      val spark = SparkSession.builder().config(rdd.sparkContext.getConf).getOrCreate()
      val df = spark.createDataFrame(rdd)
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
case class KafkaMessage(phoneNum: String, amount: String, date: String, lat: String, log: String, province: String, city: String, district: String)
