package gov.gtas.error;

import java.util.List;

import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

public class CommonValidationException extends CommonServiceException {
     /**
	 * serial version UID.
	 */
	private static final long serialVersionUID = 4913437813095082766L;

	private Errors validationErrors;
	
	public CommonValidationException(String message, Errors errors){
    	 super(CommonErrorConstants.JSON_INPUT_VALIDATION_ERROR_CODE, message);
    	 this.validationErrors = errors;
     }

	/* (non-Javadoc)
	 * @see java.lang.Throwable#getMessage()
	 */
	@Override
	public String getMessage() {
		StringBuilder msg = new StringBuilder(super.getMessage());
		msg.append("\n");
		List<FieldError> fieldErrors = validationErrors.getFieldErrors();
		for(FieldError err:fieldErrors){
			msg.append(err.getField()).append(" is invalid\n");
		}
		List<ObjectError> globalErrors = validationErrors.getGlobalErrors();
		for(ObjectError err:globalErrors){
			msg.append(err.getDefaultMessage()).append("\n");
		}
		return msg.toString();
	}
	
}
