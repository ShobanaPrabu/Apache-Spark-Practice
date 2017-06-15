package QueryPlanner

import Util.Utils
import org.apache.spark.SparkContext

/**
  * Created by vdokku on 6/15/2017.
  */
object DataFrameExample {
  def main(args: Array[String]) {

    val sc = new SparkContext(args(0), "Dataframe creation example")
    val sqlContext = new org.apache.spark.sql.SQLContext(sc)
    val inMemoryDF = Utils.createDataFrame(sqlContext)
    inMemoryDF.explain(true)
  }
}
