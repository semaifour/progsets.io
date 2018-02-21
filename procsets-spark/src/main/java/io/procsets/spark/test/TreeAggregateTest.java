package io.procsets.spark.test;

import java.util.Arrays;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TreeAggregateTest {

	public static void main(String[] args) {
		
		Logger LOG = LoggerFactory.getLogger(SparkTest2.class);

		SparkConf conf = new SparkConf().setAppName("SparkTest").setMaster("local");
		JavaSparkContext jsc = new JavaSparkContext(conf); 

		
		     JavaRDD<Integer> rdd = jsc.parallelize(Arrays.asList(-5, -4, -3, -2, -1, 1, 2, 3, 4), 10);
		     Function2<Integer, Integer, Integer> add = new Function2<Integer, Integer, Integer>() {
		       @Override
		       public Integer call(Integer a, Integer b) {
		         return a + b;
		       }
		     };
		     for (int depth = 1; depth <= 10; depth++) {
		       int sum = rdd.treeAggregate(0, add, add, depth);
		       
		       LOG.info(">>>>>>>>>>>> sum : {}", sum);
		     }
	}
}
