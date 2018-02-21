package io.procsets.proc.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import io.procsets.proc.Procedure;
import io.procsets.proc.Procontext;

/**
 * JDBC Output Proc to read data from database tables
 * 
 * <pre>
 * 
 * myview1 = ojdbc?dbtable=mytable&datasource=mydatasource*
 * 
 * OR
 * 
 * global::myveiw1 = ijdbc?....
 * 
 * </pre>
 * 
 * @author mjs
 *
 */
public class JdbcOutputProc extends Procedure {
	
	private static final long serialVersionUID = 4262207398833836899L;

	@Override
	public Procontext run(Procontext pc) {
		try {
			assertNotNullParams("dbtable","saveas");
			Map<String, String> viewparams = settings(pc);
			viewparams.putAll(paramap("", false, ""));
			Dataset<Row> resultset = pc.ss().read().format("jdbc").options(viewparams).load();
			createOrReplaceView(resultset);
			pc.pushProcStack(true, this, "DONE");
		} catch (Exception e) {
			LOG().error("proc-ijdbc-proc-run exception", e);
			pc.pushProcStack(false, this, (isDebug() ? ExceptionUtils.getStackTrace(e) : e.getMessage()));
		}
		return pc;
	}
	
	/**
	 * Returns default settings for the procedures + resolved datasource settings
	 */
	@Override
	public Map<String, String> settings(Procontext pc) {
		Map<String, String> settings = (HashMap<String, String>) super.settings(pc);
		String ds = param("datasource");
		if (ds == null) {
			ds = pc.appproperties().prop("procsets.proc.ijdbc.default.datasource", "default");
		}
		//load default settings for the connector
		settings.putAll(pc.appproperties().propsWithPrefix("procsets.proc.ijdbc.default.", true));
		//load default settings for the data source
		settings.putAll(pc.appproperties().propsWithPrefix("procsets.spark.datasource." + ds + ".", true));
		return settings;
	}


}
