package io.progsets.config.entity;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Component;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

import cz.jirutka.spring.embedmongo.EmbeddedMongoFactoryBean;
import io.progsets.common.Appproperties;


/**
 * MongodbConfiguration Configuration
 * 
 * @author mjs
 *
 */

@Component
@EnableMongoRepositories(basePackages = "io.progsets")
public class MongodbConfiguration {
	
	static Logger LOG = LoggerFactory.getLogger(MongodbConfiguration.class.getName());
	

	private static final String MONGO_DB_URL = "localhost";
	private static final String MONGO_DB_NAME = "progsets_emdb";
	    
	private List<ServerAddress> hosts = new ArrayList<ServerAddress>();;
	
	@Autowired
	Appproperties properties;
	
	public Appproperties properties() {
		return properties;
	}
	
	@PostConstruct
	public void init() {
		String tmp = properties.props().getProperty("progsets.config.store.mongodb.hosts");
		if (tmp != null) {
			LOG.info("External Mongodb is configured :"  + tmp);
			String[] hs = tmp.split(",");
			for (String s : hs) {
				String[] ss = s.split(":");
				try {
					hosts.add(new ServerAddress(ss[0], Integer.parseInt(ss[1])));
				} catch (Exception e) {
					LOG.warn("Failed to connect to mongo :" + s,  e );
				}
			}
		} else {
			LOG.info("External Mongodb is not congirued, will start embedded Mongodb at localhost");
		}
	}
	
	public MongoDbFactory mongoDbFactory() {
		try {
			if (hosts.size() > 0) {
				LOG.info("connecting to mongo");
				return new SimpleMongoDbFactory(new MongoClient(hosts), properties.props().getProperty("progsets.config.store.mongodb.database"));
			}
		} catch (Exception e) {
			LOG.warn("MongoDbFactory is not available, Entities can't be used");
		}
		return null;
	}

	@Bean
	public MongoTemplate mongoTemplate() throws Exception {
		MongoDbFactory factory = mongoDbFactory();
		if (factory != null) {
			MongoTemplate mongoTemplate = new MongoTemplate(factory);
			return mongoTemplate;
		} else {
			EmbeddedMongoFactoryBean mongo = new EmbeddedMongoFactoryBean();
		    mongo.setBindIp(MONGO_DB_URL);
		    MongoClient mongoClient = mongo.getObject();
		    MongoTemplate mongoTemplate = new MongoTemplate(mongoClient, MONGO_DB_NAME);
			return mongoTemplate;
		}
	}
	
}