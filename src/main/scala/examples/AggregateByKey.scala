package examples

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.{SQLContext, SparkSession}

/**
  * Created by vdokku on 6/15/2017.
  */
object AggregateByKey {

  def myfunc(index: Int, iter: Iterator[(String, Int)]): Iterator[String] = {
    iter.toList.map(x => "[partID:" + index + ", val: " + x + "]").iterator
  }


  def main(args: Array[String]): Unit = {

    val sparkSession = SparkSession.builder().master("local[4]")
      .appName("<<<< Aggregate by key test >>>>>")
      .getOrCreate()

    val sc = sparkSession.sparkContext
    val sqlContext = sparkSession.sqlContext


    val pairRDD = sc.parallelize(List(("cat", 2), ("cat", 5), ("mouse", 4), ("cat", 12), ("dog", 12), ("mouse", 2)), 2)

    //lets have a look at what is in the partitions
    pairRDD.mapPartitionsWithIndex(myfunc).collect.foreach(f => println(f))
    println("***********************************************")

    pairRDD.aggregateByKey(0)(math.max(_, _), _ + _).collect.foreach(f => println(f))
    println("-----------------------------------------------")

    pairRDD.aggregateByKey(100)(math.max(_, _), _ + _).collect.foreach(f => println(f))


  }
}
