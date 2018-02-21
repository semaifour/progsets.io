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
 * 
 * <pre>
 * 
 * isolr?datasource=qubercloud-elastic&saveas=myindexview&solr.zkhost=zk1,zk2&solr.collection=zkcollectionname&query=http-encoded-solr-query
 *   
 * OR 
 * 
 * mycollectionview = isolr?datasource=qubercloud-elastic&solr.zkhost=zk1,zk2&solr.collection=zkcollectionname&query=http-encoded-solr-query
 * 
 * 
 * </pre>
 * 
 * @author mjs
 *
 */
public class SolrInputProc extends Procedure {
	
	private static final long serialVersionUID = 4262207398833836899L;

	@Override
	public Procontext run(Procontext pc) {
		try {
			assertNotNullParams("saveas");
			Map<String, String> viewparams = settings(pc);
			viewparams.putAll(paramap("solr.", true, ""));
			assertNotNullKeys(viewparams, "zkhost", "collection");
			Map<String, String> filters = paramap("filter.", true , "");
			for(Map.Entry<String, String> e : filters.entrySet()) {
				String[] v = e.getValue().split("\\.");
				Dataset<Row> parentView = pc.ss().table(v[0].trim());
				StringBuilder sb = new StringBuilder();
				if (parentView != null) {
					//if filter refers to a parent view
					for(Row r : parentView.collectAsList()) {
						sb.append(r.getAs(v[1].trim()).toString());
						sb.append(" ");
					}
				} else {
					//if filter is not a parent view, use the value as it is.
					sb.append(e.getValue());
				}
				viewparams.put("filters", " +" + e.getKey() + " : (" + sb.toString() + ")" );
			}
			Dataset<Row> resultset = pc.ss().read().format("solr").options(viewparams).load();
			createOrReplaceView(resultset);
			pc.pushProcStack(true, this, "DONE");
		} catch (Exception e) {
			LOG().error("proc-SolrView-run exception", e);
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
			ds = pc.appproperties().prop("progsets.proc.isolr.default.datasource", "default");
		}
		//load default settings for the connector
		settings.putAll(pc.appproperties().propsWithPrefix("progsets.proc.isolr.default.solr.", true));
		//load default settings for the data source
		settings.putAll(pc.appproperties().propsWithPrefix("progsets.spark.datasource." + ds + ".solr.", true));
		return settings;
	}

}
