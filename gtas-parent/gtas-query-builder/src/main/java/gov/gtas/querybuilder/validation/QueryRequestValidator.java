package gov.gtas.querybuilder.validation;

import gov.gtas.web.querybuilder.model.QueryRequest;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class QueryRequestValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {

		return QueryRequest.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors e) {
		QueryRequest request = (QueryRequest) target;

	}

}
