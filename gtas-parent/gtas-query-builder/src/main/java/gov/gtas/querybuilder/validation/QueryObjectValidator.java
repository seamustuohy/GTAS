package gov.gtas.querybuilder.validation;

import gov.gtas.model.udr.json.QueryObject;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class QueryObjectValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {

		return QueryObject.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors e) {
		QueryObject query = (QueryObject) target;

	}

}
