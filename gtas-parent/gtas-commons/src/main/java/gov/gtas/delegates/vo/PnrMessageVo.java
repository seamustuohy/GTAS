package gov.gtas.delegates.vo;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import gov.gtas.model.EdifactMessage;
import gov.gtas.model.MessageStatus;
import gov.gtas.validators.Validatable;

public class PnrMessageVo implements Validatable{
	
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

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE); 
    }

	@Override
	public boolean validate() {
		if(StringUtils.isBlank(this.hashCode) ){
			return false;
		}
		return true;
	}
	
}
