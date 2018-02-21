package io.progsets.spark.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.apache.spark.sql.SparkSession;
//import org.bson.Document;
//import com.mongodb.spark.MongoSpark;
//import com.mongodb.spark.rdd.api.java.JavaMongoRDD;



public class SparkTestMongo {
	
	static Logger LOG = LoggerFactory.getLogger(SparkTestMongo.class);


	public static void main(String[] args) {
		new SparkTestMongo().init();
	}
	

	public void init() {
		try {
	
//			SparkSession spark = SparkSession.builder()
//				      .master("local")
//				      .appName("MongoSparkConnectorIntro")
//				      .config("spark.mongodb.input.uri", "mongodb://cloud.qubercomm.com:27017/facesix.site")
//				      .config("spark.mongodb.output.uri", "mongodb://cloud.qubercomm.com:27017/facesix.site_copy")
//				      .getOrCreate();
//
//		//SparkConf conf = new SparkConf().setAppName("SparkTest").setMaster("local");
//		//conf.set("es.nodes", "localhost")
//		//	.set("es.port", "9200")
//		//	.set("es.resource", "beacon-metric/visit")
//		//	.set("es.query","?q=entry");
//		
//		
//			// Create a JavaSparkContext using the SparkSession's SparkContext object
//		    JavaSparkContext jsc = new JavaSparkContext(spark.sparkContext());
//
//		    /*Start Example: Read data from MongoDB************************/
//		    JavaMongoRDD<Document> rdd = MongoSpark.load(jsc);
//		    /*End Example**************************************************/
//
//		    // Analyze data from MongoDB
//		    System.out.println(rdd.count());
//		    System.out.println(rdd.first().toJson());
//
//		    jsc.close();
//		    
//		    
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
