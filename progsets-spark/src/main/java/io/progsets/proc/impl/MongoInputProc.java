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
 * imongo?datasource=qubercloud-mongo&saveas=mycollectionview&mongodb.database=facesix&mongodb.collection=site
 * 
 * OR 
 * 
 * mycollectionview = imongo?datasource=qubercloud-mongo&mongodb.database=facesix&mongodb.collection=site&datasource=abc
 * 
 * </pre>
 * 
 * @author mjs
 *
 */
public class MongoInputProc extends Procedure {
	
	private static final long serialVersionUID = 4262207398833836899L;

	@Override
	public Procontext run(Procontext pc) {

		try {
			assertNotNullParams("saveas", "collection");
			Map<String, String> viewparams = settings(pc);
			viewparams.putAll(paramap("mongodb.", true, "spark.mongodb.input."));
			assertNotNullKeys(viewparams, "spark.mongodb.input.uri","spark.mongodb.input.database","spark.mongodb.input.collection");
			if(isDebug()) {
				LOG().info("proc options =>" + viewparams.toString());
			}
			Dataset<Row> resultset = null;
			resultset = pc.ss().read().format(viewparams.get("spark.read.format")).options(viewparams).load();
			createOrReplaceView(resultset);
			pc.pushProcStack(true, this, "DONE");
		} catch (Exception e) {
			LOG().error("proc-mongoview-run exception", e);
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
			ds = pc.appproperties().prop("progsets.proc.imongo.default.datasource", "default");
		}
		//load default settings for the connector
		settings.putAll(pc.appproperties().propsWithPrefix("progsets.proc.imongo.default.", true));
		//load default settings for the data source
		settings.putAll(pc.appproperties().propsWithPrefix("progsets.spark.datasource." + ds + ".", true));
		
		if(isparam("collection")) { settings.put("spark.mongodb.input.collection", param("collection")); }
		if(isparam("database")) { settings.put("spark.mongodb.input.database", param("database")); }
		if(isparam("uri")) { settings.put("spark.mongodb.input.uri", param("uri")); }

		return settings;
	}

}
