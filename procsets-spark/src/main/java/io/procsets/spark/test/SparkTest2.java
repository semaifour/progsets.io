package io.procsets.spark.test;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.elasticsearch.spark.rdd.api.java.JavaEsSpark;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




public class SparkTest2 {
	
	static Logger LOG = LoggerFactory.getLogger(SparkTest2.class);


	public static void main(String[] args) {
		new SparkTest2().init();
	}
	
	@PostConstruct
	public void init() {
		try {
	
		SparkConf conf = new SparkConf().setAppName("SparkTest").setMaster("local");
		conf.set("es.nodes", "localhost")
			.set("es.port", "9200")
			.set("es.resource", "beacon-metric/visit")
			.set("es.query","?q=entry");
		
		
		
		//JavaSparkContext sc = new JavaSparkContext(conf);
		
		//JavaRDD<String> distFile = sc.textFile("data.txt");
		//JavaRDD<String> lines = sc.textFile("data.txt");
		//JavaRDD<Integer> lineLengths = lines.map(s -> s.length());
		//int totalLength = lineLengths.reduce((a, b) -> a + b);
		
		JavaSparkContext jsc = new JavaSparkContext(conf); 
		//Map<String, ?> numbers = ImmutableMap.of("one", 1, "two", 2);                   
		//Map<String, ?> airports = ImmutableMap.of("OTP", "Otopeni", "SFO", "San Fran");
		
		//JavaRDD<Map<String, ?>> javaRDD = jsc.parallelize(ImmutableList.of(numbers, airports));
		//JavaEsSpark.saveToEs(javaRDD, "testspark/docs");
		
		JavaPairRDD<String, Map<String, Object>> esRDD = JavaEsSpark.esRDD(jsc, "beacon-metric/visit", "?q=entry exit");
		
		JavaRDD jRDD = esRDD.values();

		
		esRDD.foreach(m -> LOG.info(" Val {}", m));
		
		jsc.stop();
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
