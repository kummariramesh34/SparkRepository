package com.ram.edu.spark.ml

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._
import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.classification.DecisionTreeClassificationModel
import org.apache.spark.ml.classification.DecisionTreeClassifier
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator
import org.apache.spark.ml.feature.{IndexToString, StringIndexer, VectorIndexer}
import org.apache.spark.ml.feature._
import org.apache.spark.ml.classification.RandomForestClassifier

object MyRandomForestModel extends App {

	val homeDir = "file:\\\\\\D:\\SparkData\\"
			val spark = SparkSession.builder.master("local").appName("MyRandomForestModel").getOrCreate()
			// Load training data
			val fileName = s"file:\\\\\\$homeDir\\data\\sample_libsvm_data.txt"

			val sqlContext = spark.sqlContext
			val starWarsDF = sqlContext.read
			.format("com.databricks.spark.csv")
			.option("header", "true") // Use first line of all files as header
			.option("inferSchema", "true") // Automatically infer data types
			.option("delimiter", ";")
			.option("quote", "\"")
			.load(s"$homeDir\\StarWars.csv")

			starWarsDF.printSchema()
			starWarsDF.take(10).foreach(println)

			val df1 = starWarsDF.filter("eyecolor is not null").withColumn("class", expr("CASE WHEN `eyecolor` = 'blue' THEN 1.0 WHEN `eyecolor` = 'brown' THEN 2.0 WHEN `eyecolor` = 'bluegray' THEN 3.0 WHEN `eyecolor` = 'yellow' THEN 4.0 END"))
			df1.take(100).foreach( println )
			val assembler = new VectorAssembler().setInputCols(Array("height", "weight")).setOutputCol("features")
			val df3 = assembler.transform(df1)

			df3.printSchema()
			df3.take(10).foreach(println)

			val labelIndexer = new StringIndexer().setInputCol("class").setOutputCol("label")
			val df4 = labelIndexer.fit(df3).transform(df3)

			df4.printSchema()
			df4.take(10).foreach(println)

			val splitSeed = 4 
			val Array(trainingData, testData) = df4.randomSplit(Array(0.7, 0.3))

			val classifier = new RandomForestClassifier()
	.setImpurity("gini")
	.setMaxDepth(3)
	.setNumTrees(20)
	.setFeatureSubsetStrategy("auto")
	//.setSeed(4)
	val model = classifier.fit(trainingData)

	val predictions = model.transform(testData)
	predictions.select("name", "label", "prediction").show(100)
}