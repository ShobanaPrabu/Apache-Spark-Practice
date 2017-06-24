package Util


import org.apache.spark.sql.catalyst.analysis.Analyzer
import org.apache.spark.sql.types.{StringType, StructField, StructType}
import org.apache.spark.sql.{DataFrame, Row, SQLContext}

/**
  * Created by vdokku on 6/15/2017.
  */
object Utils {
  def createDataFrame(sqlContext: SQLContext): DataFrame = {

    val inMemoryData = (0 to 500).map(value => {
      val rowValues = Array(value, value + 1, value + 2, value + 3).map(value => value.toString)
      Row.fromSeq(rowValues)
    })

    val inMemoryRDD = sqlContext.sparkContext.makeRDD(inMemoryData)

    val columnNames = List("c1", "c2", "c3", "c4")

    val columnStruct = columnNames.map(colName => StructField(colName, StringType, true))

    val schema = StructType(columnStruct)

    val inMemoryDF = sqlContext.createDataFrame(inMemoryRDD, schema)
    inMemoryDF

  }

  def getAnalayzer(sqlContext: SQLContext): Analyzer = {
    import scala.reflect.runtime.universe._

    val typeMirror = runtimeMirror(this.getClass.getClassLoader)
    val instanceMirror = typeMirror.reflect(sqlContext)
    val members = instanceMirror.symbol.typeSignature.members

    def fields = members.filter(_.typeSignature <:< typeOf[Analyzer])

    val symbol = fields.head
    instanceMirror.reflectField(symbol.asTerm).get.asInstanceOf[Analyzer]
  }
}
