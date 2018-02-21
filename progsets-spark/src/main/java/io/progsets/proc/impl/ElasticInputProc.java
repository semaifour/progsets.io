package io.progsets.proc.impl;

import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import io.progsets.proc.Procedure;
import io.progsets.proc.Procontext;

/**
 * 
 * Loads data from Elasticsearch and hosts spark temp view
 * 
 * <pre>
 * 
 * Example:
 * 
 * ielastic?datasource=qubercloud-elastic&saveas=myindexview&es.xxx=yyy&index=myindex/mytype&query=search for this text
 
 * OR
 * 
 * myindexview = ielastic?datasource=qubercloud-elastic&es.xxx=yyy&index=myindex/mytype&query=search for this text

 * </pre>
 * @author mjs
 *
 */
public class ElasticInputProc extends Procedure {
	
	private static final long serialVersionUID = 4262207398833836899L;

	@Override
	public Procontext run(Procontext pc) {
		try {
			assertNotNullParams("index", "saveas");
			Map<String, String> viewoptions = settings(pc);
			viewoptions.putAll(paramap("es.", false, ""));
			if(isDebug()) {
				LOG().info("proc options =>" + viewoptions.toString());
			}
			Dataset<Row> resultset = pc.ss().read().format(viewoptions.get("spark.read.format")).options(viewoptions).load(param("index"));
			createOrReplaceView(resultset);
			pc.pushProcStack(true, this, "DONE");
		} catch (Exception e) {
			LOG().error("proc-elasticview-run exception", e);
			pc.pushProcStack(false, this, (isDebug() ? ExceptionUtils.getStackTrace(e) : e.getMessage()));
		}
		return pc;
	}

	/**
	 * Returns procedure settings and resolved datasource configurations
	 * 
	 */
	@Override
	public Map<String, String> settings(Procontext pc) {
		Map<String, String> settings = super.settings(pc);
		String ds = param("datasource");
		if (ds == null) {
			ds = pc.appproperties().prop("progsets.proc.ielastic.default.datasource", "default");
		}
		//load default settings for the connector
		settings.putAll(pc.appproperties().propsWithPrefix("progsets.proc.ielastic.default.", true));
		//load default settings for the data source
		settings.putAll(pc.appproperties().propsWithPrefix("progsets.spark.datasource." + ds +".", true));
		return settings;
	}

}
