package io.progsets.proc.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.spark.api.java.JavaRDD;

import com.google.gson.Gson;

import io.progsets.proc.Procedure;
import io.progsets.proc.Procontext;

/**
 * 
 * 
 * extract-json?field=fieldName&&input=r0&output=r0
 * 
 * @author mjs
 *
 */
public class ExtractJson extends Procedure {
	
	private static final long serialVersionUID = 8275032780668968088L;

	
	@Override
	public Procontext run(Procontext pc) {
		
		Object rdd = pc.get(param("input", "r0"));

		JavaRDD<Map<String, Object>> jRDD = resolve(rdd);

		if (jRDD == null) {
			pc.pushProcStack(false, this, "Exiting Unexpected Input. Expected: JavaPairRDD<?, Map<?, ?>> OR JavaRDD<Map<?, ?>>. Received:" + rdd.getClass());
			return pc;
		}
		Gson gson = new Gson(); 
		jRDD.foreach(doc -> {
			String val = String.valueOf(doc.get(param("field")));
			Map<String,Object> map = new HashMap<String,Object>();
			map = (Map<String,Object>) gson.fromJson(val, map.getClass());
			for(Map.Entry<String, Object> item : map.entrySet()) {
				doc.put(item.getKey(), item.getValue());
			}
		});

		pc.put(param("output", "r0"), jRDD);
		pc.pushProcStack(true, this, "DONE");

		return pc;
	}

}
