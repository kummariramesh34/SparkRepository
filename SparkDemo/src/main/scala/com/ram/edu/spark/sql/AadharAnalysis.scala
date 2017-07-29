package com.ram.edu.spark.sql

import org.apache.spark._
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.DataFrame
import com.ram.edu.spark.sql.transformations._

object AadharAnalysis extends App {

	val spark = SparkSession.builder.master("local").appName("DataFramesDemo").getOrCreate()
			val sqlContext = spark.sqlContext

			var adhaarDbtlDF:DataFrame = _
			var adhaarDetailDF:DataFrame = _
			var adhaarGenAgeDF:DataFrame = _
			var adhaarGeographyDF:DataFrame = _
			var adhaarRegEaDF:DataFrame = _

			loadInputs
			
			val lookupMap = adhaarDbtlDF
			                            .select("DistrictCode", "District")
			                            .collect
			                            .map( x => {
			                              (x.getAs[String]("DistrictCode"), x.getAs[String]("District"))
			                            }).toMap
			val lookupTransformation = new LookupTransformation()
			    .setInputCol("DistrictCode")
			    .setOutputCol("LookupDistrict")
			    .setLookupMap(lookupMap)
			
			lookupTransformation.transform(adhaarDbtlDF).show()
			//getStateWisePopulation

			def loadInputs()={
		val baseFilePath = "file:\\\\\\D:\\SparkData\\"
				val adhaarDbtlFileName = s"${baseFilePath}UIDAI-ENR-DBTL-20170708.csv"
		adhaarDbtlDF = loadCSVFileAsDataFrame(adhaarDbtlFileName)


				val adhaarDetailFileName = s"${baseFilePath}UIDAI-ENR-DETAIL-20170716.csv"
		adhaarDetailDF = loadCSVFileAsDataFrame(adhaarDetailFileName)


				val adhaarGenAgeFileName = s"${baseFilePath}UIDAI-ENR-GEN-AGE-20170716.csv"
		adhaarGenAgeDF = loadCSVFileAsDataFrame(adhaarGenAgeFileName)


				val adhaarGeographyFileName = s"${baseFilePath}UIDAI-ENR-GEOGRAPHY-20170716.csv"
		adhaarGeographyDF = loadCSVFileAsDataFrame(adhaarGeographyFileName)


				val adhaarRegEaFileName = s"${baseFilePath}UIDAI-ENR-REG-EA-20170716.csv"
		adhaarRegEaDF = loadCSVFileAsDataFrame(adhaarRegEaFileName)

	}

	def getStateWisePopulation()={

		val stateWisePopulation = adhaarDbtlDF.groupBy("StateOrUT").sum("Population")
				stateWisePopulation.explain(true)
				stateWisePopulation.show()				
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