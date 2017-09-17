name := "SparkWorks"

version := "1.0"

libraryDependencies ++= Seq("org.apache.spark" % "spark-core_2.10" % "2.1.1",
  "org.apache.spark" % "spark-sql_2.10" % "2.1.1",
  "org.apache.spark" % "spark-streaming_2.10" % "2.1.1",
  "org.apache.spark" % "spark-mllib_2.10" % "2.1.1",
  "org.apache.spark" % "spark-hive_2.10" % "2.1.1",
  "com.databricks" % "spark-avro_2.10" % "1.0.0",
  "org.apache.avro" % "avro" % "1.7.7",
  "org.apache.spark" % "spark-streaming-twitter_2.10" % "1.6.3",
  "org.twitter4j" % "twitter4j-core" % "4.0.6",
  "org.apache.avro" % "avro-mapred" % "1.7.7")

