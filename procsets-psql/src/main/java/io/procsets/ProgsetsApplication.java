package io.procsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import io.procsets.common.Appproperties;

@SpringBootApplication
public class ProgsetsApplication {
	static Logger LOG = LoggerFactory.getLogger(ProgsetsApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(ProgsetsApplication.class, args);
		LOG.info("lock and load !");
	}
	
	@Bean
	public Appproperties appproperties() {
		return Appproperties.instance();
	}
}