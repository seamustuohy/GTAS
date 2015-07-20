package gov.gtas.querybuilder.validation;

import gov.gtas.querybuilder.model.QueryRequest;
import gov.gtas.querybuilder.validation.util.QueryValidationUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class QueryRequestValidator implements Validator {
	private static final Logger logger = LoggerFactory.getLogger(QueryRequestValidator.class);
	
	@Override
	public boolean supports(Class<?> clazz) {

		return QueryRequest.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		logger.debug("In Query Request's validate method");
		QueryRequest request = (QueryRequest) target;
		
		errors.addAllErrors(QueryValidationUtils.validateQueryRequest(request));
	}

}
