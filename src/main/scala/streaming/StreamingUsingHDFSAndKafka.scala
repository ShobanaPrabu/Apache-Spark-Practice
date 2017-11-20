package streaming

import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Duration, StreamingContext}

import scala.collection.immutable.HashMap

/**
  * Created by vdokku on 11/8/2017.
  */
object StreamingUsingHDFSAndKafka {

  def main(args: Array[String]): Unit = {

    val sparkSession: SparkSession = SparkSession.builder().master("local[4]").appName("SparkStreamingOnHDFS").getOrCreate()

    // Specify the check point directory here.
    val checkpointDirectory = "hdfs://master:9000/library/SparkStreaming/Checkpoint_Data"

    val streamingContext = StreamingContext.getOrCreate(checkpointDirectory,
                              () => new StreamingContext(sparkSession.sparkContext, Duration.apply(5)))

    val lines = streamingContext.textFileStream("hdfs://master:9000/library/SparkStreaming/Data")
    // --> This is the directory specified

    // What is the purpose of the checkpoint directory.



    lines.flatMap(line => line.split(" "))
            .map(recEntry => (recEntry, 1))
              .reduceByKey(_ + _)


    streamingContext.start()
    streamingContext.awaitTermination()
    streamingContext.stop()



    // --------------------------------------------------------------------------------------


    var map =new HashMap[String, Int].empty
    map += ("HelloKafka" -> 2)

    /*

    def createStream(
      ssc: StreamingContext,
      zkQuorum: String,
      groupId: String,
      topics: Map[String, Int],
      storageLevel: StorageLevel = StorageLevel.MEMORY_AND_DISK_SER_2
    ): ReceiverInputDStream[(String, String)] = {
    val kafkaParams = Map[String, String](
      "zookeeper.connect" -> zkQuorum, "group.id" -> groupId,
      "zookeeper.connection.timeout.ms" -> "10000")
    createStream[String, String, StringDecoder, StringDecoder](
      ssc, kafkaParams, topics, storageLevel)
  }

     */

    val streamLines = KafkaUtils.createStream(streamingContext,
      "master:2181,worker1:2181,worker2:2181", "MyConsumerGroup", map)

    streamLines.flatMap(message => message._2.split(" ")).map(word => (word, 1)).reduceByKey(_+_).print()

    streamingContext.start()
    streamingContext.awaitTermination()
    streamingContext.stop()




  }

}
