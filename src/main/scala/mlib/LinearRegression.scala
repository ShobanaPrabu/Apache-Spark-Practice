package mlib


import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.regression.{LabeledPoint, LinearRegressionWithSGD}
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by vdokku on 6/17/2017.
  */
object LinearRegression {

  def main(args: Array[String]): Unit = {
    System.setProperty("hadoop.home.dir", "C:\\hadoop-common-2.2.0-bin-master\\")

    val sparkConf = new SparkConf().setAppName("<<<< Aggregate by key test >>>>> ")
      .setMaster("local[4]")

    val sc = new SparkContext(sparkConf)

//    val data = sc.textFile("src/main/resources/spark_data/lpsa.data")
      val data = sc.textFile("C:\\Venkat_Downloads\\SparkWork-master\\src\\main\\resources\\data\\ridge-data\\lpsa.data")

    val parsedData = data.map { line =>
      val parts = line.split(',')
      LabeledPoint(parts(0).toDouble, Vectors.dense(parts(1).split(' ').map(_.toDouble)))
    }.cache()

    // Build the model
    var regression = new LinearRegressionWithSGD().setIntercept(true)
    regression.optimizer.setStepSize(0.1)
    val model = regression.run(parsedData)

    // Evaluate model on training examples and compute training error
    val valuesAndPreds = parsedData.map { point =>
      val prediction = model.predict(point.features)
      (point.label, prediction)
    }
    val MSE = valuesAndPreds.map{case(v, p) => math.pow((v - p), 2)}.mean()
    println("training Mean Squared Error = " + MSE)

  }
}
