package io.progsets.zeppelin;

import javax.annotation.PostConstruct;

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

	PsqlInterpreter p = null;
	
	@PostConstruct
	public void init() {
		try {
			LOG.info("Starting ZeppelinRemoteInterpreter ...");
			RemoteInterpreterServer interpreter = new RemoteInterpreterServer(appproperties.prop("progsets.zeppelin.interpreter.port", 8176));
			interpreter.start();
		} catch (Exception e) {
			LOG.error("Failed to start ZeppelinRemoteInterpreter", e);
		}
		
	}
}
