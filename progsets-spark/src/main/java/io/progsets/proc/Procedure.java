package io.progsets.proc;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * Base class for Procedure
 * 
 * @author msathaia
 *
 */
public abstract class Procedure implements Serializable {

	private static final long serialVersionUID = 8813770234319486782L;

	Logger LOG = LoggerFactory.getLogger(this.getClass());
	
	private String name;
	private Map<String, String> parameters;

	public abstract Procontext run(Procontext pc);

	public String getName() {
		return name;
	}

	public Procedure setName(String name) {
		this.name = name;
		return this;
	}
	
	public String saveas() {
		return param("saveas");
	}

	public Map<String, String> getParameters() {
		return parameters;
	}

	public Procedure setParameters(Map<String, String> parameters) {
		this.parameters = parameters;
		return this;
	}
	
	public String getParameter(String param) {
		return this.parameters.get(param);
	}
	
	public String param(String param) {
		return this.getParameter(param);
	}

	public String param(String param, String s) {
		String s0 = this.parameters.get(param);
		return s0 != null ? s0 : s;
	}

	public int param(String param, int i) {
		try {
			i = Integer.parseInt(getParameter(param));
		} catch (Exception e) {
		}
		return i;
	}
	
	/**
	 * Returns a map of parameters matching the given prefix.
	 * 
	 * @param prefix  		prefix by which the parameter name begins with (if null/blank, all params included)
	 * @param cutPrefix 	should the prfix be cut from resulting parameter names
	 * @param prepend 		addition prefix to be prepended to the param name
	 * @return
	 */
	public Map<String, String> paramap(String prefix, boolean cutPrefix, String prepend) {
		Map<String, String> m = null;
		m = new HashMap<String, String>();
		if (prefix == null || StringUtils.isBlank(prefix)) {
			prefix = null;
			cutPrefix = false;
		}
		for(String k : parameters.keySet()) {
			if (prefix == null || StringUtils.startsWith(k, prefix)) {
				m.put(prepend + (cutPrefix ? k.substring(prefix.length()) : k), parameters.get(k));
			}
		}
		return m;
	}

	public long param(String param, long l) {
		try {
			l = Long.valueOf(getParameter(param));
		} catch (Exception e) {
		}
		return l;
	}
	
	public boolean param(String param, boolean b) {
		try {
			b = Boolean.valueOf(getParameter(param));
		} catch (Exception e) {
		}
		return b;
	}

	/**
	 * 
	 * Return true if parameter's value matches any of the given pvalues, false otherwise.
	 * 
	 * @param param
	 * @param values
	 * @return
	 */
	public boolean isparam(String param, String... pvalues) {
		if (ArrayUtils.isEmpty(pvalues)) {
			return getParameter(param) != null;
		}
		for(String value : pvalues) {
			if (StringUtils.equalsIgnoreCase(value, getParameter(param))) {
				return true;
			}
		}
		return false;
	}
	
	public Logger LOG() {
		return LOG;
	}
	
	@Override
	public String toString() {
		return "Procedure [name=" + name + ", parameters=" + parameters + "]";
	}

	/**
	 * 
	 * read & resolve input set for given proc
	 * 
	 * @param pc
	 * @return
	 */
	protected JavaRDD<Map<String, Object>> resolve(Object rdd) {

		JavaRDD<Map<String, Object>> jRDD = null;
		
		if (rdd instanceof JavaPairRDD) {
			JavaPairRDD<String, Map<String, Object>> jpRDD = (JavaPairRDD<String, Map<String, Object>>) rdd;
			jRDD = jpRDD.values();
		} else if (rdd instanceof JavaRDD) {
			jRDD = (JavaRDD<Map<String, Object>>) rdd;
		}
		return jRDD;
	}

	
	/**
	 * Returns procedure settings collection all attrs set with prefix [progsets.proc.<proc-name>.settings.]
	 * 
	 * @return
	 */
	public Map<String, String> settings(Procontext pc) {
		return (HashMap<String, String>)pc.appproperties().propsWithPrefix("progsets.proc." + this.getName() + ".settings.", true).clone();
	}
	
	public boolean assertNotNullParams(String... params) {
		for(String p : params) {
			if (param(p) == null) {
				throw new IllegalArgumentException("proc-argument-exception :param["+ p + "] is missing");
			}
		}
		return true;
	}
	
	/**
	 * Throws exception if any of the given key is not found or holds null value in the given map.
	 * Return true if all is good.
	 * Return false if either map or keys or both are null;
	 */
	public boolean assertNotNullKeys(Map<String, String> viewparams, String...keys) {
		if (viewparams == null || keys == null) return false;
		for(String k : keys) {
			if (viewparams.get(k) == null) {
				throw new IllegalArgumentException("proc-argument-exception :key["+ k + "] is missing");
			}
		}
		return true;
	}

	public boolean isDebug() {
		return isparam("debug", "true");
	}
	
	
	/**
	 * Creates/Replaces temp/global view based on saveas parameter.
	 * 
	 * <pre>
	 * 
	 * [create/replace global view]
	 *  	
	 * 	global::myviewname = ssql?sql=.....
	 *  
	 *  OR
	 * 
	 * [create/replace temp view]
	 * 
	 * 	temp::myviewname = ssql?sql=.....
	 *  
	 *  OR
	 * 
	 * [create/replace temp view]
	 * 
	 *  myviewname = ssql?sql=.....
	 *  
	 * </pre>
	 * 
	 * 
	 * @param resultset
	 * @return
	 */
	public boolean createOrReplaceView(Dataset<Row> resultset) {
		String[] saveas = param("saveas").split("::");
		if (saveas.length == 1) {
			resultset.createOrReplaceTempView(saveas[0]);
		} else if (saveas.length == 2) {
			if (saveas[0].trim().equalsIgnoreCase("global")) {
				resultset.createOrReplaceGlobalTempView(saveas[1].trim());
			} else if (saveas[0].trim().equalsIgnoreCase("temp")) {
				resultset.createOrReplaceTempView(saveas[1].trim());
			} else {
				return false;
			}
		} else {
			return false;
		}
		LOG().info("created/replaced view [" + param("saveas") + "]");
		return true;
	}
	
	
	/**
	 * 
	 * Returns a map of key = value pair for the given list of keys
	 * 
	 * 
	 * @param params
	 * @return
	 */
	public Map<String, String> paramap(String...params) {
			Map<String, String> map = new HashMap<String, String>();
	
			for(String p : params) {
				map.put(p, parameters.get(p));
			}

			return map;
	}

}
