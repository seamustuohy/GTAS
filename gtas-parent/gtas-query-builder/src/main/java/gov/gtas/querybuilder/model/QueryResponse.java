package gov.gtas.querybuilder.model;

import gov.gtas.enumtype.Status;

import java.util.ArrayList;
import java.util.List;

public class QueryResponse extends BaseQueryResponse {

	private Status status;
	private String message;
	private List<IQueryResult> result = new ArrayList<>();
	
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

	public List<IQueryResult> getResult() {
		return result;
	}

	public void setResult(List<IQueryResult> result) {
		this.result = result;
	}

}
