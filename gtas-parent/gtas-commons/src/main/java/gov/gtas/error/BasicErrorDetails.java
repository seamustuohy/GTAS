package gov.gtas.error;

import java.util.List;

public class BasicErrorDetails implements ErrorDetails {
	private Exception exception;
	
	public BasicErrorDetails(Exception exception){
		this.exception = exception;
	}

	/* (non-Javadoc)
	 * @see gov.gtas.error.ErrorDetails#getErrorId()
	 */
	@Override
	public String getErrorId() {
		return null;
	}
	@Override
	public List<String> getWarningMessages() {
		return null;
	}				
	@Override
	public String getFatalErrorMessage() {
		if(exception instanceof CommonServiceException){
			return ((CommonServiceException)exception).getMessage();
		}else{
		    return String.format(CommonErrorConstants.SYSTEM_ERROR_MESSAGE,
					System.currentTimeMillis());
		}
	}				
	@Override
	public String getFatalErrorCode() {
		if(exception instanceof CommonServiceException){
			return ((CommonServiceException)exception).getErrorCode();
		}else{
		    return CommonErrorConstants.SYSTEM_ERROR_CODE;
		}
	}

}
