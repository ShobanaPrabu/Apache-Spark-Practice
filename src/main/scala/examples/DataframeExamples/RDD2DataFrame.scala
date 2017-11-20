package examples.DataframeExamples

import org.apache.spark.sql.SparkSession

/**
  * Created by vdokku on 11/8/2017.
  */
object RDD2DataFrame {

  def main(args: Array[String]): Unit = {

    System.setProperty("hadoop.home.dir", "C:\\hadoop-common-2.2.0-bin-master")
    val sparkSession = SparkSession.builder().master("local[4]")
      .appName("<<<< Aggregate by key test >>>>>")
      .getOrCreate()

    /*val sc = sparkSession.sparkContext
    val sqlContext = sparkSession.sqlContext


    import sqlContext.implicits._
    val lines = sc.textFile("")


    val dataFrameLines = lines.map(rec => rec.split(" ")).map(x => People(x(0).toInt, x(1), x(2).toInt)).toDS()

    dataFrameLines.createOrReplaceGlobalTempView("peoples")

    val low = sqlContext.sql("select * from peoples where age < 10")
    low.map(_.getValuesMap(List("id", "name", "age"))).collect().foreach(println)


    sc.stop()*/
  }
}


case class People(var id: Int, var name: String, var age: Int)
