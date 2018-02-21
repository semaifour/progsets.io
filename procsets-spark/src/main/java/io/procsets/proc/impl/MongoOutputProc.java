package io.procsets.proc.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import com.mongodb.spark.config.ReadConfig;

import io.procsets.proc.Procedure;
import io.procsets.proc.Procontext;

/**
 * 
 * omongo?datasource=qubercloud-mongo&saveas=mycollectionview&mongodb.database=facesix&mongodb.collection=site
 * 
 * OR 
 * 
 * mycollectionview = omongo?datasource=qubercloud-mongo&mongodb.database=facesix&mongodb.collection=site&datasource=abc
 * 
 * @author mjs
 *
 */
public class MongoOutputProc extends Procedure {
	
	private static final long serialVersionUID = 4262207398833836899L;

	@Override
	public Procontext run(Procontext pc) {

		try {
			assertNotNullParams("saveas", "mongodb.collection");
			Map<String, String> viewparams = settings(pc);
			viewparams.putAll(paramap("mongodb.", true, "spark.mongodb.output."));
			assertNotNullKeys(viewparams, "spark.mongodb.output.uri","spark.mongodb.output.database","spark.mongodb.output.collection");
			ReadConfig readconfig = ReadConfig.create(viewparams);
			Dataset<Row> resultset = null;//MongoSpark.load(pc.jsc(), readconfig).toDF();
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
			ds = pc.appproperties().prop("procsets.proc.mongo.default.datasource", "default");
		}
		//load default settings for the connector
		settings.putAll(pc.appproperties().propsWithPrefix("procsets.proc.mongo.default.", true));
		//load default settings for the data source
		settings.putAll(pc.appproperties().propsWithPrefix("procsets.spark.datasource." + ds + ".", true));
		return settings;
	}

}
