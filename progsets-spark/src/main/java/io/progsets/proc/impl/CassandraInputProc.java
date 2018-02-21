package io.progsets.proc.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import io.progsets.proc.Procedure;
import io.progsets.proc.Procontext;

/**
 * 
 * <pre>
 * 
 * 	icassandra?datasource=qubercloud-mongo&saveas=mycollectionview&keyspace=facesix&table=site
 * 
 * OR 
 * 
 * 	mycollectionview = icassandra?datasource=qubercloud-mongo&keyspace=facesix&table=site
 * 
 * </pre>
 * @author mjs
 *
 */
public class CassandraInputProc extends Procedure {
	
	private static final long serialVersionUID = 4262207398833836899L;

	@Override
	public Procontext run(Procontext pc) {

		try {
			assertNotNullParams("saveas", "table", "keyspace");
			Map<String, String> viewparams = settings(pc);
			viewparams.putAll(paramap("cassandra.", true, "spark.cassandra."));
			viewparams.putAll(paramap("cluster", "keyspace", "table"));
			Dataset<Row> resultset = null;
			resultset = pc.ss().read().format(viewparams.get("spark.read.format")).options(viewparams).load();
			createOrReplaceView(resultset);
			pc.pushProcStack(true, this, "DONE");
		} catch (Exception e) {
			LOG().error("proc-cassandraview-run exception", e);
			pc.pushProcStack(false, this, (isDebug() ? ExceptionUtils.getStackTrace(e) : e.getMessage()));
		}
		return pc;
	}
	
	/**
	 * Returns default settings for the procedures + resolved datasource setting
	 */
	@Override
	public Map<String, String> settings(Procontext pc) {
		Map<String, String> settings = (HashMap<String, String>) super.settings(pc);
		String ds = param("datasource");
		if (ds == null) {
			ds = pc.appproperties().prop("progsets.proc.cassandra.default.datasource", "defaultcassandra");
		}
		//load default settings for the connector
		settings.putAll(pc.appproperties().propsWithPrefix("progsets.proc.cassandra.default.", true));
		//load default settings for the data source
		settings.putAll(pc.appproperties().propsWithPrefix("progsets.spark.datasource." + ds + ".", true));
		return settings;
	}

}
