package mlib


import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.classification.DecisionTreeClassifier
import org.apache.spark.ml.classification.DecisionTreeClassificationModel
import org.apache.spark.ml.feature.PCA
import org.apache.spark.ml.feature.{ StringIndexer, IndexToString, VectorIndexer }
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.sql.{ DataFrame, Row, SQLContext }


/**
  * Created by vdokku on 6/18/2017.
  */
object MLPipelineExample {


  def main(args: Array[String]): Unit = {


    val conf = new SparkConf().setAppName("LogisticRegressionWithLBFGSExample").setMaster("local[4]")

    System.setProperty("hadoop.home.dir", "C:\\hadoop-common-2.2.0-bin-master\\")

    val sc = new SparkContext(conf)

    val sqlContext = new SQLContext(sc)
    val data = sqlContext.read.format("libsvm").load("C:\\Venkat_DO\\Code_Base\\SparkWorks\\src\\main\\resources\\data\\sample_multiclass_classification_data.txt")

    // By doing the random splitting, we split the data into training (60%) and test (40%)
    //Training dataset will be to TRAIN model & test data is to evalite the model accuracy.

    val Array(trainingData, testData) = data.randomSplit(Array(0.6, 0.4))


    val pca = new PCA().setInputCol("features").setOutputCol("pcaFeatures").setK(2)

    val dt = new DecisionTreeClassifier().setLabelCol("label").setFeaturesCol("pcaFeatures")

    //Next, we chain the PCA and Decision Tree in a Pipeline.

    val pipeline = new Pipeline().setStages(Array(pca,dt))


//    The pipeline is trained using the training data set "trainingData".

    val model = pipeline.fit(trainingData)

    val predictions = model.transform(testData)


    predictions.show(5)

    // If we need to view the columns selectively.

    predictions.select("prediction", "label", "features").show(5)


    val evaluator = new MulticlassClassificationEvaluator().setLabelCol("label").setPredictionCol("prediction").setMetricName("accuracy")
    val accuracy = evaluator.evaluate(predictions)


    println("Accuracy " +  accuracy*100)
    println("Misclassification Error = " + (1.0 - accuracy)*100)

  }
}
