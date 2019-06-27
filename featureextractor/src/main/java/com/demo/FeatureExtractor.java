package com.demo;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.sql.SparkSession;

import java.io.BufferedWriter;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;

public class FeatureExtractor implements Serializable {

    private static final long seriaLVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(FeatureExtractor.class);

    public String readFile(String filePath,String[] startTokens,String[] endTokens) {

        SparkSession sparkSession = SparkSession
                .builder()
                .appName("Java Spark SQL basic example")
                .config("spark.master", "local")
                .getOrCreate();
        JavaSparkContext jsc = new JavaSparkContext(sparkSession.sparkContext());
        JavaRDD<String> content = jsc.textFile(filePath);
        JavaRDD<String> output = content.mapPartitions(new FlatMapFunction<Iterator<String>, String>() {
            @Override
            public Iterator<String> call(Iterator<String> t) throws Exception {
                ArrayList<String> list = new ArrayList<>();
                boolean startFlag = false;
                while (t.hasNext()) {
                    String line = t.next();
                    if (StringUtils.isNotEmpty(line)) {
                        if (checkToken(line, startTokens)) {
                            startFlag = true;
                        }
                        if (startFlag) {
                            if (!line.equals("\n"))
                                list.add(line);
                        }
                        if (checkToken(line, endTokens)) {
                            list.add("\n");
                            startFlag = false;
                        }
                    }
                }
                return list.iterator();
            }
        });

        StringBuilder sb = new StringBuilder();

        for (String line : output.collect()) {
            sb.append(line);
        }

        return sb.toString();
    }


    private boolean checkToken(String line,String[] tokens) {
        for (String token:tokens) {
            if (line.contains(token)){
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) throws Exception {
        if (args.length > 3) {

            String filePath = args[0];
            String fileName = args[1];

            String startToken = args[2];
            String endToken = args[3];

            String[] startTokens = startToken.split("\\,");
            String[] endTokens = endToken.split("\\,");

            String[] filePrt = fileName.split("\\.");
            String outputFileName = new StringBuilder().append(filePrt[0]).append("_filtered.").append(filePrt[1]).toString();
            FeatureExtractor fileReader = new FeatureExtractor();

            String outputFilePath = new StringBuilder().append(filePath).append(outputFileName).toString();
            try {
                Path path = Paths.get(outputFilePath);
                String content = fileReader.readFile(new StringBuilder().append(filePath).append(fileName).toString(), startTokens, endTokens);
                try (BufferedWriter writer = Files.newBufferedWriter(path)) {
                    writer.write(content);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("usage: Filerpath Filename Starttokens Endtokens");
            System.exit(2);
        }
    }
}
