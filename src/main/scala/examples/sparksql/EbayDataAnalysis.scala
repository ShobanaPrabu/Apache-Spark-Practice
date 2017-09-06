package examples.sparksql

import org.apache.spark.sql.SparkSession

/**
  * Created by vdokku on 7/1/2017.
  */
object EbayDataAnalysis {
  def main(args: Array[String]): Unit = {

    System.setProperty("hadoop.home.dir", "C:\\hadoop-common-2.2.0-bin-master\\")
    val sparkSession = SparkSession.builder().master("local[4]")
      .appName("my-spark-app")
      .getOrCreate()

    // Loading the CSV.



  }
}
