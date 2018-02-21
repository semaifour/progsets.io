package io.procsets.proc.impl;

import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import io.procsets.proc.Procedure;
import io.procsets.proc.Procontext;

/**
 * 
 * Generic data loader/view creators. 
 * 
 * It uses '.spark.read.format' to identify the connector and passes attributes from datasource config 
 * and parameters passed to procesure with the prefix 'proc.*' as 'spark context options' to load data.
 * 
 * 
 * <pre>
 * 
 * Example:
 * 
 * dataset = genview?datasource=name-of-generic-datasource&proc.<k1.name>=value1&proc.<k2.name>=value2...
 *
 * </pre>
 * 
 * @author mjs
 *
 */
public class GenericInputProc extends Procedure {
	
	private static final long serialVersionUID = 4262207398833836899L;

	@Override
	public Procontext run(Procontext pc) {
		try {
			assertNotNullParams("datasource", "saveas");
			Map<String, String> viewoptions = settings(pc);
			viewoptions.putAll(paramap("", false, ""));
			Dataset<Row> resultset = pc.ss().read().format(viewoptions.get("spark.read.format")).options(viewoptions).load();
			resultset.createOrReplaceTempView(param("saveas"));
			pc.pushProcStack(true, this, "DONE");
		} catch (Exception e) {
			LOG().error("proc-generic-view-run exception", e);
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
		settings.putAll(pc.appproperties().propsWithPrefix("procsets.spark.datasource." + ds +".", true));
		return settings;
	}

}