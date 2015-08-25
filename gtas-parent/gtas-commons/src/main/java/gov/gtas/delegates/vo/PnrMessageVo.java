package gov.gtas.delegates.vo;

import gov.gtas.model.EdifactMessage;
import gov.gtas.model.MessageStatus;

public class PnrMessageVo {
	private EdifactMessage edifactMessage;
	private MessageStatus status;
	
	private String filePath;
	private String error;
	private String hashCode;
	
	
	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getHashCode() {
		return hashCode;
	}

	public void setHashCode(String hashCode) {
		this.hashCode = hashCode;
	}

	public MessageStatus getStatus() {
		return status;
	}

	public void setStatus(MessageStatus status) {
		this.status = status;
	}

	public EdifactMessage getEdifactMessage() {
		return edifactMessage;
	}

	public void setEdifactMessage(EdifactMessage edifactMessage) {
		this.edifactMessage = edifactMessage;
	}
	
}
