package io.procsets.spark.test;

import javax.annotation.PostConstruct;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class SparkTestSQL {
	
	static Logger LOG = LoggerFactory.getLogger(SparkTestSQL.class);


	public static void main(String[] args) {
		new SparkTestSQL().init();
	}
	
	@PostConstruct
	public void init() {
		try {
			
			SparkConf conf = new SparkConf().setAppName("SparkTest").setMaster("local");
			conf.set("es.nodes", "localhost")
				.set("es.port", "9200")
				.set("es.resource", "beacon-metric/visit")
				.set("es.query","?q=entry");
			
			SparkSession spark = SparkSession
					  .builder()
					  .appName("Java Spark SQL basic example")
					  .config("spark.some.config.option", "some-value")
					  .config(conf)
					  .getOrCreate();
			
		JavaSparkContext jsc = new JavaSparkContext(spark.sparkContext()); 
		
		
		//JavaPairRDD<String, Map<String, Object>> esRDD = JavaEsSpark.esRDD(jsc, "beacon-metric/visit", "?q=entry");
		
		//JavaRDD<Map<String, Object>> jRDD = esRDD.values();
		
		//for(Map<String, Object> m : jRDD.collect()) {
		//	LOG.info(m.toString());
		//}
		
		Class.forName("nl.anchormen.sql4es.jdbc.ESDriver");
		
		Dataset<Row> jdbcDF = spark.read()
				  .format("jdbc")
				  .option("driver", "nl.anchormen.sql4es.jdbc.ESDriver")
				  .option("url", "jdbc:sql4es://localhost:9300/beacon-metric")
				  .option("dbtable", "visit")
				  .load();

		//Dataset<Row> resultset = spark.createDataFrame(jRDD, MyBean.class);
		
		long count = jdbcDF.count();
		
		
		LOG.info(">>>>>>>>>>>>>>>>>> COUNT >>>>>>>>>>>>> {}", count);
		
		jsc.stop();
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}

