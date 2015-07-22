package gov.gtas.querybuilder.validation;

import gov.gtas.querybuilder.constants.Constants;
import gov.gtas.querybuilder.model.QueryRequest;
import gov.gtas.querybuilder.validation.util.QueryValidationUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

public class QueryRequestValidationUtils {
	private static final Logger logger = LoggerFactory.getLogger(QueryRequestValidationUtils.class);
	
	public static Errors validateQueryRequest(QueryRequest queryRequest) {
		String objectName = Constants.QUERYREQUEST_OBJECTNAME;

		BeanPropertyBindingResult errors = new BeanPropertyBindingResult(QueryRequest.class, objectName);
		
		logger.debug("Validating " + objectName);
		if(queryRequest != null) {
			if(StringUtils.isEmpty(queryRequest.getUserId())) {
				errors.reject("", "userId must be provided; ");
			}
			if(StringUtils.isEmpty(queryRequest.getTitle())) {
				errors.reject("", "Title cannot be empty; ");
			}
			if(queryRequest.getQuery() == null) {
				errors.reject("", "Query cannot be empty; ");
			}
			
			// validate user query
			errors.addAllErrors(QueryValidationUtils.validateQueryObject(queryRequest.getQuery()));
		}
		
		return errors;
	}
}
