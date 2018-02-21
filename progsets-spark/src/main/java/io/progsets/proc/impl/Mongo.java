package io.progsets.proc.impl;

import java.util.Map;

import org.apache.spark.api.java.JavaPairRDD;
import org.elasticsearch.spark.rdd.api.java.JavaEsSpark;

import io.progsets.proc.Procedure;
import io.progsets.proc.Procontext;

/**
 * 
 * elastic?query=search for this&index=indexname/typename
 * 
 * @author mjs
 *
 */
public class Mongo extends Procedure {
	
	private static final long serialVersionUID = 4262207398833836899L;

	@Override
	public Procontext run(Procontext pc) {
		pc.sc()
		  .set("es.nodes", "localhost")
		  .set("es.port", "9200");
	
		JavaPairRDD<String, Map<String, Object>> jpRDD = JavaEsSpark.esRDD(pc.jsc(),
																		   param("index"),
																		   "?q=" + param("query", "*"));
		pc.put(param("output", "r0"), jpRDD);
		pc.pushProcStack(true, this, "DONE");
		
		return pc;
	}

}
