package io.progsets.spark.test;

import javax.annotation.PostConstruct;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class SparkTestSolr {
	
	static Logger LOG = LoggerFactory.getLogger(SparkTestSolr.class);


	public static void main(String[] args) {
		new SparkTestSolr().init();
	}
	
	@PostConstruct
	public void init() {
		try {
			
			SparkConf conf = new SparkConf().setAppName("SparkTestSolr").setMaster("local");
			
			SparkSession spark = SparkSession
					  .builder()
					  .appName("Java Spark SQL basic example")
					  .config("spark.some.config.option", "some-value")
					  .config(conf)
					  .getOrCreate();
			
		JavaSparkContext jsc = new JavaSparkContext(spark.sparkContext()); 
			
		Dataset<Row> solrdocs = spark.read()
				  .format("solr")
				  .option("zkhost", "diapp-prd1-55:2181,diapp-prd1-56:2181,diapp-prd1-57:2181")
				  .option("collection", "ccoidcbp")
				  .option("rows", 10)
				  .option("max_rows", 10)
				  .option("sort", "UNIQUE_ID")
				  .option("fields", "UNIQUE_ID")
				  .load();
		

		long count = solrdocs.count();
		
		
		LOG.info(">>>>>>>>>>>>>>>>>> COUNT >>>>>>>>>>>>> {}", count);
		
		jsc.stop();
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}

