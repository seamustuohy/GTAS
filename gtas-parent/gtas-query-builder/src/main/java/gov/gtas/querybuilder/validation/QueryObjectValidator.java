package gov.gtas.querybuilder.validation;

import gov.gtas.model.udr.json.QueryObject;
import gov.gtas.querybuilder.validation.util.QueryValidationUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class QueryObjectValidator implements Validator {
	private static final Logger logger = LoggerFactory.getLogger(QueryObjectValidator.class);
	
	@Override
	public boolean supports(Class<?> clazz) {

		return QueryObject.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		logger.debug("In Query Object's validate method");
		QueryObject query = (QueryObject) target;
		
		errors.addAllErrors(QueryValidationUtils.validateQueryObject(query));
	}

}
