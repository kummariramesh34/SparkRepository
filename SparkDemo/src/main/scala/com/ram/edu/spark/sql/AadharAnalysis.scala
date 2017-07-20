package com.ram.edu.spark.sql

import org.apache.spark._
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.DataFrame


object AadharAnalysis extends App {

	val spark = SparkSession.builder.master("local").appName("DataFramesDemo").getOrCreate()
			val sqlContext = spark.sqlContext
			val baseFilePath = "file:\\\\\\D:\\SparkData\\"

			val adhaarDbtlFileName = s"${baseFilePath}UIDAI-ENR-DBTL-20170708.csv"
	val adhaarDbtlDF = loadCSVFileAsDataFrame(adhaarDbtlFileName)


			val adhaarDetailFileName = s"${baseFilePath}UIDAI-ENR-DETAIL-20170716.csv"
	val adhaarDetailDF = loadCSVFileAsDataFrame(adhaarDetailFileName)


			val adhaarGenAgeFileName = s"${baseFilePath}UIDAI-ENR-GEN-AGE-20170716.csv"
	val adhaarGenAgeDF = loadCSVFileAsDataFrame(adhaarGenAgeFileName)


			val adhaarGeographyFileName = s"${baseFilePath}UIDAI-ENR-GEOGRAPHY-20170716.csv"
	val adhaarGeographyDF = loadCSVFileAsDataFrame(adhaarGeographyFileName)


			val adhaarRegEaFileName = s"${baseFilePath}UIDAI-ENR-REG-EA-20170716.csv"
	val adhaarRegEaDF = loadCSVFileAsDataFrame(adhaarRegEaFileName)

			getStateWisePopulation

			def getStateWisePopulation()={

		val stateWisePopulation = adhaarDbtlDF.groupBy("StateOrUT").sum("Population")
				stateWisePopulation.explain(true)
				stateWisePopulation.show()
				adhaarDbtlDF.groupBy("StateOrUT").max("Population").show()

	}
	def loadCSVFileAsDataFrame(fileName: String): DataFrame ={
		sqlContext.read
		.format("com.databricks.spark.csv")
		.option("header", "true") // Use first line of all files as header
		.option("inferSchema", "true") // Automatically infer data types
		.option("delimiter", ",")
		.load(fileName)

	}
}