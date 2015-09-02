package gov.gtas.error;

import gov.gtas.constant.CommonErrorConstants;

import java.util.LinkedList;
import java.util.List;

import org.springframework.util.CollectionUtils;

public class BasicErrorDetails implements ErrorDetails {
	private Exception exception;
	private List<String> errorDetails;
	private String errorId;
	
	public BasicErrorDetails(Exception exception){
		this.exception = exception;
		this.errorDetails = new LinkedList<String>();
		createErrorDetails(exception, this.errorDetails);
		this.errorId = String.valueOf(System.currentTimeMillis());
	}
    private void createErrorDetails(Throwable ex, List<String> details){
    	details.add("Exception class:"+ex.getClass().getSimpleName());
    	details.add("Exception messsage:"+ex.getMessage());
    	for(StackTraceElement el:ex.getStackTrace()){
    		details.add(el.toString());
    	}
    	if(ex.getCause() != null){
    		details.add(">>>>>>>> Caused by:");
    		createErrorDetails(ex.getCause(), details);
    	}
    }
	/* (non-Javadoc)
	 * @see gov.gtas.error.ErrorDetails#getErrorId()
	 */
	@Override
	public String getErrorId() {
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
