package gov.gtas.model.watchlist.json.validation;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import gov.gtas.constant.CommonErrorConstants;
import gov.gtas.enumtype.ConditionEnum;
import gov.gtas.error.CommonValidationException;
import gov.gtas.model.udr.enumtype.OperatorCodeEnum;
import gov.gtas.model.udr.json.QueryEntity;
import gov.gtas.model.udr.json.QueryObject;
import gov.gtas.model.udr.json.QueryTerm;
import gov.gtas.model.watchlist.json.WatchlistItemSpec;
import gov.gtas.model.watchlist.json.WatchlistSpec;
import gov.gtas.model.watchlist.json.WatchlistTerm;
import gov.gtas.querybuilder.validation.util.QueryValidationUtils;

/**
 * An Adapter class that uses QueryValidationUtils to validate watch list JSON
 * objects.
 * 
 * @author GTAS3
 *
 */
public class WatchlistValidationAdapter {
	public static void validateWatchlistSpec(WatchlistSpec wljson) {
		BeanPropertyBindingResult errors = new BeanPropertyBindingResult(
				WatchlistSpec.class, "watch list");
		if (StringUtils.isEmpty(wljson.getName())
				|| StringUtils.isEmpty(wljson.getEntity())) {
			errors.reject(CommonErrorConstants.NULL_ARGUMENT_ERROR_CODE,
					"Watch list name or entity name is missing.");
		}
		if (CollectionUtils.isEmpty(wljson.getWatchlistItems())) {
			errors.reject(CommonErrorConstants.NULL_ARGUMENT_ERROR_CODE,
					"Watch list does not contain any items.");
		}
		if (errors.hasErrors()) {
			createAndThrowValidationError(errors);
		}
		for (WatchlistItemSpec itm : wljson.getWatchlistItems()) {
			QueryObject wlObj = new QueryObject();
			wlObj.setCondition(ConditionEnum.AND.toString());
			List<QueryEntity> terms = new LinkedList<QueryEntity>();
			for (WatchlistTerm trm : itm.getTerms()) {
				QueryTerm t = new QueryTerm(trm.getEntity(),
						trm.getField(), trm.getType(),
						OperatorCodeEnum.EQUAL.toString(),
						new String[] { trm.getValue() });
				terms.add(t);
			}
			wlObj.setRules(terms);
			Errors err = QueryValidationUtils.validateQueryObject(wlObj);
            if(err.hasErrors()){
            	createAndThrowValidationError(err);
            }
		}
	}
	private static void createAndThrowValidationError(Errors error){
		throw new CommonValidationException("Watch list JSON has Errors", error);
	}
}
