package io.progsets.util;

import java.net.URL;

import oi.thekraken.grok.api.Grok;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FSGrok {
	
	static Logger LOG = LoggerFactory.getLogger(FSGrok.class.getName());

	static URL path = null;
	
	static {
		path = FSGrok.class.getClassLoader().getResource("patterns/patterns");
		LOG.info("Grok Patterns loaded from :" + path.getFile());
	}
	
	public static Grok newGrok(String pattern) {
		try {
			Grok grok = Grok.create(path.getFile());
			grok.compile(pattern);
			return grok;
		} catch (Exception e) {
			LOG.info("Grok initialization failed:", e);
		}
		return Grok.EMPTY;
	}
}
