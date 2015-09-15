package gov.gtas.svc.util;

import java.util.Set;

import gov.gtas.bo.RuleServiceRequest;
import gov.gtas.bo.RuleServiceResult;
import gov.gtas.svc.request.builder.PassengerFlightTuple;
/**
 * This class is for objects that carry state information during 
 * Targeting operations.
 * @author GTAS3
 *
 */
public class RuleExecutionContext {
	private Set<PassengerFlightTuple> paxFlightTuples;
	private RuleServiceRequest ruleServiceRequest;
	private RuleServiceResult ruleServiceResult;
	/**
	 * @return the paxFlightTuples
	 */
	public  Set<PassengerFlightTuple> getPaxFlightTuples() {
		return paxFlightTuples;
	}
	/**
	 * @param paxFlightTuples the paxFlightTuples to set
	 */
	public  void setPaxFlightTuples(Set<PassengerFlightTuple> paxFlightTuples) {
		this.paxFlightTuples = paxFlightTuples;
	}
	/**
	 * @return the ruleServiceRequest
	 */
	public RuleServiceRequest getRuleServiceRequest() {
		return ruleServiceRequest;
	}
	/**
	 * @param ruleServiceRequest the ruleServiceRequest to set
	 */
	public void setRuleServiceRequest(RuleServiceRequest ruleServiceRequest) {
		this.ruleServiceRequest = ruleServiceRequest;
	}
	/**
	 * @return the ruleServiceResult
	 */
	public RuleServiceResult getRuleServiceResult() {
		return ruleServiceResult;
	}
	/**
	 * @param ruleServiceResult the ruleServiceResult to set
	 */
	public void setRuleServiceResult(RuleServiceResult ruleServiceResult) {
		this.ruleServiceResult = ruleServiceResult;
	}
	
}
