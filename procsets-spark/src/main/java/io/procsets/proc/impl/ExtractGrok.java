package io.procsets.proc.impl;

import java.util.Map;

import org.apache.spark.api.java.JavaRDD;

import io.procsets.proc.Procedure;
import io.procsets.proc.Procontext;
import io.procsets.util.FSGrok;
import oi.thekraken.grok.api.Grok;
import oi.thekraken.grok.api.Match;

/**
 * 
 * 
 * extract-grok?field=fieldName&grok=%{DATE:f1}%{SPACE}%{GREEDYDATA:data}&input=r0&output=r0
 * 
 * @author mjs
 *
 */
public class ExtractGrok extends Procedure {
	
	private static final long serialVersionUID = 8275032780668968088L;

	
	@Override
	public Procontext run(Procontext pc) {
		
		Object rdd = pc.get(param("input", "r0"));

		JavaRDD<Map<String, Object>> jRDD = resolve(rdd);

		if (jRDD == null) {
			pc.pushProcStack(false, this, "Exiting Unexpected Input. Expected: JavaPairRDD<?, Map<?, ?>> OR JavaRDD<Map<?, ?>>. Received:" + rdd.getClass());
			return pc;
		}
		Grok grok = FSGrok.newGrok(param("grok"));
		jRDD.foreach(doc -> {
			String val = String.valueOf(doc.get(param("field")));
			Match match = grok.match(val);
			match.captures();
			for(Map.Entry<String, Object> item : match.toMap().entrySet()) {
				doc.put(item.getKey(), item.getValue());
			}
		});
		
		pc.put(param("output", "r0"), jRDD);
		pc.pushProcStack(true, this, "DONE");

		return pc;
	}



}
