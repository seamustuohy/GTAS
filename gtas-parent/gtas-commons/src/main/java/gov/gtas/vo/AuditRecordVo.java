package gov.gtas.vo;

import gov.gtas.model.AuditRecord;
import gov.gtas.util.DateCalendarUtils;

public class AuditRecordVo {
	private String actionType;
	private String status;
	private String message;
	private String user;
	//private String userName;
	private String timestamp;
	
	public AuditRecordVo(){}
	
	public AuditRecordVo(AuditRecord auditRecord){
		this.actionType = auditRecord.getActionType().toString();
		this.status = auditRecord.getActionStatus().toString();
		this.message = auditRecord.getMessage();
		this.user = auditRecord.getUser().getUserId();
		this.timestamp = DateCalendarUtils.formatRuleEngineDateTime(auditRecord.getTimestamp());		
	}

	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String userId) {
		this.user = userId;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	
}
