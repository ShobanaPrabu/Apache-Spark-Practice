package mlib

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.regression._
import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.functions._
/**
  * Created by vdokku on 7/5/2017.
  */
object LogisticRegressionWithGradientDescent {
  def main(args: Array[String]) {
    /**
      * Configurations for the Spark Application
      */
    val conf = new SparkConf().setAppName("One Click Regression")
      .setMaster("local[2]")

    val sc = new SparkContext(conf)

    val sqlContext = new SQLContext(sc)

    val df = sqlContext.read
      .format("com.databricks.spark.csv")
      .option("header", "true")
      .option("inferSchema", "true")
      .load("my.csv")


    // Known Constants
    val twenty: Double = 8.904
    val fifty: Double = 10.2051
    val seventyfive: Double = 11.3665


    val newDf1 = df.withColumn("featureOne", when(df("x") > twenty, df("x") - twenty).otherwise(0))
    val newDf2 = newDf1.withColumn("featureTwo", when(df("x") > fifty, df("x") - fifty).otherwise(0))
    val newDf3 = newDf2.withColumn("featureThree", when(df("x") > seventyfive, df("x") - seventyfive).otherwise(0))


    newDf3.describe().show()


    val features = List("x", "featureOne", "featureTwo", "featureThree").map(newDf3.columns.indexOf(_))

    val label = newDf3.columns.indexOf("label")

    val myRDD = newDf3.rdd.map(r => LabeledPoint(r.getDouble(label), Vectors.dense(features.map(r.getDouble(_)).toArray)))

    val model = LinearRegressionWithSGD.train(myRDD, 100)


    println(model.weights)
    print(model.intercept)

  }
}
