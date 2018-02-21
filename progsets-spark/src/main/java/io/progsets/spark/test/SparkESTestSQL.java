package io.progsets.spark.test;

import javax.annotation.PostConstruct;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class SparkESTestSQL {
	
	static Logger LOG = LoggerFactory.getLogger(SparkESTestSQL.class);


	public static void main(String[] args) {
		new SparkESTestSQL().init();
	}
	
	@PostConstruct
	public void init() {
		try {
	
		SparkConf conf = new SparkConf().setAppName("SparkTest").setMaster("local");
		conf.set("es.nodes", "localhost")
			.set("es.port", "9200")
			.set("es.resource", "beacon-metric/visit")
			.set("es.query","?q=entry");
		
		JavaSparkContext jsc = new JavaSparkContext(conf); 
		
		//JavaPairRDD<String, Map<String, Object>> esRDD = JavaEsSpark.esRDD(jsc, "beacon-metric/visit", "?q=entry");
		
		//JavaRDD jRDD = esRDD.values();
		
		LOG.info(">>>>>>>>>>>>>>>>>>> trying for count");
		
		//long count = esRDD.count();
		
		//LOG.info(" >>>>>>>>>> Size :" + count );
		
		//esRDD.foreach(m -> LOG.info(" Val {}", m));
		
		jsc.stop();
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
