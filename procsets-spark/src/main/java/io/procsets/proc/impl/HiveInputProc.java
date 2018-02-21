package io.procsets.proc.impl;

import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;

import io.procsets.proc.Procedure;
import io.procsets.proc.Procontext;

/**
 * 
 * Enables Hive Support  
 * 
 * <pre>
 * 
 * Example:
 *     ihive?datasource=my-hive-data-source-name 
 * </pre>
 * @author mjs
 *
 */
public class HiveInputProc extends Procedure {
	
	private static final long serialVersionUID = 4262207398833836899L;

	@Override
	public Procontext run(Procontext pc) {
		try {
			assertNotNullParams("datasource");
			Map<String, String> viewoptions = settings(pc);
			pc.ss().conf().set("spark.sql.warehouse.dir", viewoptions.get("path"));
			pc.pushProcStack(true, this, "DONE");
		} catch (Exception e) {
			LOG().error("proc-hiveview-run exception", e);
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
			ds = pc.appproperties().prop("procsets.proc.ihive.default.datasource", "default");
		}
		//load default settings for the connector
		settings.putAll(pc.appproperties().propsWithPrefix("procsets.proc.ihive.default.", true));
		//load default settings for the data source
		settings.putAll(pc.appproperties().propsWithPrefix("procsets.spark.datasource." + ds +".", true));
		return settings;
	}

}
