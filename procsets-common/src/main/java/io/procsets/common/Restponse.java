package io.procsets.common;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Restponse<T> {
	private boolean success;
	private int    code;
	private Date timestamp;
	private String message;
	private long timetaken;
	private String session; 
	
	private T body;
	
	public Restponse() {
		this.timestamp = new Date();
	}
	
	public Restponse(T body) {
		super();
		this.success = true;
		this.code = 200;
		this.body = body;
		this.timestamp = new Date();
		this.message = "Requested operation completed successfully.";
	}
	
	public Restponse(boolean success, int code) {
		super();
		this.success = 	success;
		this.code = code;
		this.timestamp = new Date();
	}
	
	public Restponse(boolean success, int code, T body) {
		super();
		this.success = 	success;
		this.code = code;
		this.body = body;
		this.timestamp = new Date();
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public Restponse<T> setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
		return this;
	}

	public int getCode() {
		return code;
	}

	public Restponse<T> setCode(int code) {
		this.code = code;
		return this;
	}

	public T getBody() {
		return body;
	}

	public Restponse<T> setBody(T body) {
		this.body = body;
		return this;
	}

	public boolean isSuccess() {
		return success;
	}

	public Restponse<T> setSuccess(boolean success) {
		this.success = success;
		return this;
	}

	public String getMessage() {
		return message;
	}

	public Restponse<T> setMessage(String message) {
		this.message = message;
		return this;
	}

	@Override
	public String toString() {
		return "Restponse [success=" + success + ", code=" + code + ", timestamp=" + timestamp + ", message=" + message
				+ ", body=" + body + "]";
	}

	public long getTimetaken() {
		return timetaken;
	}

	public void setTimetaken(long timetaken) {
		this.timetaken = timetaken;
	}

	public String getSession() {
		return session;
	}

	public void setSession(String session) {
		this.session = session;
	}
	
}
