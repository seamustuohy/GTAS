package gov.gtas.querybuilder.validation.util;

import gov.gtas.model.udr.json.QueryEntity;
import gov.gtas.model.udr.json.QueryObject;
import gov.gtas.model.udr.json.QueryTerm;
import gov.gtas.querybuilder.constants.Constants;
import gov.gtas.querybuilder.enums.ConditionEnum;
import gov.gtas.querybuilder.enums.EntityEnum;
import gov.gtas.querybuilder.enums.OperatorEnum;
import gov.gtas.querybuilder.enums.TypeEnum;
import gov.gtas.querybuilder.mappings.APIMapping;
import gov.gtas.querybuilder.mappings.AddressMapping;
import gov.gtas.querybuilder.mappings.CreditCardMapping;
import gov.gtas.querybuilder.mappings.DocumentMapping;
import gov.gtas.querybuilder.mappings.EmailMapping;
import gov.gtas.querybuilder.mappings.FlightMapping;
import gov.gtas.querybuilder.mappings.FrequentFlyerMapping;
import gov.gtas.querybuilder.mappings.HitsMapping;
import gov.gtas.querybuilder.mappings.IEntityMapping;
import gov.gtas.querybuilder.mappings.NameOriginMapping;
import gov.gtas.querybuilder.mappings.PNRMapping;
import gov.gtas.querybuilder.mappings.PassengerMapping;
import gov.gtas.querybuilder.mappings.PhoneMapping;
import gov.gtas.querybuilder.mappings.TravelAgencyMapping;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;

public class QueryValidationUtils {
	private static final Logger logger = LoggerFactory.getLogger(QueryValidationUtils.class);
	
	public static Errors validateQueryObject(QueryObject queryObject) {
		String objectName = Constants.QUERYOBJECT_OBJECTNAME;
		BeanPropertyBindingResult errors = new BeanPropertyBindingResult(QueryObject.class, objectName);
		
		logger.info("Validating " + objectName);
		if(queryObject != null) {
			// validate user query
			validate(queryObject, errors);
		}
		
		return errors;
	}
	
	private static void validate(QueryEntity queryEntity, Errors errors) {
		QueryObject queryObject = null;
		QueryTerm queryTerm = null;
		String condition = null;
		
		if(queryEntity instanceof QueryObject) {
			queryObject = (QueryObject) queryEntity;
			condition = queryObject.getCondition();
			
			// validate condition
			boolean validCondition = false;
			for(ConditionEnum e : ConditionEnum.values()) {
				if(e.toString().equalsIgnoreCase(condition)) {
					validCondition = true;
					break;
				}
			}
			if(!validCondition) {
				errors.reject("", "condition: \'" + condition + "\' is invalid");
			}

			// validate rules
			List<QueryEntity> rules = queryObject.getRules();
			if(rules != null) {
				for(QueryEntity rule : rules) {
					
					validate(rule, errors);
				}
			}
						
		}
		else if(queryEntity instanceof QueryTerm) {
			queryTerm = (QueryTerm) queryEntity;
			
			String entity = queryTerm.getEntity();
			String field = queryTerm.getField();
			String type = queryTerm.getType();
			String operator = queryTerm.getOperator();
			
			// validate entity
			boolean validEntity = false;
			for(EntityEnum e : EntityEnum.values()) {
				if(e.getEntityName().equalsIgnoreCase(entity)) {
					validEntity = true;
				}
			}
			if(!validEntity) {
				errors.reject("", "entity: \'" + entity + "\' is invalid");
			}
			
			if(validEntity) { // only validate the field if the entity is valid
				// validate field name
				boolean validField = false;
				switch(entity.toUpperCase()) {
					case Constants.ADDRESS:
						validField = validateField(AddressMapping.values(), field);
						break;
					case Constants.API:
						validField = validateField(APIMapping.values(), field);
						break;
					case Constants.CREDITCARD:
						validField = validateField(CreditCardMapping.values(), field);
						break;
					case Constants.DOCUMENT:
						validField = validateField(DocumentMapping.values(), field);
						break;
					case Constants.EMAIL:
						validField = validateField(EmailMapping.values(), field);
						break;
					case Constants.FLIGHT:
						validField = validateField(FlightMapping.values(), field);
						break;
					case Constants.FREQUENTFLYER:
						validField = validateField(FrequentFlyerMapping.values(), field);
						break;
					case Constants.HITS:
						validField = validateField(HitsMapping.values(), field);
						break;
					case Constants.NAMEORIGIN:
						validField = validateField(NameOriginMapping.values(), field);
						break;
					case Constants.PAX:
						validField = validateField(PassengerMapping.values(), field);
						break;
					case Constants.PHONE:
						validField = validateField(PhoneMapping.values(), field);
						break;
					case Constants.PNR:
						validField = validateField(PNRMapping.values(), field);
						break;
					case Constants.TRAVELAGENCY:
						validField = validateField(TravelAgencyMapping.values(), field);
						break;
					
				}
				if(!validField) {
					errors.reject("", "field: \'" + field + "\' is invalid");
				}																		
			}
			
			// validate type
			boolean validType = false;
			for(TypeEnum t : TypeEnum.values()) {
				if(t.getType().equalsIgnoreCase(type)) {
					validType = true;
					break;
				}
			}
			if(!validType) {
				errors.reject("", "type: \'" + type + "\' is invalid");
			}
			
			//validate operator
			boolean validOperator = false;
			for(OperatorEnum o : OperatorEnum.values()) {
				if(o.toString().equalsIgnoreCase(operator)) {
					validOperator = true;
					break;
				}
			}
			
			if(!validOperator) {
				errors.reject("", "operator: \'" + operator + "\' is invalid");
			}
			else {
				// validate that there are two values if the operator is BETWEEN
				if(OperatorEnum.BETWEEN.toString().equalsIgnoreCase(operator)) {
					List<String> values = Arrays.asList(queryTerm.getValues());
					
					if(values != null && values.size() != 2) {
						errors.reject("BETWEEN operator must have two parameters");
					}
					
				}
			}
			
		}
				
	}
	
	public static String getErrorString(Errors errors) {
		StringBuilder errorMsg = new StringBuilder();
		
		if(errors != null && errors.hasErrors()) {
			int count = 0;
			for(ObjectError e: errors.getAllErrors()) {
				if(count > 0) {
					errorMsg.append("; ");
				}
				errorMsg.append(e.getDefaultMessage());
				count++;
			}
		}
	
		return errorMsg.toString();
	}
	
	private static boolean validateField(IEntityMapping[] entityEnum, String field) {
		boolean validField = false;
		
		for(IEntityMapping e : entityEnum) {
			if(e.getFieldName().equalsIgnoreCase(field)) {
				validField = true;
				break;
			}
		}
		
		return validField;
	}
}
