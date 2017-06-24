package QueryPlanner


import Util.Utils
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.Row
import org.apache.spark.sql.types.{StringType, StructField, StructType}

/**
  * Created by vdokku on 6/15/2017.
  */
object FilterExample {
  def main(args: Array[String]) {

    val sparkConf = new SparkConf().setAppName("<<<< FOLD by key test >>>>> ")
      .setMaster("local[4]")

    val sc = new SparkContext(args(0), "Optimization example")
    val sqlContext = new org.apache.spark.sql.SQLContext(sc)
    val inMemoryDF = Utils.createDataFrame(sqlContext)
    val filteredDF = inMemoryDF.filter("c1 != 0").filter("c2 != 0")
    filteredDF.explain(true)
  }
}
