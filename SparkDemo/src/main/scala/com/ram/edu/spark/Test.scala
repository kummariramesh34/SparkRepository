package com.ram.edu.spark

import org.apache.spark._
import org.apache.spark.streaming._
import org.apache.spark.streaming.StreamingContext._

object Test {
 def main(args: Array[String]) = {
   
  val sc = getSparkContext
  
  val rdd = sc.textFile("file:\\\\\\C:\\Users\\Ramesh Kummari\\Downloads\\InterviewQuestions.txt")
  rdd.collect.foreach { println }
  //Thread.sleep(1000000)
 }
 def getSparkContext() = {
  val conf = new SparkConf().setMaster("local[*]").setAppName("Test")
  val sc = new SparkContext(conf)
  sc
 }
}