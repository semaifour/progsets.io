package io.procsets.proc.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import io.procsets.proc.Procedure;
import io.procsets.proc.Procontext;

/**
 * 
 * myjsonview = jsonify?view=abc&column=name-of-filed-contains-json-as-text
 * 
 * @author mjs
 *
 */
public class JsonifyProc extends Procedure {
	
	private static final long serialVersionUID = 2263747155049362677L;

	@Override
	public Procontext run(Procontext pc) {

		try {
			assertNotNullParams("view", "saveas", "column");
			Dataset<Row> resultset = pc.ss().table(param("view"));
			List<Row> rows = resultset.collectAsList();
			List<String> ldata = new ArrayList<String>();
			rows.forEach(t -> ldata.add(t.getAs(param("column")).toString()));
			JavaRDD<String> rdddata = pc.jsc().parallelize(ldata);
			resultset = pc.ss().read().json(rdddata);
			createOrReplaceView(resultset);
			pc.pushProcStack(true, this, "DONE");
		} catch (Exception e) {
			LOG().error("proc-jsonview-run exception", e);
			pc.pushProcStack(false, this, (isDebug() ? ExceptionUtils.getStackTrace(e) : e.getMessage()));
		}
		return pc;
	}

}
