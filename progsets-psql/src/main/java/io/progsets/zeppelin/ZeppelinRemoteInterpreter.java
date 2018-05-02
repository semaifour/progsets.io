package io.progsets.zeppelin;

import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.zeppelin.interpreter.remote.RemoteInterpreterServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.progsets.common.Appproperties;

@Service
public class ZeppelinRemoteInterpreter {

	Logger LOG = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	Appproperties appproperties;

	RemoteInterpreterServer interpreter = null;
	
	@PostConstruct
	public void start() {
		try {
			LOG.info("Starting ZeppelinRemoteInterpreter ...");
			warmup();
			interpreter = new RemoteInterpreterServer(appproperties.prop("progsets.zeppelin.interpreter.port", 8176));
			interpreter.start();
			LOG.info("Zeppeling Remote Interpreter is listening for requests at:" + interpreter.getPort());
		} catch (Exception e) {
			LOG.error("Failed to start ZeppelinRemoteInterpreter", e);
		}
		
	}
	
	private void warmup() {
		Properties prop = new Properties();
		prop.put("progsets.url", "http://localhost:8175/progsets");
		prop.put("progsets.auth", "Basic admin:admin123");
		PsqlInterpreter psqli = new PsqlInterpreter(prop);
		psqli.getFormType();
		psqli = null;
	}
	
	@PreDestroy
	public void stop() {
		try {
			interpreter.shutdown();
			interpreter = null;
		} catch (Exception e) {
			LOG.error("Exception while shutting down ZeppelinRemoteInterpreter", e);
		}
	}
}
