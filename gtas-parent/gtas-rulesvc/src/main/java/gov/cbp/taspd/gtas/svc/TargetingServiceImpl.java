package gov.cbp.taspd.gtas.svc;

import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gov.cbp.taspd.gtas.error.RuleServiceException;
import gov.cbp.taspd.gtas.model.ApisMessage;
import gov.cbp.taspd.gtas.rule.RuleService;

@Service
public class TargetingServiceImpl implements TargetingService {
	
    private final RuleService ruleService;
    
    @Autowired
    public TargetingServiceImpl(final RuleService rulesvc){
    	ruleService = rulesvc;
    }
    
	@Override
	public void analyzeApisMessage(ApisMessage message) {
		if(null == message){
			throw new RuleServiceException("Input Request cannot be null");
		}
		
        ruleService.invokeRuleset(ruleService.createRuleServiceRequest(message));
	}

}
