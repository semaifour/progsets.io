package io.procsets.proc.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;

import io.procsets.proc.Procedure;
import io.procsets.proc.Procontext;

/*
 * 
 * 
 * bucket?field=timestamp&interval=2h
 * 
 */
public class Bucket extends Procedure {
	
	private static final long serialVersionUID = -5916405933767003429L;

	@Override
	public Procontext run(Procontext pc) {
	
		JavaRDD<Map<String, Object>> jRDD = resolve(pc.get(param("input", "r0")));
		
		JavaPairRDD<String, Iterable<Map<String, Object>>>  jplRDD = jRDD.groupBy(doc -> {
			Object val = doc.get(param("field"));
			if (val instanceof Date) {
				val = bucketit((Date)val);
			} else {
				val = bucketit(String.valueOf(val));
			}
			
			return String.valueOf(val);
		});
		
		pc.put(param("output", "r0"), jplRDD);

		pc.pushProcStack(true, this, "DONE");
		LOG().info("done");
		return pc;
	}

	protected Object bucketit(String val) {
		return val;
	}

	protected static String[] DF = new String[] {"Y","M","d","h","m","s"};

	protected static Map<String, Integer[]> dateDistanceMap = new HashMap<String, Integer[]>();
	static {
		dateDistanceMap.put(DF[0], new Integer[]{Calendar.YEAR, 0});
		dateDistanceMap.put(DF[1], new Integer[]{Calendar.MONTH, 1});
		dateDistanceMap.put(DF[2], new Integer[]{Calendar.DAY_OF_MONTH,0});
		dateDistanceMap.put(DF[3], new Integer[]{Calendar.HOUR_OF_DAY, 0});
		dateDistanceMap.put(DF[4], new Integer[]{Calendar.MINUTE, 0});
		dateDistanceMap.put(DF[5], new Integer[]{Calendar.SECOND, 0});
	}
	
	protected Object bucketit(Date val) {
		String[] interval = param("interval", "1d").split("");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(val);
		StringBuilder dt = new StringBuilder();
		//read date fields until we hit the interval-field (year, month, date, hour, min, sec)
		for(String df : DF) {
			Integer[] dfis = dateDistanceMap.get(df);
			if (interval[1].equals(df)) {
				int dval = calendar.get(dfis[0]) + dfis[1];
				int div = dval / Integer.valueOf(interval[0]);
				int mod = dval % Integer.valueOf(interval[0]);
				int fac = div + mod > 0 ? 1: 0;
				dval = dval * fac;
				dt.append(digit2(dval));
				break;
			} else {
				dt.append(digit2(calendar.get(dfis[0])));				
			}
		}
		return dt.toString();
	}
	
	private String digit2(int i) {
		if (i < 10) {
			return String.valueOf("0"+i);
		} else {
			return String.valueOf(i);
		}
	}

	protected Object bucketit(Integer val) {
		return val;
	}
	
	protected Object bucketit(Float val) {
		return val;
	}
	
	protected Object bucketit(Long val) {
		return val;
	}
	
	protected Object bucketit(Double val) {
		return val;
	}





}
