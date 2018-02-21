package io.progsets.proc.impl;

import java.util.Map.Entry;

import org.apache.commons.lang3.exception.ExceptionUtils;

import io.progsets.proc.Procedure;
import io.progsets.proc.Procontext;

/**
 * To create/configure new data source for the session
 * 
 * myDSname = datasource?format=es|solr|file&clear=true|false&...datasource specific parameters ...
 * 
 * @author mjs
 *
 */
public class DatasourceProc extends Procedure {
	
	private static final long serialVersionUID = -6574048347365434027L;

	@Override
	public Procontext run(Procontext pc) {

		try {
			assertNotNullParams("saveas", "format");
			String name = param("saveas");
			pc.appproperties().props().put("progsets.spark.datasource." + name + ".spark.read.format", param("format"));
			for(Entry<String, String> param : super.getParameters().entrySet()) {
				pc.appproperties().props().put("progsets.spark.datasource." + name + "." + param.getKey(), param.getValue());
			}
			pc.pushProcStack(true, this, "DONE");
		} catch (Exception e) {
			LOG().error("proc-datasource-run exception", e);
			pc.pushProcStack(false, this, (isDebug() ? ExceptionUtils.getStackTrace(e) : e.getMessage()));
		}
		return pc;
	}
}
