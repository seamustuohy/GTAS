package gov.gtas.controller;

//import gov.gtas.bo.RuleExecutionStatistics;
import gov.gtas.constants.Constants;
import gov.gtas.error.CommonServiceException;
import gov.gtas.model.udr.json.MetaData;
import gov.gtas.model.udr.json.QueryEntity;
import gov.gtas.model.udr.json.QueryObject;
import gov.gtas.model.udr.json.QueryTerm;
import gov.gtas.model.udr.json.UdrSpecification;
import gov.gtas.model.udr.json.error.GtasJsonError;
//import gov.gtas.rule.RuleServiceResult;
import gov.gtas.svc.TargetingService;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * The REST service end-point controller for creating and managing 
 * user Defined Rules (UDR) for targeting.
 * @author GTAS3 (AB)
 *
 */
@RestController
@RequestMapping(Constants.UDR_ROOT)
public class UdrManagementController {
	@Autowired
	TargetingService targetingService;

	@RequestMapping(value = Constants.UDR_GET, method = RequestMethod.GET)
	public @ResponseBody UdrSpecification getUDR(@PathVariable String userId, @PathVariable String title) {
		System.out.println("******** user ="+userId+", title="+title);
		QueryObject queryObject = new QueryObject();
		queryObject.setCondition("OR");
		List<QueryEntity> rules = new LinkedList<QueryEntity>();
		QueryTerm trm = new QueryTerm("Pax", "firstName","EQUAL", "John");
		rules.add(trm);
		rules.add(new QueryTerm("Pax", "lastName", "EQUAL", "Jones"));
		queryObject.setRules(rules);
		
		UdrSpecification resp = new UdrSpecification(queryObject, new MetaData("Hello Rule 1", "This is a test", new Date(), "jpjones"));
		return resp;
	}

	@RequestMapping(value = Constants.UDR_POST, method = RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody UdrSpecification createUDR(@PathVariable String userId, @RequestBody UdrSpecification inputSpec) {
		System.out.println("******** user ="+userId);
		if(inputSpec != null){
			QueryObject queryObject = inputSpec.getDetails();
			System.out.println("******** condition ="+queryObject.getCondition());
			List<QueryEntity> rules = queryObject.getRules();
			System.out.println("******** rule count ="+rules.size());
			QueryTerm trm = (QueryTerm)rules.get(0);
			System.out.println("******** entity ="+trm.getEntity());
		}		
		UdrSpecification resp = inputSpec;
		return resp;
	}
	
	@ExceptionHandler(CommonServiceException.class)
	public @ResponseBody GtasJsonError handleError(CommonServiceException ex){	
		return new GtasJsonError(ex.getErrorCode(), ex.getMessage());
	}

	@ExceptionHandler(Exception.class)
	public @ResponseBody GtasJsonError handleError(Exception ex){	
		return new GtasJsonError("UNKNOWN_ERROR", ex.getMessage());
	}
}
