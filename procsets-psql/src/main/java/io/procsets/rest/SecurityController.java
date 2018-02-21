package io.procsets.rest;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.procsets.common.Restponse;
import io.procsets.util.Cryptor;

@RestController
@RequestMapping("/rest/security")
public class SecurityController  extends BaseController {
	static Logger LOG = LoggerFactory.getLogger(SecurityController.class.getName());

	@Autowired
	Cryptor cryptor;
	
    @RequestMapping("gentoken")
    public  Restponse<Map<String, String>> gentoken(@RequestParam("id") String id, @RequestParam("key") String key) {
    	Map<String, String> map = new HashMap<String, String>();

    	try {
	    	String sig = Long.toString(System.currentTimeMillis(), 16);
	    	id = cryptor.encrypt(id + ":" + sig);
	    	key = cryptor.encrypt(key + ":" + sig);
	    	sig = cryptor.encrypt(sig);
	
	    	map.put("what", "procsets api token");
	    	map.put("id", id);
	    	map.put("key", key);
	    	map.put("sig", sig);
	    	LOG.info("gentoken JSON :" + map.toString());
    	} catch(Throwable t) {
    		LOG.warn("Error while preparing token for :" + id, t);
    	}
    	return new Restponse<Map<String, String>>(map);
    }
    
    public Map<String, String> parsetoken(String id, String key, String sig) {
	    	Map<String, String> map = new HashMap<String, String>();
	    	boolean isvalid = false;
	    	try {
		    	sig = cryptor.decrypt(sig);
		    	id = cryptor.decrypt(id);
		    	key = cryptor.decrypt(key);
		    	int i = id.indexOf(sig);
		    	if (i > 0) {
		    		id = id.substring(0, i - 1);
		    		i = key.indexOf(sig);
		    		if (i > 0) {
		    			key = key.substring(0, i-1);
		    			if (id != null & id.length() > 0 && key != null & key.length() > 0) {
		    	    		isvalid = true;
		    	    	}
		    		}
		    	}
		    	map.put("id", id);
		    	map.put("key", key);
		    	map.put("sig", sig);
	    	} catch (Throwable t) {
	    		LOG.warn("Error while parsing token for :" + id, t);
	    	}
	    	map.put("isvalid", String.valueOf(isvalid));
	    	return map;
    }
    
    @RequestMapping("valtoken")
    public Restponse<Map<String, String>> valtoken(@RequestParam("id") String id, @RequestParam("key") String key, @RequestParam("sig") String sig) {
	    	Map<String, String> map = parsetoken(id, key, sig);
	    	map.put("what", "facesix val token");
	    	map.put("id", id);
	    	map.put("key", key);
	    	map.put("sig", sig);
	    	LOG.info("valtoken JSON :" + map.toString());
	    	return new Restponse<Map<String, String>>(map);
    }
    
    @RequestMapping("encrypt")
    public @ResponseBody String encrypt(@RequestParam("k") String key) {
    	try {
			return cryptor.encrypt(key);
		} catch (Exception e) {
			LOG.warn("Encrypt failed", e);
			return e.getMessage();
		}
    }
    
    @RequestMapping("decrypt")
    public Restponse<String> decrypt(@RequestParam("v") String value) {
    	try {
			return new Restponse<String>(cryptor.decrypt(value));
		} catch (Exception e) {
			LOG.warn("Decrypt failed", e);
			return new Restponse<String>(e.getMessage());
		}
    }
    
}