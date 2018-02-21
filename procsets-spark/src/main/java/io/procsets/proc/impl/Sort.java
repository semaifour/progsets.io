package io.procsets.proc.impl;

import java.util.Map;

import org.apache.spark.api.java.JavaRDD;

import io.procsets.proc.Procedure;
import io.procsets.proc.Procontext;

/**
 * 
 * 
 * sort?field=fieldName&order=ASC|DESC&input=r0&output=r0
 * 
 * @author mjs
 *
 */
public class Sort extends Procedure {
	
	private static final long serialVersionUID = 8275032780668968088L;

	
	@Override
	public Procontext run(Procontext pc) {
		
		Object rdd = pc.get(param("input", "r0"));

		JavaRDD<Map<String, Object>> jRDD = resolve(rdd);

		if (jRDD == null) {
			pc.pushProcStack(false, this, "Exiting Unexpected Input. Expected: JavaPairRDD<?, Map<?, ?>> OR JavaRDD<Map<?, ?>>. Received:" + rdd.getClass());
			return pc;
		}
		
		jRDD = jRDD.sortBy(doc -> { return doc.get(param("field"));}, false, jRDD.getNumPartitions());
		
		pc.put(param("output", "r0"), jRDD);
		
		pc.pushProcStack(true, this, "DONE");

		return pc;
	}



}
