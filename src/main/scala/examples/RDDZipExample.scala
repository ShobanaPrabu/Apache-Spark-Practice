package examples

import org.apache.spark.sql.SparkSession

/**
  * Created by vdokku on 6/25/2017.
  */
object RDDZipExample {


  def main(args: Array[String]): Unit = {


    val sparkSession = SparkSession.builder().master("local[4]")
      .appName("<<<< Aggregate by key test >>>>>")
      .getOrCreate()

    val sc = sparkSession.sparkContext
    val sqlContext = sparkSession.sqlContext

  }
}
