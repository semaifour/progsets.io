package io.procsets.spark.test;

import javax.annotation.PostConstruct;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.elasticsearch.spark.sql.api.java.JavaEsSparkSQL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class SparkTest {
	
	static Logger LOG = LoggerFactory.getLogger(SparkTest.class);


	public static void main(String[] args) {
		new SparkTest().init();
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
		
		//SQLContext sql = new SQLContext(jsc.sc());
		
		//JavaPairRDD<String, Map<String, Object>> esRDD = JavaEsSpark.esRDD(jsc, "beacon-metric/visit", "?q=entry");
		
		//JavaRDD<Map<String, Object>> jRDD = esRDD.values();
		
		//for(Map<String, Object> m : jRDD.collect()) {
		//	LOG.info(m.toString());
		//}
		
		Dataset<Row> resultset = JavaEsSparkSQL.esDF(spark, "beacon-metric/visit", "?q=entry");
		
		long count = resultset.count();
		
		LOG.info(">>>>>>>>>>>>>>>>>> COUNT >>>>>>>>>>>>> {}", count);

		resultset.createOrReplaceTempView("metric");
		
		resultset = spark.sql("select * from metric where timeElapsed > 1000");
		
		count = resultset.count();
		
		LOG.info(">>>>>>>>>>>>>>>>>> COUNT >>>>>>>>>>>>> {}", count);

		resultset.foreach(row -> {
			LOG.info(row.toString());
		});
		
		jsc.stop();
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}

