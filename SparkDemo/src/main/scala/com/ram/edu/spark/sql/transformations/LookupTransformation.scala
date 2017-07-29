package com.ram.edu.spark.sql.transformations

import org.apache.spark._
import org.apache.spark.sql._
import org.apache.spark.sql.functions.udf
import scala.collection.immutable.Map
class LookupTransformation {
  var inputCol:String = _
  var outputCol: String = _
  var lookupMap: Map[String, String] = _
  
  def setInputCol(inputCol: String)={
    this.inputCol = inputCol
    this
  }
  def setOutputCol(outputCol: String)={
    this.outputCol = outputCol
    this
  }
  def setLookupMap(lookupMap: Map[String, String])={
    this.lookupMap = lookupMap
    this
  }
  
  def getInputCol(): String={
    this.inputCol 
  }
  def getOutputCol(): String={
    this.outputCol 
  }
  def getLookupMap(): Map[String, String]={
    this.lookupMap 
  }
  
  def transform(dataFrame: DataFrame):DataFrame ={
    getLookupMap.foreach { println }
    val lookupUDF = lookup(getLookupMap)
    dataFrame.withColumn(getOutputCol, lookupUDF(dataFrame(getInputCol)))
  }
  
  def lookup(map : Map[String, String])={
   udf { keyCol: String =>  map.get(keyCol)}
  }
}