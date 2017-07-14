package com.ram.edu.spark.sql

import org.apache.spark._
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions.monotonically_increasing_id

object DataFramesDemo extends App {
	val spark = SparkSession.builder.master("local").appName("DataFramesDemo").getOrCreate()


	    val homeDir = args(0) //Provide the project path as command line args
			val sqlContext = spark.sqlContext
			val starWarsDF = sqlContext.read
			.format("com.databricks.spark.csv")
			.option("header", "true") // Use first line of all files as header
			.option("inferSchema", "true") // Automatically infer data types
			.option("delimiter", ";")
			.load(s"file:\\\\\\$homeDir\\data\\StarWars.csv")
			
			starWarsDF.show()
			val starWarsWithSlNoDF = starWarsDF.withColumn("SlNo", monotonically_increasing_id())
			starWarsWithSlNoDF.show()
		val subWayDF = sqlContext.read
			.format("com.databricks.spark.csv")
			.option("header", "true") 
			.option("inferSchema", "true") 		
			.load(s"file:\\\\\\$homeDir\\data\\DOITT_SUBWAY_STATION_01_13SEPT2010.csv")
			
			
			subWayDF.show
}