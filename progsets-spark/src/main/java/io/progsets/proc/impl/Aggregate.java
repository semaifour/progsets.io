package io.progsets.proc.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function2;

import io.progsets.proc.Procedure;
import io.progsets.proc.Procontext;

/**
 * 
 * 
 * aggregate?groupby=fieldName&aggby=fieldName&input=r0&output=r0
 * 
 * Input: JavaPairRDD<String, Map<String, Object>>
 * Output: JavaPairRDD<Object, Stats>
 * 
 * @author mjs
 *
 */
public class Aggregate extends Procedure {
	
	private static final long serialVersionUID = 8275032780668968088L;

	public class Stats extends HashMap<String, Object> implements Serializable {
		private static final long serialVersionUID = 7281650325192992752L;
	
		public Stats(String aggby, String groupby) {
			super();
			put("aggby", aggby);
			put("groupby", groupby);
			put("min", 0d);
			put("max", 0d);
			put("sum", 0d);
			put("mean",0d);
			put("count", 0l);
		}
		
		public String aggby() {
			return String.valueOf(get("aggby"));
		}
		
		public String groupby() {
			return String.valueOf(get("groupby"));
		}
	
		public Double min() {
			return (Double) get("min");
		}
		
		public Double max() {
			return (Double) get("max");
		}
				
		public Double mean() {
			return (Double) get("mean");
		}
		
		public Long count() {
			return (Long) get("count");
		}
		
		public Double sum() {
			return (Double) get("sum");
		}
		
		public Object group() {
			return (Object) get("group");
		}

		public Stats min(Double val) {
			Double min = min();
			min = min > val ? val : min;
			put("min", min);
			return this;
		}

		public Stats max(Double val) {
			Double max = max();
			max = max > val ? max : val;
			put("max", max);
			return this;
		}
		
		public Stats group(Object g) {
			put("group", g);
			return this;
		}

		public Stats sum(Double sum) {
			put("sum", sum);
			return this;
		}

		public Stats count(long count) {
			put("count", count);
			return this;
		}
		
		public Stats mean(Double mean) {
			put("mean", mean);
			return this;
		}

	}
	
	@Override
	public Procontext run(Procontext pc) {
		
		Object rdd = pc.get(param("input", "r0"));

		JavaRDD<Map<String, Object>> jRDD = resolve(rdd);

		if (jRDD == null) {
			pc.pushProcStack(false, this, "Exiting Unexpected Input. Expected: JavaPairRDD<?, Map<String, Object>> OR JavaRDD<Map<String, Object>>. Received:" + rdd.getClass());
			return pc;
		}
		
		JavaPairRDD<String, Iterable<Map<String, Object>>>  jplRDD = jRDD.groupBy(doc -> {
			Object val = doc.get(param("groupby"));
			return String.valueOf(val);
		});

		Function2<Stats, Iterable<Map<String, Object>>, Stats> addAndCount = new Function2<Stats, Iterable<Map<String, Object>>, Stats>() {
		    @Override
		    public Stats call(Stats a, Iterable<Map<String, Object>> list) {
		    	for(Map<String, Object> doc : list) {
		    		Object val = doc.get(param("aggby"));
		    		a.group(doc.get(param("groupby")));
		    		Number num = null;
		    		if (val == null) continue;
		    		if (val instanceof Number) {
			    		num = ((Number) val);
			    		a.count(a.count() + 1);
		    		} else {
		    			try {
		    				num = Double.valueOf(String.valueOf(val));
				    		a.count(a.count() + 1);
		    			} catch(Exception e) {
		    			}
		    		}
		    		if (num != null) {
		    			a.sum(a.sum() + num.doubleValue());
		    			a.min(num.doubleValue());
		    			a.max(num.doubleValue());
		    			a.mean(a.sum()/a.count());
		    		}
		    	}
		    	return a;
		    }
	    };
	    
	    Function2<Stats, Stats, Stats> combine = new Function2<Stats, Stats, Stats>() {
		    @Override
		    public Stats call(Stats a, Stats b) {
		    	a.sum(a.sum() + b.sum());
		    	a.count(a.count() + b.count());
		    	a.min(b.min());
		    	a.max(b.max());
		    	a.mean(a.sum()/a.count());
		    	return a;
		    }
	    };

		JavaPairRDD<String, Stats> result = jplRDD.aggregateByKey(new Stats(param("aggby"), param("groupby")), addAndCount, combine);
		
		pc.put(param("output", "r0"), result);
		pc.pushProcStack(true, this, "DONE");

		return pc;
	}

}
