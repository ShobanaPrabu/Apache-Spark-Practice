package mlib

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.ml.feature.VectorAssembler
import org.apache.spark.ml.regression.LinearRegression
import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.functions._

/**
  * Created by vdokku on 7/5/2017.
  */
object LogisticRegressionWithGradientDescentModel {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("One Click Regression")
      .setMaster("local[2]")

    val sc = new SparkContext(conf)

    val sqlContext = new SQLContext(sc)

    val df = sqlContext.read
      .format("com.databricks.spark.csv")
      .option("header", "true")
      .option("inferSchema", "true")
      .load("my.csv")


    // Some Known Constants
    val twenty: Double = 8.904
    val fifty: Double = 10.2051
    val seventyfive: Double = 11.3665



    val newDf1 = df.withColumn("featureOne", when(df("x") > twenty, df("x")-twenty).otherwise(0))
    val newDf2 = newDf1.withColumn("featureTwo", when(df("x") > fifty, df("x")-fifty).otherwise(0))
    val newDf3 = newDf2.withColumn("featureThree", when(df("x") > seventyfive, df("x")-seventyfive).otherwise(0))


    /**
      * Logic for Conversions to the RDD Data Type
      */
    val assembler = new VectorAssembler()
      .setInputCols(Array("x","featureOne","featureTwo","featureThree"))
      .setOutputCol("features")

    val output = assembler.transform(newDf3)

    val regModelData = output.select("label", "features")


    val target = regModelData.columns.indexOf("label")



    println(regModelData)

    val model = new LinearRegression()
    val results = model.fit(regModelData)



    println(results.coefficients)
    println(results.intercept)
    println(results.summary.predictionCol)

    sc.stop()


  }
}
