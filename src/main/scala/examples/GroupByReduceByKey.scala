package examples

import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by vdokku on 6/15/2017.
  */
object GroupByReduceByKey {
  def main(args: Array[String]) {
    val conf = new SparkConf().setAppName("Spark-GroupByKey-ReduceByKey-Example").setMaster("local[4]")
    val sc = new SparkContext(conf)
    val words = Array("a", "b", "b", "c", "d", "e", "a", "b", "b", "c", "d", "e", "b", "b", "c", "d", "e")
    val wordPairsRDD = sc.parallelize(words).map(word => (word, 1))

    val wordCountsWithReduce = wordPairsRDD
      .reduceByKey(_ + _)
      .collect()
    wordCountsWithReduce.foreach(f => println(f))

    //Avoid GroupByKey
    println("Avoid GroupByKey")
    val wordCountsWithGroup = wordPairsRDD
      .groupByKey()
      .map(t => (t._1, t._2.sum))
      .collect()
    wordCountsWithGroup.foreach(f => println(f))



  }
}
