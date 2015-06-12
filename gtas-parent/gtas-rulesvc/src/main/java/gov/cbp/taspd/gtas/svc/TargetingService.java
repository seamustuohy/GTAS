/**
 * 
 */
package gov.cbp.taspd.gtas.svc;

import gov.cbp.taspd.gtas.model.ApisMessage;
import gov.cbp.taspd.gtas.rule.RuleServiceResult;


/**
 * @author GTAS3
 *
 */

public interface TargetingService {
	RuleServiceResult analyzeApisMessage(ApisMessage message);
}
