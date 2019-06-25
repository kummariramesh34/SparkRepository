package com.ram.edu.spark.ml

import org.apache.spark._
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions._
import com.ram.edu.spark.sql.transformations._
import scala.util.matching.Regex

object SQLInjection extends App{
  println("Test")
  
  	val homeDir = args(0)
			val spark = SparkSession.builder.master("local").appName("SQLInjection").getOrCreate()
			val sqlContext = spark.sqlContext
			// Load training data
			val plainFileName = s"file:\\\\\\$homeDir\\plain.txt"
			val plainDF = loadCSVFileAsDataFrame(plainFileName).withColumnRenamed("_c0", "raw_sql").withColumn("type", lit("plain"))
			val sqliFileName = s"file:\\\\\\$homeDir\\sql_querys.txt"
			val sqlDF = loadCSVFileAsDataFrame(sqliFileName).withColumnRenamed("_c0", "raw_sql").withColumn("type", lit("sqli"))
	//		val regex = "(?P<UNION>UNION\\s+(ALL\\s+)?SELECT)|(?P<PREFIX>([\'\"\\)]|((\'|\"|\\)|\\d+|\\w+)\\s))(\\|\\|\\&\\&|and|or|as|where|IN\\sBOOLEAN\\sMODE)(\\s|\\()(\\(?\'?-?\\d+\'?(=|LIKE|<|>|<=|>=)\'?-?\\d+|\\(?[\'\"\\\"]\\s+[\'\"\\\"](\\s+)?(=|LIKE|<|>|<=|>=)(\\s+)?[\'\"\\\"]))|(?P<USUAL>([\'\"]\\s*)(\\|\\||\\&\\&|and|or)(\\s*[\'\"])(\\s*[\'\"])=)|(?P<DROP>;\\s*DROP\\s+(TABLE|DATABASE)\\s(IF\\s+EXISTS\\s)?\\s+)|(?P<NOTIN>\\snot\\sin\\s?\\((\\d+|(\'|\")\\w+(\'|\"))\\))|(?P<LIMIT>LIMIT\\s+\\d+(\\s+)?,(\\s+)?\\d+)|GROUP_CONCAT\\((?P<GRPCONCAT>.*?)\\)|(?P<ORDERBY>ORDER\\s+BY\\s+\\d+)|CONCAT\\((?P<CONCAT>.*?)\\)|(?P<CASEWHEN>\\(CASE\\s(\\d+\\s|\\(\\d+=\\d+\\)\\s|NULL\\s)?WHEN\\s(\\d+|\\(?\\d+=\\d+\\)?|NULL)\\sTHEN\\s(\\d+|\\(\\d+=\\d+\\)|NULL)\\sELSE)|(?P<DBNAME>(?:(?:m(?:s(?:ysaccessobjects|ysaces|ysobjects|ysqueries|ysrelationships|ysaccessstorage|ysaccessxml|ysmodules|ysmodules2|db)|aster\\.\\.sysdatabases|ysql\\.db)|s(?:ys(?:\\.database_name|aux)|chema(?:\\W*\\(|_name)|qlite(_temp)?_master)|d(?:atabas|b_nam)e\\W*\\(|information_schema|pg_(catalog|toast)|northwind|tempdb)))|(?P<DATABASE>DATABASE\\(\\))|(?P<DTCNAME>table_name|column_name|table_schema|schema_name)|(?P<CAST>CAST\\(.*AS\\s+\\w+\\))|(?P<INQUERY>\\(SELECT[^a-z_0-9])|(?P<CHRBYPASS>((CHA?R\\(\\d+\\)(,|\\|\\||\\+)\\s?)+)|CHA?R\\((\\d+,\\s?)+\\))|(?P<FROMDB>\\sfrom\\s(dual|sysmaster|sysibm)[\\s.:])|(?P<MYSQLFUNC>[^.](ABS|ACOS|ADDDATE|ADDTIME|AES_DECRYPT|AES_ENCRYPT|ANY_VALUE|ASCII|ASIN|ASYMMETRIC_DECRYPT|ASYMMETRIC_DERIVE|ASYMMETRIC_ENCRYPT|ASYMMETRIC_SIGN|ASYMMETRIC_VERIFY|ATAN|ATAN2|AVG|BENCHMARK|BIN|BIT_AND|BIT_COUNT|BIT_LENGTH|BIT_OR|BIT_XOR|CAST|CEIL|CEILING|CHAR|CHAR_LENGTH|CHARACTER_LENGTH|CHARSET|COALESCE|COERCIBILITY|COLLATION|COMPRESS|CONCAT|CONCAT_WS|CONNECTION_ID|CONV|CONVERT|CONVERT_TZ|COS|COT|COUNT|COUNT|CRC32|CREATE_ASYMMETRIC_PRIV_KEY|CREATE_ASYMMETRIC_PUB_KEY|CREATE_DH_PARAMETERS|CREATE_DIGEST|CURDATE|CURRENT_DATE|CURRENT_TIME|CURRENT_TIMESTAMP|CURRENT_USER|CURTIME|DATABASE|DATE|DATE_ADD|DATE_FORMAT|DATE_SUB|DATEDIFF|DAY|DAYNAME|DAYOFMONTH|DAYOFWEEK|DAYOFYEAR|DECODE|DEFAULT|DEGREES|ELT|ENCODE|EXP|EXPORT_SET|EXTRACT|EXTRACTVALUE|FIELD|FIND_IN_SET|FLOOR|FORMAT|FOUND_ROWS|FROM_BASE64|FROM_DAYS|FROM_UNIXTIME|GeometryCollection|GET_FORMAT|GET_LOCK|GREATEST|GROUP_CONCAT|GTID_SUBSET|GTID_SUBTRACT|HEX|HOUR|IF|IFNULL|IIF|IN|INET_ATON|INET_NTOA|INET6_ATON|INET6_NTOA|INSERT|INSTR|INTERVAL|IS_FREE_LOCK|IS_IPV4|IS_IPV4_COMPAT|IS_IPV4_MAPPED|IS_IPV6|IS_USED_LOCK|ISNULL|JSON_APPEND|JSON_ARRAY|JSON_ARRAY_APPEND|JSON_ARRAY_INSERT|JSON_CONTAINS|JSON_CONTAINS_PATH|JSON_DEPTH|JSON_EXTRACT|JSON_INSERT|JSON_KEYS|JSON_LENGTH|JSON_MERGE|JSON_OBJECT|JSON_QUOTE|JSON_REMOVE|JSON_REPLACE|JSON_SEARCH|JSON_SET|JSON_TYPE|JSON_UNQUOTE|JSON_VALID|LAST_INSERT_ID|LCASE|LEAST|LEFT|LENGTH|LineString|LN|LOAD_FILE|LOCALTIME|LOCALTIMESTAMP|LOCATE|LOG|LOG10|LOG2|LOWER|LPAD|LTRIM|MAKE_SET|MAKEDATE|MAKETIME|MASTER_POS_WAIT|MAX|MBRContains|MBRCoveredBy|MBRCovers|MBRDisjoint|MBREquals|MBRIntersects|MBROverlaps|MBRTouches|MBRWithin|MICROSECOND|MID|MIN|MINUTE|MOD|MONTH|MONTHNAME|MultiLineString|MultiPoint|MultiPolygon|NAME_CONST|NOT IN|NOW|NULLIF|OCT|OCTET_LENGTH|OLD_PASSWORD|ORD|PERIOD_ADD|PERIOD_DIFF|PI|Point|Polygon|POSITION|POW|POWER|PROCEDURE ANALYSE|QUARTER|QUOTE|RADIANS|RAND|RANDOM_BYTES|RELEASE_ALL_LOCKS|RELEASE_LOCK|REPEAT|REPLACE|REVERSE|RIGHT|ROUND|ROW_COUNT|RPAD|RTRIM|SCHEMA|SEC_TO_TIME|SECOND|SESSION_USER|SHA1|SHA2|SIGN|SIN|SLEEP|SOUNDEX|SPACE|SQRT|ST_Area|ST_AsBinary|ST_AsGeoJSON|ST_AsText|ST_Buffer|ST_Buffer_Strategy|ST_Centroid|ST_Contains|ST_ConvexHull|ST_Crosses|ST_Difference|ST_Dimension|ST_Disjoint|ST_Distance|ST_Distance_Sphere|ST_EndPoint|ST_Envelope|ST_Equals|ST_ExteriorRing|ST_GeoHash|ST_GeomCollFromText|ST_GeomCollFromWKB|ST_GeometryN|ST_GeometryType|ST_GeomFromGeoJSON|ST_GeomFromText|ST_GeomFromWKB|ST_InteriorRingN|ST_Intersection|ST_Intersects|ST_IsClosed|ST_IsEmpty|ST_IsSimple|ST_IsValid|ST_LatFromGeoHash|ST_Length|ST_LineFromText|ST_LineFromWKB|ST_LongFromGeoHash|ST_MakeEnvelope|ST_MLineFromText|ST_MLineFromWKB|ST_MPointFromText|ST_MPointFromWKB|ST_MPolyFromText|ST_MPolyFromWKB|ST_NumGeometries|ST_NumInteriorRing|ST_NumPoints|ST_Overlaps|ST_PointFromGeoHash|ST_PointFromText|ST_PointFromWKB|ST_PointN|ST_PolyFromText|ST_PolyFromWKB|ST_Simplify|ST_SRID|ST_StartPoint|ST_SymDifference|ST_Touches|ST_Union|ST_Validate|ST_Within|ST_X|ST_Y|StartPoint|STD|STDDEV|STDDEV_POP|STDDEV_SAMP|STR_TO_DATE|STRCMP|SUBDATE|SUBSTR|SUBSTRING|SUBSTRING_INDEX|SUBTIME|SUM|SYSDATE|SYSTEM_USER|TAN|TIME|TIME_FORMAT|TIME_TO_SEC|TIMEDIFF|TIMESTAMP|TIMESTAMPADD|TIMESTAMPDIFF|TO_BASE64|TO_DAYS|TO_SECONDS|TRIM|TRUNCATE|UCASE|UNCOMPRESS|UNCOMPRESSED_LENGTH|UNHEX|UNIX_TIMESTAMP|UpdateXML|UPPER|USER|UTC_DATE|UTC_TIME|UTC_TIMESTAMP|UUID|UUID_SHORT|VALIDATE_PASSWORD_STRENGTH|VALUES|VAR_POP|VAR_SAMP|VARIANCE|VERSION|WAIT_FOR_EXECUTED_GTID_SET|WAIT_UNTIL_SQL_THREAD_AFTER_GTIDS|WEEK|WEEKDAY|WEEKOFYEAR|WEIGHT_STRING|YEAR|YEARWEEK)\\()|(?P<BOOLEAN>\'?-?\\d+\'?(=|LIKE)\'?-?\\d+($|\\s|\\)|,|--|#)|[\'\"\\\"]\\s+[\'\"\\\"](\\s+)?(=|LIKE)(\\s+)?[\'\"\\\"]\\s+)|(?P<PLAIN>(@|##|#)[A-Z]\\w+|[A-Z]\\w*(?=\\s*\\.)|(?<=\\.)[A-Z]\\w*|[A-Z]\\w*(?=\\()|`(``|[^`])*`|�(��|[^�])*�|[_A-Z][_$#\\w]".r
			val regex = "(UNION\\s+(ALL\\s+)?SELECT)".r
			val df= sqlDF.union(plainDF)
			df.show
			
			val sqlTokenizer = udf { s: String => {
			  val matches = regex.findAllIn(s)
			  val grouped = matches.toList.groupBy { x => x }
			  println("#######"+grouped)
			  "####"
			  }
      }
			val tokendDF = df.withColumn("sql", sqlTokenizer(col("raw_sql")))
			
			tokendDF.show
			
			
			
			
			
	
  
  def loadCSVFileAsDataFrame(fileName: String): DataFrame ={
		sqlContext.read
		.format("com.databricks.spark.csv")
		.option("header", "false") // Use first line of all files as header
		.option("inferSchema", "true") // Automatically infer data types		
		.load(fileName)

	}
}