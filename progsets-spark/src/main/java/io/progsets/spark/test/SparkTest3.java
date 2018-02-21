package io.progsets.spark.test;

import javax.annotation.PostConstruct;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.elasticsearch.spark.sql.api.java.JavaEsSparkSQL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.spark.MongoSpark;



public class SparkTest3 {
	
	static Logger LOG = LoggerFactory.getLogger(SparkTest3.class);


	public static void main(String[] args) {
		new SparkTest3().init();
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
				      .config("spark.mongodb.input.uri", "mongodb://cloud.qubercomm.com:27017/facesix.site")
				      .config("spark.mongodb.output.uri", "mongodb://cloud.qubercomm.com:27017/facesix.site_copy")
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
		
		
	    //JavaMongoRDD<Document> rdd = MongoSpark.load(jsc);

	    Dataset<Row> implicitDS = MongoSpark.load(jsc).toDF();

		
		jsc.stop();
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}

