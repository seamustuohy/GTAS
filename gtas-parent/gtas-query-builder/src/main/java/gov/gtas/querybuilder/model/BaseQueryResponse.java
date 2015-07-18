package gov.gtas.querybuilder.model;

import gov.gtas.querybuilder.enums.Status;

public abstract class BaseQueryResponse implements IQueryResponse {

	private Status status;
	private String message;

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}