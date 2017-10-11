package StackOverflow

import org.apache.spark.SparkContext
import org.apache.spark.sql.SparkSession

/**
  * Created by vdokku on 10/10/2017.
  */
object GroupByAndDivideOperation {


  //https://stackoverflow.com/questions/39946210/spark-2-0-datasets-groupbykey-and-divide-operation-and-type-safety?rq=1



  case class MyClass (c1: String,
                      c2: String,
                      c3: String,
                      c4: Double)


  case class AnotherClass(key: (String, String, String), sum: Double)


  def main(args: Array[String]): Unit = {

    val spark: SparkSession = SparkSession.builder().master("local[4]").appName("QueryingWithStreamingSources").getOrCreate()
    val sc: SparkContext = spark.sparkContext


/*


Problem #1 - divide operation on aggregated column- Consider below code - I have a DataSet[MyCaseClass] and I wanted to groupByKey on c1,c2,c3 and sum(c4) / 8. The below code works well if I just calculate the sum but it gives compile time error for divide(8). I wonder how I can achieve following.

Problem #2 - converting groupedByKey result to another Typed DataFrame - Now second part of my problem is I want output again a typed DataSet. For that I have another case class (not sure if it is needed) but I am not sure how to map with grouped result -

* */

    import org.apache.spark.sql.expressions.scalalang.typed.{sum => typedSum}
    import org.apache.spark.sql.functions._
    import spark.sqlContext.implicits._



    val eight = lit(8.0)
      .as[Double]  // Not necessary

    val sumByEight = typedSum[MyClass](_.c4)
      .divide(eight)
      .as[Double]  // Required
      .name("div(sum(c4), 8)")



    val myCaseClass = Seq(
      MyClass("a", "b", "c", 2.0),
      MyClass("a", "b", "c", 3.0)
    ).toDS

    myCaseClass
      .groupByKey(myCaseClass => (myCaseClass.c1, myCaseClass.c2, myCaseClass.c3))
      .agg(sumByEight)

/*

+-------+---------------+
|    key|div(sum(c4), 8)|
+-------+---------------+
|[a,b,c]|          0.625|
+-------+---------------+


 */

    myCaseClass
      .groupByKey(myCaseClass => (myCaseClass.c1, myCaseClass.c2, myCaseClass.c3))
      .agg(typedSum[MyClass](_.c4).name("sum"))
      .as[AnotherClass]

/*

+-------+---+
|    key|sum|
+-------+---+
|[a,b,c]|5.0|
+-------+---+


 */

/*
https://stackoverflow.com/questions/46549688/euclidean-distance-in-spark-2-1
https://stackoverflow.com/questions/40057563/replace-missing-values-with-mean-spark-dataframe


https://stackoverflow.com/questions/46540839/using-spark-how-do-i-read-multiple-files-in-parallel-from-different-folders-in
https://stackoverflow.com/questions/46541998/queries-with-streaming-sources-must-be-executed-with-writestream-start
https://stackoverflow.com/questions/46543424/spark-dataframe-convert-element-to-string
https://stackoverflow.com/questions/46543435/value-tail-is-not-a-member-of-string-string
https://stackoverflow.com/questions/46545754/is-there-a-hook-for-executor-startup-in-spark
https://stackoverflow.com/questions/46545911/java-lang-outofmemoryerror-requested-array-size-exceeds-vm-limit-during-pyspa
https://stackoverflow.com/questions/40057563/replace-missing-values-with-mean-spark-dataframe
https://stackoverflow.com/questions/46549688/euclidean-distance-in-spark-2-1
https://stackoverflow.com/questions/46555685/how-to-pipe-a-grouped-by-key-rdd
https://stackoverflow.com/questions/46557471/in-apache-spark-csv-we-are-giving-the-delimiter-for-field-but-how-to-give-delimi
https://stackoverflow.com/questions/46559664/spark-dataframe-groupby-and-compute-complex-aggregate-function
https://stackoverflow.com/questions/46564195/when-function-in-not-working-in-spark-data-frame-with-auto-detect-schema
https://stackoverflow.com/questions/46566737/comparing-columns-in-two-data-frame-in-spark
https://stackoverflow.com/questions/46566374/spark-scala-nested-structtype-conversion-to-map
https://stackoverflow.com/questions/46567578/poor-weak-scaling-of-apache-spark-join-operation
https://stackoverflow.com/questions/46569591/error-calculating-squared-distance-in-spark-scala
https://stackoverflow.com/questions/46568435/how-to-create-schema-structtype-with-one-or-many-structtypes
https://stackoverflow.com/questions/46572541/scala-dynamically-joining-data-frames
https://stackoverflow.com/questions/46576057/create-column-with-a-running-total-in-a-spark-dataset
https://stackoverflow.com/questions/46578034/difference-between-registertemptable-and-createtempview-in-apache-spark
https://stackoverflow.com/questions/46578759/update-schema-for-dataframe-in-apache-spark
https://stackoverflow.com/questions/46580253/collect-list-by-preserving-order-based-on-another-variable

*/



    import org.apache.spark.sql.functions._
    import org.apache.spark.ml.linalg.Vector

    val euclidean = udf((v1: Vector, v2: Vector) => ???)  // Fill with preferred logic

    val jP2 = jP.withColumn("dist", euclidean($"features", $"episodeFeatures"))




  }
}
