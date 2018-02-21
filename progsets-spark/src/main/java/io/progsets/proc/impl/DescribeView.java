package io.progsets.proc.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.types.StructField;

import io.progsets.proc.Procedure;
import io.progsets.proc.Procontext;

/**
 * Describes the given views
 * 
 *  <pre>
 * 
 * describe?view=abc,xyz
 * 
 * </pre>
 * 
 */
public class DescribeView extends Procedure {

	private static final long serialVersionUID = -6488413717858677822L;

	public Procontext run(Procontext pc) {
		try {
			assertNotNullParams("view");
			Map<String, Object> result = new HashMap<String, Object>();
			Dataset<Row> resultset = null;
			List<Object> cols = null;
			Map<String, String> c = null;
			for(String v : param("view").split(",")) {
				try {
					resultset = pc.ss().table(v.trim());
					cols = new ArrayList<Object>();
					for(StructField f : resultset.schema().fields()) {
						c = new HashMap<String, String>();
						cols.add(c);
						c.put("name", f.name());
						c.put("type", f.dataType().typeName());
						c.put("nullable", String.valueOf(f.nullable()));
						c.put("comment", f.getComment().toString());
					}
					result.put(v + "-schema", cols);
					cols = null; 
				} catch(Exception e) {
					if (isDebug()) {
						result.put(v, ExceptionUtils.getStackTrace(e));
					}
					LOG().info("proc-describe-view-iter-exception", e);
				}
			}
			pc.result(result);
			pc.pushProcStack(true, this, "DONE");
		} catch (Exception e) {
			LOG().error("proc-describe-run-exception", e);
			pc.pushProcStack(false, this, (isDebug() ? ExceptionUtils.getStackTrace(e) : e.getMessage()));
		}
		return pc;	
	}
}
