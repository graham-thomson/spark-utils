import org.apache.spark.sql.SparkSession
import org.apache.spark.ml.linalg._
import org.apache.spark.sql.expressions.UserDefinedFunction

import math.sqrt

object FeatureUDF {

  def subtractArrays (a: Array[Double], b: Array[Double]): Array[Double] = {
    a.zip(b).map { case (x, y) => x - y}
  }

  def l2Norm (a: Array[Double]): Double = {
    sqrt(a.map(x => x * x).sum)
  }

  def euclideanDistance (a: Vector, b: Vector): Double = {
    l2Norm(subtractArrays(a.toArray, b.toArray))
  }

  def registerUdf: UserDefinedFunction = {
    val spark = SparkSession.builder().getOrCreate()
    spark.udf.register("euclideanDistance", (a: Vector, b: Vector) => euclideanDistance(a, b))
  }
}
