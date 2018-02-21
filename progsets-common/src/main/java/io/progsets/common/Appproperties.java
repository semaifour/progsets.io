package io.progsets.common;

import java.io.InputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;

import io.progsets.util.SimpleCache;

public class Appproperties implements Serializable {
	
	private static final long serialVersionUID = -1855444891883298612L;
	
	static Appproperties _instance;
	static Properties appprops;
	static SimpleCache<HashMap<String, String>> propsmapcache = new SimpleCache<HashMap<String, String>>("propsmapcache", -1);
	
	private Appproperties() {}
	
	@PostConstruct
	public void init() {
		String cname = System.getProperty("ps.app", "app") 
							+ "-" + System.getProperty("ps.env", "env")
							+ "-" + "application.properties";
		appprops = new Properties();
		appprops.put("CONFIG_NAME", cname);
		try {
			InputStream is = this.getClass().getResourceAsStream("/"+cname);
			appprops.load(is);
		} catch (Exception e) {
		}
	}
	
	public Properties props() {
		return appprops;
	}

	public String appname() {
		return prop("progsets.spark.appname", "progsets-default");
	}
	
	public String master() {
		return prop("progsets.spark.master", "local");
	}

	public String prop(String property, String defaultValue) {
		return appprops.getProperty(property, defaultValue);
	}
	
	public int prop(String property, int defaultValue) {
		String str = appprops.getProperty(property);
		try {
			if (StringUtils.isNotEmpty(str)) {
				return Integer.parseInt(str);
			}
		} catch(Exception e) {}
		return defaultValue;
	}

	
	/**
	 * Returns a map of properties matching the given prefix. Cuts the prefix from key names if true.
	 * 
	 */
	public HashMap<String, String> propsWithPrefix(String prefix, boolean cutprefix) {
		return propsWithPrefix(prefix, cutprefix, null);
	}
	/**
	 * Returns a map of properties matching the give prefix. Cuts the prefix from key names if true. Prepends give prependfix if not null.
	 * 
	 * @param prefix
	 * @param cutprefix
	 * @param prependfix
	 * @return
	 */
	public HashMap<String, String> propsWithPrefix(String prefix, boolean cutprefix, String prependfix) {
		HashMap<String, String> m = propsmapcache.get(prefix + "-" + cutprefix + "-" + prependfix);
		if (m != null) return m;
		m = new HashMap<String, String>();
		String key = null, newkey = null;
		for(Object o : appprops.keySet()) {
			key = String.valueOf(o);
			if (StringUtils.startsWith(key, prefix)) {
				newkey = cutprefix ? key.substring(prefix.length()) : key;
				newkey = prependfix != null ? prependfix + newkey : newkey;
				m.put(newkey, appprops.getProperty(key));
			}
		}
		propsmapcache.putForGood(prefix + "-" + cutprefix + "-" + prependfix, m);
		return m;
	}

	/**
	 * Singleton instance
	 * 
	 * @return
	 */
	public static synchronized Appproperties instance() {
		if (_instance == null) {
			_instance = new Appproperties();
		}
		return _instance;
	}

	public String[] getUnrestrictedUris() {
		return null;
	}

	public String getServerContextPath() {
		// TODO Auto-generated method stub
		return null;
	}
}
