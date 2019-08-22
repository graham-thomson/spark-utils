import org.apache.spark.sql.SparkSession
import org.apache.spark.ml.linalg._
import org.apache.spark.sql.expressions.UserDefinedFunction

import math.sqrt

object FeatureUDF {

  def addArrays (a: Array[Double], b: Array[Double]): Array[Double] = {
    a.zip(b).map { case (x, y) => x + y}
  }

  def subtractArrays (a: Array[Double], b: Array[Double]): Array[Double] = {
    a.zip(b).map { case (x, y) => x - y}
  }

  def l1Norm (a: Array[Double]): Double = {
    a.sum
  }

  def l2Norm (a: Array[Double]): Double = {
    sqrt(a.map(x => x * x).sum)
  }

  def nonZeroIndices(a: Array[Double]): Array[Int] = {
    a.zipWithIndex.collect{
      case x if x._1 != 0.0 => x._2
    }
  }

  def jaccardDistance(a: Array[Double], b: Array[Double]): Double = {
    1 - (nonZeroIndices(a).intersect(nonZeroIndices(b)).length.toDouble/
      nonZeroIndices(a).union(nonZeroIndices(b)).toSet.size.toDouble)
  }

  def euclideanDistance (a: Array[Double], b: Array[Double]): Double = {
    l2Norm(subtractArrays(a, b))
  }

  def jaccardDistanceVector(a: Vector, b: Vector): Double = {
    jaccardDistance(a.toArray, b.toArray)
  }

  def euclideanDistanceVector (a: Vector, b: Vector): Double = {
    euclideanDistance(a.toArray, b.toArray)
  }

  def registerUdf: UserDefinedFunction = {
    val spark = SparkSession.builder().getOrCreate()
    spark.udf.register("euclideanDistance", (a: Vector, b: Vector) => euclideanDistanceVector(a, b))
    spark.udf.register("jaccardDistance", (a: Vector, b: Vector) => jaccardDistanceVector(a, b))
  }
}
