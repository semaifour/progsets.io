package io.procsets.rest;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.procsets.common.Restponse;

@RestController
public class GreetingRestController extends BaseController {
	
	@GetMapping("/")
	public Restponse<Map<String, Object>> home(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> hi = new HashMap<String, Object>();
		hi.put("name", "procsets-psql service end-point");
		hi.put("version", "0.0.1");
		hi.put("time", new Date());
		hi.put("status", "running");
		return new Restponse<Map<String, Object>>(hi);
	}
	
	@GetMapping("/help")
	public Restponse<Map<String, Object>> help(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> hi = new HashMap<String, Object>();
		hi.put("name", "procsets-psql service end-point");
		hi.put("version", "0.0.1");
		hi.put("time", new Date());
		hi.put("status", "running");
		return new Restponse<Map<String, Object>>(hi);
	}

	
}
