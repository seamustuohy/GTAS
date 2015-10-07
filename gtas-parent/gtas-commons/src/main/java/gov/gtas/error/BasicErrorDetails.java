package gov.gtas.error;

import java.util.List;

import org.springframework.util.CollectionUtils;

public class BasicErrorDetails implements ErrorDetails {
	private List<String> errorDetails;
	private long errorId;
	private String errorCode;
	private String errorDescription;
	
	public BasicErrorDetails(Long id, String code, String description, List<String> details){
		this.errorDetails = details;
		this.errorCode = code;
		this.errorDescription = description;
		this.errorId = id;
	}
	/* (non-Javadoc)
	 * @see gov.gtas.error.ErrorDetails#getErrorId()
	 */
	@Override
	public Long getErrorId() {
		return errorId;
	}
	@Override
	public String[] getErrorDetails() {
		if(CollectionUtils.isEmpty(this.errorDetails)){
			return new String[0];
		} else {
			String[] detArr = new String[this.errorDetails.size()];
			detArr = this.errorDetails.toArray(detArr);
			return detArr;			
		}
	}				
	@Override
	public String getErrorDescription() {
		//return String.format(CommonErrorConstants.SYSTEM_ERROR_MESSAGE,	System.currentTimeMillis());
		return this.errorDescription;
	}				
	@Override
	public String getErrorCode() {
		return this.errorCode;
	}
	/**
	 * @param errorDetails the errorDetails to set
	 */
	public void setErrorDetails(List<String> errorDetails) {
		this.errorDetails = errorDetails;
	}
	/**
	 * @param errorCode the errorCode to set
	 */
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	/**
	 * @param errorDescription the errorDescription to set
	 */
	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}

}
