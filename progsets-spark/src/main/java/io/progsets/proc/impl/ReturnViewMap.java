package io.progsets.proc.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import io.progsets.proc.Procedure;
import io.progsets.proc.Procontext;

/**
 * 
 * Returns one or more views as list of maps
 * 
 * returnviewmap?view=myviewname1,myviewname2
 * 
 */
public class ReturnViewMap extends Procedure {

	private static final long serialVersionUID = 5462017043671728400L;

	public Procontext run(Procontext pc) {
		try {
			assertNotNullParams("view");
			Map<String, Object> result = new HashMap<String, Object>();
			Dataset<Row> resultset = null;
			List<Map<String, Object>> list = null;
			Map<String, Object> record = null;
		
			for(String v : param("view").split(",")) {
				try {
					resultset = pc.ss().table(v.trim());
					list = new ArrayList<Map<String, Object>>();
					for(Row row : resultset.collectAsList()) {
						record = new HashMap<String, Object>();
						for (String f : row.schema().fieldNames()) {
							record.put(f, row.getAs(f));
						}
						list.add(record);
					}
					result.put(v, list);
					list = null;
					//result.put(v, resultset.toJSON().collect());
				} catch(Exception e) {
					if (isDebug()) {
						result.put(v, ExceptionUtils.getStackTrace(e));
					}
					LOG().info("proc-returnviewmap-view-iter-exception", e);
				}
			}
			pc.result(result);
			pc.pushProcStack(true, this, "DONE");
		} catch (Exception e) {
			LOG().error("proc-returnviewmap-run-exception", e);
			pc.pushProcStack(false, this, (isDebug() ? ExceptionUtils.getStackTrace(e) : e.getMessage()));
		}
		return pc;	
	}
}
