package io.procsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource("classpath:${ps.app:app}-${ps.env:env}-application.properties")
public class Appconfig {

	@Autowired
	Environment env;
	
	public Environment env() {
		return this.env;
	}
}
