package io.procsets.proc.impl;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import io.procsets.proc.Procedure;
import io.procsets.proc.Procontext;

/**
 * SSQL - Spark SQL 
 * 
 * <pre>
 * ssql?sql=select * from this-view&viewas=myview1
 * 
 * OR
 * myview1 = ssql?sql=select * from this-view
 * 
 * OR
 * 
 * global::myveiw1 = ssql?sql=....
 * 
 * 
 *
 * </pre>
 * @author mjs
 *
 */
public class SparkSQLProc extends Procedure {
	
	private static final long serialVersionUID = 4262207398833836899L;

	@Override
	public Procontext run(Procontext pc) {
		try {
			assertNotNullParams("sql","saveas");
			String ssql = param("sql");
			LOG().info("Firing SSQL :[{}]", ssql);
			Dataset<Row> resultset = pc.ss().sql(ssql);
			createOrReplaceView(resultset);
			pc.pushProcStack(true, this, "DONE");
		} catch (Exception e) {
			LOG().error("proc-ssql-run exception", e);
			pc.pushProcStack(false, this, (isDebug() ? ExceptionUtils.getStackTrace(e) : e.getMessage()));
		}
		return pc;
	}

}
