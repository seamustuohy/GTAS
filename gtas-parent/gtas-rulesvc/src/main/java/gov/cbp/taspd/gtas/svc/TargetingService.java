/**
 * 
 */
package gov.cbp.taspd.gtas.svc;

import gov.cbp.taspd.gtas.model.ApisMessage;


/**
 * @author GTAS3
 *
 */

public interface TargetingService {
  void analyzeApisMessage(ApisMessage message);
}
