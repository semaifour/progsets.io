package io.progsets.rest;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.progsets.common.Restponse;

@RestController
public class GreetingRestController extends BaseController {
	
	@GetMapping("/")
	public Restponse<Map<String, Object>> home(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> hi = new HashMap<String, Object>();
		hi.put("name", "progsets-psql service end-point");
		hi.put("version", "0.0.1");
		hi.put("time", new Date());
		hi.put("status", "running");
		return new Restponse<Map<String, Object>>(hi);
	}
	
	@GetMapping("/help")
	public Restponse<Map<String, Object>> help(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> hi = new HashMap<String, Object>();
		hi.put("Action URL", "$curl -XPOST\n"
				+ "-H \"Authorization: Basic admin:admin123\" -H\"Content-Type:text/psql\" http://localhost:8175/progesets/rest/psql/exe");
		hi.put("BODY Content", "credit = ifile?file.path=file:///Users/megandran/Downloads/MyTestData.csv&file.split=true&file.hasheader=true&file.columns=Date:string,Transdate:string,Ref:string,Description:string,Amount:string,Balance:string credit = convert?view=credit&columns=Date:date:mm/dd/yyyy,Transdate:date:mm/dd/yyyy,Ref:string,Description:string,Amount:double,Balance:double result = ssql?sql=select sum(Amount) as Amount from credit return?view=result&as=maps&debug=true close\n"
				+ "\n"
				+ "");
		hi.put("time", new Date());
		hi.put("status", "running");
		return new Restponse<Map<String, Object>>(hi);
	}

	
}
