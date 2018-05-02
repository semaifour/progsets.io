package io.progsets.zeppelin;

import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.zeppelin.interpreter.Interpreter;
import org.apache.zeppelin.interpreter.InterpreterContext;
import org.apache.zeppelin.interpreter.InterpreterResult;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequest;
import com.mashape.unirest.request.HttpRequestWithBody;

public class PsqlInterpreter extends Interpreter {

	Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	
	public static final String PROGSETS_URL = "progsets.url";
	public static final String PROGSETS_AUTH = "progsets.auth";

	public PsqlInterpreter(Properties property) {
		super(property);
	}

	@Override
	public void open() {
		LOGGER.info("open");
		LOGGER.info("URL :" + super.getProperty(PROGSETS_URL));
	}
	
	@Override
	public void cancel(InterpreterContext interpreterContext) {
		LOGGER.info("cancelled");
	}

	@Override
	public void close() {
		LOGGER.info("closed");
	}

	@Override
	public FormType getFormType() {
		return FormType.NONE;
	}

	@Override
	public int getProgress(InterpreterContext interpreterContext) {
		return 0;
	}

	@Override
	public InterpreterResult interpret(String code, InterpreterContext interpreterContext) {
		logger.info("Run PSQL Code:'" + code + "'");
		try {
			if (StringUtils.isEmpty(code) || StringUtils.isEmpty(code.trim())) {
			      return new InterpreterResult(InterpreterResult.Code.SUCCESS);
			}
			return invoke(code, interpreterContext);
		} catch (Exception e) {
			LOGGER.error("Error running PSQL", e);
			return new InterpreterResult(InterpreterResult.Code.ERROR, InterpreterResult.Type.TEXT, e.toString());
		}
	}
	
	protected void addAngularObject(InterpreterContext interpreterContext, String prefix, Object obj) {
	    interpreterContext.getAngularObjectRegistry().add(
	        prefix + "_" + interpreterContext.getParagraphId().replace("-", "_"),
	        obj, null, null);
	  }
	
	protected InterpreterResult invoke(String cmd, InterpreterContext interpreterContext) throws UnirestException {
		String data = null;
		try {
			HttpRequest request = prepareUriRestRequest(cmd, interpreterContext);
			HttpResponse<JsonNode> result = request.asJson();
			if (result.getStatus() >= 200 && result.getStatus() <= 300) {
				JSONObject response = result.getBody() != null ? result.getBody().getObject() : null;
				if (response.getBoolean("success")) {
					JSONArray resultlist = response.getJSONArray("body");
					JSONObject table0 = resultlist.getJSONObject(0);
					data = table0.getString("data");
					addAngularObject(interpreterContext, "progsets", data); //TODO: find why to do this
					return new InterpreterResult(InterpreterResult.Code.SUCCESS,
				        					 InterpreterResult.Type.TABLE,
				        					 data);
				} else {
					data = response.getString("message");
					addAngularObject(interpreterContext, "progsets", data); //TODO: find why to do this
					return new InterpreterResult(InterpreterResult.Code.ERROR,
	   					 InterpreterResult.Type.TEXT,
	   					 response.getString("message"));
				}
			} else {
				throw new UnirestException("Progsets REST invocation Error Code :" + result.getStatus() + ", Text :" + result.getStatusText());
			}
		} catch (Exception e) {
			return new InterpreterResult(InterpreterResult.Code.ERROR,
  					 InterpreterResult.Type.TEXT, e.toString());
		}

	}

	protected HttpRequest prepareUriRestRequest(String cmd, InterpreterContext interpreterContext) throws UnirestException {
		HttpRequestWithBody request = null;
		request = Unirest.post(super.getProperty(PROGSETS_URL) + "/rest/psql/exe?return.as=sheet");
		request.header("Accepts", "application/json")
				.header("Authorization", super.getProperty(PROGSETS_AUTH))
				.header("Content-Type", "text/psql")
				.body(cmd).getHttpRequest();
		return request;
	}


	public static void main(String[] args) throws UnirestException {
		Properties prop = new Properties();
		prop.put("progsets.url", "http://localhost:8175/progsets");
		prop.put("progsets.auth", "Basic admin:admin123");
		
		PsqlInterpreter psqli = new PsqlInterpreter(prop);
		
		String psql = "finBookings = imongo?database=dgmdb&collection=FinanceBookingsData&datasource=defaultmongo"
		+ "\r\nbookings = ssql?sql=select quarter, sum(bookings) as totalbookings from finBookings group by quarter,be  limit 5 "
		+ "\r\nreturn?view=bookings&as=sheet"
		+ "\r\nclose";
		
		InterpreterResult value = psqli.interpret(psql, null);
		
		System.out.println(value.toString());
		
	}
}
