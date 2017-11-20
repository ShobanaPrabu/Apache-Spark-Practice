package StackOverflow

import breeze.linalg
import org.apache.spark.SparkContext
import org.apache.spark.sql.SparkSession

/**
  * Created by vdokku on 10/10/2017.
  */
object Spark_SQL_WindowFunction {


  def main(args: Array[String]): Unit = {


    /*

    +----------------+----------+-------------+
|       user_name|login_date|became_active|
+----------------+----------+-------------+
|SirChillingtonIV|2012-01-04|   2012-01-04|
|Booooooo99900098|2012-01-04|   2012-01-04|
|Booooooo99900098|2012-01-06|   2012-01-04|
|  OprahWinfreyJr|2012-01-10|   2012-01-10|
|SirChillingtonIV|2012-01-11|   2012-01-11|
+----------------+----------+-------------+



I have the following data:

+-----+----+-----+
|event|t   |type |
+-----+----+-----+
| A   |20  | 1   |
| A   |40  | 1   |
| B   |10  | 1   |
| B   |20  | 1   |
| B   |120 | 1   |
| B   |140 | 1   |
| B   |320 | 1   |
| B   |340 | 1   |
| B   |360 | 7   |
| B   |380 | 1   |
+-----+-----+----+
And what I want is something like this:

+-----+----+----+
|event|t   |grp |
+-----+----+----+
| A   |20  |1   |
| A   |40  |1   |
| B   |10  |2   |
| B   |20  |2   |
| B   |120 |3   |
| B   |140 |3   |
| B   |320 |4   |
| B   |340 |4   |
| B   |380 |5   |
+-----+----+----+







SQL	DataFrame API
Ranking functions	                      rank	rank
                                        dense_rank	denseRank
                                         percent_rank	percentRank
                                         ntile	ntile
                                         row_number	rowNumber



Analytic functions	        cume_dist	cumeDist
                            first_value	firstValue
                            last_value	lastValue
                            lag	lag
                            lead	lead





    val spark: SparkSession = SparkSession.builder().master("local[4]").appName("QueryingWithStreamingSources").getOrCreate()
    val sc: SparkContext = spark.sparkContext
    import spark.sqlContext.implicits._

    import org.apache.spark.sql.expressions.Window
    import org.apache.spark.sql.functions._


    val windowSpec = Window.partitionBy("event").orderBy("t")

    val newSession = (coalesce(
      ($"t" - lag($"t", 1).over(windowSpec)),
      lit(0)
    ) > 50).cast("bigint")

    val sessionized = df.withColumn("session", linalg.sum(newSession).over(userWindow))


    val newSession =  (coalesce(
      ($"t" - lag($"t", 1).over(windowSpec)),
      lit(0)
    ) > 50 || lead($"type",1).over(windowSpec) =!= 7 ).cast("bigint")




    val newSession =  (coalesce(
      ($"t" - lag($"t", 1).over(win)),
      lit(0)
    ) > 50
      or $"type"===7) // also start new group in this case
      .cast("bigint")

    df.withColumn("session", sum(newSession).over(win))
      .where($"type"=!=7) // remove these rows
      .orderBy($"event",$"t")
      .show



//    val jdbcDF = spark.sqlContext.read.format("jdbc").options("").load()
      val jdbcDF = spark.sqlContext.read.format("jdbc").load()



    jdbcDF.groupBy("VA_HOSTNAME").agg(count("*")).show()


*/


  }
}
