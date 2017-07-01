package examples

import org.apache.spark.sql.SparkSession


/**
  * Created by vdokku on 6/29/2017.
  */
object SparkRankingProblem {

  def main(args: Array[String]): Unit = {

    System.setProperty("hadoop.home.dir", "C:\\hadoop-common-2.2.0-bin-master\\")
    val sparkSession = SparkSession.builder().master("local[4]")
      .appName("my-spark-app")
      .getOrCreate()

    val rdd = sparkSession.sparkContext.parallelize(Seq(("user_1", "object_1", 3),
      ("user_1", "object_2", 2),
      ("user_2", "object_1", 5),
      ("user_2", "object_2", 2),
      ("user_2", "object_2", 6)))



/*

import org.apache.spark.sql.expressions.Window
window = Window.partitionBy(df('user_id')).orderBy(df('score').desc)

df.select('*', rank.over(window).alias('rank')).filter(col('rank') <= 2).show
 */


  }
}
