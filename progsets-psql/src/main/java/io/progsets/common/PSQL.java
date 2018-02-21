package io.progsets.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import io.progsets.proc.Procedure;
import io.progsets.proc.ProcedureFactory;

/**
 * 
 * Query represents a procesets query defined procset query language as below
 * 
 * 
 * <pre>
 * 
 * 	Format:
 * 			procname?k1=v1&k2=v2|procname?k1=v1&k2=v2...|.....
 * 
 * 	Example:
 * 
 * 			queryelastic?index=myindex/mytype&query=regex&size=1000&from=0&out=r0|countbyfield?field=f1&out=r0count|...
 * </pre>
 * 
 * 
 * @author mjs
 *
 */
public class PSQL {

	private String queryString = null;
	private ArrayList<Procedure> procedures = new ArrayList<Procedure>();
	private Map<String, String> overrides;
	
	public static PSQL parse(String psql, Map<String, String> overrides) {
		PSQL instance = new PSQL();
		instance.setQueryString(psql);
		String[] procs = psql.split("(\\|)|(\\r?\\n)");
		for (String p : procs) {
			try {
				if(StringUtils.startsWith(p, "#")) continue;
				int i = p.indexOf("?");
				String name = p.trim();
				Map<String, String> params = new HashMap<String, String>();
				if (i > 0) {
					name = name.substring(0, i);
					String[] args = p.substring(i+1).split("&");
					for(String arg : args) {
						i = arg.indexOf("=");
						if (i > 0) params.put(arg.substring(0, i), arg.substring(i+1));
					}
					if ((i = name.indexOf("=")) > 0) {
						params.put("saveas", name.substring(0, i).trim());
						name = name.substring(i+1).trim();
					}
				}
				if(overrides != null) params.putAll(overrides);
				instance.procedures.add(ProcedureFactory.procedure(name, params));
			} catch(Exception e) {
				throw new RuntimeException("Parse error. PSQL:[" + p + "]", e);
			}
		}
		return instance;
	}
	
	public static PSQL parse(String psql) {
		return parse(psql, null);
	}

	public String getQueryString() {
		return queryString;
	}

	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}

	public ArrayList<Procedure> getProcedures() {
		return procedures;
	}

	public void setProcedures(ArrayList<Procedure> procedures) {
		this.procedures = procedures;
	}

	@Override
	public String toString() {
		return "Query [queryString=" + queryString + ", procedures=" + procedures + "]";
	}

	public void setOverrides(Map<String, String> overrides) {
		this.overrides = overrides;
	}
	
	public Map<String, String> overrides() {
		return this.overrides;
	}
	
}
