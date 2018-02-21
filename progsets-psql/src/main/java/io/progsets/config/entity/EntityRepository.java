package io.progsets.config.entity;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface EntityRepository extends MongoRepository<Entity, String> {
	public Entity findByIdAndType(String id, String type);
	public Entity findByNameAndType(String name, String type);
	public List<Entity> findByStatusAndType(String status, String type);
	public List<Entity> findByType(String type);
}