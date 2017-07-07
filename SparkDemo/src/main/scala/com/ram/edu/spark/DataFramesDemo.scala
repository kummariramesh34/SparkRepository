package com.ram.edu.spark

import org.apache.spark._
import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions.monotonically_increasing_id

object DataFramesDemo extends App {
	val spark = SparkSession.builder.master("local").appName("DataFramesDemo").getOrCreate()


			val sqlContext = spark.sqlContext
			val starWarsDF = sqlContext.read
			.format("com.databricks.spark.csv")
			.option("header", "true") // Use first line of all files as header
			.option("inferSchema", "true") // Automatically infer data types
			.option("delimiter", ";")
			.load("file:\\\\\\\\D:\\SparkData\\StarWars.csv")
			
			starWarsDF.show()
			val starWarsWithSlNoDF = starWarsDF.withColumn("SlNo", monotonically_increasing_id())
			starWarsWithSlNoDF.show()
		
}