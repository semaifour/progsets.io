package io.progsets.config.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "entity")
public class Entity {

	@Id
	String id;
	String pkid;
	String type;
	String desription;
	String name;
	String content;
	String status;
	long timestmap;
	int version;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDesription() {
		return desription;
	}
	public void setDesription(String desription) {
		this.desription = desription;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public long getTimestmap() {
		return timestmap;
	}
	public void setTimestmap(long timemillis) {
		this.timestmap = timemillis;
	}
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	public String getPkid() {
		return pkid;
	}
	public void setPkid(String pkid) {
		this.pkid = pkid;
	}
	@Override
	public String toString() {
		return "Entity [pkid=" + pkid + ", type=" + type + ", desription=" + desription + ", name="
				+ name + ", content=" + content + ", status=" + status + ", timestmap=" + timestmap + ", version="
				+ version + "]";
	}
	
}
