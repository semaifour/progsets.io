package io.progsets.proc.impl;

import java.util.Map;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;

import io.progsets.proc.Procedure;
import io.progsets.proc.Procontext;

/*
 * render?input=r0
 * 
 */
public class Render extends Procedure {

	private static final long serialVersionUID = 288892200755020146L;

	public Procontext run(Procontext pc) {
		Object result = (Map<?, ?>) pc.get(param("input"));
		result = result != null ? result : pc.get("r0");
		if (result instanceof JavaRDD) {
			pc.result(((JavaRDD) result).collect());
		} else if (result instanceof JavaPairRDD) {
			pc.result(((JavaPairRDD) result).collect());
		}
		return pc;
	}
}
