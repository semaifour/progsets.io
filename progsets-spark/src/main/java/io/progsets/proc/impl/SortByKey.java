package io.progsets.proc.impl;

import org.apache.spark.api.java.JavaPairRDD;

import io.progsets.proc.Procedure;
import io.progsets.proc.Procontext;

/**
 * 
 * Input: JavaPairRDD<String, ?>
 * 
 * sortkey?order=ASC|DESC&input=r0&output=r0
 * 
 * @author mjs
 *
 */
public class SortByKey extends Procedure {
	
	private static final long serialVersionUID = 8275032780668968088L;

	
	@Override
	public Procontext run(Procontext pc) {
		
		JavaPairRDD<String, ?> jpRDD = (JavaPairRDD<String, ?>) pc.get(param("input", "r0"));;

		if (jpRDD == null) {
			pc.pushProcStack(false, this, "Exiting Unexpected Input. Expected: JavaPairRDD<String, ?> OR JavaRDD<Map<?, ?>>");
			return pc;
		}
		
		
		jpRDD = jpRDD.sortByKey(isparam("ascending", "TRUE"));
		
		pc.put(param("output", "r0"), jpRDD);
		
		pc.pushProcStack(true, this, "DONE");

		return pc;
	}



}
