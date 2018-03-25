package io.progsets.rest;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.progsets.common.PSQL;
import io.progsets.common.Restponse;
import io.progsets.proc.Procedure;
import io.progsets.proc.Procontext;
import io.progsets.util.SimpleCache;

@RestController
@RequestMapping("rest/psql")
public class PSQLRestController extends BaseController {
	
	private SimpleCache<Procontext> procontextCache = new SimpleCache<Procontext>("procontextCache", 4*60*60*1000);
	

	/**
	 * Restful end-point for GET: rest/psql/exe to execute a saved query
	 * 
	 * @param request
	 * @param response
	 * @param savedquery - path variable : unique name of the saved query
	 * @param exeid		- execution id to track spark jobs
	 * @return
	 */
	@GetMapping(value="/exe", produces = "application/json")
	public Restponse<Object> exeget(HttpServletRequest request,
								 HttpServletResponse response,
								 @RequestParam String psql,
								 @RequestParam(value="exeid", required=false) String exeid) {
		return exe(request, response, psql, exeid);
	}
	
	/**
	 * Restful end-point for POST: rest/psql/exe
	 * 
	 * @param request
	 * @param response
	 * @param psql
	 * @param exeid		- execution id to track spark jobs
	 * @return
	 */
	@PostMapping(value="/exe", produces = "application/json", consumes="text/psql")
	public Restponse<Object> exepost(HttpServletRequest request,
								 HttpServletResponse response,
								 @RequestBody String psql,
								 @RequestParam(value="exeid", required=false) String exeid) {
		return exe(request, response, psql, exeid);
	}
	
	/**
	 * Executes the given PSQL and returns query result
	 * 
	 * @param request
	 * @param response
	 * @param psql
	 * @param exeid		- execution id to track spark jobs
	 * @return
	 */
	protected Restponse<Object> exe(HttpServletRequest request,HttpServletResponse response, String psql, String exeid) {
 
		long starttime = System.currentTimeMillis();
		Restponse<Object> result = null;
		Procontext pc = null;
		try {
			LOG.info("PSQL:[{}]", psql);
			PSQL psqlo = PSQL.parse(psql, getOverrides(request));
			pc = procontextCache.get(request.getSession().getId());
			if (pc == null || !pc.isValid()) {
				pc = new Procontext(exeid == null ? appproperties().appname() : exeid, 
									appproperties().master(), appproperties);
				procontextCache.put(request.getSession().getId(), pc);
			}
			for(Procedure proc : psqlo.getProcedures()) {
				LOG.info("invoke proc [{}] with parameters [{}]", proc.getName(), proc.getParameters().toString());
				proc.run(pc);
				LOG.info("proc [{}] returned with success status [{}] with message [{}]", proc.getName(), pc.procStack().peek().a(), pc.procStack().peek().c());
				if (!pc.wasLastProcSuccess()) {
					result = new Restponse<Object>(false, 300).setMessage(pc.procStack().peek().c()).setBody(proc);
					LOG.info("PSQL execution terminated and exiting with error code 300");
					break;
				}
			}
			if (result == null) result = new Restponse<Object>(pc.getResultlist());
		} catch(Exception e) {
			result = new Restponse<Object>(false, 300).setMessage(e.getMessage());
			LOG.error("Error executing psql", e);
		}
		result.setTimetaken(System.currentTimeMillis() - starttime);
		result.setSession(request.getSession().getId());
		return result;
	}

	
	/**
	 * 
	 * Convert http parameters map from Map <String, String[]> to Map <String, String> 
	 * 
	 * @param request
	 * @return
	 */
	private Map<String, String> getOverrides(HttpServletRequest request) {
		Map<String, String> overrides = new HashMap<String, String>();
		for(Entry<String, String[]> entry : request.getParameterMap().entrySet()) {
			overrides.put(entry.getKey(), entry.getValue()[0]);
		}
		return overrides;
	}

}
