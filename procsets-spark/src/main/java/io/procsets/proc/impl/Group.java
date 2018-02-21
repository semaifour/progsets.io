package io.procsets.proc.impl;

import java.util.Map;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;

import io.procsets.proc.Procedure;
import io.procsets.proc.Procontext;

/**
 * 
 * group?groupby=fieldName&input=r0&output=r0
 * 
 * Input: JavaPairRDD<String, Map<String, Object>>
 * Output: JavaPairRDD<Object, Iterable<Map<String, Object>>>
 * 
 * @author mjs
 *
 */
public class Group extends Procedure {
	
	private static final long serialVersionUID = -3275875589592253877L;

	@Override
	public Procontext run(Procontext pc) {
		Object rdd = pc.get(param("input", "r0"));

		JavaRDD<Map<String, Object>> jRDD = resolve(rdd);

		if (jRDD == null) {
			pc.pushProcStack(false, this, "Exiting Unexpected Input. Expected: JavaPairRDD<?, Map<?, ?>> OR JavaRDD<Map<?, ?>>. Received:" + rdd.getClass());
			return pc;
		}

		JavaPairRDD<Object, Iterable<Map<String, Object>>>  jplRDD = jRDD.groupBy(doc -> {
			Object val = doc.get(param("groupby"));
			if (param("interval") != null) {
				
			}
			return val;
		});
		
		pc.put(param("output", "r0"), jplRDD);
		pc.pushProcStack(true, this, "DONE");

		return pc;
	}

}
