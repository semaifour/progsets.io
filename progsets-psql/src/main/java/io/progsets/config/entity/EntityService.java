
package io.progsets.config.entity;

import java.util.List;

import org.apache.lucene.queryparser.classic.QueryParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service 
public class EntityService {
	
	static Logger LOG = LoggerFactory.getLogger(EntityService.class.getName());
	
	@Autowired
	private EntityRepository repository;
	
	public EntityService() {
	}
	
	public Page<Entity> findAll(Pageable pageable) {
		return repository.findAll(pageable);
	}
	
	public Entity findByName(String name, String type) {
		return repository.findByNameAndType(name, type);
	}
	
	public List<Entity> findByStatus(String status, String type) {
		return repository.findByStatusAndType(status, type);
	}
	
	public List<Entity> findByType(String type) {
		return repository.findByType(type);
	}

	public Entity findByPkid(String pkid, String type) {
		return repository.findByIdAndType(pkid, type);
	}
	
	public boolean exists(String id) {
		return repository.exists(QueryParser.escape(id));
	}
	
	public void deleteAll() {
		repository.deleteAll();
	}
	
	public Entity delete(String pkid, String type) {
		Entity entity = findByPkid(pkid, type);
		if (entity != null) {
			entity = delete(entity);
		}
		return entity;
	}
	
	public Entity delete(Entity entity) {
		repository.delete(entity);
		return entity;
	}
	
	public long count() {
		return repository.count();
	}
	
	public Entity save(Entity entity, String type) {
		entity.setType(type);
		entity = repository.save(entity);
		if (entity.getPkid() == null)  {
			entity.setPkid(entity.getId());
			entity = repository.save(entity);
		}
		return entity;
	}

	public Iterable<Entity> findAll() {
		return repository.findAll();
	}
	
}
