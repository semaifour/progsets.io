package io.procsets.proc.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import io.procsets.proc.Procedure;
import io.procsets.proc.Procontext;

/**
 * Returns one or more views as list of columns & rows
 * 
 * returnviewrow?view=abc,xyz
 * 
 */
public class ReturnViewRow extends Procedure {

	private static final long serialVersionUID = 5462017043671728400L;

	public Procontext run(Procontext pc) {
		try {
			assertNotNullParams("view");
			Map<String, Object> result = new HashMap<String, Object>();
			Dataset<Row> resultset = null;
			List<Object> list = null;
			List<Object> r = null;
			for(String v : param("view").split(",")) {
				try {
					resultset = pc.ss().table(v.trim());
					list = new ArrayList<Object>();
					result.put(v + "-columns", Arrays.asList(resultset.columns()));
					for(Row row : resultset.collectAsList()) {
						r = new ArrayList<Object>();
						for(String c : resultset.columns()) {
							r.add(row.getAs(c));
						}
						list.add(r);
					}
					result.put(v + "-rows", list);
					list = null;
				} catch(Exception e) {
					if (isDebug()) {
						result.put(v, ExceptionUtils.getStackTrace(e));
					}
					LOG().info("proc-returnviewrow-view-iter-exception", e);
				}
			}
			pc.result(result);
			pc.pushProcStack(true, this, "DONE");
		} catch (Exception e) {
			LOG().error("proc-returnviewrow-run-exception", e);
			pc.pushProcStack(false, this, (isDebug() ? ExceptionUtils.getStackTrace(e) : e.getMessage()));
		}
		return pc;	
	}
}
