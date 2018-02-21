package io.progsets.zeppelin;

import java.util.Properties;

import org.apache.zeppelin.interpreter.InterpreterContext;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequest;
import com.mashape.unirest.request.HttpRequestWithBody;

public class SavedPsqlInterpreter extends PsqlInterpreter {

	public SavedPsqlInterpreter(Properties property) {
		super(property);
	}

	@Override
	protected HttpRequest prepareUriRestRequest(String cmd, InterpreterContext interpreterContext) throws UnirestException {
		HttpRequestWithBody request = null;
		request = Unirest.post(super.getProperty(PROGSETS_URL) + "/rest/psql/exe/" + cmd + "?return.as=sheet");
		request.header("Accepts", "application/json")
				.header("Authorization", super.getProperty(PROGSETS_AUTH))
				.body(cmd).getHttpRequest();
		return request;
	}
}
