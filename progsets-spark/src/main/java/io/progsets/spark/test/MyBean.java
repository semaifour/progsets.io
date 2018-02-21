package io.progsets.spark.test;

import java.io.Serializable;

public class MyBean implements Serializable {
	
	private static final long serialVersionUID = 1744020473813710088L;

	public String id;
	public String opcode;
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}

	public String getOpcode() {
		return opcode;
	}

	public void setOpcode(String opcode) {
		this.opcode = opcode;
	}
}