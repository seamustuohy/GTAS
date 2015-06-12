package gov.cbp.taspd.gtas.controller;

import gov.cbp.taspd.gtas.bo.RuleExecutionStatistics;
import gov.cbp.taspd.gtas.constants.URIConstants;
import gov.cbp.taspd.gtas.querybuilder.QueryBuilderService;
import gov.cbp.taspd.gtas.svc.TargetingService;
import gov.cbp.taspd.gtas.web.model.QueryBuilderTable;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping(URIConstants.UDR_BUILDER_ROOT)
public class UdrManagementController {
	@Autowired
	TargetingService targetingService;
	
	@RequestMapping(value = URIConstants.UDR_TEST, method = RequestMethod.GET)
	public @ResponseBody RuleExecutionStatistics testUDR() {
		
		
		return new RuleExecutionStatistics();
	}

}
