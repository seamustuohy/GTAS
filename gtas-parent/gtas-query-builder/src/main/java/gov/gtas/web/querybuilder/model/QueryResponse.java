package gov.gtas.web.querybuilder.model;

import java.util.ArrayList;
import java.util.List;

public class QueryResponse {

	private Status status;
	private String message;
	private List<QueryResult> result = new ArrayList<>();
	
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
	
	public List<QueryResult> getResult() {
		return result;
	}
	
	public void setResult(List<QueryResult> result) {
		this.result = result;
	}
	
}
