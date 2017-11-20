package mlib

import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.regression.{LabeledPoint, LinearRegressionWithSGD}

/**
  *
  *
  *
  *  http://www.bmc.com/blogs/sgd-linear-regression-example-apache-spark/
  *
  * Created by vdokku on 7/5/2017.
  */
object SGDLinearRegression {

  import org.apache.spark.sql.SparkSession
  System.setProperty("hadoop.home.dir", "C:\\Venkata_DO\\hadoop-common-2.2.0-bin-master")
  val spark: SparkSession =
    SparkSession
      .builder()
      .appName("Time Usage")
      .config("spark.master", "local")
      .getOrCreate()

  // For implicit conversions like converting RDDs to DataFrames
  def main(args: Array[String]): Unit = {


    val data = spark.sparkContext.textFile("src/main/resources/data/ridge-data/lpsa.data")

    val parsedData = data.map { line =>
      val x : Array[String] = line.replace(",", " ").split(" ")
      val y = x.map{ (a => a.toDouble)}
      val d = y.size - 1
      val c = Vectors.dense(y(0),y(d))
      LabeledPoint(y(0), c)
    }.cache()

    val numIterations = 100
    val stepSize = 0.00000001
    val model = LinearRegressionWithSGD.train(parsedData, numIterations, stepSize)

    val valuesAndPreds = parsedData.map { point =>
      val prediction = model.predict(point.features)
      (point.label, prediction)
    }

    valuesAndPreds.foreach((result) => println(s"predicted label: ${result._1}, actual label: ${result._2}"))

    val MSE = valuesAndPreds.map{ case(v, p) => math.pow((v - p), 2) }.mean()
    println("training Mean Squared Error = " + MSE)
  }
}
