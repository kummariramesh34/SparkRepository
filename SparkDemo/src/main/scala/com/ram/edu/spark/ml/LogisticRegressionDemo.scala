package com.ram.edu.spark.ml

import org.apache.spark._
import org.apache.spark.sql.SparkSession
import org.apache.spark.ml.classification.LogisticRegression

object LogisticRegressionDemo extends App {

	val homeDir = args(0) //Provide the project path as command line args
			val spark = SparkSession.builder.master("local").appName("LogisticRegressionDemo").getOrCreate()
			// Load training data
			val fileName = s"file:\\\\\\$homeDir\\data\\sample_libsvm_data.txt"
			println("########## :: "+fileName)
			val training = spark.read.format("libsvm").load(fileName)

			val lr = new LogisticRegression()
	.setMaxIter(10)
	.setRegParam(0.3)
	.setElasticNetParam(0.8)

	// Fit the model
	val lrModel = lr.fit(training)

	// Print the coefficients and intercept for logistic regression
	println(s"Coefficients: ${lrModel.coefficients} Intercept: ${lrModel.intercept}")

	// We can also use the multinomial family for binary classification
	val mlr = new LogisticRegression()
	.setMaxIter(10)
	.setRegParam(0.3)
	.setElasticNetParam(0.8)
	.setFamily("multinomial")

	val mlrModel = mlr.fit(training)

	// Print the coefficients and intercepts for logistic regression with multinomial family
	println(s"Multinomial coefficients: ${mlrModel.coefficientMatrix}")
	println(s"Multinomial intercepts: ${mlrModel.interceptVector}")
}