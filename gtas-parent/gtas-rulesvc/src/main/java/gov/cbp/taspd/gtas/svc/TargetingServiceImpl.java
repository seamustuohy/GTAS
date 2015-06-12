package gov.cbp.taspd.gtas.svc;

import gov.cbp.taspd.gtas.constant.RuleServiceConstants;
import gov.cbp.taspd.gtas.error.RuleServiceErrorHandler;
import gov.cbp.taspd.gtas.model.ApisMessage;
import gov.cbp.taspd.gtas.rule.RuleService;
import gov.cbp.taspd.gtas.rule.RuleServiceResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TargetingServiceImpl implements TargetingService {
    @Autowired
    private RuleServiceErrorHandler errorHandler;
    
    private final RuleService ruleService;
    
    @Autowired
    public TargetingServiceImpl(final RuleService rulesvc){
    	ruleService = rulesvc;
    }
    
	@Override
	public RuleServiceResult analyzeApisMessage(ApisMessage message) {
		if(null == message){
			throw errorHandler.createException(RuleServiceConstants.NULL_ARGUMENT_ERROR_CODE, "ApisMessage", "TargetingServiceImpl.analyzeApisMessage()");
		}
		
        RuleServiceResult res = ruleService.invokeRuleset(ruleService.createRuleServiceRequest(message));
        return res;
	}

}
