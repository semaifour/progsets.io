package io.procsets.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import io.procsets.common.Appproperties;

public class BaseController {

	Logger LOG = LoggerFactory.getLogger(this.getClass());

	@Autowired
	Appproperties appproperties;
	
	public Appproperties appproperties() {
		return this.appproperties;
	}
	
	public Logger LOG() {
		return this.LOG;
	}
}
