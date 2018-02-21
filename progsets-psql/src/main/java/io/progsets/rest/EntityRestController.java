package io.progsets.rest;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.progsets.common.Restponse;
import io.progsets.config.entity.Entity;
import io.progsets.config.entity.EntityService;

/**
 * EntityRestController exposes rest APIs to manage dynamic entities definitions such as saved 'querie', 'visual', 'dashboard', 'mytype', etc . 
 * 
 * As a metadata service, this service can be used by client apps to store any data string and retrieve.
 * 
 * 
 * 
 * @author megandran
 *
 */
@RestController
@RequestMapping("rest/psql/entity")
public class EntityRestController extends BaseController {
	
	@Autowired
	EntityService entityservice;
	
	@GetMapping(value="/{type}", produces = "application/json")
	public Restponse<List<Entity>> list(HttpServletRequest request,
								 HttpServletResponse response,
								 @PathVariable String type) {
		return new Restponse<List<Entity>>(entityservice.findByType(type));
	}

	@PostMapping(value="/{type}", produces = "application/json", consumes = "text/plain")
	public Restponse<Entity> create(HttpServletRequest request,
								 HttpServletResponse response,
								 @PathVariable String type,
								 @RequestParam String name,
								 @RequestParam (required=false) String desc,
								 @RequestBody String content) {
		Entity entity = new Entity();
		entity.setName(name);
		entity.setType(type);
		entity.setDesription(desc != null ? desc : type + " " + name);
		entity.setContent(content);
		entity.setStatus("ACTIVE");
		entity.setTimestmap(System.currentTimeMillis());
		entity = entityservice.save(entity, type);
		return new Restponse<Entity>(entity);
	}
	
	@PutMapping(value="/{type}/{pkid}", produces = "application/json", consumes = "application/json")
	public Restponse<Entity> save(HttpServletRequest request,
								 HttpServletResponse response,
								 @PathVariable String type,
								 @PathVariable String pkid,
								 @RequestBody Entity entity) {
		entity.setPkid(pkid);
		entity.setId(entity.getPkid());
		entity = entityservice.save(entity, type);
		return new Restponse<Entity>(entity);
	}

	@GetMapping(value="/{type}/{pkid}", produces = "application/json")
	public Restponse<Entity> get(HttpServletRequest request,
								 HttpServletResponse response,
								 @PathVariable String type,
								 @PathVariable String pkid) {
		Entity entity = entityservice.findByPkid(pkid, type);
		return new Restponse<Entity>(entity);
	}

	@DeleteMapping(value="/{type}/{pkid}", produces = "application/json")
	public Restponse<Entity> delete(HttpServletRequest request,
								 HttpServletResponse response,
								 @PathVariable String type,
								 @PathVariable String pkid) {
		Entity entity = entityservice.delete(pkid, type);
		entity.setStatus("__DELETED__");
		return new Restponse<Entity>(entity);
	}	
}
